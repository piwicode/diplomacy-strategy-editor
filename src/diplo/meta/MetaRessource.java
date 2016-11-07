package diplo.meta;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.Hashtable;

public class MetaRessource {

    public Hashtable<String, MetaTwoStateIcon> TwoStateIcon =
            new Hashtable<String, MetaTwoStateIcon>();

    public MetaRessource(String filename) {
        try {
            //-- Create a builder factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            InputStream stream = getClass().getResourceAsStream(filename);
            Document doc = factory.newDocumentBuilder().parse(stream);
            Element root = doc.getDocumentElement();

            NodeList list = root.getElementsByTagName("icon");
            for (int i = 0; i < list.getLength(); i++) {
                Element elem = (Element) list.item(i);
                String key = elem.getAttribute("name");
                MetaTwoStateIcon obj = new MetaTwoStateIcon(elem);
                TwoStateIcon.put(key, obj);
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
