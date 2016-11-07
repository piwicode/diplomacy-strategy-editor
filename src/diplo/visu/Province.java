package diplo.visu;

import java.util.Iterator;
import java.util.Vector;
import diplo.meta.*;
import diplo.framework.*;

public class Province extends BaseComponent {

    final MetaProvince meta;
    final Map father;
    Troop owner;
    ResourceCenter ressourceCenter = null;
    final public Vector<Place> places = new Vector<Place>();

    @Override
    public void queryRedraw() {
        father.queryRedraw();
    }

    Map GetMap() {
        return father;
    }

    Province(final Map map, final Province province) {
        if (map == null) {
            throw new IllegalArgumentException();
        }
        if (province == null) {
            throw new IllegalArgumentException();
        }
        father = map;
        meta = province.meta;
        for (Place place : province.places) {
            places.add(new Place(this, place));
        }
        childs.addAll(places);
        if (province.ressourceCenter != null) {
            ressourceCenter = province.ressourceCenter.copyResourceCenter(this);
            childs.add(ressourceCenter);
        }
    }

    Province(Map f, MetaProvince province) {
        meta = province;
        father = f;
        Iterator it = meta.places.iterator();
        while (it.hasNext()) {
            places.add(new Place(this, (MetaPlace) it.next()));
        }
        childs.addAll(places);
    }

    boolean DoClearTroop(boolean simul) {
        Iterator it = places.iterator();
        while (it.hasNext()) {
            if (!((Place) it.next()).doClearTroop(simul)) {
                return false;
            }
        }
        return true;
    }

    public boolean DoClearRessource(boolean simul) {
        if (simul) {
            return true;
        }
        childs.remove(ressourceCenter);
        ressourceCenter = null;
        return true;
    }

    public boolean DoCaptureRessource(MetaNation nation, boolean simul) {
        if (!meta.ressource) {
            return false;
        }

        if (!DoClearRessource(simul)) {
            return false;
        }
        if (simul) {
            return true;
        }
        ressourceCenter = new ResourceCenter(this, nation);
        childs.add(ressourceCenter);
        return true;
    }

    public boolean DoCaptureRessource(MetaNation mn) {
        if (!meta.ressource) {
            return false;
        }
        if (!DoClearRessource(false)) {
            return false;
        }
        ressourceCenter = new ResourceCenter(this, mn);
        childs.add(ressourceCenter);
        return true;
    }

    void DoReviewOrders() {
        Iterator it = places.iterator();
        while (it.hasNext()) {
            ((Place) it.next()).doReviewOrders();
        }
    }

    Troop GetFleet() {
        Iterator it = places.iterator();
        while (it.hasNext()) {
            Troop t = ((Place) it.next()).getFleet();
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    Troop GetTroop() {
        Iterator it = places.iterator();
        while (it.hasNext()) {
            Troop t = ((Place) it.next()).troop;
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    Troop GetNeighborFleet() {
        Iterator it = places.iterator();
        while (it.hasNext()) {
            Troop t = ((Place) it.next()).getNeighborFleet();
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    Place GetPlace(String name) {
        Iterator it = places.iterator();
        while (it.hasNext()) {
            Place p = ((Place) it.next());
            if (p.meta.name.compareToIgnoreCase(name) == 0) {
                return p;
            }
        }
        return null;
    }
}
