/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainFrame.java
 *
 * Created on 26 mars 2011, 09:30:40
 */
package diplo.framework;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.imageio.ImageIO;
import javax.jnlp.BasicService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;
import space.app.common.AboutDialog;
import space.app.common.DbgError;
import space.app.common.LocationHandler;

/**
 *
 * @author Pierre
 */
public class ApplicationFrame extends javax.swing.JFrame {

    final LocationHandler locationHandler = new LocationHandler(this);

    /** Creates new form MainFrame */
    public ApplicationFrame(File file) {
        initComponents();
        if (file != null) {
            mainPanel1.load(file);
        }
        try {
            setIconImage(ImageIO.read(getClass().getResource("/res/TU_A.gif")));
        } catch (IOException ex) {
            Logger.getLogger(MainPanel.class.getName()).log(Level.SEVERE, null, ex);
        }

        locationHandler.load();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel1 = new diplo.framework.MainPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuFile = new javax.swing.JMenu();
        jMenuNewItem = new javax.swing.JMenuItem();
        jMenuOpenItem = new javax.swing.JMenuItem();
        jMenuSaveItem = new javax.swing.JMenuItem();
        jMenuExitItem = new javax.swing.JMenuItem();
        jMenuHelp = new javax.swing.JMenu();
        jMenuHelpItem = new javax.swing.JMenuItem();
        jMenuAboutItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Diplomacy Strategy Editor");
        getContentPane().add(mainPanel1, java.awt.BorderLayout.CENTER);

        jMenuFile.setText("File");

        jMenuNewItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuNewItem.setText("New");
        jMenuNewItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuNewItemActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuNewItem);

        jMenuOpenItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuOpenItem.setText("Open");
        jMenuOpenItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuOpenItemActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuOpenItem);

        jMenuSaveItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuSaveItem.setText("Save");
        jMenuSaveItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuSaveItemActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuSaveItem);

        jMenuExitItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuExitItem.setText("Exit");
        jMenuExitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuExitItemActionPerformed(evt);
            }
        });
        jMenuFile.add(jMenuExitItem);

        jMenuBar1.add(jMenuFile);

        jMenuHelp.setText("Edit");

        jMenuHelpItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuHelpItem.setText("Help");
        jMenuHelpItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuHelpItemActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuHelpItem);

        jMenuAboutItem.setText("About");
        jMenuAboutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuAboutItemActionPerformed(evt);
            }
        });
        jMenuHelp.add(jMenuAboutItem);

        jMenuBar1.add(jMenuHelp);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuOpenItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuOpenItemActionPerformed

        final Preferences node = mainPanel1.preferenceNode();
        final JFileChooser fc = new JFileChooser(node.get("lastDirectory", null));
        fc.setFileFilter(new FileNameExtensionFilter("Diplomacy Strategy (.diplo)", "diplo"));
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        node.put("lastDirectory", fc.getCurrentDirectory().toString());
        final File selectedItem = fc.getSelectedFile();
        if (selectedItem == null) {
            // no file selected
        } else {

            mainPanel1.load(selectedItem);
        }

    }//GEN-LAST:event_jMenuOpenItemActionPerformed

    private void jMenuSaveItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuSaveItemActionPerformed
        final Preferences node = mainPanel1.preferenceNode();
        final JFileChooser fc = new JFileChooser(node.get("lastDirectory", null));
        fc.setFileFilter(new FileNameExtensionFilter("Diplomacy Strategy (.diplo)", "diplo"));
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        node.put("lastDirectory", fc.getCurrentDirectory().toString());
        File selectedItem = fc.getSelectedFile();

        if (selectedItem == null) {
            // no file selected
        } else {
            if (selectedItem.toString().endsWith(".diplo") == false) {
                selectedItem = new File(selectedItem.toString() + ".diplo");
            }
            mainPanel1.save(selectedItem);
        }
    }//GEN-LAST:event_jMenuSaveItemActionPerformed

    private void jMenuNewItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNewItemActionPerformed
        mainPanel1.clearStrategy();
    }//GEN-LAST:event_jMenuNewItemActionPerformed

    private void jMenuExitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuExitItemActionPerformed
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_jMenuExitItemActionPerformed

    private void jMenuHelpItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuHelpItemActionPerformed
        try {
            System.getProperties().list(System.out);
            final BasicService bs = (BasicService) ServiceManager.lookup(
                    "javax.jnlp.BasicService");
            DbgError.assertNotNull("Can't find JNLP service", bs);
            final String address = System.getProperty("help.url");
            DbgError.assertNotNull("Can't find help.url system property. Deployment Error ?",
                    address);
            bs.showDocument(new URL(address));
        } catch (MalformedURLException ex) {
            DbgError.report(ex);
        } catch (UnavailableServiceException ex) {
            DbgError.report(ex);
        }
    }//GEN-LAST:event_jMenuHelpItemActionPerformed

    private void jMenuAboutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuAboutItemActionPerformed
        final AboutDialog aboutDialog = new AboutDialog(this, true);
        aboutDialog.setVisible(true);
    }//GEN-LAST:event_jMenuAboutItemActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /**
         * Parse the command line arguments
         */
        if (args == null) {
            throw new IllegalArgumentException("Unexpected code path");

        }
        final File meshDescriptionFile;
        if (args.length == 0) {
            meshDescriptionFile = null;
        } else if (args.length == 2 && args[0].equals("-open")) {
            meshDescriptionFile = new File(args[1]);
        } else {
            throw new IllegalArgumentException("The program argument error:" + Arrays.asList(
                    args));
        }

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                setupLookAndFeel();
                new ApplicationFrame(meshDescriptionFile).setVisible(true);
            }
        });

    }

    static void setupLookAndFeel() {

        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(ApplicationFrame.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuAboutItem;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuExitItem;
    private javax.swing.JMenu jMenuFile;
    private javax.swing.JMenu jMenuHelp;
    private javax.swing.JMenuItem jMenuHelpItem;
    private javax.swing.JMenuItem jMenuNewItem;
    private javax.swing.JMenuItem jMenuOpenItem;
    private javax.swing.JMenuItem jMenuSaveItem;
    private diplo.framework.MainPanel mainPanel1;
    // End of variables declaration//GEN-END:variables
}
