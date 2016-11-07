package diplo.framework;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.List;

public abstract class GraphicalComponent extends BaseComponent {

    public Shape shape;
    public boolean highlighted = false;

    public GraphicalComponent() {
    }

    @Override
    public void draw(Graphics2D gfx) {
        super.draw(gfx);
        highlighted = false;
    }

    @Override
    public void pick(final PickPath currentPath,
            final AffineTransform currentTransf,
            final Point2D iPickPoint,
            final List<PickPath> result) {

        final Point2D p = new Point2D.Double();
        try {
            currentTransf.inverseTransform(iPickPoint, p);
            if (shape != null && shape.contains(p)) {
                result.add(new PickPath(currentPath));
            }
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }

        super.pick(currentPath, currentTransf, iPickPoint, result);
    }
}
