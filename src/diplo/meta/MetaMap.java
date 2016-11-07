package diplo.meta;

import java.io.IOException;
import java.io.InputStream;
import java.lang.String;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import diplo.framework.MainPanel;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MetaMap {

    final String mapImageFN;
    final public BufferedImage mapImage;
    final String mapXMLFN;
    public final List<MetaProvince> provinces = new ArrayList<MetaProvince>();

    MetaMap(Element elem) {
        mapImageFN = elem.getAttribute("mapImage");
        mapXMLFN = elem.getAttribute("mapXML");
        mapImage = MainPanel.loadImage(mapImageFN);

        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            final InputStream stream = getClass().getResourceAsStream(mapXMLFN);
            final Document doc = factory.newDocumentBuilder().parse(stream);
            final Element eModel = doc.getDocumentElement();
            final NodeList list = elem.getElementsByTagName("province");
            for (int i = 0; i < list.getLength(); i++) {
                provinces.add(new MetaProvince((Element) list.item(i), eModel));
            }

        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
