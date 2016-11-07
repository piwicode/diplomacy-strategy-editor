package diplo.meta;

import diplo.framework.MainPanel;
import org.w3c.dom.Element;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class MetaNation {

    final String fullName;
    final public String shortName;
    final String flagFN;
    final String armyFN;
    final String fleetFN;
    final public Color color;
    final public Map<TroopType, BufferedImage> image;

    MetaNation(Element elem) {

        //-- Parse XML
        fullName = elem.getAttribute("fullName");
        shortName = elem.getAttribute("shortName");
        flagFN = elem.getAttribute("flagFN");
        armyFN = elem.getAttribute("armyFN");
        fleetFN = elem.getAttribute("fleetFN");
        color = Color.decode(elem.getAttribute("color"));
        //-- Load images througt the network
        final EnumMap<TroopType, BufferedImage> enumMap = new EnumMap<TroopType, BufferedImage>(TroopType.class);
        enumMap.put(TroopType.Flag, MainPanel.loadImage(flagFN));
        enumMap.put(TroopType.Army, MainPanel.loadImage(armyFN));
        enumMap.put(TroopType.Fleet, MainPanel.loadImage(fleetFN));
        image = Collections.unmodifiableMap(enumMap);
    }
}

