package diplo.meta;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MetaPlace {

    public final MetaProvince father;
    public final Polygon polygon = new Polygon();
    public final String name;
    public Point2D center;
    public final Color fillColor;
    public final Color lineColor;
    public final List<MetaAdjacency> adjacencies = new ArrayList<MetaAdjacency>();

    public boolean IsAdjacentTo(String place, TroopType type) {
        Iterator it = adjacencies.iterator();
        while (it.hasNext()) {
            MetaAdjacency ma = (MetaAdjacency) it.next();
            if (ma.type == type && ma.IsAdjacentTo(place)) {
                return true;
            }
        }
        return false;
    }

    MetaPlace(MetaProvince f, Element eModel, Element ePoly) {
        father = f;
        name = eModel.getAttribute("name");
        NodeList list = eModel.getElementsByTagName("adjacency");
        for (int i = 0; i < list.getLength(); i++) {
            adjacencies.add(new MetaAdjacency((Element) list.item(i)));
        }

        //-- load polygon			
        list = ePoly.getElementsByTagName("place");
        for (int i = 0; i < list.getLength(); i++) {
            final Element elem = (Element) list.item(i);
            String tname = elem.getAttribute("name");
            if (tname.compareToIgnoreCase(name) != 0) {
                continue;
            }

            try {
                double x = Integer.parseInt(elem.getAttribute("supply_x"));
                double y = Integer.parseInt(elem.getAttribute("supply_y"));
                father.center = new Point2D.Double(x, y);
            } catch (java.lang.NumberFormatException e) {
            }

            fillColor = new Color(Integer.parseInt(elem.getAttribute("fill")));
            lineColor = new Color(Integer.parseInt(elem.getAttribute("line")));
            final NodeList nl = elem.getElementsByTagName("vertex");
            for (int j = 0; j < nl.getLength(); j++) {
                Element e = (Element) nl.item(j);
                int xx = Integer.parseInt(e.getAttribute("x"));
                int yy = Integer.parseInt(e.getAttribute("y"));
                polygon.addPoint(xx, yy);
            }
            try {
                double x = Integer.parseInt(elem.getAttribute("x"));
                double y = Integer.parseInt(elem.getAttribute("y"));
                center = new Point2D.Double(x, y);
            } catch (java.lang.NumberFormatException e) {
                throw new RuntimeException(e);
            }

            return;
        }
        throw new IllegalArgumentException();

    }

    public boolean isSolid() {
        for (final MetaAdjacency adjacency : adjacencies) {
            if (adjacency.type == TroopType.Army) {
                return true;
            }
        }
        return false;
    }

    public boolean isLiquid() {
        for (final MetaAdjacency adjacency : adjacencies) {
            if (adjacency.type == TroopType.Fleet) {
                return true;
            }
        }
        return false;
    }
}
