package diplo.visu;

import java.awt.event.*;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.*;
import diplo.framework.*;
import diplo.meta.MetaGame;
import java.util.List;

public class Frame extends GraphicalComponent implements ComponentListener {

    final Root root;
    boolean inDrag = false;
    Point2D Center = new Point2D.Double();

    Frame(Root root, MetaGame g, double w, double h) {
        if (root == null) {
            throw new IllegalArgumentException();
        }
        this.root = root;

        after.scale(.1, .1);
        setSize(root.getPannelWidth(), root.getPannelHeight());
        root.addComponentListener(this);
    }
    // Set the size of mouse interception area

    void setSize(double w, double h) {
        shape = new Rectangle2D.Double(0., 0., w, h);
    }
    // ComponentListener::componentHidden

    public void componentHidden(ComponentEvent e) {
    }
    // ComponentListener::componentMoved

    public void componentMoved(ComponentEvent e) {
    }
    // ComponentListener::componentResized

    public void componentResized(ComponentEvent e) {
        Component c = e.getComponent();
        setSize(c.getWidth(), c.getHeight());
    }
    // ComponentListener::componentShown

    public void componentShown(ComponentEvent e) {
    }

    public void doReframeOn(Point2D center, double w, double h) {
        System.out.println("coord:" + center.getX() + "," + center.getY());
        double hmin = 65;
        double hmax = 5;
        double wmin = 5;
        double wmax = 5;
        double ww = ((Rectangle2D) shape).getWidth() - wmin - wmax;
        double wh = ((Rectangle2D) shape).getHeight() - hmin - hmax;
        double ratio = 1;
        if (w / h > ww / wh) {
            ratio = ww / w;
        } else {
            ratio = wh / h;
        }

        AffineTransform t = new AffineTransform();
        t.setToTranslation(-center.getX(), -center.getY());
        after.preConcatenate(t);
        t.setToScale(ratio, ratio);
        after.preConcatenate(t);
        t.setToTranslation(wmin + ww * .5, hmin + wh * .5);
        after.preConcatenate(t);
    }

    @Override
    public boolean onPress(int button, int modifiers, PickPath path, Point2D point) {
        if (button == MouseEvent.BUTTON2 || button == MouseEvent.BUTTON3) {
            inDrag = true;
            return true;
        } else {
            return false;
        }
    }

    ;

    @Override
    public void onDrag(int button, int modifiers, PickPath me, List<PickPath> path, Point2D point, Point2D move) {
        if (button == MouseEvent.BUTTON2 || button == MouseEvent.BUTTON3) {
            Point2D pt = move;
            //*pt = me.getTransform().deltaTransform(move, pt);
            if (button == MouseEvent.BUTTON2) {
                final AffineTransform t = new AffineTransform();
                t.translate(pt.getX(), pt.getY());
                t.concatenate(after);
                after.setTransform(t);
            }
            if (button == MouseEvent.BUTTON3) {
                double s = Math.exp(pt.getY() / 100.);

                final AffineTransform t = new AffineTransform();
                t.translate(root.getPannelWidth() * .5, root.getPannelHeight() * .5);
                t.scale(s, s);
                t.translate(-root.getPannelWidth() / 2., -root.getPannelHeight() / 2.);
                t.concatenate(after);
                after.setTransform(t);

            }
            root.querryRedraw();
        }
    }

    @Override
    public void onRelease(int button, int modifiers, List<PickPath> path, Point2D point) {

        if (button == MouseEvent.BUTTON2 || button == MouseEvent.BUTTON3) {
            inDrag = false;
            queryRedraw();
        }

    }

    public void queryRedraw() {
        root.querryRedraw();
    }

    public void Draw(Graphics2D gfx, AffineTransform t2D) {
        if (inDrag) {
            gfx.translate(((Rectangle2D.Double) shape).getWidth() / 2.,
                    ((Rectangle2D.Double) shape).getHeight() / 2.);
            gfx.drawLine(0, 10, 0, 20);
            gfx.drawLine(0, -10, 0, -20);
            gfx.drawLine(10, 0, 20, 0);
            gfx.drawLine(-10, 0, -20, 0);
        }


    }
}
