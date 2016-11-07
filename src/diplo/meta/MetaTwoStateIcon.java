package diplo.meta;

import org.w3c.dom.Element;
import diplo.framework.MainPanel;
import java.awt.image.BufferedImage;

public class MetaTwoStateIcon {

    public String fileName[] = new String[2];
    public BufferedImage image[] = new BufferedImage[2];

    public MetaTwoStateIcon(Element elem) {
        fileName[0] = elem.getAttribute("default");
        fileName[1] = elem.getAttribute("highlight");
        image[0] = MainPanel.loadImage(fileName[0]);
        image[1] = MainPanel.loadImage(fileName[1]);
    }
}
