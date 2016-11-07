package diplo.visu;

import java.util.Iterator;
import java.awt.*;
import java.awt.event.*;
import diplo.framework.*;
import diplo.meta.*;

public class TemplateToolbar extends GraphicalComponent implements ComponentListener {

    @Override
    public void queryRedraw() {
        father.queryRedraw();
    }
    final Root father;

    TemplateToolbar(Root f, MetaGame g, MetaRessource r) {

        father = f;
        gameRule = g;
        Iterator it = gameRule.nations.iterator();
        double tx = 10.;
        double ty = 10.;
        double tymin = 5.;
        double tymax = 60.;

        TemplateMap tm = new TemplateMap(this, g.map);
        tm.before.translate(tx, tymin);
        tm.fit(tymax - tymin);
        tx += ((Rectangle.Double) tm.shape).getWidth() * tm.before.getScaleX() + 10.;
        childs.add(tm);

        while (it.hasNext()) {
            ty = tymin;
            MetaNation n = (MetaNation) it.next();
            //-- Flag
            TemplateTroop flag = new TemplateTroop(this, TroopType.Flag, n);
            flag.before.translate(tx, ty);
            ty += ((Rectangle.Double) flag.shape).getHeight() + 5.;
            childs.add(flag);

            //-- Fleet
            TemplateTroop fleet = new TemplateTroop(this, TroopType.Fleet, n);
            fleet.before.translate(tx, ty);
            ty += ((Rectangle.Double) fleet.shape).getHeight() + 5.;
            childs.add(fleet);

            //-- Army
            TemplateTroop army = new TemplateTroop(this, TroopType.Army, n);
            army.before.translate(tx, ty);
            ty += ((Rectangle.Double) army.shape).getHeight() + 5.;
            childs.add(army);

            tx += ((Rectangle.Double) army.shape).getWidth() + 10.;
        }

        StaticBean sb = new StaticBean(this, r);
        sb.before.translate(tx, 5.);
        sb.fit(ty - 10.);
        tx += ((Rectangle.Double) sb.shape).getWidth() * sb.before.getScaleX() + 10.;
        childs.add(sb);



        shape = new Rectangle.Double(0, 0, tx, ty);
        before.setToTranslation((father.getPannelWidth() - tx) * .5, 0);

        father.addComponentListener(this);
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        int tx = (int) ((Rectangle.Double) shape).width;
        Component a = e.getComponent();
        before.setToTranslation((a.getWidth() - tx) * .5, 0);
    }

    public void componentShown(ComponentEvent e) {
    }

    public Strategy getStrategy() {
        return father.getStrategy();
    }

    @Override
    public void draw(Graphics2D gfx) {
        gfx.setPaint(Color.darkGray);
        gfx.fill(shape);
        super.draw(gfx);
    }
    MetaGame gameRule;

    void querryRedraw() {
        father.querryRedraw();
    }
  
}
