package diplo.visu;

import diplo.framework.BaseComponent;
import diplo.framework.MainPanel;
import diplo.meta.*;
import java.awt.event.ComponentListener;

public class Root extends BaseComponent {

    final MainPanel mainPanel;

    @Override
    public void queryRedraw() {
        mainPanel.queryRedraw();
    }

    public void SetStrategy(final BaseComponent c) {
        ((BaseComponent) childs.get(0)).childs.clear();
        ((BaseComponent) childs.get(0)).childs.add(c);
    }

    public Strategy getStrategy() {
        return (Strategy) ((BaseComponent) childs.get(0)).childs.get(0);
    }

    public Root(MainPanel mainPanel, MetaGame g, MetaRessource r) {
        if (mainPanel == null) {
            throw new IllegalArgumentException();
        }
        this.mainPanel = mainPanel;
        childs.add(new Frame(this, g, 1152, 962));
        childs.add(new TemplateToolbar(this, g, r));
    }

    public void querryRedraw() {
        mainPanel.queryRedraw();
    }

    public Frame getFrame() {
        return (Frame) childs.get(0);
    }


    void addComponentListener(ComponentListener aThis) {
        mainPanel.addComponentListener(aThis);
    }

    double getPannelWidth() {
        return mainPanel.getWidth();
    }

    double getPannelHeight() {
        return mainPanel.getHeight();
    }
}
