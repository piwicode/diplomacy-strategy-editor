package diplo.visu;

import diplo.framework.*;
import diplo.meta.*;
import java.awt.geom.Point2D;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Strategy extends BaseComponent {

    MainPanel panel;

    public Strategy(MetaGame mg, MainPanel a) {
        if (a == null) {
            throw new IllegalArgumentException();
        }
        panel = a;
    }

    public Strategy(MetaGame mg, MainPanel a, Element xmldata) {
        if (a == null) {
            throw new IllegalArgumentException();
        }
        panel = a;
        for (Node node = xmldata.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeName().compareToIgnoreCase("map") == 0) {
                childs.add(new Map(this, mg, (Element) node, a));
            }
        }
    }

    public Element BuidXML(Document document) {
        Element node = document.createElement("strategy");
        Iterator it = childs.iterator();
        while (it.hasNext()) {
            node.appendChild(((Map) it.next()).BuidXML(document));
        }
        return node;
    }

    public void DoCreateMap(Point2D pos, TemplateMap tl) {
        childs.add(new Map((BaseComponent) this, tl.meta, pos));
    }

    @Override
    public void queryRedraw() {
        panel.queryRedraw();
    }
}
