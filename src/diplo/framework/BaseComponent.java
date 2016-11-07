package diplo.framework;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseComponent {

    public final List<BaseComponent> childs = new ArrayList<BaseComponent>();
    public final AffineTransform before = new AffineTransform();
    public final AffineTransform after = new AffineTransform();

    //Picking querry interface
    public boolean onClick(int button, int modifiers, int clickCount, PickPath me, Point2D point) {
        return false;
    }

    public boolean onPress(int button, int modifiers, PickPath me, Point2D point) {
        return false;
    }

    public void onDrag(int button, int modifiers, PickPath me, List<PickPath> under, Point2D point, Point2D move) {
    }

    public void onRelease(int button, int modifiers, List<PickPath> path, Point2D point) {
    }

    public void draw(Graphics2D gfx) {
        final AffineTransform prevTransf0 = gfx.getTransform();
        gfx.transform(after);
        final AffineTransform prevTransf1 = gfx.getTransform();
        for (BaseComponent c : childs) {
            gfx.setTransform(prevTransf1);
            gfx.transform(c.before);
            c.draw(gfx);
        }
        gfx.setTransform(prevTransf0);
    }

    public void pick(final PickPath currentPath,
            final AffineTransform currentTransf,
            final Point2D iPickPoint,
            final List<PickPath> result) {
        if (currentPath == null) {
            throw new IllegalArgumentException();
        }
        if (currentTransf == null) {
            throw new IllegalArgumentException();
        }
        if (iPickPoint == null) {
            throw new IllegalArgumentException();
        }
        if (result == null) {
            throw new IllegalArgumentException();
        }
        final AffineTransform prefTransf0 = new AffineTransform(currentTransf);
        currentTransf.concatenate(after);
        final AffineTransform prefTransf1 = new AffineTransform(currentTransf);
        for (final BaseComponent c : childs) {
            currentTransf.setTransform(prefTransf1);
            currentTransf.concatenate(c.before);
            currentPath.push(c);
            c.pick(currentPath, currentTransf, iPickPoint, result);
            currentPath.pop();
        }
        currentTransf.setTransform(prefTransf0);
    }

    abstract public void queryRedraw();
}
