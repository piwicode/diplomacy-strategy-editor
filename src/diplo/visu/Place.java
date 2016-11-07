package diplo.visu;

import diplo.framework.*;
import java.awt.Stroke;
import java.awt.Composite;
import diplo.meta.*;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;


import org.w3c.dom.Element;

public class Place extends GraphicalComponent {

    final MetaPlace meta;
    final Province father;
    Troop troop;

    @Override
    public void queryRedraw() {
        father.queryRedraw();
    }

    Map getMap() {
        return father.GetMap();
    }
    final static BasicStroke fullLine = new BasicStroke(3.0f);

    Place(Province f, MetaPlace m) {
        father = f;
        meta = m;
        shape = m.polygon;
    }

    Place(Province f, Place p) {
        father = f;
        meta = p.meta;
        shape = p.shape;
        if (p.troop != null) {
            troop = new Troop(this, p.troop.nation, p.troop.type);
            childs.add(troop);
        } else {
            troop = null;
        }
    }

    @Override
    public void draw(Graphics2D gfx) {
        if (highlighted) {
            final Composite c = gfx.getComposite();
            final Stroke s = gfx.getStroke();
            gfx.setStroke(fullLine);
            gfx.setColor(meta.lineColor);
            gfx.draw(shape);
            gfx.setColor(meta.fillColor);
            gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
            gfx.fill(shape);
            gfx.setComposite(c);
            gfx.setStroke(s);
        }
        super.draw(gfx);
    }

    public boolean doClearTroop(boolean simul) {
        if (simul) {
            return true;
        }
        childs.remove(troop);
        troop = null;
        return true;
    }

    public boolean isAdjacentTo(String place, TroopType type) {
        return meta.IsAdjacentTo(place, type);
    }

    public void doCreateTroop(MetaGame mg, Element xmldata) {
        doClearTroop(false);
        MetaNation mn = mg.getMetaNation(xmldata.getAttribute("nation"));
        final TroopType type = TroopType.valueOf(xmldata.getAttribute("type"));
        troop = new Troop(this, mn, type);
        childs.add(troop);
    }

    public boolean doDropTroop(TroopType type, MetaNation nation, boolean simul) {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        if (nation == null) {
            throw new IllegalArgumentException();
        }

        if (type == TroopType.Army && !meta.isSolid()) {
            return false;
        }
        if (type == TroopType.Fleet && !meta.isLiquid()) {
            return false;
        }
        if (type == TroopType.Flag) {
            if (!meta.isSolid()) {
                return false;
            }
            return father.DoCaptureRessource(nation, simul);
        }

        if (!father.DoClearTroop(simul)) {
            return false;
        }
        if (simul) {
            return true;
        }
        troop = new Troop(this, nation, type);
        childs.add(troop);
        return true;
    }

    void doReviewOrders() {
        if (troop != null) {
            troop.doReviewOrders();
        }
    }

    Troop getFleet() {
        if (troop != null && troop.type == TroopType.Fleet) {
            return troop;
        } else {
            return null;
        }
    }

    Troop getNeighborFleet() {
        for (MetaAdjacency a : meta.adjacencies) {
            if (a.type != TroopType.Fleet) {
                continue;
            }
            final Map m = getMap();
            for (final String placeName : a.ref) {
                final Place p = m.getPlace(placeName);
                if (p == null) {
                    continue;
                }
                final Troop t = p.getFleet();
                if (t != null) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public void pick(PickPath currentPath, AffineTransform currentTransf, Point2D iPickPoint, List<PickPath> result) {
        super.pick(currentPath, currentTransf, iPickPoint, result);

    }
}
