package diplo.visu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import diplo.framework.*;
import diplo.meta.*;
import java.util.Vector;
import java.util.Iterator;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import org.w3c.dom.*;

public class Map extends DragableImageComponent {

    final MetaMap meta;
    public Vector<Map> maps = new Vector<Map>();
    public Vector<Province> provinces = new Vector<Province>();
    final BaseComponent father;
    final static BasicStroke fullLine = new BasicStroke(8.0f);
    Point2D impact = new Point2D.Double();

    public Map(BaseComponent f, final MetaMap meta, Point2D pos) {
        if (meta == null) {
            throw new IllegalArgumentException();
        }
        this.meta = meta;
        shape = new Rectangle.Float(0, 0, meta.mapImage.getWidth(), meta.mapImage.getHeight());

        father = f;
        Iterator it = meta.provinces.iterator();
        while (it.hasNext()) {
            provinces.add(new Province(this, (MetaProvince) it.next()));
        }
        childs.addAll(provinces);
        before.setToTranslation(pos.getX() - meta.mapImage.getWidth() * .5,
                pos.getY() - meta.mapImage.getHeight() * .5);
    }

    public Map(BaseComponent f, Map map) {
        this.meta = map.meta;
        this.father = f;
        shape = new Rectangle.Float(0, 0, meta.mapImage.getWidth(), meta.mapImage.getHeight());

        for (Province p : map.provinces) {
            provinces.add(new Province(this, p));
        }
        childs.addAll(provinces);
    }

    public Map(BaseComponent f, MetaGame mg, Element xmldata, MainPanel a) {
        this.meta = mg.map;
        this.father = f;
        shape = new Rectangle.Float(0, 0, meta.mapImage.getWidth(), meta.mapImage.getHeight());

        for (MetaProvince mp : mg.map.provinces) {
            provinces.add(new Province(this, mp));
        }
        childs.addAll(provinces);
        double x = Double.parseDouble(xmldata.getAttribute("x"));
        double y = Double.parseDouble(xmldata.getAttribute("y"));
        before.setToTranslation(x, y);

        for (Node node = xmldata.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getNodeName().compareToIgnoreCase("map") == 0) {
                final Map map = new Map(this, mg, (Element) node, a);
                maps.add(map);
                childs.add(map);
            }
            if (node.getNodeName().compareToIgnoreCase("troop") == 0) {
                final Element elem = (Element) node;
                if (elem.getAttribute("type").compareToIgnoreCase("Flag") == 0) {
                    Province prov = getPlace(elem.getAttribute("place")).father;
                    prov.DoCaptureRessource(mg.getMetaNation(elem.getAttribute("nation")));
                } else {
                    Place place = getPlace(elem.getAttribute("place"));
                    place.doCreateTroop(mg, elem);
                }
            }
        }
    }

    public Element BuidXML(Document document) {
        Element node = document.createElement("map");
        node.setAttribute("x", Double.toString(before.getTranslateX()));
        node.setAttribute("y", Double.toString(before.getTranslateY()));

        Iterator it = provinces.iterator();
        while (it.hasNext()) {
            Province p = (Province) it.next();
            Troop t = p.GetTroop();
            if (t != null) {
                node.appendChild(t.buidXML(document));
            }
            if (p.ressourceCenter != null) {
                node.appendChild(p.ressourceCenter.BuidXML(document));
            }
        }

        it = maps.iterator();
        while (it.hasNext()) {
            Map p = (Map) it.next();
            node.appendChild(p.BuidXML(document));
        }
        return node;
    }

    public void DoCreateMap(Point2D pos, TemplateMap tl) {
        Map map = new Map(this, tl.meta, pos);
        maps.add(map);
        childs.add(map);
    }

    @Override
    public BufferedImage getImage() {
        return meta.mapImage;
    }

    @Override
    public void draw(Graphics2D gfx) {

        if (dragTransform != null) {
            final AffineTransform prevTransf = gfx.getTransform();
            gfx.transform(dragTransform);
            gfx.draw(shape);
            gfx.setTransform(prevTransf);
        }

        for (final Map p : maps) {
            gfx.setPaint(Color.LIGHT_GRAY);
            int x[] = new int[4];
            int y[] = new int[4];
            int h = (int) shape.getBounds2D().getHeight();
            int w = (int) shape.getBounds2D().getWidth();
            int tx = (int) (p.before.getTranslateX());
            int ty = (int) (p.before.getTranslateY());
            x[0] = 0;
            y[0] = h;
            x[1] = w;
            y[1] = h;
            x[2] = x[1] + tx;
            y[2] = ty;
            x[3] = x[0] + tx;
            y[3] = ty;
            gfx.fillPolygon(x, y, 4);
        }

        super.draw(gfx);
    }

    @Override
    public boolean onPress(int button, int modifiers, PickPath me, Point2D point) {
        return button == MouseEvent.BUTTON1;
    }

    @Override
    public void onDrag(int button, int modifiers, PickPath me, List<PickPath> path, Point2D point, Point2D move) {
        if (button == MouseEvent.BUTTON1) {

            if ((modifiers & MouseEvent.SHIFT_MASK) != 0) {
                Point2D pt = new Point2D.Double();
                try {

                    me.getTransform().inverseTransform(point, pt);
                } catch (NoninvertibleTransformException i) {
                }
                if (dragTransform == null) {
                    dragTransform = new AffineTransform();
                }
                dragTransform.setToTranslation(pt.getX() - (shape.getBounds2D().getWidth() / 2),
                        pt.getY() - (shape.getBounds2D().getHeight() / 2));
                queryRedraw();
            } else {
                if (dragTransform != null) {
                    before.concatenate(dragTransform);
                    dragTransform = null;
                }
                Point2D pt = new Point2D.Double();
                try {

                    AffineTransform tr = me.getTransform().createInverse();
                    tr.deltaTransform(move, pt);
                    before.translate(pt.getX(), pt.getY());
                } catch (NoninvertibleTransformException i) {
                }
                queryRedraw();
            }
            ProcessInteraction(button, modifiers, path, true);
        }
    }

    @Override
    public void onRelease(int button, int modifiers, List<PickPath> path, Point2D point) {
        if (button == MouseEvent.BUTTON1) {
            if (dragTransform != null) {
                final Map m = new Map(this, this.meta, new Point2D.Double(dragTransform.getTranslateX(), dragTransform.getTranslateY()));
                m.before.setTransform(dragTransform);
                childs.add(m);
                maps.add(m);
                dragTransform = null;
            }
            ProcessInteraction(button, modifiers, path, false);
        }
        queryRedraw();
    }

    boolean doClearMap(boolean simul) {
        if (simul) {
            return true;
        }
        father.childs.remove(this);
        if (father.getClass().equals(Map.class)) {
            ((Map) father).maps.remove(this);
        }
        return true;
    }

    @Override
    boolean doInteraction(int button, int modifiers, BaseComponent c, boolean simul) {
        if (c.getClass().equals(StaticBean.class)
                && doClearMap(simul)) {
            return true;
        }
        return false;
    }

    void doReviewOrders() {
        Iterator it = provinces.iterator();
        while (it.hasNext()) {
            ((Province) it.next()).DoReviewOrders();
        }
    }

    Place getPlace(String name) {
        Iterator it = provinces.iterator();
        while (it.hasNext()) {
            Place p = ((Province) it.next()).GetPlace(name);
            if (p != null) {
                return p;
            }
        }
        return null;
    }

    @Override
    public boolean onClick(int button, int modifiers, int clickCount, PickPath me, Point2D point) {
        if (button == 1 && clickCount > 1) {
            AffineTransform t = me.getTransform();

            Point2D tmp = new Point2D.Double(shape.getBounds2D().getCenterX(),
                    shape.getBounds2D().getCenterY());
            Point2D c = new Point2D.Double();
            t.transform(tmp, c);

            tmp.setLocation(shape.getBounds().getWidth(), shape.getBounds().getHeight());
            Point2D d = new Point2D.Double();
            t.deltaTransform(tmp, d);

            //FIXME
            //MainPanel.getApplet().doReframeOn(c, d.getX(), d.getY());
            return true;
        }
        return false;
    }

    public void queryRedraw() {
        father.queryRedraw();
    }
}
