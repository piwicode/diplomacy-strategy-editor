package diplo.visu;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ListIterator;
import diplo.framework.*;
import java.util.List;

public abstract class DragableImageComponent extends ImageComponent {

    AffineTransform dragTransform = null;

    public DragableImageComponent() {
    }

    

    @Override
    public void draw(Graphics2D gfx) {
        gfx.drawImage(getImage(), 0, 0, null);
        if (dragTransform != null) {
            gfx.drawImage(getImage(), dragTransform, null);
        }
        super.draw(gfx);
    }

    //-- Picking querry interface
    @Override
    public boolean onClick(int button, int modifiers, int clickCount, PickPath path, Point2D point) {
        return true;//Not transparent to click events
    }

    @Override
    public boolean onPress(int button, int modifiers, PickPath path, Point2D point) {
        if (button == MouseEvent.BUTTON1) {
            dragTransform = new AffineTransform();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDrag(int button, int modifiers, final PickPath me,
            final List<PickPath> path, final Point2D point, final Point2D move) {
        if (button == MouseEvent.BUTTON1) {
            Point2D pt = new Point2D.Double();
            try {                
                me.getTransform().inverseTransform(point, pt);
            } catch (NoninvertibleTransformException i) {
            }
            dragTransform.setToTranslation(pt.getX() - ((Rectangle.Double) shape).width / 2,
                    pt.getY() - ((Rectangle.Double) shape).height / 2);
            queryRedraw();
            ProcessInteraction(button, modifiers, path, true);
        }
    }

    @Override
    public void onRelease(int button, int modifiers, List<PickPath> path, Point2D point) {

        if (button == MouseEvent.BUTTON1) {
            ProcessInteraction(button, modifiers, path, false);
            queryRedraw();
        }
        dragTransform = null;
    }

    boolean ProcessInteraction(int button, int modifiers, List<PickPath> path, boolean simul) {
        final ListIterator it1 = path.listIterator(path.size());
        while (it1.hasPrevious()) {
            BaseComponent c = ((PickPath) it1.previous()).getTarget();
            if (doInteraction(button, modifiers, c, true)) {
                if (simul || doInteraction(button, modifiers, c, false)) {
                    try {
                        ((GraphicalComponent) c).highlighted = simul;
                    } catch (java.lang.ClassCastException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }

    boolean doInteraction(int button, int modifiers, BaseComponent c, boolean simul) {
        return false;
    }
}
