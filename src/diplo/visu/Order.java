package diplo.visu;

import diplo.meta.*;

import java.awt.BasicStroke;
import java.awt.geom.Ellipse2D;
import java.awt.Graphics2D;
import diplo.framework.GraphicalComponent;
import java.awt.geom.Point2D;
import java.awt.Shape;
import java.awt.Polygon;
import java.awt.Color;

import org.w3c.dom.Element;

public class Order extends GraphicalComponent {

    @Override
    public void queryRedraw() {
        father.queryRedraw();
    }

    enum Type {

        Hold, Support, Attack, Convoy
    }
    Troop father;
    Type type;
    Place first;
    Place second;
    boolean success;
    //Static graphic object
    final static Color hlcolor = Color.ORANGE;
    final static float dash1[] = {10.0f, 5.0f};
    final static BasicStroke dashedHold = new BasicStroke(5.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);
    final static BasicStroke dashedLine = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 20.0f, dash1, 0.0f);
    final static BasicStroke fullLine = new BasicStroke(3.0f);

    Order(Troop f) {
        father = f;
        DoHold(false);
    }

    Order(Troop f, Element xmldata) {
        father = f;
        String tt = xmldata.getAttribute("order");
        if (tt.compareToIgnoreCase("Hold") == 0) {
            type = Type.Hold;
        } else if (tt.compareToIgnoreCase("Support") == 0) {
            type = Type.Support;
        } else if (tt.compareToIgnoreCase("Attack") == 0) {
            type = Type.Attack;
        } else if (tt.compareToIgnoreCase("Convoy") == 0) {
            type = Type.Convoy;
        }

        first = GetMap().getPlace(xmldata.getAttribute("order_first"));
        second = GetMap().getPlace(xmldata.getAttribute("order_second"));
        Build();
    }

   

    boolean DoHold(boolean simul) {
        if (simul) {
            return true;
        }
        type = Type.Hold;
        first = null;
        second = null;
        Build();
        return true;
    }

    //Support defence
    boolean DoSupport(Place target, boolean simul) {
        if (father.isAdjacentTo(target.meta.name) == false) {
            return false;
        }
        if (target.troop == null) {
            return false;
        }
        if (simul) {
            return true;
        }
        type = Type.Support;
        first = target;
        second = null;
        Build();
        return true;
    }
    //Support attaque

    boolean DoSupport(Place t1, Place t2, boolean simul) {
        if (t2 == null) {
            return DoSupport(t1, simul);
        }
        if (father.isAdjacentTo(t2.meta.name) == false) {
            return false;
        }
        if (t1.troop == null) {
            return false;
        }
        if (father.type == TroopType.Fleet
                && t1.troop.type == TroopType.Army
                && t1.isAdjacentTo(t2.meta.name, TroopType.Army) == false) {
            return DoConvoy(t1, t2, simul);
        }

        if (t1.troop.order == null) {
            return false;
        }
        if (t1.troop.order.type != Type.Attack) {
            return false;
        }
        if (t1.troop.order.first != t2) {
            return false;
        }
        if (simul) {
            return true;
        }

        type = Type.Support;
        first = t1;
        second = t2;
        Build();
        return true;
    }

    Map GetMap() {
        return father.getMap();
    }

    boolean DoConvoy(Place f1, Place f2, boolean simul) {
        if (father.type != TroopType.Fleet) {
            return false;
        }
        if (f1.getMap() != GetMap()) {
            return false;
        }
        if (f2.getMap() != GetMap()) {
            return false;
        }
        if (f1.isAdjacentTo(father.father.meta.name, TroopType.Fleet) == false
                & f2.isAdjacentTo(father.father.meta.name, TroopType.Fleet) == false) {
            return false;
        }
        if (f1.troop == null) {
            return false;
        }
        if (f1.troop.order == null) {
            return false;
        }
        if (f1.troop.order.type != Type.Attack) {
            return false;
        }
        if (f1.troop.order.first != f2) {
            return false;
        }
        if (simul) {
            return true;
        }
        type = Type.Convoy;
        first = f1;
        second = f2;
        Build();
        return true;

    }

    boolean DoAttack(Place target, boolean simul) {
        if (target.getMap() != GetMap()) {
            return false;
        }
        if (father.isAdjacentTo(target.meta.name) == false) {
            if (target.meta.isSolid() == false) {
                return false;
            }
            Troop f1 = father.father.father.GetNeighborFleet();
            if (f1 == null) {
                return false;
            }
            Troop f2 = target.father.GetNeighborFleet();
            if (f2 == null) {
                return false;
            }
            if (simul) {
                return true;
            }
            type = Type.Attack;
            first = target;
            second = null;
            Build();
            f1.order.DoConvoy(father.father, target, false);
            f2.order.DoConvoy(father.father, target, false);
            return true;
        }
        if (simul) {
            return true;
        }
        type = Type.Attack;
        first = target;
        second = null;
        shape = GetArrowPickShape(father.father.meta.center, first.meta.center, 15., 15.);
        Build();
        return true;
    }

    boolean DoSupport(Order target, boolean simul) {
        if (target.GetMap() != GetMap()) {
            return false;
        }
        if (target == this) {
            return false;
        }
        if (target.type == Order.Type.Attack)//Support an attack
        {
            return DoSupport(target.father.father, target.first, simul);
        }

        if (target.type == Order.Type.Hold)//Support an attack
        {
            return DoSupport(target.father.father, simul);
        }

        return false;
    }

    public void Build() {

        if (type == Type.Hold) {
            shape = new Ellipse2D.Double(father.father.meta.center.getX() - 15,
                    father.father.meta.center.getY() - 15, 30, 30);
        } else if (type == Type.Attack || (type == Type.Support && second == null)) {
            shape = GetArrowPickShape(father.father.meta.center, first.meta.center, 15., 15.);
        } else if (type == Type.Support && second != null) {
            Point2D pt = new Point2D.Double(
                    (first.meta.center.getX() + second.meta.center.getX()) * .5,
                    (first.meta.center.getY() + second.meta.center.getY()) * .5);
            shape = GetArrowPickShape(father.father.meta.center, pt, 15., 15.);
        } else if (type == Type.Convoy) {
            double p1x = first.meta.center.getX();
            double p1y = first.meta.center.getY();
            double p2x = second.meta.center.getX();
            double p2y = second.meta.center.getY();
            double vx = p2x - p1x;
            double vy = p2y - p1y;
            double l = vx * vx + vy * vy;
            double pl = (father.father.meta.center.getX() - p1x) * vx
                    + (father.father.meta.center.getY() - p1y) * vy;
            if (pl > l * .9) {
                pl = l * .9;
            }
            if (pl < l * .1) {
                pl = l * .1;
            }

            Point2D pt = new Point2D.Double(p1x + vx * pl / l, p1y + vy * pl / l);
            shape = GetArrowPickShape(father.father.meta.center, pt, 15., 15.);
        }
    }

    public void DrawArrow(Point2D start, Point2D end, Graphics2D gfx, double scut, double ecut) {
        double d = start.distance(end);
        double c1 = scut / d;
        double c2 = 1. - ecut / d;
        d -= scut + ecut;
        double x2 = start.getX();
        double y2 = start.getY();
        double x1 = end.getX();
        double y1 = end.getY();

        double xx1 = c1 * x1 + (1. - c1) * x2;
        double yy1 = c1 * y1 + (1. - c1) * y2;
        double xx2 = c2 * x1 + (1. - c2) * x2;
        double yy2 = c2 * y1 + (1. - c2) * y2;
        gfx.drawLine((int) xx1, (int) yy1, (int) xx2, (int) yy2);

        double dx = xx2 - xx1;
        double dy = yy2 - yy1;
        dx = dx / d * 10.;
        dy = dy / d * 10.;

        double nx, ny;
        nx = dx * .86 + dy * .50;
        ny = -dx * .50 + dy * .86;
        gfx.drawLine((int) (xx2 - nx), (int) (yy2 - ny), (int) xx2, (int) yy2);
        nx = dx * .86 - dy * .50;
        ny = +dx * .50 + dy * .86;
        gfx.drawLine((int) (xx2 - nx), (int) (yy2 - ny), (int) xx2, (int) yy2);
    }

    public Shape GetArrowPickShape(Point2D start, Point2D end, double scut, double ecut) {
        double d = start.distance(end);
        double c1 = scut / d;
        double c2 = 1. - ecut / d;
        d -= scut + ecut;
        double x2 = start.getX();
        double y2 = start.getY();
        double x1 = end.getX();
        double y1 = end.getY();

        double xx1 = c1 * x1 + (1. - c1) * x2;
        double yy1 = c1 * y1 + (1. - c1) * y2;
        double xx2 = c2 * x1 + (1. - c2) * x2;
        double yy2 = c2 * y1 + (1. - c2) * y2;

        double dx = xx2 - xx1;
        double dy = yy2 - yy1;
        dx = dx / d * 5.;
        dy = dy / d * 5.;
        Polygon bb = new Polygon();
        bb.addPoint((int) (xx1 - dy), (int) (yy1 + dx));
        bb.addPoint((int) (xx1 + dy), (int) (yy1 - dx));
        bb.addPoint((int) (xx2 + dy), (int) (yy2 - dx));
        bb.addPoint((int) (xx2 - dy), (int) (yy2 + dx));
        return bb;
    }

    @Override
    public void draw(Graphics2D gfx) {
        
        if (type == Type.Hold) {
            gfx.setStroke(dashedHold);
            gfx.setColor(highlighted ? hlcolor : father.nation.color);
            gfx.drawOval((int) father.father.meta.center.getX() - 15,
                    (int) father.father.meta.center.getY() - 15, 30, 30);
        } else if (type == Type.Attack || (type == Type.Support && second == null)) {
            gfx.setStroke(type == Type.Attack ? fullLine : dashedLine);
            gfx.setColor(highlighted ? hlcolor : father.nation.color);
            DrawArrow(father.father.meta.center, first.meta.center, gfx, 15., 15.);
        } else if (type == Type.Support && second != null) {
            if (first.troop.order.type != Type.Attack) {
                gfx.setStroke(dashedLine);
            }
            gfx.setColor(highlighted ? hlcolor : father.nation.color);
            Point2D pt = new Point2D.Double(
                    (first.meta.center.getX() + second.meta.center.getX()) * .5,
                    (first.meta.center.getY() + second.meta.center.getY()) * .5);
            DrawArrow(father.father.meta.center, pt, gfx, 15., 0);
        } else if (type == Type.Convoy) {
            gfx.setStroke(dashedLine);
            gfx.setColor(highlighted ? hlcolor : father.nation.color);
            Point2D pt = new Point2D.Double(
                    (first.meta.center.getX() + second.meta.center.getX()) * .5,
                    (first.meta.center.getY() + second.meta.center.getY()) * .5);
            DrawArrow(father.father.meta.center, pt, gfx, 15., 0);
        }
        super.draw(gfx);
    }

    void DoReviewOrders() {
        if (type == Type.Hold) {
        } else if (type == Type.Support) {
            if (DoSupport(first, second, true) == false) {
                DoHold(false);
            }
        } else if (type == Type.Attack) {
            if (DoAttack(first, true) == false) {
                DoHold(false);
            }
        } else if (type == Type.Convoy) {
            if (DoConvoy(first, second, true) == false) {
                DoHold(false);
            }

        }
    }
}
