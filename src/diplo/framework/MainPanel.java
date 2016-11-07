package diplo.framework;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ListIterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import diplo.meta.*;
import diplo.visu.Root;
import diplo.visu.Strategy;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import space.app.common.DbgError;

public class MainPanel extends JPanel {

    final private Root root;
    final private MetaGame gameRule;
    final MetaRessource gameRessource;
    Strategy strategy;

    public static BufferedImage loadImage(String resource) {
        try {
            return ImageIO.read(MainPanel.class.getResource(resource));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public MainPanel() {
        new MouseButtonManager(MouseEvent.BUTTON1);
        new MouseButtonManager(MouseEvent.BUTTON2);
        new MouseButtonManager(MouseEvent.BUTTON3);

        //-- Creation of the game rule structure
        gameRule = new MetaGame("/res/gamerule.xml");
        gameRessource = new MetaRessource("/res/ressource.xml");

        //-- Creation of the visu tree
        root = new Root(this, gameRule, gameRessource);
        strategy = new Strategy(gameRule, this);
        root.SetStrategy(strategy);
    }

    public void doReframeOn(Point2D center, double w, double h) {
        root.getFrame().doReframeOn(center, w, h);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        root.draw((Graphics2D) g);

    }

    public void clearStrategy() {
        strategy = new Strategy(gameRule, this);
        root.SetStrategy(strategy);
        repaint();
    }

    public Preferences preferenceNode() {
        return Preferences.userNodeForPackage(getClass());
    }

    void load(File selectedItem) {
        // read the file
        System.out.println("reading file " + selectedItem);
        try {
            final FileInputStream fis = new FileInputStream(selectedItem);
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            final Document doc = factory.newDocumentBuilder().parse(fis);
            final Element xmlroot = doc.getDocumentElement();
            strategy = new Strategy(gameRule, this, xmlroot);
            root.SetStrategy(strategy);
            repaint();
        } catch (FileNotFoundException e) {
            DbgError.report(e);
        } catch (ParserConfigurationException e) {
            DbgError.report(e);
        } catch (IOException e) {
            DbgError.report(e);
        } catch (SAXException e) {
            DbgError.report(e);
        }
    }

    /**
     * Save the current strategy to a file
     * @param outputFile not null refetence to a file where the user has write rights
     */
    void save(final File outputFile) {
        if (outputFile == null) {
            throw new IllegalArgumentException();
        }
        // read the file
        System.out.println("writing file " + outputFile);
        try {
            // Création d'un nouveau DOM
            final DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
            final DocumentBuilder constructeur = fabrique.newDocumentBuilder();
            final Document document = constructeur.newDocument();

            // Propri�t�s du DOM
            document.setXmlVersion("1.0");
            document.setXmlStandalone(true);

            // Cr�ation de l'arborescence du DOM
            Element racine = strategy.BuidXML(document);
            document.appendChild(racine);

            final DOMSource domSource = new DOMSource(document);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);


            final FileWriter out = new FileWriter(outputFile);
            out.write(writer.toString());
            out.close();
        } catch (FileNotFoundException e) {
            DbgError.report(e);
        } catch (ParserConfigurationException e) {
            DbgError.report(e);
        } catch (TransformerException e) {
            DbgError.report(e);
        } catch (IOException e) {
            DbgError.report(e);
        }
    }

    class MouseButtonManager implements MouseMotionListener, MouseListener {

        int handeledButton;
        PickPath mousePressedObject = null;
        Point2D lastPosition = new Point2D.Float();
        boolean pressed;

        MouseButtonManager(int b) {
            handeledButton = b;
            addMouseMotionListener(this);
            addMouseListener(this);
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
            if (!pressed) {
                return;
            }
            if (mousePressedObject != null) {
                final List<PickPath> result = pickQuerry(root, lastPosition);
                BaseComponent c = mousePressedObject.getTarget();
                Point2D pt = new Point2D.Double(e.getX() - lastPosition.getX(),
                        e.getY() - lastPosition.getY());
                c.onDrag(handeledButton, e.getModifiers(), mousePressedObject,
                        result, e.getPoint(), pt);
            }
            lastPosition.setLocation(e.getX(), e.getY());
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() != handeledButton) {
                return;
            }

            lastPosition.setLocation(e.getX(), e.getY());
            final List<PickPath> result = pickQuerry(root, lastPosition);
            final ListIterator it = result.listIterator(result.size());
            while (it.hasPrevious()) {
                mousePressedObject = (PickPath) it.previous();
                BaseComponent c = mousePressedObject.getTarget();
                if (c.onClick(handeledButton, e.getModifiers(), e.getClickCount(),
                        mousePressedObject, lastPosition)) {
                    return;
                }
            }
            mousePressedObject = null;
        }

        public void mousePressed(MouseEvent e) {
            if (e.getButton() != handeledButton) {
                return;
            }
            pressed = true;
            lastPosition.setLocation(e.getX(), e.getY());
            final List<PickPath> result = pickQuerry(root, lastPosition);
            ListIterator it = result.listIterator(result.size());
            while (it.hasPrevious()) {
                mousePressedObject = (PickPath) it.previous();
                BaseComponent c = mousePressedObject.getTarget();
                if (c.onPress(handeledButton, e.getModifiers(), mousePressedObject, lastPosition)) {

                    return;
                }
            }
            mousePressedObject = null;
        }

        public void mouseReleased(MouseEvent e) {
            if (e.getButton() != handeledButton) {
                return;
            }
            pressed = false;
            lastPosition.setLocation(e.getX(), e.getY());
            if (mousePressedObject == null) {
                return;
            }
            final List<PickPath> result = pickQuerry(root, lastPosition);
            final BaseComponent c = mousePressedObject.getTarget();
            c.onRelease(handeledButton, e.getModifiers(), result, lastPosition);

        }

        private List<PickPath> pickQuerry(final Root root, final Point2D lastPosition) {
            final PickPath pp = new PickPath();
            final AffineTransform af = new AffineTransform();
            final List<PickPath> result = new ArrayList<PickPath>();
            root.pick(pp, af, lastPosition, result);
            return result;
        }
    }

    public void queryRedraw() {
        repaint();
    }
}
