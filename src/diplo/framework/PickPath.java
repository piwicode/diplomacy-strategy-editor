package diplo.framework;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PickPath {

    private final List<BaseComponent> path = new ArrayList<BaseComponent>();

    public PickPath() {
    }

    public PickPath(PickPath p) {
        path.addAll(p.path);
    }

    public void push(BaseComponent i) {
        path.add(i);

    }

    public void pop() {
        path.remove(path.size() - 1);

    }

    public BaseComponent getTarget() {
        return path.get(path.size() - 1);
    }

    public AffineTransform getTransform() {
        final AffineTransform t2D = new AffineTransform();
        final Iterator<BaseComponent> iterator = path.iterator();
        while (iterator.hasNext()) {
            BaseComponent c = iterator.next();
            t2D.concatenate(c.before);
            if (iterator.hasNext() == false) {
                break;
            }
            t2D.concatenate(c.after);
        }
        return t2D;
    }
}
