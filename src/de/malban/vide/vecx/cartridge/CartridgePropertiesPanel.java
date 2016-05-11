package de.malban.vide.vecx.cartridge;


import de.malban.config.Configuration;
import de.malban.gui.CSAMainFrame;
import static de.malban.gui.CSAMainFrame.invokeSystemFile;
import de.malban.gui.Stateable;
import de.malban.gui.Windowable;
import de.malban.gui.components.CSAView;
import de.malban.gui.dialogs.InternalFrameFileChoser;
import de.malban.gui.panels.LogPanel;
import static de.malban.gui.panels.LogPanel.WARN;
import de.malban.util.Downloader;
import de.malban.util.DownloaderPanel;
import de.malban.util.UtilityImage;
import static de.malban.vide.vecx.cartridge.Cartridge.*;
import de.malban.vide.vedi.project.FileChooserCellEditor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;

public class CartridgePropertiesPanel extends javax.swing.JPanel  implements
        Windowable, Stateable{
    transient LogPanel log = (LogPanel) Configuration.getConfiguration().getDebugEntity();
    public boolean isLoadSettings() { return true; }
    private CartridgeProperties mCartridgeProperties = new CartridgeProperties();
    private CartridgePropertiesPool mCartridgePropertiesPool;
    private int mClassSetting=0;
    private CSAView mParent = null;
    private boolean init = false;
    private javax.swing.JMenuItem mParentMenuItem = null;

    class BankMainTableModel extends AbstractTableModel
    {
        private BankMainTableModel()
        {
        }

        public int getRowCount()
        {
            if (mCartridgeProperties == null) return 0;
            if (mCartridgeProperties.mFullFilename == null) return 0;
            return mCartridgeProperties.mFullFilename.size();
        }
        public int getColumnCount()
        {
            return 2;
        }
        public Object getValueAt(int row, int col)
        {
            if (col == 0) 
            {
                return ""+row;
            }
            if (mCartridgeProperties == null) return "";
            if (mCartridgeProperties.mFullFilename == null) return "";
            if (col == 1)
            {
                String name = mCartridgeProperties.mFullFilename.elementAt(row);
                Path p = Paths.get(name);
                return p.getFileName();
            }
            return "-";
        }
        public String getColumnName(int column) {

            if (column ==0) return "#";
            if (column ==1) return "main file for bank";
            return "";
        }
        // input data column
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
        // input data column
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 1) return true;
            return false;
        }
        // input data column
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (mCartridgeProperties == null) return ;
            if (mCartridgeProperties.mFullFilename == null) return ;
            if (mCartridgeProperties.mFullFilename.size()<= rowIndex) return ;
            if (columnIndex == 1)
            {
                String path = aValue.toString();
                path = de.malban.util.Utility.makeRelative(path);
                mCartridgeProperties.mFullFilename.setElementAt(path, rowIndex);
            }
        }
    }        
    BankMainTableModel model = new BankMainTableModel();
    
    public static String SID = "cartPropEdit";
    public String getID()
    {
        return SID;
    }
    public Serializable getAdditionalStateinfo()
    {
        return null;
    }
    public void setAdditionalStateinfo(Serializable ser)
    {
    }

    @Override
    public void closing()
    {
        deinit();
    }
    @Override
    public void setParentWindow(CSAView jpv)
    {
        mParent = jpv;
    }
    @Override
    public void setMenuItem(javax.swing.JMenuItem item)
    {
        mParentMenuItem = item;
        mParentMenuItem.setText("CartPropEdit");
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
    public void deinit()
    {
        init = false;
    }
    /** Creates new form CartridgePropertiesPanel */
    public CartridgePropertiesPanel() {
        initComponents();
        
        jTable1.setModel(model);
        updateTable();
        mCartridgePropertiesPool = new CartridgePropertiesPool();
        singleImagePanel3.setPreferredSize(new Dimension(138, 198));
        singleImagePanel3.setBounds(singleImagePanel3.getBounds().x, singleImagePanel3.getBounds().y,138, 198);
        resetConfigPool(true, "Cartridge");
        init = true;
    }
    private void updateTable()
    {
        jTable1.getColumnModel().getColumn(1).setCellEditor(new FileChooserCellEditor("."+File.separator));

        jTable1.getColumnModel().getColumn(0).setPreferredWidth(5);                
        jTable1.getColumnModel().getColumn(0).setWidth(5);  
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);                
        jTable1.getColumnModel().getColumn(1).setWidth(200);  
    }
    public static CartridgeProperties getCartridgeProp(String someName)
    {
        CartridgeProperties cart = null;
        try
        {
            CartridgePropertiesPool pool= new CartridgePropertiesPool();
            Collection<CartridgeProperties> colC = pool.getHashMap().values();
            Iterator<CartridgeProperties> iterC = colC.iterator();

            Path p = Paths.get(someName);
            String nameOnly = p.getFileName().toString().toLowerCase();
            String justName = nameOnly.substring(0,nameOnly.length()-4);
            while (iterC.hasNext())
            {
                CartridgeProperties item = iterC.next();
                if (item.getFullFilename().size()<=0) continue;
                p = Paths.get(item.getFullFilename().elementAt(0));
                String itemNameOnly = p.getFileName().toString().toLowerCase();
                if (nameOnly.equals(itemNameOnly)) return item;
                String justItemName = itemNameOnly.substring(0,itemNameOnly.length()-4);
                if (justName.equals(justItemName)) return item;
            }
        }
        catch (Throwable ex)
        {
            // ignore errors and return null...
        }
        return cart;
    }

    private void resetConfigPool(boolean select, String klasseToSet) /* allneeded*/
    {
        mClassSetting++;
        Collection<String> collectionKlasse = mCartridgePropertiesPool.getKlassenHashMap().values();
        Iterator<String> iterKlasse = collectionKlasse.iterator();
        int i = 0;
        String klasse = "";

        jComboBoxKlasse.removeAllItems();
        while (iterKlasse.hasNext())
        {
            String item = iterKlasse.next();
            jComboBoxKlasse.addItem(item);
            if (select)
            {
                if (klasseToSet.length()==0)
                {
                    if (i==0)
                    {
                        jComboBoxKlasse.setSelectedIndex(i);
                        jTextFieldKlasse.setText(item);
                        klasse = item;
                    }
                }
                else
                {
                    if (klasseToSet.equalsIgnoreCase(item))
                    {
                        jComboBoxKlasse.setSelectedIndex(i);
                        jTextFieldKlasse.setText(item);
                        klasse = item;
                    }
                }
            }
            i++;
        }
        if ((select) && (klasse.length()==0))
        {
            if (jComboBoxKlasse.getItemCount()>0)
            {
                jComboBoxKlasse.setSelectedIndex(0);
                jTextFieldKlasse.setText(jComboBoxKlasse.getSelectedItem().toString());
                klasse = jComboBoxKlasse.getSelectedItem().toString();
            }
        }
        if (!select)  jComboBoxKlasse.setSelectedIndex(-1);

        Collection<CartridgeProperties> colC = mCartridgePropertiesPool.getMapForKlasse(klasse).values();
        Iterator<CartridgeProperties> iterC = colC.iterator();
        /** Sorting */  Vector<String> nnames = new Vector<String>(); while (iterC.hasNext()) nnames.addElement(iterC.next().mName); Collections.sort(nnames, new Comparator<String>() {@Override
            public int compare(String s1, String s2) { return s1.compareTo(s2); } });
        
        
        
        jComboBoxName.removeAllItems();
        i = 0;
        for (int j = 0; j < nnames.size(); j++)
        {
            String name = nnames.elementAt(j);
            jComboBoxName.addItem(name);
            if ((i==0) && (select))
            {
                jComboBoxName.setSelectedIndex(0);
                mCartridgeProperties = mCartridgePropertiesPool.get(name);
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
        mCartridgeProperties = new CartridgeProperties();
        mCartridgeProperties.mClass = "Cartridge";
        
        jTextPane2.setText("");
        jTextPane3.setText("");
        singleImagePanel1.unsetImage();
        singleImagePanel2.unsetImage();
        singleImagePanel3.unsetImage();
        singleImagePanel4.unsetImage();
        singleImagePanel5.unsetImage();
        setAllFromCurrent();
        mClassSetting--;
    }

    private void setAllFromCurrent() /* allneeded*/
    {
        mClassSetting++;
        jComboBoxKlasse.setSelectedItem(mCartridgeProperties.mClass);
        jTextFieldKlasse.setText(mCartridgeProperties.mClass);
        jComboBoxName.setSelectedItem(mCartridgeProperties.mName);
        jTextFieldName.setText(mCartridgeProperties.mName);

	jTextField2.setText(mCartridgeProperties.mCartName);
	jTextField1.setText(mCartridgeProperties.mAuthor);
	jTextField3.setText(mCartridgeProperties.mYear);
	jTextField4.setText(""+mCartridgeProperties.mCRC);
	jTextField1.setText(mCartridgeProperties.mInformation);
	jTextPane4.setText(mCartridgeProperties.mCheats);
	jTextPane5.setText(mCartridgeProperties.mEastereggs);
	jTextFieldPath.setText(mCartridgeProperties.mHomepage);
	jTextFieldPath4.setText(mCartridgeProperties.mInstructionFile);
	jTextFieldPath10.setText(mCartridgeProperties.mCartridgeImage);
	jTextFieldPath5.setText(mCartridgeProperties.mFrontImage);
	jTextFieldPath6.setText(mCartridgeProperties.mBackImage);
	jTextFieldPath2.setText(mCartridgeProperties.mOverlay);
	jTextFieldPath7.setText(mCartridgeProperties.mCritic);
	jTextFieldPath1.setText(mCartridgeProperties.mBinaryLink);
	jTextPane1.setText(mCartridgeProperties.mLicence);
	jTextFieldPath3.setText(mCartridgeProperties.mCopyrightType);
	jTextFieldPath8.setText(mCartridgeProperties.mPDFLink);
	jTextFieldPath9.setText(mCartridgeProperties.mPDFFile);
	jTextFieldPath11.setText(mCartridgeProperties.mInGameImage);

        jCheckBox11.setSelected(mCartridgeProperties.mCompleteGame );
        jCheckBox12.setSelected(mCartridgeProperties.mDemo );
        jCheckBox13.setSelected(mCartridgeProperties.mSnippet );
        jCheckBox14.setSelected(mCartridgeProperties.mHomebrew );
        
        
        int flag = mCartridgeProperties.mTypeFlags;
        jCheckBox1.setSelected((flag&FLAG_VEC_VOICE)!=0);
        jCheckBox2.setSelected((flag&FLAG_RAM_DS2430A)!=0);
        jCheckBox9.setSelected((flag&FLAG_RAM_ANIMACTION)!=0);
        jCheckBox10.setSelected((flag&FLAG_RAM_RA_SPECTRUM)!=0);
        jCheckBox3.setSelected((flag&FLAG_LIGHTPEN1)!=0);
        jCheckBox4.setSelected((flag&FLAG_LIGHTPEN2)!=0);
        jCheckBox5.setSelected((flag&FLAG_IMAGER)!=0);
        jCheckBox6.setSelected((flag&FLAG_EXTREM_MULTI)!=0);
        jCheckBox7.setSelected((flag&FLAG_BANKSWITCH_DONDZILA)!=0);
        jCheckBox8.setSelected((flag&FLAG_BANKSWITCH_VECFLASH)!=0);
        
        // load images
        if (jTextFieldPath2.getText().trim().length()>2)
        {
            singleImagePanel3.setImage(convertSeperator(jTextFieldPath2.getText()), true);
        }
        if (jTextFieldPath5.getText().trim().length()>2)
        {
            singleImagePanel1.setImage(convertSeperator(jTextFieldPath5.getText()), true);
        }
        if (jTextFieldPath6.getText().trim().length()>2)
        {
            singleImagePanel2.setImage(convertSeperator(jTextFieldPath6.getText()), true);
        }
        if (jTextFieldPath10.getText().trim().length()>2)
        {
            singleImagePanel4.setImage(convertSeperator(jTextFieldPath10.getText()), true);
        }
        if (jTextFieldPath11.getText().trim().length()>2)
        {
            singleImagePanel5.setImage(convertSeperator(jTextFieldPath11.getText()), true);
        }
        // load texts
        if (jTextFieldPath4.getText().trim().length()>2)
        {
            try 
            {
                FileReader fr = new FileReader(convertSeperator(jTextFieldPath4.getText()));
                jTextPane2.read(fr, null);
                fr.close();
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        if (jTextFieldPath7.getText().trim().length()>2)
        {
            try 
            {
                FileReader fr = new FileReader(convertSeperator(jTextFieldPath7.getText()));
                jTextPane3.read(fr, null);
                fr.close();
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
        
        jButtonFileSelect15.setEnabled(jCheckBox6.isSelected());
        jTextFieldPath12.setEnabled(jCheckBox6.isSelected());
        jTextFieldPath12.setText(mCartridgeProperties.mextremeVecFileImage);
        


// direct edit thru model	mCartridgeProperties.mFullFilename=new Vector<String>();
        updateTable();
	//mCartridgeProperties.mOther="";
        
        mClassSetting--;
    }

    private void readAllToCurrent() /* allneeded*/
    {
        String textFilenameBase = "documents"+File.separator+"games"+File.separator+mCartridgeProperties.mName+"_";
        // new instruction file
        if (jTextPane2.getText().trim().length()>2)
        {
            if (jTextFieldPath4.getText().trim().length()<2)
            {
                // generate filename and save text
                // no questions asked!
                String filename = textFilenameBase+"instructions.txt";
                de.malban.util.UtilityFiles.createTextFile(filename, jTextPane2.getText());
                jTextFieldPath4.setText(filename);
            }
        }
        // new critics file
        if (jTextPane3.getText().trim().length()>2)
        {
            if (jTextFieldPath7.getText().trim().length()<2)
            {
                // generate filename and save text
                // no questions asked!
                String filename = textFilenameBase+"critics.txt";
                de.malban.util.UtilityFiles.createTextFile(filename, jTextPane3.getText());
                jTextFieldPath7.setText(filename);
            }
        }
        
        mCartridgeProperties.mClass = jTextFieldKlasse.getText();
        mCartridgeProperties.mName = jTextFieldName.getText();

	mCartridgeProperties.mCartName=jTextField2.getText();
	mCartridgeProperties.mAuthor=jTextField1.getText();
	mCartridgeProperties.mYear=jTextField3.getText();
	mCartridgeProperties.mCRC=de.malban.util.UtilityString.Int0(jTextField4.getText());
	mCartridgeProperties.mInformation=jTextField1.getText();
	mCartridgeProperties.mCheats=jTextPane4.getText();
	mCartridgeProperties.mEastereggs=jTextPane5.getText();
	mCartridgeProperties.mHomepage=jTextFieldPath.getText();
	mCartridgeProperties.mInstructionFile=jTextFieldPath4.getText();
	mCartridgeProperties.mCartridgeImage=jTextFieldPath10.getText();
	mCartridgeProperties.mFrontImage=jTextFieldPath5.getText();
	mCartridgeProperties.mBackImage=jTextFieldPath6.getText();
	mCartridgeProperties.mOverlay=jTextFieldPath2.getText();
	mCartridgeProperties.mCritic=jTextFieldPath7.getText();
	mCartridgeProperties.mBinaryLink=jTextFieldPath1.getText();
	mCartridgeProperties.mLicence=jTextPane1.getText();
	mCartridgeProperties.mCopyrightType=jTextFieldPath3.getText();
	mCartridgeProperties.mPDFLink=jTextFieldPath8.getText();
	mCartridgeProperties.mPDFFile=jTextFieldPath9.getText();
	mCartridgeProperties.mInGameImage=jTextFieldPath11.getText();
        
        mCartridgeProperties.mCompleteGame =  jCheckBox11.isSelected();
        mCartridgeProperties.mDemo =  jCheckBox12.isSelected();
        mCartridgeProperties.mSnippet =  jCheckBox13.isSelected();
        mCartridgeProperties.mHomebrew =  jCheckBox14.isSelected();
        
        mCartridgeProperties.mextremeVecFileImage = jTextFieldPath12.getText();
        
	mCartridgeProperties.mOther="";
        
        int flag = 0;
        if (jCheckBox1.isSelected()) flag+=FLAG_VEC_VOICE;
        if (jCheckBox2.isSelected()) flag+=FLAG_RAM_DS2430A;
        if (jCheckBox9.isSelected()) flag+=FLAG_RAM_ANIMACTION;
        if (jCheckBox10.isSelected()) flag+=FLAG_RAM_RA_SPECTRUM;
        if (jCheckBox3.isSelected()) flag+=FLAG_LIGHTPEN1;
        if (jCheckBox4.isSelected()) flag+=FLAG_LIGHTPEN2;
        if (jCheckBox5.isSelected()) flag+=FLAG_IMAGER;
        if (jCheckBox6.isSelected()) flag+=FLAG_EXTREM_MULTI;
        if (jCheckBox7.isSelected()) flag+=FLAG_BANKSWITCH_DONDZILA;
        if (jCheckBox8.isSelected()) flag+=FLAG_BANKSWITCH_VECFLASH;
	mCartridgeProperties.mTypeFlags=flag;
        
// direct edit thru model	mCartridgeProperties.mFullFilename=new Vector<String>();
        
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
        jComboBoxKlasse = new javax.swing.JComboBox();
        jComboBoxName = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldName = new javax.swing.JTextField();
        jTextFieldKlasse = new javax.swing.JTextField();
        jButtonNew = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jButtonSaveAsNew = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jCheckBox8 = new javax.swing.JCheckBox();
        jCheckBox9 = new javax.swing.JCheckBox();
        jCheckBox10 = new javax.swing.JCheckBox();
        jButtonFileSelect1 = new javax.swing.JButton();
        jTextFieldPath = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldPath1 = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextFieldPath2 = new javax.swing.JTextField();
        jButtonFileSelect3 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jTextFieldPath3 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jLabel12 = new javax.swing.JLabel();
        jButtonFileSelect10 = new javax.swing.JButton();
        jButtonFileSelect11 = new javax.swing.JButton();
        singleImagePanel3 = new de.malban.graphics.SingleImagePanel();
        jCheckBox11 = new javax.swing.JCheckBox();
        jCheckBox12 = new javax.swing.JCheckBox();
        jCheckBox13 = new javax.swing.JCheckBox();
        jCheckBox14 = new javax.swing.JCheckBox();
        jButtonDownloadConfig = new javax.swing.JButton();
        jTextFieldPath12 = new javax.swing.JTextField();
        jButtonFileSelect15 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldPath4 = new javax.swing.JTextField();
        jButtonFileSelect4 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jLabel19 = new javax.swing.JLabel();
        jTextFieldPath8 = new javax.swing.JTextField();
        jButtonFileSelect8 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jTextFieldPath9 = new javax.swing.JTextField();
        jButtonFileSelect9 = new javax.swing.JButton();
        jButtonFileSelect12 = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        singleImagePanel4 = new de.malban.graphics.SingleImagePanel();
        jButtonNew3 = new javax.swing.JButton();
        jButtonFileSelect13 = new javax.swing.JButton();
        jTextFieldPath10 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jTextFieldPath5 = new javax.swing.JTextField();
        jButtonFileSelect5 = new javax.swing.JButton();
        singleImagePanel1 = new de.malban.graphics.SingleImagePanel();
        jButtonNew1 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jTextFieldPath6 = new javax.swing.JTextField();
        jButtonFileSelect6 = new javax.swing.JButton();
        singleImagePanel2 = new de.malban.graphics.SingleImagePanel();
        jButtonNew2 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextFieldPath7 = new javax.swing.JTextField();
        jButtonFileSelect7 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextPane3 = new javax.swing.JTextPane();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextPane4 = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTextPane5 = new javax.swing.JTextPane();
        jLabel22 = new javax.swing.JLabel();
        jTextFieldPath11 = new javax.swing.JTextField();
        jButtonFileSelect14 = new javax.swing.JButton();
        jButtonNew4 = new javax.swing.JButton();
        singleImagePanel5 = new de.malban.graphics.SingleImagePanel();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jComboBoxKlasse.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cartridge" }));
        jComboBoxKlasse.setEnabled(false);
        jComboBoxKlasse.setPreferredSize(new java.awt.Dimension(28, 19));
        jComboBoxKlasse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxKlasseActionPerformed(evt);
            }
        });

        jComboBoxName.setPreferredSize(new java.awt.Dimension(28, 19));
        jComboBoxName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxNameActionPerformed(evt);
            }
        });

        jLabel3.setText("Name");

        jLabel4.setText("Class");

        jTextFieldKlasse.setText("Cartridge");
        jTextFieldKlasse.setEnabled(false);

        jButtonNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/new.png"))); // NOI18N
        jButtonNew.setText("New");
        jButtonNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewActionPerformed(evt);
            }
        });

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/disk.png"))); // NOI18N
        jButtonSave.setText("Save");
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonSaveAsNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/disk_add.png"))); // NOI18N
        jButtonSaveAsNew.setText("Save as new");
        jButtonSaveAsNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveAsNewActionPerformed(evt);
            }
        });

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/cross.png"))); // NOI18N
        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldKlasse)
                    .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBoxName, 0, 152, Short.MAX_VALUE)
                    .addComponent(jComboBoxKlasse, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonNew))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSaveAsNew)
                    .addComponent(jButtonDelete))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jButtonDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonSaveAsNew)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonNew)
                        .addComponent(jComboBoxKlasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComboBoxName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTextFieldKlasse, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(jTextFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jLabel1.setText("Author");

        jLabel2.setText("Cart name");

        jLabel5.setText("Year");

        jLabel6.setText("CRC");

        jCheckBox1.setText("VecVoice");

        jCheckBox2.setText("RAM DS2430A");

        jCheckBox3.setText("Lightpen Port 1");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox4.setText("Lightpen Port 2");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jCheckBox5.setText("3d Imager");
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });

        jCheckBox6.setText("extreme multi");
        jCheckBox6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox6ActionPerformed(evt);
            }
        });

        jCheckBox7.setText("Bankswitch Dondzila");

        jCheckBox8.setText("Bankswitch VecFlesh");
        jCheckBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox8ActionPerformed(evt);
            }
        });

        jCheckBox9.setText("RAM Animaction");

        jCheckBox10.setText("RAM RA Spectrum");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox10, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCheckBox4, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox1)
                            .addComponent(jCheckBox2)
                            .addComponent(jCheckBox3)))
                    .addComponent(jCheckBox5)
                    .addComponent(jCheckBox6)
                    .addComponent(jCheckBox8)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jCheckBox9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBox7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox8))
        );

        jButtonFileSelect1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/page_link.png"))); // NOI18N
        jButtonFileSelect1.setToolTipText("opens system browser to homepage");
        jButtonFileSelect1.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect1ActionPerformed(evt);
            }
        });

        jLabel7.setText("Homepage");

        jLabel8.setText("Binary link");

        jTextFieldPath1.setToolTipText("As of now only links for ONE file can be added.\nI know of no multi bank downloadable files for vectrex yet.");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Main for Bank"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
        jScrollPane2.setViewportView(jTable1);

        jLabel9.setText("Filename(s)");

        jLabel10.setText("Overlay");

        jButtonFileSelect3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_go.png"))); // NOI18N
        jButtonFileSelect3.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect3ActionPerformed(evt);
            }
        });

        jLabel11.setText("Rights");

        jScrollPane1.setViewportView(jTextPane1);

        jLabel12.setText("Licence");

        jButtonFileSelect10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/add.png"))); // NOI18N
        jButtonFileSelect10.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect10ActionPerformed(evt);
            }
        });

        jButtonFileSelect11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/delete.png"))); // NOI18N
        jButtonFileSelect11.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect11ActionPerformed(evt);
            }
        });

        singleImagePanel3.setDrawCheckers(true);
        singleImagePanel3.setMaximumSize(new java.awt.Dimension(138, 198));
        singleImagePanel3.setMinimumSize(new java.awt.Dimension(138, 198));

        javax.swing.GroupLayout singleImagePanel3Layout = new javax.swing.GroupLayout(singleImagePanel3);
        singleImagePanel3.setLayout(singleImagePanel3Layout);
        singleImagePanel3Layout.setHorizontalGroup(
            singleImagePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );
        singleImagePanel3Layout.setVerticalGroup(
            singleImagePanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 198, Short.MAX_VALUE)
        );

        jCheckBox11.setText("Complete game");

        jCheckBox12.setText("Demo");

        jCheckBox13.setText("Snippet");

        jCheckBox14.setText("Homebrew");

        jButtonDownloadConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_link.png"))); // NOI18N
        jButtonDownloadConfig.setToolTipText("if link to a binary is given, the link the binary is downloaded");
        jButtonDownloadConfig.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonDownloadConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownloadConfigActionPerformed(evt);
            }
        });

        jTextFieldPath12.setToolTipText("Bin file for extreme multi (in case of \"a la big apple\")");
        jTextFieldPath12.setEnabled(false);

        jButtonFileSelect15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_go.png"))); // NOI18N
        jButtonFileSelect15.setEnabled(false);
        jButtonFileSelect15.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect15ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(4, 4, 4))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jButtonFileSelect11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButtonFileSelect10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(18, 18, 18)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldPath, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextFieldPath1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonDownloadConfig, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonFileSelect1)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldPath3, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(jTextFieldPath2, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonFileSelect3)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(singleImagePanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBox11)
                            .addComponent(jCheckBox13)
                            .addComponent(jCheckBox12)
                            .addComponent(jCheckBox14)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jTextFieldPath12, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButtonFileSelect15))
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonFileSelect1)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel7)
                                .addComponent(jTextFieldPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel8)
                                .addComponent(jTextFieldPath1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jButtonDownloadConfig))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButtonFileSelect10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonFileSelect11)
                                .addGap(32, 32, 32))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonFileSelect3)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(jTextFieldPath2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonFileSelect15)
                        .addComponent(jTextFieldPath12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jTextFieldPath3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(singleImagePanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jCheckBox11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox14)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("General", jPanel3);

        jLabel13.setText("Textfile");

        jButtonFileSelect4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_go.png"))); // NOI18N
        jButtonFileSelect4.setToolTipText("select text file");
        jButtonFileSelect4.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect4ActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jTextPane2);

        jLabel19.setText("PDFLink");

        jButtonFileSelect8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_link.png"))); // NOI18N
        jButtonFileSelect8.setToolTipText("link to a downloadable pdf");
        jButtonFileSelect8.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect8ActionPerformed(evt);
            }
        });

        jLabel20.setText("PDFFile");

        jButtonFileSelect9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_go.png"))); // NOI18N
        jButtonFileSelect9.setToolTipText("select pdf file");
        jButtonFileSelect9.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect9ActionPerformed(evt);
            }
        });

        jButtonFileSelect12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/gui/Adobe_PDF_file_icon_16x16.png"))); // NOI18N
        jButtonFileSelect12.setToolTipText("open system pdf viewer");
        jButtonFileSelect12.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect12ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addGap(12, 12, 12))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTextFieldPath4, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonFileSelect4))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTextFieldPath8, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonFileSelect8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jTextFieldPath9, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonFileSelect9)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonFileSelect12)
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldPath4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFileSelect4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextFieldPath8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFileSelect8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldPath9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(jButtonFileSelect9)
                    .addComponent(jButtonFileSelect12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Instruction", jPanel4);

        javax.swing.GroupLayout singleImagePanel4Layout = new javax.swing.GroupLayout(singleImagePanel4);
        singleImagePanel4.setLayout(singleImagePanel4Layout);
        singleImagePanel4Layout.setHorizontalGroup(
            singleImagePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        singleImagePanel4Layout.setVerticalGroup(
            singleImagePanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 527, Short.MAX_VALUE)
        );

        jButtonNew3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/paste_plain.png"))); // NOI18N
        jButtonNew3.setToolTipText("paste from clipboard and save");
        jButtonNew3.setPreferredSize(new java.awt.Dimension(20, 20));
        jButtonNew3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNew3ActionPerformed(evt);
            }
        });

        jButtonFileSelect13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_go.png"))); // NOI18N
        jButtonFileSelect13.setToolTipText("select picture of box front");
        jButtonFileSelect13.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect13ActionPerformed(evt);
            }
        });

        jLabel21.setText("File");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(singleImagePanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldPath10, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonFileSelect13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNew3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonFileSelect13)
                    .addComponent(jButtonNew3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jTextFieldPath10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(singleImagePanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Cartridge", jPanel10);

        jLabel14.setText("File");

        jButtonFileSelect5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_go.png"))); // NOI18N
        jButtonFileSelect5.setToolTipText("select picture of box front");
        jButtonFileSelect5.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout singleImagePanel1Layout = new javax.swing.GroupLayout(singleImagePanel1);
        singleImagePanel1.setLayout(singleImagePanel1Layout);
        singleImagePanel1Layout.setHorizontalGroup(
            singleImagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        singleImagePanel1Layout.setVerticalGroup(
            singleImagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 527, Short.MAX_VALUE)
        );

        jButtonNew1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/paste_plain.png"))); // NOI18N
        jButtonNew1.setToolTipText("paste from clipboard and save");
        jButtonNew1.setPreferredSize(new java.awt.Dimension(20, 20));
        jButtonNew1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNew1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldPath5, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonFileSelect5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNew1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(singleImagePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextFieldPath5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFileSelect5)
                    .addComponent(jButtonNew1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(singleImagePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Front", jPanel6);

        jLabel15.setText("File");

        jButtonFileSelect6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_go.png"))); // NOI18N
        jButtonFileSelect6.setToolTipText("select picture of box back");
        jButtonFileSelect6.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout singleImagePanel2Layout = new javax.swing.GroupLayout(singleImagePanel2);
        singleImagePanel2.setLayout(singleImagePanel2Layout);
        singleImagePanel2Layout.setHorizontalGroup(
            singleImagePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        singleImagePanel2Layout.setVerticalGroup(
            singleImagePanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 527, Short.MAX_VALUE)
        );

        jButtonNew2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/paste_plain.png"))); // NOI18N
        jButtonNew2.setToolTipText("paste from clipboard and save");
        jButtonNew2.setPreferredSize(new java.awt.Dimension(20, 20));
        jButtonNew2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNew2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldPath6, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonFileSelect6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNew2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(singleImagePanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextFieldPath6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFileSelect6)
                    .addComponent(jButtonNew2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(singleImagePanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Back", jPanel7);

        jLabel16.setText("File");

        jButtonFileSelect7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_go.png"))); // NOI18N
        jButtonFileSelect7.setToolTipText("select text file");
        jButtonFileSelect7.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect7ActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(jTextPane3);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldPath7, javax.swing.GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonFileSelect7))
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonFileSelect7)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel16)
                        .addComponent(jTextFieldPath7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Critics", jPanel8);

        jLabel17.setText("Eastereggs");

        jLabel18.setText("Cheats");

        jScrollPane5.setViewportView(jTextPane4);

        jScrollPane6.setViewportView(jTextPane5);

        jLabel22.setText("In game image");

        jButtonFileSelect14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/folder_go.png"))); // NOI18N
        jButtonFileSelect14.setToolTipText("select picture of box back");
        jButtonFileSelect14.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonFileSelect14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileSelect14ActionPerformed(evt);
            }
        });

        jButtonNew4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/paste_plain.png"))); // NOI18N
        jButtonNew4.setToolTipText("paste from clipboard and save");
        jButtonNew4.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonNew4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNew4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout singleImagePanel5Layout = new javax.swing.GroupLayout(singleImagePanel5);
        singleImagePanel5.setLayout(singleImagePanel5Layout);
        singleImagePanel5Layout.setHorizontalGroup(
            singleImagePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        singleImagePanel5Layout.setVerticalGroup(
            singleImagePanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(264, 264, 264)
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                        .addGap(110, 110, 110))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(jTextFieldPath11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonFileSelect14)
                                .addGap(3, 3, 3)
                                .addComponent(jButtonNew4))
                            .addComponent(singleImagePanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonFileSelect14)
                            .addComponent(jTextFieldPath11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonNew4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(singleImagePanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Other", jPanel9);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jTabbedPane1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewActionPerformed
        mClassSetting++;
        mCartridgeProperties = new CartridgeProperties();
        clearAll();
        resetConfigPool(false, "Cartridge");
        mClassSetting--;
}//GEN-LAST:event_jButtonNewActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed

        readAllToCurrent();
        mCartridgePropertiesPool.put(mCartridgeProperties);
        String currentName = mCartridgeProperties.mName;
        mCartridgePropertiesPool.save();
        mClassSetting++;
        String klasse = jTextFieldKlasse.getText();
        resetConfigPool(true, klasse);
        mClassSetting--;
        jComboBoxName.setSelectedItem(currentName); 
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonSaveAsNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveAsNewActionPerformed
        mCartridgeProperties = new CartridgeProperties();
        readAllToCurrent();
        mCartridgePropertiesPool.putAsNew(mCartridgeProperties);
        mCartridgePropertiesPool.save();
        mClassSetting++;
        String klasse = jTextFieldKlasse.getText();
        resetConfigPool(true,klasse);
        jComboBoxName.setSelectedItem(mCartridgeProperties.mName);
        mClassSetting--;
    }//GEN-LAST:event_jButtonSaveAsNewActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        readAllToCurrent();
        mCartridgePropertiesPool.remove(mCartridgeProperties);
        mCartridgePropertiesPool.save();
        mClassSetting++;
        String klasse = jTextFieldKlasse.getText();
        resetConfigPool(true,klasse);

        if (jComboBoxName.getSelectedIndex() == -1)
        {
            clearAll();
        }

        String key = jComboBoxName.getSelectedItem().toString();
        mCartridgeProperties = mCartridgePropertiesPool.get(key);
        setAllFromCurrent();

        mClassSetting--;
}//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jComboBoxKlasseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxKlasseActionPerformed
        if (mClassSetting >0 ) return;
        mClassSetting++;;

        String selected = jComboBoxKlasse.getSelectedItem().toString();
        clearAll();
        resetConfigPool(true, selected);
        jTextFieldKlasse.setText(jComboBoxKlasse.getSelectedItem().toString());
        String key = jComboBoxName.getSelectedItem().toString();
        mCartridgeProperties = mCartridgePropertiesPool.get(key);
        setAllFromCurrent();
        mClassSetting--;
    }//GEN-LAST:event_jComboBoxKlasseActionPerformed

    private void jComboBoxNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxNameActionPerformed
        if (mClassSetting > 0 ) return;
        String key = jComboBoxName.getSelectedItem().toString();
        clearAll();
        mCartridgeProperties = mCartridgePropertiesPool.get(key);
        setAllFromCurrent();
    }//GEN-LAST:event_jComboBoxNameActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox5ActionPerformed

    private void jCheckBox6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox6ActionPerformed
        jButtonFileSelect15.setEnabled(jCheckBox6.isSelected());
        jTextFieldPath12.setEnabled(jCheckBox6.isSelected());
    }//GEN-LAST:event_jCheckBox6ActionPerformed

    private void jCheckBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox8ActionPerformed

    private void jButtonFileSelect1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect1ActionPerformed

        if (jTextFieldPath.getText().trim().length() <= 7) return;
        try
        {
            if (Desktop.isDesktopSupported()) 
            {
                // Windows, mac
                Desktop.getDesktop().browse(new URI(jTextFieldPath.getText()));
            } 
            else 
            {
                // Ubuntu
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("/usr/bin/firefox -new-window " + jTextFieldPath.getText());
            }        
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButtonFileSelect1ActionPerformed

    

    
    private void jButtonFileSelect3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect3ActionPerformed

        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setMultiSelectionEnabled(false);
        if (lastImagePath.length()==0)
        {
            fc.setCurrentDirectory(new java.io.File("overlays"+File.separator));
        }
        else
        {
            fc.setCurrentDirectory(new java.io.File(lastImagePath));
        }
        FileNameExtensionFilter  filter = new  FileNameExtensionFilter("Images", "png");
        fc.setFileFilter(filter);
        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        File files = fc.getSelectedFile();
        if (files != null) 
        {
            String fullPath = fc.getSelectedFile().getAbsolutePath();
            lastImagePath = fullPath;
            jTextFieldPath2.setText(de.malban.util.Utility.makeRelative(fullPath));
            singleImagePanel3.setImage(jTextFieldPath2.getText(), true);
        }

    }//GEN-LAST:event_jButtonFileSelect3ActionPerformed

    private void jButtonFileSelect4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect4ActionPerformed
        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setMultiSelectionEnabled(false);
        if (lastImagePath.length()==0)
        {
            fc.setCurrentDirectory(new java.io.File("."+File.separator));
        }
        else
        {
            fc.setCurrentDirectory(new java.io.File(lastImagePath));
        }
        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        File files = fc.getSelectedFile();
        if (files != null) 
        {
            String fullPath = fc.getSelectedFile().getAbsolutePath();
            jTextFieldPath4.setText(de.malban.util.Utility.makeRelative(fullPath));
        }
        try 
        {
            FileReader fr = new FileReader(jTextFieldPath4.getText());
            jTextPane2.read(fr, null);
            fr.close();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        
    }//GEN-LAST:event_jButtonFileSelect4ActionPerformed
    String lastImagePath="";
    private void jButtonFileSelect5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect5ActionPerformed
        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setMultiSelectionEnabled(false);
        if (lastImagePath.length()==0)
        {
            fc.setCurrentDirectory(new java.io.File("."+File.separator));
        }
        else
        {
            fc.setCurrentDirectory(new java.io.File(lastImagePath));
        }
        FileNameExtensionFilter  filter = new  FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp", "gif");
        fc.setFileFilter(filter);
        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        File files = fc.getSelectedFile();
        if (files != null) 
        {
            String fullPath = fc.getSelectedFile().getAbsolutePath();
            lastImagePath = fullPath;

            jTextFieldPath5.setText(de.malban.util.Utility.makeRelative(fullPath));
            singleImagePanel1.setImage(jTextFieldPath5.getText());
            singleImagePanel1.scaleToFit();
        }
    }//GEN-LAST:event_jButtonFileSelect5ActionPerformed

    private void jButtonFileSelect6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect6ActionPerformed
        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setMultiSelectionEnabled(false);
        if (lastImagePath.length()==0)
        {
            fc.setCurrentDirectory(new java.io.File("."+File.separator));
        }
        else
        {
            fc.setCurrentDirectory(new java.io.File(lastImagePath));
        }
        FileNameExtensionFilter  filter = new  FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp", "gif");
        fc.setFileFilter(filter);
        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        File files = fc.getSelectedFile();
        if (files != null) 
        {
            String fullPath = fc.getSelectedFile().getAbsolutePath();
            lastImagePath = fullPath;

            jTextFieldPath6.setText(de.malban.util.Utility.makeRelative(fullPath));
            singleImagePanel2.setImage(jTextFieldPath6.getText());
            singleImagePanel2.scaleToFit();
        }
    }//GEN-LAST:event_jButtonFileSelect6ActionPerformed

    private void jButtonFileSelect7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect7ActionPerformed
        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setMultiSelectionEnabled(false);
        if (lastImagePath.length()==0)
        {
            fc.setCurrentDirectory(new java.io.File("."+File.separator));
        }
        else
        {
            fc.setCurrentDirectory(new java.io.File(lastImagePath));
        }
        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        File files = fc.getSelectedFile();
        if (files != null) 
        {
            String fullPath = fc.getSelectedFile().getAbsolutePath();
            jTextFieldPath7.setText(de.malban.util.Utility.makeRelative(fullPath));
        }
        try 
        {
            FileReader fr = new FileReader(jTextFieldPath7.getText());
            jTextPane3.read(fr, null);
            fr.close();
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtonFileSelect7ActionPerformed

    private void jButtonFileSelect8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect8ActionPerformed
        DownloaderPanel panel = new DownloaderPanel();
        panel.setValues("PDF", jTextFieldPath8.getText(), true);
        ((CSAMainFrame)mParent).showPanelModal(panel, "PDF Download config", 700, 400);
        Downloader d = panel.getDownloader();
        jTextFieldPath8.setText(d.mName);
        mCartridgeProperties.mFullFilename.clear();

        if (!d.getisZip())
            jTextFieldPath9.setText(d.getDestinationDirAll());
        else
        {
            for (int i=0; i< d.getFileUnpacked().size(); i++)
            {
                // only ONE PDF -> allways the first!
                jTextFieldPath9.setText(d.getFileUnpacked().elementAt(0));
            }
        }
    }//GEN-LAST:event_jButtonFileSelect8ActionPerformed

    private void jButtonFileSelect9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect9ActionPerformed
       
        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setMultiSelectionEnabled(false);
        if (lastImagePath.length()==0)
        {
            fc.setCurrentDirectory(new java.io.File("."+File.separator));
        }
        else
        {
            fc.setCurrentDirectory(new java.io.File(lastImagePath));
        }
        FileNameExtensionFilter  filter = new  FileNameExtensionFilter("PDF", "PDF");
        fc.setFileFilter(filter);
        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        File files = fc.getSelectedFile();
        if (files != null) 
        {
            String fullPath = fc.getSelectedFile().getAbsolutePath();
            jTextFieldPath9.setText(de.malban.util.Utility.makeRelative(fullPath));
        }
    }//GEN-LAST:event_jButtonFileSelect9ActionPerformed

    private void jButtonNew1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNew1ActionPerformed
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) 
        {
            try
            {
                Image img = (Image) t.getTransferData(DataFlavor.imageFlavor);
                singleImagePanel1.setImage(UtilityImage.toBufferedImage(img));
                singleImagePanel1.scaleToFit();
                jTextFieldPath5.setText("."+File.separator+"cartridges"+File.separator+"images"+File.separator+""+jTextField2.getText()+de.malban.Global.getRand().nextLong()+".png");
                BufferedImage image = singleImagePanel1.getImage();
                try
                {
                    File outputfile = new File(jTextFieldPath5.getText());
                    ImageIO.write(image, "png", outputfile);
                }
                catch (IOException e)
                {
                    System.out.println(e);
                }
            }
            catch (Exception x)
            {
                System.out.println("Paste not successfull");
            }
        }
    }//GEN-LAST:event_jButtonNew1ActionPerformed

    private void jButtonNew2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNew2ActionPerformed
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) 
        {
            try
            {
                Image img = (Image) t.getTransferData(DataFlavor.imageFlavor);
                singleImagePanel2.setImage(UtilityImage.toBufferedImage(img));
                singleImagePanel2.scaleToFit();
                jTextFieldPath6.setText("."+File.separator+"cartridges"+File.separator+"images"+File.separator+""+jTextField2.getText()+de.malban.Global.getRand().nextLong()+".png");
                BufferedImage image = singleImagePanel2.getImage();
                try
                {
                    File outputfile = new File(jTextFieldPath6.getText());
                    ImageIO.write(image, "png", outputfile);
                }
                catch (IOException e)
                {
                    System.out.println(e);
                }
            }
            catch (Exception x)
            {
                System.out.println("Paste not successfull");
            }
        }
    }//GEN-LAST:event_jButtonNew2ActionPerformed

    private void jButtonFileSelect10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect10ActionPerformed
        mCartridgeProperties.mFullFilename.addElement("");
        updateTable();
    }//GEN-LAST:event_jButtonFileSelect10ActionPerformed

    private void jButtonFileSelect11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect11ActionPerformed
        if (mCartridgeProperties.mFullFilename.size()<=0) return;
        mCartridgeProperties.mFullFilename.removeElementAt(mCartridgeProperties.mFullFilename.size()-1);
        updateTable();
        
    }//GEN-LAST:event_jButtonFileSelect11ActionPerformed

    private void jButtonFileSelect12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect12ActionPerformed

        boolean ok = DownloaderPanel.ensureLocalFile("PDF", mCartridgeProperties.mPDFLink, jTextFieldPath9.getText());
        if (!ok)
        {
            log.addLog("CartridgeProperties: files could not be downloaded to local", WARN);
            return;
        }
        String file = jTextFieldPath9.getText();
        invokeSystemFile(new File(file));
    }//GEN-LAST:event_jButtonFileSelect12ActionPerformed

    private void jButtonNew3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNew3ActionPerformed
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) 
        {
            try
            {
                Image img = (Image) t.getTransferData(DataFlavor.imageFlavor);
                singleImagePanel4.setImage(UtilityImage.toBufferedImage(img));
                singleImagePanel4.scaleToFit();
                jTextFieldPath10.setText("."+File.separator+"cartridges"+File.separator+"images"+File.separator+""+jTextField2.getText()+de.malban.Global.getRand().nextLong()+".png");
                BufferedImage image = singleImagePanel4.getImage();
                try
                {
                    File outputfile = new File(jTextFieldPath10.getText());
                    ImageIO.write(image, "png", outputfile);
                }
                catch (IOException e)
                {
                    System.out.println(e);
                }
            }
            catch (Exception x)
            {
                System.out.println("Paste not successfull");
            }
        }
    }//GEN-LAST:event_jButtonNew3ActionPerformed

    private void jButtonFileSelect13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect13ActionPerformed
        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setMultiSelectionEnabled(false);
        if (lastImagePath.length()==0)
        {
            fc.setCurrentDirectory(new java.io.File("."+File.separator));
        }
        else
        {
            fc.setCurrentDirectory(new java.io.File(lastImagePath));
        }
        FileNameExtensionFilter  filter = new  FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp", "gif");
        fc.setFileFilter(filter);
        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        File files = fc.getSelectedFile();
        if (files != null) 
        {
            String fullPath = fc.getSelectedFile().getAbsolutePath();
            lastImagePath = fullPath;

            jTextFieldPath10.setText(de.malban.util.Utility.makeRelative(fullPath));
            singleImagePanel4.setImage(jTextFieldPath10.getText());
            singleImagePanel4.scaleToFit();
        }
    }//GEN-LAST:event_jButtonFileSelect13ActionPerformed

    private void jButtonFileSelect14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect14ActionPerformed
        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setMultiSelectionEnabled(false);
        if (lastImagePath.length()==0)
        {
            fc.setCurrentDirectory(new java.io.File("."+File.separator));
        }
        else
        {
            fc.setCurrentDirectory(new java.io.File(lastImagePath));
        }
        FileNameExtensionFilter  filter = new  FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "bmp", "gif");
        fc.setFileFilter(filter);
        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        File files = fc.getSelectedFile();
        if (files != null) 
        {
            String fullPath = fc.getSelectedFile().getAbsolutePath();
            lastImagePath = fullPath;

            jTextFieldPath11.setText(de.malban.util.Utility.makeRelative(fullPath));
            singleImagePanel5.setImage(jTextFieldPath11.getText());
            singleImagePanel5.scaleToFit();
        }
    }//GEN-LAST:event_jButtonFileSelect14ActionPerformed

    private void jButtonNew4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNew4ActionPerformed
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        if (t != null && t.isDataFlavorSupported(DataFlavor.imageFlavor)) 
        {
            try
            {
                Image img = (Image) t.getTransferData(DataFlavor.imageFlavor);
                singleImagePanel5.setImage(UtilityImage.toBufferedImage(img));
                singleImagePanel5.scaleToFit();
                jTextFieldPath11.setText("."+File.separator+"cartridges"+File.separator+"images"+File.separator+""+jTextField2.getText()+de.malban.Global.getRand().nextLong()+".png");
                BufferedImage image = singleImagePanel5.getImage();
                try
                {
                    File outputfile = new File(jTextFieldPath11.getText());
                    ImageIO.write(image, "png", outputfile);
                }
                catch (IOException e)
                {
                    log.addLog(e, WARN);
                }
               }
            catch (Exception x)
            {
                    log.addLog(x, WARN);
            }
        }    }//GEN-LAST:event_jButtonNew4ActionPerformed

    private void jButtonDownloadConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownloadConfigActionPerformed
        DownloaderPanel panel = new DownloaderPanel();
//        panel.setValues("Cartridge", jTextFieldPath1.getText(), true);
        panel.setValues("Cartridge", jTextFieldPath1.getText(), false);
        ((CSAMainFrame)mParent).showPanelModal(panel, "Bin Download config", 700, 400);
        Downloader d = panel.getDownloader();
        jTextFieldPath1.setText(d.mName);
        mCartridgeProperties.mFullFilename.clear();

        if (!d.getisZip())
            mCartridgeProperties.mFullFilename.addElement(d.getDestinationDirAll());
        else
        {
            for (int i=0; i< d.getFileUnpacked().size(); i++)
            {
                mCartridgeProperties.mFullFilename.addElement(d.getFileUnpacked().elementAt(i));
            }
        }
        updateTable();
    }//GEN-LAST:event_jButtonDownloadConfigActionPerformed

    private void jButtonFileSelect15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFileSelect15ActionPerformed
        
        InternalFrameFileChoser fc = new de.malban.gui.dialogs.InternalFrameFileChoser();
        fc.setMultiSelectionEnabled(false);
        fc.setCurrentDirectory(new java.io.File(""));
        int r = fc.showOpenDialog(Configuration.getConfiguration().getMainFrame());
        if (r != InternalFrameFileChoser.APPROVE_OPTION) return;
        File files = fc.getSelectedFile();
        if (files != null) 
        {
            String fullPath = fc.getSelectedFile().getAbsolutePath();
            jTextFieldPath12.setText(de.malban.util.Utility.makeRelative(fullPath));
        }        
    }//GEN-LAST:event_jButtonFileSelect15ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDownloadConfig;
    private javax.swing.JButton jButtonFileSelect1;
    private javax.swing.JButton jButtonFileSelect10;
    private javax.swing.JButton jButtonFileSelect11;
    private javax.swing.JButton jButtonFileSelect12;
    private javax.swing.JButton jButtonFileSelect13;
    private javax.swing.JButton jButtonFileSelect14;
    private javax.swing.JButton jButtonFileSelect15;
    private javax.swing.JButton jButtonFileSelect3;
    private javax.swing.JButton jButtonFileSelect4;
    private javax.swing.JButton jButtonFileSelect5;
    private javax.swing.JButton jButtonFileSelect6;
    private javax.swing.JButton jButtonFileSelect7;
    private javax.swing.JButton jButtonFileSelect8;
    private javax.swing.JButton jButtonFileSelect9;
    private javax.swing.JButton jButtonNew;
    private javax.swing.JButton jButtonNew1;
    private javax.swing.JButton jButtonNew2;
    private javax.swing.JButton jButtonNew3;
    private javax.swing.JButton jButtonNew4;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSaveAsNew;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox10;
    private javax.swing.JCheckBox jCheckBox11;
    private javax.swing.JCheckBox jCheckBox12;
    private javax.swing.JCheckBox jCheckBox13;
    private javax.swing.JCheckBox jCheckBox14;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JCheckBox jCheckBox9;
    private javax.swing.JComboBox jComboBoxKlasse;
    private javax.swing.JComboBox jComboBoxName;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextFieldKlasse;
    private javax.swing.JTextField jTextFieldName;
    private javax.swing.JTextField jTextFieldPath;
    private javax.swing.JTextField jTextFieldPath1;
    private javax.swing.JTextField jTextFieldPath10;
    private javax.swing.JTextField jTextFieldPath11;
    private javax.swing.JTextField jTextFieldPath12;
    private javax.swing.JTextField jTextFieldPath2;
    private javax.swing.JTextField jTextFieldPath3;
    private javax.swing.JTextField jTextFieldPath4;
    private javax.swing.JTextField jTextFieldPath5;
    private javax.swing.JTextField jTextFieldPath6;
    private javax.swing.JTextField jTextFieldPath7;
    private javax.swing.JTextField jTextFieldPath8;
    private javax.swing.JTextField jTextFieldPath9;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JTextPane jTextPane3;
    private javax.swing.JTextPane jTextPane4;
    private javax.swing.JTextPane jTextPane5;
    private de.malban.graphics.SingleImagePanel singleImagePanel1;
    private de.malban.graphics.SingleImagePanel singleImagePanel2;
    private de.malban.graphics.SingleImagePanel singleImagePanel3;
    private de.malban.graphics.SingleImagePanel singleImagePanel4;
    private de.malban.graphics.SingleImagePanel singleImagePanel5;
    // End of variables declaration//GEN-END:variables

    public static String convertSeperator(String filename)
    {
        String ret = de.malban.util.UtilityString.replace(filename, "/", File.separator);
        ret = de.malban.util.UtilityString.replace(ret, "\\", File.separator);
        return ret;
    }
}
