package diplo.visu;

import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import diplo.framework.*;
import diplo.meta.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class TemplateMap extends DragableImageComponent {
    @Override
    public void queryRedraw() {
        father.queryRedraw();
    }
    final MetaMap meta;
    final TemplateToolbar father;

    TemplateMap(TemplateToolbar f, MetaMap m) {
        father = f;
        meta = m;
        build();
    }

    @Override
    public BufferedImage getImage() {
        return meta.mapImage;
    }

    public Strategy getStrategy() {
        return father.getStrategy();
    }

    @Override
    public void onRelease(int button, int modifiers, List<PickPath> path, Point2D point) {
        super.onRelease(button, modifiers, path, point);
        if (button == MouseEvent.BUTTON1) {
            if (ProcessInteraction(button, modifiers, path, false) == false) {
                Root r = father.father;
                PickPath p = new PickPath();
                p.push(r);
                p.push(r.childs.get(0));
                p.push(r.getStrategy());
                Point2D ptDst = new Point2D.Double();
                try {
                    p.getTransform().inverseTransform(point, ptDst);
                } catch (NoninvertibleTransformException i) {
                }
                r.getStrategy().DoCreateMap(ptDst, this);
            }
        }
    }

    @Override
    boolean doInteraction(int button, int modifiers, BaseComponent c, boolean simul) {
        if (c.getClass().equals(StaticBean.class)) {
            return true;
        }
        if (c.getClass().equals(TemplateToolbar.class)) {
            return true;
        }
        return false;
    }
}
