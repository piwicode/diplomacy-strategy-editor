package diplo.framework;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ImageComponent extends GraphicalComponent {

    public ImageComponent() {
    }

    public void build() {
        shape = new Rectangle.Double(0, 0,
                getImage().getWidth(),
                getImage().getHeight());
    }

    public void fit(double h) {
        double s1 = h / ((Rectangle.Double) shape).getHeight();
        before.scale(s1, s1);
    }

    public abstract BufferedImage getImage();

    @Override
    public void draw(Graphics2D gfx) {
        gfx.drawImage(getImage(),0,0,null);
        super.draw(gfx);
    }
}
