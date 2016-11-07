package diplo.meta;

import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.xml.sax.*;

import java.util.Iterator;
import java.util.List;

public class MetaGame {

    public final List<MetaNation> nations = new ArrayList<MetaNation>();
    public final MetaMap map;

    public MetaGame(final String filename) {
        if (filename == null) {
            throw new IllegalArgumentException();
        }
        try {
            //-- Create a builder factory
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            final InputStream stream = getClass().getResourceAsStream(filename);
            final Document doc = factory.newDocumentBuilder().parse(stream);
            final Element elem = doc.getDocumentElement();
            final NodeList armyNodes = elem.getElementsByTagName("army");
            for (int i = 0; i < armyNodes.getLength(); i++) {
                nations.add(new MetaNation((Element) armyNodes.item(i)));
            }
            final NodeList mapNodes = elem.getElementsByTagName("map");
            map = new MetaMap((Element) mapNodes.item(0));
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MetaNation getMetaNation(String shortName) {
        for (final MetaNation nation : nations) {
            if (nation.shortName.compareToIgnoreCase(shortName) == 0) {
                return nation;
            }
        }
        throw new IllegalArgumentException();
    }
}
