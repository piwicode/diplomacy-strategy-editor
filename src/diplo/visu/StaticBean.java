package diplo.visu;

import diplo.meta.*;

public class StaticBean extends TwoStateIcon {

    public StaticBean(TemplateToolbar toolbar, MetaRessource m) {
        super(toolbar, m.TwoStateIcon.get("bean"));
    }
}
