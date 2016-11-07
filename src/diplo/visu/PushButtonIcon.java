package diplo.visu;

import java.awt.geom.Point2D;

import diplo.meta.MetaTwoStateIcon;
import diplo.framework.*;
import java.util.List;

public abstract class PushButtonIcon extends TwoStateIcon {

    public PushButtonIcon(TemplateToolbar toolbar, MetaTwoStateIcon m) {
        super(toolbar, m);
        if (toolbar == null) {
            throw new IllegalArgumentException();
        }


    }

    @Override
    public boolean onPress(int button, int modifiers, PickPath me, Point2D point) {
        toolbar.querryRedraw();
        highlighted = true;
        return true;
    }

    @Override
    public void onRelease(int button, int modifiers, List<PickPath> path, Point2D point) {
        toolbar.querryRedraw();
        highlighted = false;
    }
}
