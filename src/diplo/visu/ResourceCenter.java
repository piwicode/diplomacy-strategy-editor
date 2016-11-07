package diplo.visu;

import java.awt.Rectangle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import diplo.framework.BaseComponent;
import diplo.meta.*;
import java.awt.image.BufferedImage;

public class ResourceCenter extends DragableImageComponent {

    final private Province province;
    final private MetaNation nation;

    public ResourceCenter copyResourceCenter(Province father) {
        return new ResourceCenter(father, nation);
    }

    ResourceCenter(Province father, MetaNation t) {
        province = father;
        nation = t;
        if (province.meta.center != null) {
            before.translate(province.meta.center.getX(), province.meta.center.getY());
        }
        build();
        before.translate(-((Rectangle.Double) shape).width * .5,
                -((Rectangle.Double) shape).height * .5);
    }

    public Element BuidXML(Document document) {
        Element node = document.createElement("troop");
        node.setAttribute("place", province.meta.places.get(0).name);
        node.setAttribute("nation", nation.shortName);
        node.setAttribute("type", "Flag");
        return node;
    }
//	-- Constructeur

    @Override
    public BufferedImage getImage() {
        return nation.image.get(TroopType.Flag);
    }

    @Override
    boolean doInteraction(int button, int modifiers, BaseComponent c, boolean simul) {
        if (c.getClass().equals(StaticBean.class)
                && province.DoClearRessource(simul)) {
            return true;
        }
        if (c.getClass().equals(Place.class)
                && ((Place) c).doDropTroop(TroopType.Flag, nation, simul)
                && province.DoClearRessource(simul)) {
            return true;
        }
        return false;
    }

    @Override
    public void queryRedraw() {
        province.queryRedraw();
    }
}
