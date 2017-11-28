/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * StarterKit.java
 *jButtonNext
 * Created on 08.04.2010, 17:50:20
 */

package de.malban.gui.panels;

import de.malban.Global;
import de.malban.config.Configuration;
import de.malban.config.theme.Theme;
import de.malban.gui.CSAMainFrame;
import de.malban.gui.Windowable;
import de.malban.gui.components.CSAInternalFrame;
import de.malban.gui.components.CSAView;
import de.malban.vide.TipOfTheDay;
import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
/**
 *
 * @author Malban
 */
public class TipOfDayGUI extends javax.swing.JPanel implements Windowable{

    /** Creates new form StarterKit */

    private CSAView mParent = null;
    private javax.swing.JMenuItem mParentMenuItem = null;
    private int mClassSetting=0;

    Color backfade = new Color(50,50,50,200);


    private int index = 0;

    @Override
    public void closing(){}
    @Override
    public void setParentWindow(CSAView jpv)
    {
        mParent = jpv;
    }
    @Override public boolean isIcon()
    {
        CSAMainFrame frame = ((CSAMainFrame)Configuration.getConfiguration().getMainFrame());
        if (frame.getInternalFrame(this) == null) return false;
        return frame.getInternalFrame(this).isIcon();
    }
    @Override public void setIcon(boolean b)
    {
        CSAMainFrame frame = ((CSAMainFrame)Configuration.getConfiguration().getMainFrame());
        if (frame.getInternalFrame(this) == null) return;
        try
        {
            frame.getInternalFrame(this).setIcon(b);
        }
        catch (Throwable e){}
    }
    @Override
    public void setMenuItem(javax.swing.JMenuItem item)
    {
        mParentMenuItem = item;
        mParentMenuItem.setText("Tip of Day!");
    }
    @Override
    public javax.swing.JMenuItem getMenuItem()
    {
        return mParentMenuItem;
    }
    @Override
    public javax.swing.JPanel getPanel()
    {
        return this;
    }

    public TipOfDayGUI() {
        initComponents();
        Theme t = Configuration.getConfiguration().getCurrentTheme();
        Image image = t.getImage("VectrexConsoleSmall.png");
        
        if (image != null)
        {
            jLabel3.setIcon(new javax.swing.ImageIcon(image));

            Rectangle bounds = jLabel3.getBounds();
            bounds.height = image.getHeight(null);
            bounds.width = image.getWidth(null);
            jLabel3.setBounds(bounds);
        }
        
        
//        jButtonNext.setIcon(new javax.swing.ImageIcon(t.getImage("VectrexConsoleSmall.png")));

        String text = TipOfTheDay.tipsOfDay[Global.getRand().nextInt(TipOfTheDay.tipsOfDay.length)];

        jLabelText.setText("<html>"+text+"</html>");

        setOpaque(false);
        setDoubleBuffered(false);
        setForeground(Color.white);
        setBackground(backfade);
//        setComponentZOrder(jPanelmana, 0);
    }
    @Override
    public void paintComponent(java.awt.Graphics g)
    {
        g.setColor(backfade);
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel1 = new javax.swing.JPanel();
        jLabelText = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabelTitel = new javax.swing.JLabel();
        jButtonNext = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(102, 102, 102));
        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(null);

        jLayeredPane1.setName("jLayeredPane1"); // NOI18N
        jLayeredPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLayeredPane1MouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 102, 102));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setOpaque(false);

        jLabelText.setFont(jLabelText.getFont().deriveFont(jLabelText.getFont().getStyle() | java.awt.Font.BOLD, 16));
        jLabelText.setForeground(new java.awt.Color(255, 255, 204));
        jLabelText.setText("<html> Last thing to do - is start the game!<BR><BR> For further information, regarding e.g. the game controls, please read the Help. <BR><BR>Thanks and have fun.... </html>"); // NOI18N
        jLabelText.setName("jLabelText"); // NOI18N
        jLabelText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelTextMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelText, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelText, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        );

        jLayeredPane1.add(jPanel1);
        jPanel1.setBounds(10, 40, 450, 180);

        add(jLayeredPane1);
        jLayeredPane1.setBounds(0, 100, 523, 220);

        jLabel3.setName("jLabel3"); // NOI18N
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        add(jLabel3);
        jLabel3.setBounds(340, 10, 110, 120);

        jLabelTitel.setBackground(new java.awt.Color(51, 51, 51));
        jLabelTitel.setFont(jLabelTitel.getFont().deriveFont(jLabelTitel.getFont().getStyle() | java.awt.Font.BOLD, 20));
        jLabelTitel.setForeground(new java.awt.Color(255, 255, 204));
        jLabelTitel.setText("Tip of the day!"); // NOI18N
        jLabelTitel.setName("jLabelTitel"); // NOI18N
        jLabelTitel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelTitelMouseClicked(evt);
            }
        });
        jLabelTitel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jLabelTitelKeyTyped(evt);
            }
        });
        add(jLabelTitel);
        jLabelTitel.setBounds(10, 10, 144, 27);

        jButtonNext.setText("ok");
        jButtonNext.setName("jButtonNext"); // NOI18N
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });
        add(jButtonNext);
        jButtonNext.setBounds(410, 330, 50, 20);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/VideNameSmall.png"))); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        add(jLabel1);
        jLabel1.setBounds(50, 40, 240, 100);
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButtonNextActionPerformed
    {//GEN-HEADEREND:event_jButtonNextActionPerformed
        mParent.removeTOD(this);

    }//GEN-LAST:event_jButtonNextActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        mParent.removeTOD(this);
    }//GEN-LAST:event_formMouseClicked

    private void jLayeredPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLayeredPane1MouseClicked
        mParent.removeTOD(this);
    }//GEN-LAST:event_jLayeredPane1MouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        mParent.removeTOD(this);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabelTitelKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jLabelTitelKeyTyped
        mParent.removeTOD(this);
    }//GEN-LAST:event_jLabelTitelKeyTyped

    private void jLabelTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelTextMouseClicked
        mParent.removeTOD(this);
    }//GEN-LAST:event_jLabelTextMouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        mParent.removeTOD(this);
    }//GEN-LAST:event_jLabel3MouseClicked

    private void jLabelTitelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelTitelMouseClicked
        mParent.removeTOD(this);
    }//GEN-LAST:event_jLabelTitelMouseClicked

    CSAInternalFrame startGame= null;
    CSAInternalFrame importer= null;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonNext;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelText;
    private javax.swing.JLabel jLabelTitel;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    public void deinit()
    {
        jLabel3 = null;
    }
    public void deIconified() { }
}
