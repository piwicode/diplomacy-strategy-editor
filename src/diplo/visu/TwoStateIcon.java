package diplo.visu;

import java.awt.*;
import diplo.meta.MetaTwoStateIcon;
import diplo.framework.*;
import java.awt.image.BufferedImage;

public class TwoStateIcon extends ImageComponent {

    final TemplateToolbar toolbar;
    final MetaTwoStateIcon meta;

    @Override
    public void queryRedraw() {
        toolbar.queryRedraw();
    }

    public TwoStateIcon(TemplateToolbar toolbar, MetaTwoStateIcon m) {
        if (toolbar == null) {
            throw new IllegalArgumentException();
        }
        this.toolbar = toolbar;
        meta = m;
        build();
    }

    @Override
    public BufferedImage getImage() {
        return meta.image[highlighted ? 1 : 0];
    }

    @Override
    public void draw(Graphics2D gfx) {
        gfx.drawImage(getImage(), 0, 0, null);

        super.draw(gfx);
    }
}
