package diplo.visu;

import diplo.framework.*;
import diplo.meta.*;
import java.awt.image.BufferedImage;

public class TemplateTroop extends DragableImageComponent {

    final TemplateToolbar toolbar;
    public final MetaNation metaNation;
    public final TroopType type;

    @Override
    public void queryRedraw() {
        toolbar.queryRedraw();
    }

    TemplateTroop(TemplateToolbar toolbar, TemplateTroop t) {
        this.toolbar = toolbar;
        shape = t.shape;
        metaNation = t.metaNation;
        type = t.type;
    }

    TemplateTroop(TemplateToolbar toolbar, TroopType t, MetaNation n) {
        this.toolbar = toolbar;
        type = t;
        metaNation = n;
        build();
    }

    //-- Constructeur
    @Override
    public BufferedImage getImage() {
        return metaNation.image.get(type);
    }

    @Override
    boolean doInteraction(int button, int modifiers, BaseComponent c, boolean simul) {
        if (c.getClass().equals(StaticBean.class)) {
            return true;
        }
        if (c.getClass().equals(TemplateToolbar.class)) {
            return true;
        }
        if (c.getClass().equals(Place.class)
                && ((Place) c).doDropTroop(type, metaNation, simul)) {
            return true;
        }
        return false;
    }
}
