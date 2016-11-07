package diplo.meta;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Element;

public class MetaAdjacency {

    public final TroopType type;
    public final String shortName;
    public final Set<String> ref;

    MetaAdjacency(Element elem) {
        String ts = elem.getAttribute("type");
        if (ts.compareToIgnoreCase("solid") == 0) {
            type = TroopType.Army;
        } else if (ts.compareToIgnoreCase("liquid") == 0) {
            type = TroopType.Fleet;
        } else {
            throw new IllegalArgumentException();
        }
        shortName = elem.getAttribute("shortName");
        ref = new HashSet<String>(Arrays.asList(elem.getAttribute("refs").split(" ")));
    }

    public boolean IsAdjacentTo(String place) {
        return ref.contains(place);
    }
}
