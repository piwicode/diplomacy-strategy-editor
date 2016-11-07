package diplo.visu;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import diplo.framework.*;
import diplo.meta.*;

public class Troop extends DragableImageComponent {

    @Override
    public void queryRedraw() {
        father.queryRedraw();
    }
    final Order order;
    public final Place father;
    public final MetaNation nation;
    public final TroopType type;

    Troop(Place father, MetaNation nation, TroopType type) {
        if (father == null) {
            throw new IllegalArgumentException();
        }
        if (nation == null) {
            throw new IllegalArgumentException();
        }
        if (type == null) {
            throw new IllegalArgumentException();
        }
        this.father = father;
        this.nation = nation;
        this.type = type;
        build();//FIXME
        double x = father.meta.center.getX() - ((Rectangle.Double) shape).width / 2;
        double y = father.meta.center.getY() - ((Rectangle.Double) shape).height / 2;
        before.translate(x, y);
        after.translate(-x, -y);
        order = new Order(this);
        childs.add(order);
    }

    public Element buidXML(Document document) {
        Element node = document.createElement("troop");
        node.setAttribute("place", father.meta.name);
        node.setAttribute("nation", nation.shortName);
        if (type == TroopType.Fleet) {
            node.setAttribute("type", "Fleet");
        } else if (type == TroopType.Army) {
            node.setAttribute("type", "Army");
        }
        if (order.type == Order.Type.Attack) {
            node.setAttribute("order", "Attack");
        } else if (order.type == Order.Type.Convoy) {
            node.setAttribute("order", "Convoy");
        } else if (order.type == Order.Type.Hold) {
            node.setAttribute("order", "Hold");
        } else if (order.type == Order.Type.Support) {
            node.setAttribute("order", "Support");
        }
        if (order.first != null) {
            node.setAttribute("order_first", order.first.meta.name);
        }
        if (order.second != null) {
            node.setAttribute("order_second", order.second.meta.name);
        }
        return node;
    }

    public boolean isAdjacentTo(String place) {
        return father.isAdjacentTo(place, type);
    }

    public boolean doMoveTroop(Place place, boolean simul) {
        return place.troop == null
                && father.doClearTroop(simul)
                && place.doDropTroop(type, nation, simul);
    }

    Map getMap() {
        return father.getMap();
    }

    @Override
    boolean doInteraction(int button, int modifiers, BaseComponent c, boolean simul) {
        if (doInteractionInt(button, modifiers, c, simul)) {
            if (simul == false) {
                father.father.father.doReviewOrders();
            }
            return true;
        } else {
            return false;
        }
    }

    boolean doInteractionInt(int button, int modifiers, BaseComponent c, boolean simul) {
        if (c instanceof StaticBean && father.doClearTroop(simul)) {
            return true;
        }
        if (c instanceof TemplateToolbar && father.doClearTroop(simul)) {
            return true;
        }
        if (c == father && order.DoHold(simul)) {
            return true;
        }

        if ((modifiers & MouseEvent.CTRL_MASK) != 0) {
            if (c.getClass().equals(Place.class)
                    && order.DoAttack((Place) c, simul)) {
                return true;
            }
            return false;
        }
        if ((modifiers & MouseEvent.SHIFT_MASK) != 0) {
            if (c.getClass().equals(Place.class)
                    && order.DoSupport((Place) c, simul)) {
                return true;
            }
            return false;
        }
        if (c instanceof Order && order.DoSupport((Order) c, simul)) {
            return true;
        }

        if (c instanceof Place && doMoveTroop((Place) c, simul)) {
            return true;
        }
        return false;
    }

    void doReviewOrders() {
        order.DoReviewOrders();
    }

    @Override
    public BufferedImage getImage() {
        return nation.image.get(type);
    }
}
