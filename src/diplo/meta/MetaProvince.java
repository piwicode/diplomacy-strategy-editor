package diplo.meta;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.*;

public class MetaProvince {

    final String fullName;
    final public boolean ressource;
    final public List<MetaPlace> places = new ArrayList<MetaPlace>();
    public Point2D center;

    boolean IsCoastal() {
        Iterator it = places.iterator();
        while (it.hasNext()) {
            if (((MetaPlace) it.next()).isLiquid()) {
                return true;
            }
        }
        return false;

    }

    MetaProvince(Element eModel, Element ePoly) {
        fullName = eModel.getAttribute("fullName");
        ressource = Boolean.parseBoolean(eModel.getAttribute("ressource"));

        final NodeList list = eModel.getElementsByTagName("place");
        for (int i = 0; i < list.getLength(); i++) {
            places.add(new MetaPlace(this, (Element) list.item(i), ePoly));
        }
    }
}
