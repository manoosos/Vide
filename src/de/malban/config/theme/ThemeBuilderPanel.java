/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ThemeBuilderPanel.java
 *
 * Created on 04.05.2012, 10:58:07
 */
package de.malban.config.theme;

import de.malban.gui.components.CSAView;
import de.malban.config.Configuration;
import de.malban.config.Logable;
import de.malban.gui.CSAMainFrame;
import de.malban.gui.Windowable;
import de.malban.gui.panels.LogPanel;
import java.io.File;
import java.net.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author malban
 */
public class ThemeBuilderPanel extends javax.swing.JPanel implements Windowable, INetThemeConstants{


    static final class HW
    {
        public int w = -1;
        public int h = -1;
        public HW() {}
        public HW(int _w, int _h) {w=_w; h=_h;}
    }

    static final HashMap<Integer, HW> hwMap = new HashMap<Integer, HW>();
    static
    {
        hwMap.put(THEME_GAMEBACK, new HW());
        hwMap.put(THEME_TITEL , new HW());
        hwMap.put(THEME_DEATH , new HW());
        hwMap.put(THEME_PAUSE , new HW());
        hwMap.put(THEME_ALL_MANA_BIG , new HW(100,100));

        hwMap.put(THEME_WHITE_MANA_BIG , new HW(90,90));
        hwMap.put(THEME_BLACK_MANA_BIG , new HW(90,90));
        hwMap.put(THEME_RED_MANA_BIG , new HW(90,90));
        hwMap.put(THEME_GREEN_MANA_BIG , new HW(90,90));
        hwMap.put(THEME_BLUE_MANA_BIG , new HW(90,90));

        hwMap.put(THEME_WHITE_MANA_SMALL , new HW(16,16));
        hwMap.put(THEME_BLACK_MANA_SMALL , new HW(16,16));
        hwMap.put(THEME_RED_MANA_SMALL , new HW(16,16));
        hwMap.put(THEME_GREEN_MANA_SMALL , new HW(16,16));
        hwMap.put(THEME_BLUE_MANA_SMALL , new HW(16,16));

        hwMap.put(THEME_CARD_BACK , new HW(285,200));

        hwMap.put(THEME_CROWN_BIG , new HW(250,250));
        hwMap.put(THEME_CROWN_SMALL , new HW(30,30));
        hwMap.put(THEME_ATTACKER , new HW(285,200));
        hwMap.put(THEME_DEFENDER , new HW(285,200));

        hwMap.put(THEME_SHOP , new HW());
        hwMap.put(THEME_SICK , new HW(155,155));
        hwMap.put(THEME_ALL_MANA_SMALL , new HW(16,16));
        hwMap.put(THEME_ALIVE , new HW());

    }
    
    int currentId = -1;
    
    Vector<InetImageBaseData> data = new Vector<InetImageBaseData>();
    InetImageBaseData currentData = null;
    private int mClassSetting=0;
    private InetThemeData mInetThemeData = new InetThemeData();
    private InetThemeDataPool mInetThemeDataPool;
  
    private CSAView mParent = null;
    private javax.swing.JMenuItem mParentMenuItem = null;
    Proxy proxy = Proxy.NO_PROXY;

    @Override
    public void closing(){}
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
    public void setParentWindow(CSAView jpv)
    {
        mParent = jpv;
        iNetPictureImportPanel1.setParentWindow(mParent);
    }
    @Override
    public void setMenuItem(javax.swing.JMenuItem item)
    {
        mParentMenuItem = item;
        mParentMenuItem.setText("Theme builder");
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
    int inEvent=0;
    /** Creates new form ThemeBuilderPanel */
    public ThemeBuilderPanel() {
        
        initComponents();
        inEvent++;
        jComboBoxThemeID.removeAllItems();
        for (int i=0; i<THEME_ID_NAME.length; i++ )
        {
            jComboBoxThemeID.addItem(THEME_ID_NAME[i]);
        }
        jComboBoxThemeID.setSelectedIndex(-1);
        jTextField2.setText("");
        
        mInetThemeDataPool = new InetThemeDataPool();
        resetConfigPool(false, "");
        iNetPictureImportPanel1.hideSave();
        inEvent--;
        
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButtonCreateLocalTheme = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jComboBoxName = new javax.swing.JComboBox();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAsNew = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonNew = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        iNetPictureImportPanel1 = new de.malban.config.theme.INetPictureImportPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldGameBack = new javax.swing.JTextField();
        jCheckBoxScaleBack = new javax.swing.JCheckBox();
        jCheckBoxScaleTitle = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBoxThemeID = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();

        setPreferredSize(new java.awt.Dimension(914, 600));

        jPanel1.setName("jPanel1"); // NOI18N

        jButtonCreateLocalTheme.setText("Create local theme");
        jButtonCreateLocalTheme.setName("jButtonCreateLocalTheme"); // NOI18N
        jButtonCreateLocalTheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCreateLocalThemeActionPerformed(evt);
            }
        });

        jButton2.setText("Proxy Configuration");
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Name");
        jLabel3.setName("jLabel3"); // NOI18N

        jTextFieldName.setName("jTextFieldName"); // NOI18N

        jComboBoxName.setName("jComboBoxName"); // NOI18N
        jComboBoxName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNameActionPerformed(evt);
            }
        });

        jButtonSave.setText("Save");
        jButtonSave.setName("jButtonSave"); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonSaveAsNew.setText("Save as new");
        jButtonSaveAsNew.setName("jButtonSaveAsNew"); // NOI18N
        jButtonSaveAsNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsNewActionPerformed(evt);
            }
        });

        jButtonDelete.setText("Delete");
        jButtonDelete.setName("jButtonDelete"); // NOI18N
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonNew.setText("New");
        jButtonNew.setName("jButtonNew"); // NOI18N
        jButtonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBoxName, 0, 133, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSaveAsNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete)
                        .addGap(52, 52, 52)
                        .addComponent(jButtonCreateLocalTheme))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(484, 484, 484)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBoxName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonSave)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonNew)
                        .addComponent(jButtonSaveAsNew)
                        .addComponent(jButtonDelete)
                        .addComponent(jButtonCreateLocalTheme)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("One Image setting"));
        jPanel2.setName("jPanel2"); // NOI18N

        iNetPictureImportPanel1.setName("iNetPictureImportPanel1"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(iNetPictureImportPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 912, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(iNetPictureImportPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("General Theme settings"));
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel1.setText("Game background filename");
        jLabel1.setName("jLabel1"); // NOI18N

        jTextFieldGameBack.setName("jTextFieldGameBack"); // NOI18N

        jCheckBoxScaleBack.setText("Scale background");
        jCheckBoxScaleBack.setName("jCheckBoxScaleBack"); // NOI18N

        jCheckBoxScaleTitle.setText("Scale title");
        jCheckBoxScaleTitle.setName("jCheckBoxScaleTitle"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextFieldGameBack, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxScaleBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBoxScaleTitle)
                .addContainerGap(291, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldGameBack, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxScaleBack)
                    .addComponent(jCheckBoxScaleTitle))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Theme ID");
        jLabel2.setName("jLabel2"); // NOI18N

        jComboBoxThemeID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxThemeID.setName("jComboBoxThemeID"); // NOI18N
        jComboBoxThemeID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxThemeIDActionPerformed(evt);
            }
        });

        jTextField2.setEnabled(false);
        jTextField2.setName("jTextField2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(51, 51, 51)
                .addComponent(jComboBoxThemeID, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(287, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBoxThemeID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(124, 124, 124))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCreateLocalThemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCreateLocalThemeActionPerformed
        
        if (jTextFieldName.getText().length() == 0) return;
        String name = jTextFieldName.getText();

        // make set Dir
        String dirName = "theme"+File.separator+name;
        File dir = new File(dirName);
        if (!dir.exists())
        {
            try
            {
                dir.mkdir();
            }
            catch (Throwable e)
            {
                e.printStackTrace();
                Configuration.getConfiguration().getDebugEntity().addLog(e,LogPanel.ERROR);
            }
        }
        else
        {
            int result = Configuration.getConfiguration().showConfirmDialog("Theme already exists, do you want to overwrite existing files?", "Overwrite?");        
            if (result != Configuration.DIALOG_YES) return;
        }
        
        // create Theme (xml)
         ThemeData themeData = new ThemeData();
         themeData.mGameImage = "theme"+File.separator+name+File.separator+jTextFieldGameBack.getText();
         themeData.mResizeGameImage = jCheckBoxScaleBack.isSelected();
         themeData.mResizeTitleImage = jCheckBoxScaleTitle.isSelected();
         themeData.mName = name;
         themeData.mClass = "Themes";
         ThemeDataPool mThemeDataPool = new ThemeDataPool(".."+File.separator+"theme"+File.separator+name+File.separator+"Theme.xml");
         mThemeDataPool.put(themeData);
         mThemeDataPool.save();
        
        // process iNet pictures!
         for (int i = 0; i < data.size(); i++)
         {
            InetImageBaseData inetImageBaseData = data.elementAt(i);
            iNetPictureImportPanel1.importImage(inetImageBaseData, name, data);
         }
         // done!
        
    }//GEN-LAST:event_jButtonCreateLocalThemeActionPerformed

    private void jComboBoxNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNameActionPerformed
        if (mClassSetting > 0 ) return;
        String key = jComboBoxName.getSelectedItem().toString();
        mInetThemeData = mInetThemeDataPool.get(key);
        setAllFromCurrent();
}//GEN-LAST:event_jComboBoxNameActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        
        readAllToCurrent();
        InetImageBaseData.fromBase(mInetThemeData, data);
        mInetThemeDataPool.put(mInetThemeData);
        mInetThemeDataPool.save();
        mClassSetting++;
        String klasse = "ALL";
        resetConfigPool(true, klasse);
        jComboBoxName.setSelectedItem(mInetThemeData.mName);
        mClassSetting--;
}//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSaveAsNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveAsNewActionPerformed
        mInetThemeData = new InetThemeData();
        readAllToCurrent();
        InetImageBaseData.fromBase(mInetThemeData, data);

        mInetThemeDataPool.putAsNew(mInetThemeData);
        mInetThemeDataPool.save();
        mClassSetting++;
        String klasse = "ALL";
        resetConfigPool(true,klasse);
        jComboBoxName.setSelectedItem(mInetThemeData.mName);
        mClassSetting--;
}//GEN-LAST:event_jButtonSaveAsNewActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        readAllToCurrent();
        
        InetImageBaseData.fromBase(mInetThemeData, data);
        
        
        
        mInetThemeDataPool.remove(mInetThemeData);
        mInetThemeDataPool.save();
        mClassSetting++;
        String klasse = "ALL";
        resetConfigPool(true,klasse);
        
        if (jComboBoxName.getSelectedIndex() == -1) {
            clearAll();
        }
        
        String key = jComboBoxName.getSelectedItem().toString();
        mInetThemeData = mInetThemeDataPool.get(key);
        setAllFromCurrent();
        
        mClassSetting--;
}//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewActionPerformed
        mClassSetting++;
        mInetThemeData = new InetThemeData();

        currentId = -1;
        data = new Vector<InetImageBaseData>();
        currentData = null;

        clearAll();
        resetConfigPool(false, "");
        mClassSetting--;
}//GEN-LAST:event_jButtonNewActionPerformed

    public Proxy getProxy()
    {
        return proxy;
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        ProxyPanel panel = new ProxyPanel();

        mParent.showPanelModal(panel, "Proxy settings", 500, 300);
        proxy = panel.getProxy();
        iNetPictureImportPanel1.setProxy(proxy);
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jComboBoxThemeIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxThemeIDActionPerformed
        if (inEvent!=0) return;
        int id = jComboBoxThemeID.getSelectedIndex();
        if (id == -1) return;

        if (currentId != -1) 
            readCurrentEdit();
        currentId = id;
        writeCurrentEdit();
        
        jTextField2.setText(THEME_IMAGE_NAME[currentId]);
    }//GEN-LAST:event_jComboBoxThemeIDActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.malban.config.theme.INetPictureImportPanel iNetPictureImportPanel1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonCreateLocalTheme;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonNew;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSaveAsNew;
    private javax.swing.JCheckBox jCheckBoxScaleBack;
    private javax.swing.JCheckBox jCheckBoxScaleTitle;
    private javax.swing.JComboBox jComboBoxName;
    private javax.swing.JComboBox jComboBoxThemeID;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextFieldGameBack;
    private javax.swing.JTextField jTextFieldName;
    // End of variables declaration//GEN-END:variables



    private void resetConfigPool(boolean select, String klasseToSet) /* allneeded*/
    {
        mClassSetting++;
        int i = 0;

        
        Collection<InetThemeData> colC = mInetThemeDataPool.getHashMap().values();
        Iterator<InetThemeData> iterC = colC.iterator();

        jComboBoxName.removeAllItems();
        i = 0;
        while (iterC.hasNext())
        {
            InetThemeData item = iterC.next();
            jComboBoxName.addItem(item.mName);
            if ((i==0) && (select))
            {
                jComboBoxName.setSelectedIndex(0);
                mInetThemeData = mInetThemeDataPool.get(item.mName);
                setAllFromCurrent();
            }
            i++;
        }
        if (!select)  jComboBoxName.setSelectedIndex(-1);
        mClassSetting--;
    }

    private void clearAll() /* allneeded*/
    {
        mClassSetting++;
        mInetThemeData = new InetThemeData();
        iNetPictureImportPanel1.clearAll();
        setAllFromCurrent();
        mClassSetting--;
    }

    private void setAllFromCurrent() /* allneeded*/
    {
        mClassSetting++;

        jComboBoxName.setSelectedItem(mInetThemeData.mName);
        jTextFieldName.setText(mInetThemeData.mName);

        jComboBoxThemeID.setSelectedIndex(-1);
        jTextField2.setText("");
        jTextFieldGameBack.setText(mInetThemeData.mGameBackgroundImageName);
        jCheckBoxScaleBack.setSelected(mInetThemeData.mThemeResizeBackImage);
        jCheckBoxScaleTitle.setSelected(mInetThemeData.mThemeResizeTitleImage);
        
        data = InetImageBaseData.toBase(mInetThemeData);
        
        currentId = -1;
        mClassSetting--;
    }

    private void readAllToCurrent() /* allneeded*/
    {

        mInetThemeData.mName = jTextFieldName.getText();

        mInetThemeData.mGameBackgroundImageName = jTextFieldGameBack.getText();
        mInetThemeData.mThemeResizeBackImage = jCheckBoxScaleBack.isSelected();
        mInetThemeData.mThemeResizeTitleImage = jCheckBoxScaleTitle.isSelected();
        readCurrentEdit();
    }
    
    void readCurrentEdit()
    {
        if (currentData != null) 
        {
            currentData.id = currentId;
            iNetPictureImportPanel1.readData(currentData);
        }
        else
        {
            currentData = new InetImageBaseData();
            currentData.id = currentId;
            iNetPictureImportPanel1.readData(currentData);
            data.addElement(currentData);
        }
    }
    void writeCurrentEdit()
    {
        currentData = null;
        for (int i=0; i< data.size(); i++)
        {
            InetImageBaseData c = data.elementAt(i);
            if(currentId == c.id)
            {
                currentData = c;
                break;
            }
        }
        
        
        if (currentData != null) 
        {
            currentData.id = currentId;
            iNetPictureImportPanel1.writeData(currentData, data);
        }
        else
        {
            currentData = new InetImageBaseData();
            data.addElement(currentData);
            currentData.id = currentId;
            currentData.saveName = THEME_IMAGE_NAME[currentId];
            if ((jTextFieldGameBack.getText().length()!=0) && (currentData.id==0))
                currentData.saveName = jTextFieldGameBack.getText();
            
            iNetPictureImportPanel1.writeData(currentData, data);
        }
        
        HW hw = hwMap.get(currentId);
        if (hw != null)
        {
            if (hw.h!=-1)
            {
                iNetPictureImportPanel1.setScale(true, hw.h, hw.w);
            }
        }
        
    }
    
}
