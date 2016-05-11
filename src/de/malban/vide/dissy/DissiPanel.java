/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templatesƒ
 * and open the template in the editor.
 */
package de.malban.vide.dissy;

import de.malban.gui.Stateable;
import de.malban.gui.Windowable;
import de.malban.gui.components.CSAView;
import de.malban.util.KeyboardListener;
import de.malban.util.syntax.Syntax.TokenStyles;
import de.malban.vide.VideConfig;
import static de.malban.vide.dissy.MemoryInformation.DIS_TYPE_UNKOWN;
import static de.malban.vide.dissy.MemoryInformation.MEM_TYPE_ROM;
import de.malban.vide.vecx.Breakpoint;
import de.malban.vide.vecx.cartridge.Cartridge;
import de.malban.vide.vecx.cartridge.CartridgeEvent;
import de.malban.vide.vecx.cartridge.CartridgeListener;
import de.malban.vide.vecx.CodeScanMemory;
import de.malban.vide.vecx.Updatable;
import de.malban.vide.vecx.VecXPanel;
import static de.malban.vide.vecx.VecXStatics.EMU_EXIT_BREAKPOINT_BREAK;
import static de.malban.vide.vecx.VecXStatics.EMU_EXIT_BREAKPOINT_CONTINUE;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import static javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN;
import static javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

/**
 *
 * @author malban
 */
public class DissiPanel extends javax.swing.JPanel  implements
        Windowable, Stateable, Updatable, CartridgeListener{
    public boolean isLoadSettings() { return true; }

    public static final String MESSAGE_INFO = "body";
    public static final String MESSAGE_WARN = "endtag";
    public static final String MESSAGE_ERR = "error";
    VideConfig config = VideConfig.getConfig();
    
    
    // output "Tabs" for source generation
    int TAB_EQU = 30;
    int TAB_EQU_VALUE = 40;
    int TAB_MNEMONIC = 20;
    int TAB_OP = 30;
    int TAB_COMMENT = 50;
    
    
    String loadedName = "";
    
    
    private CSAView mParent = null;
    private javax.swing.JMenuItem mParentMenuItem = null;
    private int mClassSetting=0;
    boolean init = false;
    private VecXPanel vecxPanel = null; // needed for vectrex memory access
    boolean keyEventsAreSet = false;
    private boolean updateEnabled = false;
    boolean bankswitchInfo = false;

    // remember last jumped to addresses
    MemoryInformationTableModel model=null;
    DASM6809 dasm = new DASM6809();
    ArrayList<Integer> rowStack = new ArrayList<Integer>();
    int rowStackPosition = -1;
    ArrayList<TableColumn> allColumns;
    
    
    public static ArrayList<DissiPanel> panels = new ArrayList<DissiPanel>();
    public static String SID = "dissi";
    public String getID()
    {
        return SID;
    }
    public void setVecxy(VecXPanel v)
    {
        vecxPanel = v;
        if (vecxPanel!=null)
        {
            jPanelDebug.setVisible(true);
        }
        else
        {
            jPanelDebug.setVisible(false);
        }
    }

    @Override
    public void closing()
    {
        if (vecxPanel != null) vecxPanel.resetDissi();
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
        mParentMenuItem.setText("Dissi");
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
        panels.remove(this);
    }
    /**
     * Creates new form DissiPanel
     */
    public  DissiPanel() {
        initComponents();
        jEditorPane1.setEditable(false);
        jEditorPane1.setContentType("text/html");
        initTable();
        jPanelDebug.setVisible(false);
        jLabel3.setVisible(false);
        setupKeyEvents();
        panels.add(this);

    }
    void reset()
    {
        deinit();
        if (vecxPanel != null)
        {
            dis(vecxPanel.getCartridge());
        }
        else
        {
            printMessage("Can't reset, haven't got vecxy for cartridge information.", MESSAGE_ERR);
        }
        panels.add(this);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItemCode = new javax.swing.JMenuItem();
        jMenuItemByte = new javax.swing.JMenuItem();
        jMenuItemWord = new javax.swing.JMenuItem();
        jMenuItemChar = new javax.swing.JMenuItem();
        jMenuItemUngroup = new javax.swing.JMenuItem();
        jMenuItemJoin = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItemC9 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanelDebug = new javax.swing.JPanel();
        jButtonRinbufferUndo = new javax.swing.JButton();
        jButtonRingbufferRedo = new javax.swing.JButton();
        jButtonMultiStep = new javax.swing.JButton();
        jButtonsetBreakpoint = new javax.swing.JButton();
        jButtonStepOut = new javax.swing.JButton();
        jButtonOverstep = new javax.swing.JButton();
        jButtonDebugStep = new javax.swing.JButton();
        jButtonDebug = new javax.swing.JButton();
        jButtonDump1 = new javax.swing.JButton();
        jButtonViai = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jCheckBoxVectorSelect = new javax.swing.JCheckBox();
        jButtonShowVars = new javax.swing.JButton();
        jButtonWRTracker = new javax.swing.JButton();
        jButtonShowLabels = new javax.swing.JButton();
        jButtonShowPSG = new javax.swing.JButton();
        jButtonShowBreakpoints = new javax.swing.JButton();
        jButtonAdressBack = new javax.swing.JButton();
        jButtonAdressForward = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jButtonSearchNext = new javax.swing.JButton();
        jTextFieldSearch = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButtonSearchPrevious = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextFieldSearch1 = new javax.swing.JTextField();
        jButtonSearchNext2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableSource = buildTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jEditorPane1 = new javax.swing.JEditorPane();
        jTextFieldCommand = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButtonShowLabels2 = new javax.swing.JButton();
        jButtonCNTOutput = new javax.swing.JButton();
        jButtonDASMOutput = new javax.swing.JButton();
        jButtonVectrorScreenshot = new javax.swing.JButton();

        jPopupMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPopupMenu1MouseExited(evt);
            }
        });

        jMenuItemCode.setText("cast to code");
        jMenuItemCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCodeActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemCode);

        jMenuItemByte.setText("cast to byte");
        jMenuItemByte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemByteActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemByte);

        jMenuItemWord.setText("cast to word");
        jMenuItemWord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemWordActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemWord);

        jMenuItemChar.setText("cast to char");
        jMenuItemChar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCharActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemChar);

        jMenuItemUngroup.setText("ungroup");
        jMenuItemUngroup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemUngroupActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemUngroup);

        jMenuItemJoin.setText("join (same data types)");
        jMenuItemJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemJoinActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItemJoin);

        jMenu1.setText("join #");

        jMenuItem1.setText("2");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("3");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("4");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("5");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem5.setText("6");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem5);

        jPopupMenu1.add(jMenu1);

        jMenu2.setText("DP");

        jMenuItem6.setText("$C8");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItemC9.setText("$C9");
        jMenuItemC9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemC9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemC9);

        jMenuItem8.setText("$CA");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setText("$CB");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem7.setText("$D0");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jPopupMenu1.add(jMenu2);

        setName("dissy"); // NOI18N

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("ofi");
        jCheckBox2.setToolTipText("Only Full Instructions! - Memory locations that \"belong\" to other mnemonics are not shown.");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("nu");
        jCheckBox1.setToolTipText("<html>no unkown!<BR>\nDon't show locations that are \"unkown\" (not read from file), this <b>allways</b> includes RAM!<BR>(Since disassembled data is static)</html>\n"); // NOI18N
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jPanelDebug.setToolTipText("");

        jButtonRinbufferUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/arrow_turn_left.png"))); // NOI18N
        jButtonRinbufferUndo.setToolTipText("Take the last instruction back - step back, undo...");
        jButtonRinbufferUndo.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonRinbufferUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRinbufferUndoActionPerformed(evt);
            }
        });

        jButtonRingbufferRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/arrow_turn_right.png"))); // NOI18N
        jButtonRingbufferRedo.setToolTipText("redo last instruction (not executed, but \"reload\" state\")");
        jButtonRingbufferRedo.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonRingbufferRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRingbufferRedoActionPerformed(evt);
            }
        });

        jButtonMultiStep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/arrow_rotate_clockwise.png"))); // NOI18N
        jButtonMultiStep.setToolTipText("Multi Step");
        jButtonMultiStep.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonMultiStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMultiStepActionPerformed(evt);
            }
        });

        jButtonsetBreakpoint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/stop.png"))); // NOI18N
        jButtonsetBreakpoint.setToolTipText("Breakpoint set to current selected address (in dissy)");
        jButtonsetBreakpoint.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonsetBreakpoint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonsetBreakpointActionPerformed(evt);
            }
        });

        jButtonStepOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/arrow_out.png"))); // NOI18N
        jButtonStepOut.setToolTipText("Step Out. Breakpoint, wenn PC = [S]");
        jButtonStepOut.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonStepOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStepOutActionPerformed(evt);
            }
        });

        jButtonOverstep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/arrow_refresh.png"))); // NOI18N
        jButtonOverstep.setToolTipText("Overstep next instruction (breakpoint PC+1 instruction) (uses correct dissi!)");
        jButtonOverstep.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonOverstep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOverstepActionPerformed(evt);
            }
        });

        jButtonDebugStep.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/bullet_go.png"))); // NOI18N
        jButtonDebugStep.setToolTipText("single Step");
        jButtonDebugStep.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonDebugStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDebugStepActionPerformed(evt);
            }
        });

        jButtonDebug.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/bug_go.png"))); // NOI18N
        jButtonDebug.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonDebug.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDebugActionPerformed(evt);
            }
        });

        jButtonDump1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/monitor_add.png"))); // NOI18N
        jButtonDump1.setToolTipText("show VIA monitor");
        jButtonDump1.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonDump1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDump1ActionPerformed(evt);
            }
        });

        jButtonViai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/table.png"))); // NOI18N
        jButtonViai.setToolTipText("show memory dump");
        jButtonViai.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonViai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViaiActionPerformed(evt);
            }
        });

        jPanel2.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/mouse_error.png"))); // NOI18N
        jLabel1.setToolTipText("Select Vectors and show information");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        jPanel2.add(jLabel1);
        jLabel1.setBounds(20, 2, 16, 16);

        jCheckBoxVectorSelect.setToolTipText("Select Vectors and show information");
        jCheckBoxVectorSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxVectorSelectActionPerformed(evt);
            }
        });
        jPanel2.add(jCheckBoxVectorSelect);
        jCheckBoxVectorSelect.setBounds(2, 2, 17, 17);

        jButtonShowVars.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/table_refresh.png"))); // NOI18N
        jButtonShowVars.setToolTipText("show variables");
        jButtonShowVars.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonShowVars.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowVarsActionPerformed(evt);
            }
        });

        jButtonWRTracker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/chart_curve_edit.png"))); // NOI18N
        jButtonWRTracker.setToolTipText("show tracker");
        jButtonWRTracker.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonWRTracker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonWRTrackerActionPerformed(evt);
            }
        });

        jButtonShowLabels.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/table_relationship.png"))); // NOI18N
        jButtonShowLabels.setToolTipText("show labels");
        jButtonShowLabels.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonShowLabels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowLabelsActionPerformed(evt);
            }
        });

        jButtonShowPSG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/sound_add.png"))); // NOI18N
        jButtonShowPSG.setToolTipText("show psg registers");
        jButtonShowPSG.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonShowPSG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowPSGActionPerformed(evt);
            }
        });

        jButtonShowBreakpoints.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/application_lightning.png"))); // NOI18N
        jButtonShowBreakpoints.setToolTipText("show breakpoints");
        jButtonShowBreakpoints.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonShowBreakpoints.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowBreakpointsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelDebugLayout = new javax.swing.GroupLayout(jPanelDebug);
        jPanelDebug.setLayout(jPanelDebugLayout);
        jPanelDebugLayout.setHorizontalGroup(
            jPanelDebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDebugLayout.createSequentialGroup()
                .addComponent(jButtonDebug)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDebugStep)
                .addGap(5, 5, 5)
                .addComponent(jButtonOverstep)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonStepOut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonsetBreakpoint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonMultiStep)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonRinbufferUndo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonRingbufferRedo)
                .addGap(26, 26, 26)
                .addComponent(jButtonWRTracker)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDump1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonViai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonShowVars)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonShowLabels)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonShowPSG)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonShowBreakpoints)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelDebugLayout.setVerticalGroup(
            jPanelDebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanelDebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jButtonViai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonDump1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonDebug, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonStepOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonOverstep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonsetBreakpoint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonMultiStep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonDebugStep, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRingbufferRedo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonRinbufferUndo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonWRTracker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonShowVars, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonShowLabels, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonShowPSG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonShowBreakpoints, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jButtonAdressBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/resultset_previous.png"))); // NOI18N
        jButtonAdressBack.setToolTipText("<html>\ngo to last visited adress\n<BR>\nAll visited adresses are on a stack.<BR>\nThe current displayed address is NOT on the stack.<BR>\nIf the current address is change by the user, and than goes<BR>\nback and forth, the \"new\" last address is visted, not the \"old\"<BR>\nlast address!<BR>\n\n</html>\n");
        jButtonAdressBack.setEnabled(false);
        jButtonAdressBack.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonAdressBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdressBackActionPerformed(evt);
            }
        });

        jButtonAdressForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/resultset_next.png"))); // NOI18N
        jButtonAdressForward.setToolTipText("<html>\ngo to next visited adress\n<BR>\nAll visited adresses are on a stack.<BR>\nThe current displayed address is NOT on the stack.<BR>\nIf the current address is change by the user, and than goes<BR>\nback and forth, the \"new\" last address is visted, not the \"old\"<BR>\nlast address!<BR>\n\n</html>\n");
        jButtonAdressForward.setEnabled(false);
        jButtonAdressForward.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonAdressForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAdressForwardActionPerformed(evt);
            }
        });

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/webcam.png"))); // NOI18N
        jToggleButton1.setToolTipText("Toggle Update (always or only while debug)");
        jToggleButton1.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jToggleButton1.setSize(new java.awt.Dimension(20, 20));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButtonSearchNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/resultset_next.png"))); // NOI18N
        jButtonSearchNext.setToolTipText("search forward");
        jButtonSearchNext.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonSearchNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchNextActionPerformed(evt);
            }
        });

        jTextFieldSearch.setToolTipText("searches in labels and in comments (case independent)");
        jTextFieldSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearchActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("search:");

        jButtonSearchPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/resultset_previous.png"))); // NOI18N
        jButtonSearchPrevious.setToolTipText("search backwards");
        jButtonSearchPrevious.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonSearchPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchPreviousActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 51, 51));
        jLabel3.setText("not found");

        jTextFieldSearch1.setToolTipText("searches in labels and in comments (case independent)");
        jTextFieldSearch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSearch1ActionPerformed(evt);
            }
        });

        jButtonSearchNext2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/lightbulb.png"))); // NOI18N
        jButtonSearchNext2.setToolTipText("search forward");
        jButtonSearchNext2.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonSearchNext2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSearchNext2ActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel4.setText("highlight op:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSearchPrevious)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSearchNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jTextFieldSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSearchNext2)
                .addGap(0, 346, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextFieldSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel4))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButtonSearchNext, javax.swing.GroupLayout.Alignment.TRAILING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(1, 1, 1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))))
            .addComponent(jButtonSearchPrevious)
            .addComponent(jButtonSearchNext2)
        );

        jSplitPane1.setDividerLocation(470);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(1.0);

        jTableSource.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableSource.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jTableSource.setShowHorizontalLines(false);
        jTableSource.setShowVerticalLines(false);
        jTableSource.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableSourceMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTableSource);

        jSplitPane1.setTopComponent(jScrollPane2);

        jPanel3.setPreferredSize(new java.awt.Dimension(50, 50));
        jPanel3.setSize(new java.awt.Dimension(50, 50));

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jEditorPane1.setEditable(false);
        jScrollPane3.setViewportView(jEditorPane1);

        jTextFieldCommand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCommandActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
            .addComponent(jTextFieldCommand)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addGap(2, 2, 2)
                .addComponent(jTextFieldCommand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        jSplitPane1.setBottomComponent(jPanel3);

        jCheckBox3.setSelected(true);
        jCheckBox3.setText("BIOS");
        jCheckBox3.setToolTipText("show/not show BIOS in disassembly"); // NOI18N
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jButtonShowLabels2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/exclamation.png"))); // NOI18N
        jButtonShowLabels2.setToolTipText("Reset dissi, do everything from start. This mainly makes sense in combination with\n \"codescan\".");
        jButtonShowLabels2.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonShowLabels2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonShowLabels2ActionPerformed(evt);
            }
        });

        jButtonCNTOutput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/page_code.png"))); // NOI18N
        jButtonCNTOutput.setToolTipText("save gathered information to CNT-file");
        jButtonCNTOutput.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonCNTOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCNTOutputActionPerformed(evt);
            }
        });

        jButtonDASMOutput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/page_save.png"))); // NOI18N
        jButtonDASMOutput.setToolTipText("save dissi as asm file");
        jButtonDASMOutput.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonDASMOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDASMOutputActionPerformed(evt);
            }
        });

        jButtonVectrorScreenshot.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/malban/vide/images/vectorCam.png"))); // NOI18N
        jButtonVectrorScreenshot.setToolTipText("vector-screenshot");
        jButtonVectrorScreenshot.setMargin(new java.awt.Insets(0, 1, 0, -1));
        jButtonVectrorScreenshot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVectrorScreenshotActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonAdressBack)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAdressForward)
                .addGap(33, 33, 33)
                .addComponent(jPanelDebug, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDASMOutput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCNTOutput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonVectrorScreenshot)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonShowLabels2)
                .addGap(18, 18, 18)
                .addComponent(jCheckBox3)
                .addGap(6, 6, 6)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheckBox2)
                .addContainerGap())
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonAdressBack)
                    .addComponent(jButtonAdressForward)
                    .addComponent(jPanelDebug, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheckBox1)
                        .addComponent(jCheckBox2)
                        .addComponent(jCheckBox3))
                    .addComponent(jButtonDASMOutput, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonCNTOutput, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonShowLabels2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonVectrorScreenshot, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        correctModel();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        if (model!=null)
        {
            model.setFullDisplay(!jCheckBox2.isSelected());
        }
        correctModel();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jTableSourceMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSourceMousePressed
        JTable table =(JTable) evt.getSource();
        Point p = evt.getPoint();
        int row = table.rowAtPoint(p);
        int col = table.columnAtPoint(p);
        if (evt.getButton() == MouseEvent.BUTTON3)
        {
            jPopupMenu1.show(jTableSource, evt.getX()-20,evt.getY()-20);
        }        
        if (vecxPanel==null) return;

        if (evt.getClickCount() == 2) 
        {
            if (model.convertViewToModel(col) == 8) // zeiger auf adresse
            {
                MemoryInformation memInfo = model.getValueAt(row);
                boolean bit8 = false;
                String adr = (String) model.getValueAt( row,  model.convertViewToModel(col));
                if (adr.trim().length() <1) return;
                if (adr.trim().length() <=4) bit8 = true;
                int a = DASM6809.toNumber(adr);
                int current = memInfo.address;
                
                if (bit8) 
                    a += current+memInfo.length; // offsets are calculated AFTER the instruction!
                
                if (memInfo.disassembledMnemonic.toLowerCase().contains("lb")) // long RELATIV branch
                {
                    a += current+memInfo.length; // offsets are calculated AFTER the instruction!
                }
                
                if (KeyboardListener.isShiftDown())
                    vecxPanel.setDumpToAddress(a&0xffff);
                else 
                    goAddress(a&0xffff,true, true, true);
            }
            if (model.convertViewToModel(col) == 0) // zeiger auf adresse
            {
                MemoryInformation memInfo = model.getValueAt(row);
                if (KeyboardListener.isShiftDown())
                    vecxPanel.setDumpToAddress(memInfo.address);
            }
        }
        else if (evt.getClickCount() == 1) 
        {
            String clicky="";
            if (model.convertViewToModel(col) == 1)
            {
                MemoryInformation memInfo = model.getValueAt(row);
                if (memInfo.labels.size()>0) clicky=memInfo.labels.get(0);
            }
            model.setHighliteClick(clicky);
            updateTableOnly();
        }
            

        
    }//GEN-LAST:event_jTableSourceMousePressed

    private void jButtonDebugActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDebugActionPerformed
        // start running in debug mode
        // or go to pause and debug
        if (vecxPanel==null) return;
        vecxPanel.debugAction();
        updateTableOnly();
    }//GEN-LAST:event_jButtonDebugActionPerformed

    private void jButtonDebugStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDebugStepActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.debugStepAction();
        updateTableOnly();
    }//GEN-LAST:event_jButtonDebugStepActionPerformed

    private void jButtonOverstepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOverstepActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.debugOverstepAction();
        updateTableOnly();
    }//GEN-LAST:event_jButtonOverstepActionPerformed

    private void jButtonStepOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStepOutActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.debugStepoutAction();
        updateTableOnly();
    }//GEN-LAST:event_jButtonStepOutActionPerformed

    private void jButtonsetBreakpointActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonsetBreakpointActionPerformed
        if (vecxPanel==null) return; 
        vecxPanel.debugBreakpointAction();
    }//GEN-LAST:event_jButtonsetBreakpointActionPerformed

    private void jButtonMultiStepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMultiStepActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.debugMultistepAction();
        updateTableOnly();
    }//GEN-LAST:event_jButtonMultiStepActionPerformed

    private void jButtonRingbufferRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRingbufferRedoActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.debugRedoAction();
        updateTableOnly();
    }//GEN-LAST:event_jButtonRingbufferRedoActionPerformed

    private void jButtonRinbufferUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRinbufferUndoActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.debugUndoAction();
        updateTableOnly();
    }//GEN-LAST:event_jButtonRinbufferUndoActionPerformed

    private void jButtonViaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViaiActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.showDumpi();
    }//GEN-LAST:event_jButtonViaiActionPerformed

    private void jButtonDump1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDump1ActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.showViai();
    }//GEN-LAST:event_jButtonDump1ActionPerformed

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        jCheckBox1.setSelected(jCheckBoxVectorSelect.isSelected());
        jCheckBox1ActionPerformed(null);
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jCheckBoxVectorSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxVectorSelectActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.setMouseMode(jCheckBoxVectorSelect.isSelected());
    }//GEN-LAST:event_jCheckBoxVectorSelectActionPerformed

    int getTopRow()
    {
        JViewport viewport = jScrollPane2.getViewport();

        Point p = viewport.getViewPosition();
        return jTableSource.rowAtPoint(p);   
    }
    
    void saveLastAdress()
    {
        int saveRow =-1;
        int[] selected = jTableSource.getSelectedRows();
        if (selected.length>0) saveRow=selected[0];
        else saveRow = getTopRow();
        if (saveRow==-1) return;
        
        rowStack.add(saveRow);
        rowStackPosition = -1;
        jButtonAdressBack.setEnabled(true);
        jButtonAdressForward.setEnabled(false);
    }
    private void jButtonAdressBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdressBackActionPerformed
        if (rowStackPosition==0) return;
        if (rowStack.size() == 0) return;
        if (rowStackPosition == -1) rowStackPosition = rowStack.size();
        rowStackPosition--;
        if (rowStackPosition == 0)jButtonAdressBack.setEnabled(false);
        jButtonAdressForward.setEnabled(true);
        Integer row = rowStack.remove(rowStackPosition);
        rowStack.add(rowStackPosition, getTopRow());
        ensureVisible(jTableSource, row,0) ;
    }//GEN-LAST:event_jButtonAdressBackActionPerformed

    private void jButtonAdressForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAdressForwardActionPerformed
        if (rowStackPosition==-1) return;
        if (rowStack.size() == 0) return;
        if (rowStackPosition >= rowStack.size()) return;
        Integer row = rowStack.remove(rowStackPosition);
        rowStack.add(rowStackPosition, getTopRow());
        rowStackPosition++;
        if (rowStackPosition == rowStack.size())
        {
            jButtonAdressForward.setEnabled(false);
            rowStackPosition = -1;
        }
        jButtonAdressBack.setEnabled(true);
        ensureVisible(jTableSource, row,0);        
    }//GEN-LAST:event_jButtonAdressForwardActionPerformed

    private void jButtonShowVarsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowVarsActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.showVari();
    }//GEN-LAST:event_jButtonShowVarsActionPerformed

    private void jButtonWRTrackerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonWRTrackerActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.showTracki();
    }//GEN-LAST:event_jButtonWRTrackerActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        updateEnabled = jToggleButton1.isSelected();
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButtonShowLabelsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowLabelsActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.showLabi();
    }//GEN-LAST:event_jButtonShowLabelsActionPerformed

    private void jPopupMenu1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPopupMenu1MouseExited
        jPopupMenu1.setVisible(false);
    }//GEN-LAST:event_jPopupMenu1MouseExited
    
    private void jMenuItemByteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemByteActionPerformed
        updateToNewType(MemoryInformation.DIS_TYPE_DATA_BYTE, 1);
    }//GEN-LAST:event_jMenuItemByteActionPerformed

    private void jMenuItemCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCodeActionPerformed

        updateToNewType(MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_GENERAL, 1);
    }//GEN-LAST:event_jMenuItemCodeActionPerformed

    private void jMenuItemCharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCharActionPerformed
        updateToNewType(MemoryInformation.DIS_TYPE_DATA_CHAR, 1);
    }//GEN-LAST:event_jMenuItemCharActionPerformed

    private void jMenuItemWordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemWordActionPerformed
        updateToNewType(MemoryInformation.DIS_TYPE_DATA_WORD, 2);
    }//GEN-LAST:event_jMenuItemWordActionPerformed

    private void jMenuItemUngroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemUngroupActionPerformed
        joinData(1);
    }//GEN-LAST:event_jMenuItemUngroupActionPerformed
    
    private void jMenuItemJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemJoinActionPerformed
        int[] selected = jTableSource.getSelectedRows();
        MemoryInformationTableModel model = (MemoryInformationTableModel) jTableSource.getModel();
        
        int max = 0;
        for (int row: selected)
        {
            MemoryInformation memInfo = model.getValueAt(row);
            int len = memInfo.length;
            for (int a=memInfo.address; a<memInfo.address+len; a++)
            {
                max++;
            }
        }        
        joinData(max);
    
    }//GEN-LAST:event_jMenuItemJoinActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        joinData(2);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        joinData(3);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        joinData(4);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        joinData(5);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        joinData(6);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        updateToNewDP(0xc8);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        updateToNewDP(0xd0);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItemC9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemC9ActionPerformed
        updateToNewDP(0xc9);
    }//GEN-LAST:event_jMenuItemC9ActionPerformed

    private void jTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchActionPerformed
        jButtonSearchNextActionPerformed(null);
    }//GEN-LAST:event_jTextFieldSearchActionPerformed

    private void jButtonSearchPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchPreviousActionPerformed
        jLabel3.setVisible(false);
        int searchStart = 0;
        String text = jTextFieldSearch.getText();
        if (text.trim().length() == 0) return;
        int[] selected = jTableSource.getSelectedRows();
        MemoryInformationTableModel model = (MemoryInformationTableModel) jTableSource.getModel();
        if (selected.length!= 0) 
            searchStart = model.getValueAt(selected[0]).address-1;
        if (searchStart<=0) searchStart = 65535;
        int adr = searchForString(searchStart, text, false);
        int row = model.getNearestVisibleRow(adr);
        if (row == -1) return;
        
        goRow(row, true);

    }//GEN-LAST:event_jButtonSearchPreviousActionPerformed

    private void jButtonSearchNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchNextActionPerformed
        jLabel3.setVisible(false);
        int searchStart = 0;
        String text = jTextFieldSearch.getText();
        if (text.trim().length() == 0) return;
        int[] selected = jTableSource.getSelectedRows();
        MemoryInformationTableModel model = (MemoryInformationTableModel) jTableSource.getModel();
        if (selected.length!= 0) 
            searchStart = model.getValueAt(selected[0]).address+1;
        if (searchStart>=65536) searchStart = 0;
        int adr = searchForString(searchStart, text, true);
        int row = model.getNearestVisibleRow(adr);
        if (row == -1) return;
        
        goRow(row, true);
    }//GEN-LAST:event_jButtonSearchNextActionPerformed

    ArrayList<String> commandHistory = new ArrayList<String>();
    int commandHistoryPosition = -1;
    public void commandHistoryNext()
    {
        if (commandHistoryPosition==-1) return; // we have no history
        if (commandHistoryPosition == commandHistory.size()-1)
        {
            // new is allways empty
            jTextFieldCommand.setText("");
            commandHistoryPosition = -1;
            return;
        }
        commandHistoryPosition++;
        jTextFieldCommand.setText(commandHistory.get(commandHistoryPosition));
    }
    public void commandHistoryPrevious()
    {
        if (commandHistoryPosition==0) return; // we have no further history
        if (commandHistory.isEmpty()) return;
        if (commandHistoryPosition==-1)  // we are not yet in  history
        {
            commandHistoryPosition = commandHistory.size()-1;
            jTextFieldCommand.setText(commandHistory.get(commandHistoryPosition));
            return;
        }
        commandHistoryPosition--;
        jTextFieldCommand.setText(commandHistory.get(commandHistoryPosition));
    }
    
    private void jTextFieldCommandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCommandActionPerformed
       
        commandHistoryPosition = -1;
        commandHistory.add(jTextFieldCommand.getText());
        String command = jTextFieldCommand.getText();
        jTextFieldCommand.setText("");
        executeCommand(command);
    }//GEN-LAST:event_jTextFieldCommandActionPerformed

    int spaceTo(StringBuilder s, int posNow, int upTo)
    {
        s.append(" ");posNow++; //at least 1;
        while (posNow++<upTo) s.append(" ");
        return posNow;
    }
    void outputDASM()
    {
        if (loadedName==null) return;
        if (loadedName.trim().length()==0) return;
        
        int currentBank = getMemory().currentBank;
// todo disasm olny to loadlen        int loadLen = ;
        for (int b = 0; b<getMemory().maxBank; b++)
        {
            StringBuilder s = new StringBuilder();
            int start = 0;
            int end = 0xbfff;
            ArrayList<String> comments;
            ArrayList<String> labels;
            HashMap<String, String> doneLabels = new HashMap<>();
            int dp = 0;
            int brokenLength = 0;
            boolean breakable = false;
            int c = 0;
            getMemory().setBank(b);
            spaceTo(s, c, TAB_MNEMONIC);
            s.append("bank ").append(b).append("\n");

            for (int m = start; m<end; )
            {
                /*
                if (m==0x0765)
                    System.out.println("");
                */
                breakable = false;
                c = 0;
                MemoryInformation memInfo = getMemory().memMap.get(m);
                int length = memInfo.length;
                if (memInfo.disType == MemoryInformation.DIS_TYPE_DATA_BYTE) breakable = true;
                if (memInfo.disType == MemoryInformation.DIS_TYPE_DATA_CHAR) breakable = true;
                if (memInfo.disType == MemoryInformation.DIS_TYPE_DATA_WORD) breakable = true;
                if (memInfo.disType == MemoryInformation.DIS_TYPE_DATA_WORD_POINTER) breakable = true;

                if (memInfo.directPageAddress != dp)
                {
                    dp = memInfo.directPageAddress;
                    spaceTo(s, c, TAB_MNEMONIC);
                    s.append("direct $").append(String.format("%02X",memInfo.directPageAddress )).append("\n");
                    c=0;
                }
                if (memInfo.memType == MEM_TYPE_ROM)
                {
                    if (memInfo.disType == DIS_TYPE_UNKOWN) 
                    {
                        m++;
                        continue;
                    }
                    comments = memInfo.comments;
                    labels = memInfo.labels;
                    if (comments.size()>1)
                    {
                        for (int i=1; i<comments.size(); i++)
                        {
                            s.append("; ").append(comments.get(i)).append("\n");
                        }
                    }
                    if (labels.size()>0)
                    {
                        for (int i=0; i<labels.size(); i++)
                        {
                            s.append(labels.get(i)).append(":\n");
                            doneLabels.put(labels.get(i), labels.get(i));
                        }
                    }

                    c=spaceTo(s, c, TAB_MNEMONIC);
                    s.append(memInfo.disassembledMnemonic);
                    c+=memInfo.disassembledMnemonic.length();

                    c=spaceTo(s, c, TAB_OP);
                    s.append(memInfo.disassembledOperand);
                    c+=memInfo.disassembledOperand.length();

                    if (comments.size()>0)
                    {
                        spaceTo(s, c, TAB_COMMENT);
                        String cc = comments.get(0);
                        s.append("; ").append(cc);
                        if (!cc.endsWith("\n"))
                            s.append("\n");
                    }
                    else
                    {
                        s.append("\n");
                    }
                    m+=length;
                    if (length==0) m++;
                }
                else
                {
                    m++;
                }
            }
            StringBuilder s2 = new StringBuilder();
            for (int m = 0; m<65536; m++)
            {
                MemoryInformation memInfo = getMemory().memMap.get(m);
                labels = memInfo.labels;
                if (labels.size()>0)
                {
                    for (int i=0; i<labels.size(); i++)
                    {
                        if (doneLabels.get(labels.get(i)) == null)
                        {
                            c=0;
                            doneLabels.put(labels.get(i), labels.get(i));
                            s2.append(labels.get(i));
                            c+=labels.get(i).length();
                            c=spaceTo(s2, c, TAB_EQU);
                            s2.append("EQU");
                            c+=3;
                            spaceTo(s2, c, TAB_EQU_VALUE);
                            s2.append(String.format("$%04X",memInfo.address )).append("\n");
                        }
                    }
                }
                labels = memInfo.immediateLabels;
                if (labels.size()>0)
                {
                    for (int i=0; i<labels.size(); i++)
                    {
                        if (doneLabels.get(labels.get(i)) == null)
                        {
                            c=0;
                            doneLabels.put(labels.get(i), labels.get(i));
                            s2.append(labels.get(i));
                            c+=labels.get(i).length();
                            c=spaceTo(s2, c, TAB_EQU);
                            s2.append("EQU");
                            c+=3;
                            spaceTo(s2, c, TAB_EQU_VALUE);
                            s2.append(String.format("$%04X",memInfo.address )).append("\n");
                        }

                    }
                }
                // and all direct labels!
                Iterator it = getMemory().directLabels.entrySet().iterator();
                while (it.hasNext()) 
                {
                    Map.Entry pair = (Map.Entry)it.next();
                    int directPageHi = (int) pair.getKey();
                    HashMap<Integer, MemoryInformation> entries = (HashMap<Integer, MemoryInformation>) pair.getValue();

                    Iterator it2 = entries.entrySet().iterator();
                    while (it2.hasNext()) 
                    {
                        Map.Entry pair2 = (Map.Entry)it2.next();
                        int directPageLo = (int) pair2.getKey();
                        String directLabel = (String)pair2.getValue();
                        if (doneLabels.get(directLabel) == null)
                        {
                            int value = directPageHi*256+directPageLo;
                            c=0;
                            doneLabels.put(directLabel, directLabel);
                            s2.append(directLabel);
                            c+=directLabel.length();
                            c=spaceTo(s2, c, TAB_EQU);
                            s2.append("EQU");
                            c+=3;
                            spaceTo(s2, c, TAB_EQU_VALUE);
                            s2.append(String.format("$%04X",value )).append("  ; DP accessed\n");
                        }
                    }
                }
            }
            s2.append(s);
            try
            {
                String name = loadedName;
                int li = name.lastIndexOf(".");
                if (li<0) return;
                name = name.substring(0,li)+"."+b+".dasm.asm";
                PrintWriter out = new PrintWriter(name);
                out.println(s2.toString());
                out.close();
                printMessage("DASM output saved to \""+name+"\"", MESSAGE_INFO);
            }
            catch (Throwable e)
            {
                printMessage("Error saving DASM file...", MESSAGE_ERR);
            }        
        }
        getMemory().setBank(currentBank);
    }
    void outputDASMShort()
    {
        if (loadedName==null) return;
        if (loadedName.trim().length()==0) return;
        StringBuilder s = new StringBuilder();
        int start = 0;
        int end = 0xbfff;
        ArrayList<String> comments;
        ArrayList<String> labels;
        HashMap<String, Integer> doneLabels = new HashMap<String, Integer>();

        
        for (int m = 0; m<65536; m++)
        {
            MemoryInformation memInfo = getMemory().memMap.get(m);
            labels = memInfo.labels;
            if (labels.size()>0)
            {
                for (int i=0; i<labels.size(); i++)
                {
                    if (doneLabels.get(labels.get(i).trim()) == null)
                        doneLabels.put(labels.get(i).trim(), memInfo.address);
                    
                }
            }
            labels = memInfo.immediateLabels;
            if (labels.size()>0)
            {
                for (int i=0; i<labels.size(); i++)
                {
                    if (doneLabels.get(labels.get(i).trim()) == null)
                        doneLabels.put(labels.get(i).trim(), memInfo.address);
                }
                
            }
        }        
        
        
        int dp = 0;
        for (int m = start; m<end; )
        {
            int c = 0;
            MemoryInformation memInfo = getMemory().memMap.get(m);
            if (memInfo.directPageAddress != dp)
            {
                dp = memInfo.directPageAddress;
                c=spaceTo(s, c, TAB_MNEMONIC);
                s.append("direct $").append(String.format("%02X",memInfo.directPageAddress )).append("\n");
            }
            if (memInfo.memType == MEM_TYPE_ROM)
            {
                if (memInfo.disType == DIS_TYPE_UNKOWN) 
                {
                    m++;
                    continue;
                }
                c=spaceTo(s, c, TAB_MNEMONIC);
                s.append(memInfo.disassembledMnemonic);
                c+=memInfo.disassembledMnemonic.length();
                c=spaceTo(s, c, TAB_OP);
                if (memInfo.disassembledMnemonic.equals("DB"))
                {
                    boolean first = true;
                    for (int i=0; i<memInfo.length; i++)
                    {
                        if (!first)s.append(", ");
                        s.append("$").append(String.format("%02X",getMemory().memMap.get(m+i).content ));
                        first = false;
                    }
                }
                else if (memInfo.disassembledMnemonic.equals("DW"))
                {
                    boolean first = true;
                    for (int i=0; i<memInfo.length; i+=2)
                    {
                        if (!first)s.append(", ");
                        s.append("$").append(String.format("%04X",((getMemory().memMap.get(m+i).content*256)+(getMemory().memMap.get(m+i+1).content*256) )& 0xffff));
                        first = false;
                    }
                }
                else
                {
                    s.append(operandToNumber(memInfo.disassembledOperand, doneLabels));
                    c+=memInfo.disassembledOperand.length();
                }
                
                
                s.append("\n");
                m+=memInfo.length;
                if (memInfo.length==0) m++;
            }
            else
            {
                m++;
            }
        }
        
        try
        {
            String name = loadedName;
            int li = name.lastIndexOf(".");
            if (li<0) return;
            name = name.substring(0,li)+".dasm";

            
            
            PrintWriter out = new PrintWriter(name);
            out.println(s.toString());
            out.close();
            printMessage("DASM output saved to \""+name+"\"", MESSAGE_INFO);
        }
        catch (Throwable e)
        {
            printMessage("Error saving DASM file...", MESSAGE_ERR);
        }
    }
    String operandToNumber(String op, HashMap<String, Integer> doneLabels)
    {
        if (op.length()==0) return"";
        String pre = "";
        String post = "";
        op = de.malban.util.UtilityString.replace(op, "$", "");
        if (op.startsWith("#")) 
        {
            pre += "#";
            op = op.substring(1);
        }
        if (op.startsWith("<")) 
        {
            pre += "<";
            op = op.substring(1);
        }
        if (op.startsWith("[")) 
        {
            pre += "[";
            op = op.substring(1);
        }
        if (op.startsWith("<")) 
        {
            pre += "<";
            op = op.substring(1);
        }
        if (op.startsWith(">")) 
        {
            pre += ">";
            op = op.substring(1);
        }
        if (op.indexOf(",")!=-1)
        {
            post += op.substring(op.indexOf(","));
            op = op.substring(0, op.indexOf(","));
        }
        if (op.indexOf("]")!=-1)
        {
            post = op.substring(op.indexOf("]"))+post;
            op = op.substring(0, op.indexOf("]"));
        }
        
        Integer adr = doneLabels.get(op);
        if (adr != null)
        {
            return pre+String.format("$%04X",adr)+post;
        }
        boolean dollar = true;
        if (op.trim().length()==0)
            dollar = false;
            else
        if (post.contains(","))
        {
            {
                if (op.toLowerCase().equals("a")) dollar = false;
                if (op.toLowerCase().equals("b")) dollar = false;
                if (op.toLowerCase().equals("d")) dollar = false;
                if (op.toLowerCase().equals("x")) dollar = false;
                if (op.toLowerCase().equals("y")) dollar = false;
                if (op.toLowerCase().equals("s")) dollar = false;
                if (op.toLowerCase().equals("u")) dollar = false;
                if (op.toLowerCase().equals("pc")) dollar = false;
                if (op.toLowerCase().equals("dp")) dollar = false;
            }
        }
            
        if (dollar)
            return de.malban.util.UtilityString.replace(pre+"$"+op+post, "$-", "-$") ;
        return pre+op+post;
    }
    
    
    private void jButtonDASMOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDASMOutputActionPerformed
        if (!KeyboardListener.isShiftDown())
            outputDASM();
        else
            outputDASMShort();
    }//GEN-LAST:event_jButtonDASMOutputActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        
        correctModel();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jTextFieldSearch1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearch1ActionPerformed
        model.setHighliteLabel(jTextFieldSearch1.getText());
        updateTableOnly();
    }//GEN-LAST:event_jTextFieldSearch1ActionPerformed

    private void jButtonSearchNext2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchNext2ActionPerformed
   
        model.setHighliteLabel(jTextFieldSearch1.getText());
        updateTableOnly();
    }//GEN-LAST:event_jButtonSearchNext2ActionPerformed

    private void jButtonShowLabels2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowLabels2ActionPerformed
        reset();
    }//GEN-LAST:event_jButtonShowLabels2ActionPerformed

    private void jButtonCNTOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCNTOutputActionPerformed
        outputCNT();
    }//GEN-LAST:event_jButtonCNTOutputActionPerformed

    private void jButtonShowPSGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowPSGActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.showPSG();
    }//GEN-LAST:event_jButtonShowPSGActionPerformed

    private void jButtonShowBreakpointsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonShowBreakpointsActionPerformed
        if (vecxPanel==null) return;
        vecxPanel.showBreakpoints();
    }//GEN-LAST:event_jButtonShowBreakpointsActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        updateToNewDP(0xca);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        updateToNewDP(0xcb);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jButtonVectrorScreenshotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVectrorScreenshotActionPerformed
        vecxPanel.vectorScreenshot();
    }//GEN-LAST:event_jButtonVectrorScreenshotActionPerformed

    private int searchForString(int start, String text, boolean forward)
    {
        int foundAt = -1;
        int addi = forward?1:-1;
            
        for (int i=start; (((i&0xffff) !=0) || (i==start)); i+=addi)
        {
            MemoryInformation memInfo =  dasm.myMemory.memMap.get(i);
            for (String l: memInfo.labels)
            {
                if (l.toLowerCase().contains(text.toLowerCase()))
                {
                    foundAt = i;
                    jLabel3.setVisible(true);
                    jLabel3.setForeground(Color.black);
                    jLabel3.setText("Found in label at: " + String.format("$%04X",i&0xffff ));
                    break;
                }
            }
            if (foundAt!=-1) break;
            for (String c: memInfo.comments)
            {
                if (c.toLowerCase().contains(text.toLowerCase()))
                {
                    foundAt = i;
                    jLabel3.setVisible(true);
                    jLabel3.setForeground(Color.black);
                    jLabel3.setText("Found in comment at: " + String.format("$%04X",i&0xffff ));
                    break;
                }
            }
            if (foundAt!=-1) break;
        }
        if (foundAt == -1)
        {
            jLabel3.setVisible(true);
            jLabel3.setForeground(Color.red);
            jLabel3.setText("not found");
        }

        return foundAt;
    }
    void updateToNewDP(int dp)
    {
        int[] selected = jTableSource.getSelectedRows();
        MemoryInformationTableModel model = (MemoryInformationTableModel) jTableSource.getModel();
        
        int max = 0;
        int end = 0;
        for (int row: selected)
        {
            MemoryInformation memInfo = model.getValueAt(row);
            int len = memInfo.length;
            for (int a=memInfo.address; a<memInfo.address+len; a++)
            {
                // join / ungroup makes no sense for NON code
                if (dasm.myMemory.memMap.get(a).disType <MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_1_LENGTH) continue;

                dasm.myMemory.memMap.get(a).directPageAddress = dp;

                dasm.myMemory.memMap.get(a).disType = MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_GENERAL;
                dasm.myMemory.memMap.get(a).belongsToInstruction = null;
                dasm.myMemory.memMap.get(a).disassembledMnemonic = "";
                dasm.myMemory.memMap.get(a).disassembledOperand = "";
                dasm.myMemory.memMap.get(a).page = -1;
                dasm.myMemory.memMap.get(a).hexDump = "";
                dasm.myMemory.memMap.get(a).isInstructionByte = 0;
                dasm.myMemory.memMap.get(a).referingToAddress = -1;
                dasm.myMemory.memMap.get(a).referingAddressMode = -1;
                dasm.myMemory.memMap.get(a).length = 1;
                dasm.myMemory.memMap.get(a).done = false;
                dasm.myMemory.memMap.get(a).familyBytes.clear();
                
            }
            end = memInfo.address+len;
        }        
        
        dasm.reDisassemble(end>0xe000);
        updateTable();                    
    }
    void updateToNewType(int type, int l)
    {
        int[] selected = jTableSource.getSelectedRows();
        MemoryInformationTableModel model = (MemoryInformationTableModel) jTableSource.getModel();
        int end = 0;
        for (int row: selected)
        {
            MemoryInformation memInfo = model.getValueAt(row);
            int len = memInfo.length;
            for (int a=memInfo.address; a<memInfo.address+len; a++)
            {
                dasm.myMemory.memMap.get(a).disType = type;
                dasm.myMemory.memMap.get(a).belongsToInstruction = null;
                dasm.myMemory.memMap.get(a).disassembledMnemonic = "";
                dasm.myMemory.memMap.get(a).disassembledOperand = "";
                dasm.myMemory.memMap.get(a).page = -1;
                dasm.myMemory.memMap.get(a).hexDump = "";
                dasm.myMemory.memMap.get(a).isInstructionByte = 0;
                dasm.myMemory.memMap.get(a).referingToAddress = -1;
                dasm.myMemory.memMap.get(a).referingAddressMode = -1;
                dasm.myMemory.memMap.get(a).length = l;
                dasm.myMemory.memMap.get(a).done = false;
                dasm.myMemory.memMap.get(a).familyBytes.clear();
            }
            end = memInfo.address+len;
        }
        dasm.reDisassemble(end>0xe000);

        updateTable();        
    }
    private void joinData(int ll)
    {
        int[] selected = jTableSource.getSelectedRows();
        MemoryInformationTableModel model = (MemoryInformationTableModel) jTableSource.getModel();
        int end = 0;
        for (int row: selected)
        {
            MemoryInformation memInfo = model.getValueAt(row);
            int len = memInfo.length;
            for (int a=memInfo.address; a<memInfo.address+len; a++)
            {
                // join / ungroup makes no sense for code
                if (dasm.myMemory.memMap.get(a).disType >=MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_1_LENGTH) continue;
                
                dasm.myMemory.memMap.get(a).disTypeCollectionMax = ll;
                
                dasm.myMemory.memMap.get(a).disassembledMnemonic = "";
                dasm.myMemory.memMap.get(a).disassembledOperand = "";
                dasm.myMemory.memMap.get(a).page = -1;
                dasm.myMemory.memMap.get(a).hexDump = "";
                dasm.myMemory.memMap.get(a).isInstructionByte = 0;
                dasm.myMemory.memMap.get(a).referingToAddress = -1;
                dasm.myMemory.memMap.get(a).referingAddressMode = -1;
                dasm.myMemory.memMap.get(a).length = 1;
                dasm.myMemory.memMap.get(a).done = false;
                dasm.myMemory.memMap.get(a).familyBytes.clear();
            }
            end = memInfo.address+len;
        }
        dasm.reDisassemble(end>0xe000);
        updateTable();            
    }
    
    private void updateTable()
    {
        JViewport vp = (JViewport)jTableSource.getParent();
        Point pos = vp.getViewPosition();
        correctModel();
        vp.setViewPosition(pos);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdressBack;
    private javax.swing.JButton jButtonAdressForward;
    private javax.swing.JButton jButtonCNTOutput;
    private javax.swing.JButton jButtonDASMOutput;
    private javax.swing.JButton jButtonDebug;
    private javax.swing.JButton jButtonDebugStep;
    private javax.swing.JButton jButtonDump1;
    private javax.swing.JButton jButtonMultiStep;
    private javax.swing.JButton jButtonOverstep;
    private javax.swing.JButton jButtonRinbufferUndo;
    private javax.swing.JButton jButtonRingbufferRedo;
    private javax.swing.JButton jButtonSearchNext;
    private javax.swing.JButton jButtonSearchNext2;
    private javax.swing.JButton jButtonSearchPrevious;
    private javax.swing.JButton jButtonShowBreakpoints;
    private javax.swing.JButton jButtonShowLabels;
    private javax.swing.JButton jButtonShowLabels2;
    private javax.swing.JButton jButtonShowPSG;
    private javax.swing.JButton jButtonShowVars;
    private javax.swing.JButton jButtonStepOut;
    private javax.swing.JButton jButtonVectrorScreenshot;
    private javax.swing.JButton jButtonViai;
    private javax.swing.JButton jButtonWRTracker;
    private javax.swing.JButton jButtonsetBreakpoint;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBoxVectorSelect;
    private javax.swing.JEditorPane jEditorPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemByte;
    private javax.swing.JMenuItem jMenuItemC9;
    private javax.swing.JMenuItem jMenuItemChar;
    private javax.swing.JMenuItem jMenuItemCode;
    private javax.swing.JMenuItem jMenuItemJoin;
    private javax.swing.JMenuItem jMenuItemUngroup;
    private javax.swing.JMenuItem jMenuItemWord;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelDebug;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTableSource;
    private javax.swing.JTextField jTextFieldCommand;
    private javax.swing.JTextField jTextFieldSearch;
    private javax.swing.JTextField jTextFieldSearch1;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables

    private void handleCodeScan()
    {
        if (vecxPanel==null) return;
        CodeScanMemory csmem = vecxPanel.getCodeScanMemory();
        
        if (csmem == null) return;
        
        for (int b=0; b< csmem.getBankCount(); b++)
        {
            csmem.setCurrentBank(b);
            int code = 0;
            int data = 0;
            int unkown = 0;
            for (int i=0; i<0xbfff; i++)
            {
                CodeScanMemory.MemInfo codeScanMemInfo = csmem.mem[i];
                if ((codeScanMemInfo.codeScanType & CodeScanMemory.MemInfo.MEMORY_CODE ) == CodeScanMemory.MemInfo.MEMORY_CODE)
                {
                    getMemoryInformation(i, b).disType = MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_GENERAL;
                    code++;
                            
                    continue;
                }
                if ((codeScanMemInfo.codeScanType & CodeScanMemory.MemInfo.MEMORY_READ ) == CodeScanMemory.MemInfo.MEMORY_READ)
                {
                    getMemoryInformation(i, b).disType = MemoryInformation.DIS_TYPE_DATA_BYTE;
                    data++;
                    continue;
                }
                unkown++;
            }
            /*
            System.out.println("Codescan data for Bank: "+b);
            System.out.println("code: "+code);
            System.out.println("data: "+data);
            System.out.println("unkown: "+unkown);
            */
        }
        
    }
    
    // depricated - dont use!
    String dis(String name, int startAddress)
    {
        init = false;
        dasm.reset();
        loadedName = name;
        Path path = Paths.get(name);
     
        handleCodeScan();

        if (config.lstFirst)
        {
            if (!dasm.tryLoadList(name))
                dasm.tryLoadCNT(name);
        }
        else
        {
            if (!dasm.tryLoadCNT(name))
            dasm.tryLoadList(name);
        }
        
        dasm.setCreateLabels(config.createUnkownLabels);
        String ret = "";
        try
        {
            byte[] data = Files.readAllBytes(path);
            
            ret = dasm.disassemble(data,startAddress, startAddress, config.assumeVectrex);
        }
        catch (Throwable e)
        {
            System.out.println(de.malban.util.Utility.getStackTrace(e));
            ret = dasm.disassemble(null,startAddress, startAddress, config.assumeVectrex);
        }
        
        correctModel();
        init = true;
        return ret;
    }
    public String dis(Cartridge card)
    {
        init = false;
        dasm.reset();
        
        panels.remove(this);
        panels.add(this);
        
        loadedName = card.cartName;
     
        card.addCartridgeListener(this);
        
        handleCodeScan();

        String lastName = card.getBankeRomName(0);
        if (config.lstFirst)
        {
            if (!dasm.tryLoadList(lastName))
                dasm.tryLoadCNT(lastName);
        }
        else
        {
            if (!dasm.tryLoadCNT(lastName))
            dasm.tryLoadList(lastName);
        }

        dasm.setCreateLabels(config.createUnkownLabels);
        String ret = "";
        try
        {
            for (int b=0;b<card.getBankCount(); b++)
            {
                if (card.getBankeRomName(b) != null)
                {
                    if ((lastName != null)&&(lastName.length()!=0))
                    {
                        if (!lastName.equals(card.getBankeRomName(b)))
                        {
                            dasm.resetInfo();
                            lastName = card.getBankeRomName(b);
                            if (config.lstFirst)
                            {
                                if (!dasm.tryLoadList(lastName))
                                    dasm.tryLoadCNT(lastName);
                            }
                            else
                            {
                                if (!dasm.tryLoadCNT(lastName))
                                dasm.tryLoadList(lastName);
                            }
                        }
                    }
                }
                ret = dasm.disassemble(card.getByteData(b),0, 0, config.assumeVectrex, b);
            }
        }
        catch (Throwable e)
        {
            System.out.println(de.malban.util.Utility.getStackTrace(e));
        }
        // in case banks were set by CNT file
        getMemory().setBank(0);
        correctModel();
        init = true;
        
        initOldBreakPoints();
        
        return ret;
    }    
    public boolean isInit()
    {
        return init;
    }
    public void correctModel()
    {
        if (model == null) return ;
        
        model.showAll();
        if (jCheckBox1.isSelected())
            model.reduceUnkown();
        if (jCheckBox2.isSelected())
            model.reduceCompleteInstructions();
        if (!jCheckBox3.isSelected())
            model.reduceBIOS();
        jTableSource.tableChanged(null);
        setUpTableColumns();
        mClassSetting++;
        jTableSource.setAutoResizeMode(AUTO_RESIZE_LAST_COLUMN);
        for (int i=0; i< model.getColumnCount(); i++)
        {
            int colData = model.convertViewToModel(i);
            jTableSource.getColumnModel().getColumn(i).setPreferredWidth(model.getColWidth(colData));                
            jTableSource.getColumnModel().getColumn(i).setWidth(model.getColWidth(colData));  
        }
        jTableSource.setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        mClassSetting--;
    }
    
    public void updateTableOnly()
    {
        jTableSource.repaint();
    }
    
    public void goRow(int row, boolean forceTopRow)
    {
        if (!init) return;
        
        saveLastAdress();
        
        
        jTableSource.setRowSelectionInterval(row, row);
        if (forceTopRow)
            ensureVisible(jTableSource, row,0) ;
        // and jump there!
        jTableSource.scrollRectToVisible(jTableSource.getCellRect(row, 0, true));
    }    
    public void goAddress(int address, boolean forceTopRow, boolean userGo, boolean forceUpdate)
    {
        if (!init) return;
        
        if (userGo)
        {
            saveLastAdress();
        }

        if (!forceUpdate) // single step e.g. always must update!
        {
            if (!userGo)
            {
                if (!updateEnabled) return;
            }
        }
        
        
        // select line in table and jump to display that!
        int row = model.getNearestVisibleRow( address);
        if (row == -1 ) return;
        // select
        
        ensureVisible(jTableSource,  row,  0);
    }
    public static void ensureVisible(JTable table, int row, int col)
    {
        table.setRowSelectionInterval(row, row);
        if (!isCellVisible(table,  row,  col))
            scrollToVisibleMid(table, row,col);
    }
    
    public static boolean isCellVisible(JTable table, int row, int col) {
        if (!(table.getParent() instanceof JViewport)) 
        {
            return false;
        }
        JViewport viewport = (JViewport) table.getParent();
        Rectangle rect = table.getCellRect(row, col, true);
        Point pt = viewport.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);
        return new Rectangle(viewport.getExtentSize()).contains(rect);
      }
    public static void scrollToVisibleMid(JTable table, int row, int col )
    {
        if (!(table.getParent() instanceof JViewport)) 
        {
            return;
        }
        JViewport viewport = (JViewport) table.getParent();
        Rectangle rect = table.getCellRect(row, col, true);
        Rectangle viewRect = viewport.getViewRect();
        rect.setLocation(rect.x - viewRect.x, rect.y - viewRect.y);

        int centerX = (viewRect.width - rect.width) / 2;
        int centerY = (viewRect.height - rect.height) / 2;
        if (rect.x < centerX) {
          centerX = -centerX;
        }
        if (rect.y < centerY) {
          centerY = -centerY;
        }
        rect.translate(centerX, centerY);
        viewport.scrollRectToVisible(rect);

            
    }
    
    /*
    public static void scrollToVisible(JTable table, int rowIndex, int vColIndex) 
    {
        // down
        JViewport vp = (JViewport)table.getParent();
        int bottomIndex = table.getModel().getRowCount()-1;
        table.setRowSelectionInterval(bottomIndex, bottomIndex);
        table.changeSelection(bottomIndex,0,false,false);
        Rectangle r = table.getCellRect(bottomIndex-1, 0, true);
        int vph = vp.getExtentSize().height;
        r.y += vph;
        table.scrollRectToVisible(r);

        
        // and UP!
        int currentSelectedRow = table.getSelectedRow();
        
        try
        {
          table.changeSelection(rowIndex,0,false,false);
          if(rowIndex > currentSelectedRow)
          {
            r = table.getCellRect(rowIndex-1, 0, true);
            vph = vp.getExtentSize().height;
            r.y += vph;
            table.scrollRectToVisible(r);
          }
        }
        catch(Exception e){/*error message*b/}

    
    }
*/
    public int getInstructionLengthAt(int address)
    {
        if (!init) return 1;
        return dasm.getInstructionLengthAt(address);
    }
    
    private void initTable()
    {
        model = dasm.getTableModel();
        jTableSource.setModel(model);
        jTableSource.tableChanged(null);
        jTableSource.setAutoResizeMode(AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        jTableSource.setRowSelectionAllowed(true);
        jTableSource.setDefaultRenderer(Object.class, new DefaultTableCellRenderer()
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) 
            {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

                if (table.getModel() instanceof MemoryInformationTableModel)
                {
                    MemoryInformationTableModel model = (MemoryInformationTableModel)table.getModel();

                    MemoryInformation memInfo = model.getValueAt(row);
                    
                    int currentBank = 0;
                    int currentAddress = 0;
                    if (vecxPanel != null)
                    {
                        currentBank = vecxPanel.getCurrentBank();
                        currentAddress = vecxPanel.getCurrentAddress();
                        
                    }
                    
                    if ((memInfo.address == currentAddress) && ( getCurrentBank() == currentBank))
                    {
                        setBackground(new Color(200,200,255));
                    }
                    else if (memInfo.hasBreakpoint())
                    {
                        setBackground(Color.RED);
                        // setForeground(Color.WHITE);
                    } 
                    else 
                    {
                        if (isSelected)
                        {
                            setBackground(table.getSelectionBackground());
                            setForeground(table.getSelectionForeground());
                        }
                        else
                        {
                            Color back = model.getBackground(row, col);
                            Color fore = model.getForeground(row, col);
                            if (back != null)
                                setBackground(back);
                            else
                                setBackground(table.getBackground());
                            if (fore != null)
                                setForeground(fore);
                            else
                                setForeground(table.getForeground());
                        }
                    }       
                }
                return this;
            }   
        });        
        
        model.addTableModelListener(new TableModelListener() 
        {

            public void tableChanged(TableModelEvent tme) 
            {
                if (tme.getType() == TableModelEvent.UPDATE) 
                {
                    int row = tme.getFirstRow();
                    int col = tme.getColumn();
                    if (model.convertViewToModel(col) == 1)
                    {
                        // labels
                        // do complete disasm
                        varUpdate();
                    }
                    if (model.convertViewToModel(col) == 11) // comment
                    {
                        MemoryInformation memInfo = model.getValueAt(row);
                        if (memInfo == null) return;

                        removeHeyDissi(memInfo);

                        for (String comment: memInfo.comments)
                        {
                            int hey = comment.toLowerCase().indexOf("hey dissi");
                            if (hey <0) continue;
                            addHeyDissi(memInfo.address, model.getOrgData().currentBank, comment);
                        }
                    }
                }
            }
      });
      allColumns = new ArrayList<>();
      for (int i=0; i< jTableSource.getColumnCount(); i++)
      {
          allColumns.add(jTableSource.getColumnModel().getColumn(i));
      }
      setUpTableColumns();
      jTableSource.getColumnModel().addColumnModelListener(new TableColumnModelListener()
      {
            public void columnMarginChanged(ChangeEvent e)
            {
                if (mClassSetting==0)
                    rememberColumnsWidth();
            }
            public void columnSelectionChanged(ListSelectionEvent e)
            {
            }
            public void columnAdded(TableColumnModelEvent e)
            {
            }
            public void columnRemoved(TableColumnModelEvent e)
            {
            }
            public void columnMoved(TableColumnModelEvent e)
            {
            }
      });
    }
    
    
    
    public int getCurrentAddress()
    {
        if (!init) return -1;
        int row = jTableSource.getSelectedRow();
        if (row == -1) return -1;
        MemoryInformationTableModel model = (MemoryInformationTableModel)jTableSource.getModel();
        MemoryInformation memInfo = model.getValueAt(row);
        return memInfo.address;
    }

    public MemoryInformation getMemoryInformation(int address, int bank)
    {
        return dasm.myMemory.get(address, bank);
    }
    public Memory getMemory()
    {
        return dasm.myMemory;
    }
        
    JTable buildTable()
    {
        JTable table = new JTable(){

	


            @Override
            public Component prepareEditor(TableCellEditor editor, int row, int column) 
            {
                Component c = super.prepareEditor(editor, row, column);
                if (column == 1)
                {
                    if (c instanceof JTextField)
                    {
                        JTextField tf = (JTextField) c;
                        if (jTableSource.getModel() instanceof MemoryInformationTableModel)
                        {
                            MemoryInformationTableModel model = (MemoryInformationTableModel) jTableSource.getModel();
                            MemoryInformation memInfo = getMemoryInformation(model.getValueAt(row).address, getCurrentBank());
                            String ediString ="";
                            for (String l: memInfo.labels)
                            {
                                if (l.trim().length()>0)
                                    ediString+=l+":";
                            }
                            if (ediString.length()>0)  
                                ediString = ediString.substring(0, ediString.length()-1);
                            tf.setText(ediString);
                        }                        
                    }
                }
                return c;
            }


            //Implement table cell tool tips.           
            public String getToolTipText(MouseEvent e) 
            {
                if (vecxPanel==null) return null;
                String tip = "<html>";
                if (jTableSource.getModel() instanceof MemoryInformationTableModel)
                {
                    MemoryInformationTableModel model = (MemoryInformationTableModel) jTableSource.getModel();
                    java.awt.Point p = e.getPoint();
                    int rowIndex = rowAtPoint(p);
                    int colIndex = columnAtPoint(p);

                    try 
                    {
                        MemoryInformation memInfo = getMemoryInformation(model.getValueAt(rowIndex).address, vecxPanel.getCurrentBank());
                        // labels
                        if (model.convertViewToModel(colIndex) == 1)
                        {
                            // show complete labels as tt
                            tip+="<pre>";
                            for (int i = 0;i< memInfo.length; i++)
                            {
                                MemoryInformation next = model.getOrgData().memMap.get(memInfo.address+i);
                                for (int ii = 0;ii< next.labels.size(); ii++)
                                {
                                    tip += String.format("$%04X",memInfo.address+i )+"| "+next.labels.get(ii)+":\n";
                                }
                            }
                            tip+="</pre>";
                        }
                        if (model.convertViewToModel(colIndex) == 8)
                        {
                            boolean bit8 = false;
                            String adr = (String) model.getValueAt( rowIndex,  model.convertViewToModel(colIndex));
                            if (adr.trim().length() <1) return "";
                            if (adr.trim().length() <4) bit8 = true;
                            int a = DASM6809.toNumber(adr);
                            int current = memInfo.address;
                            if (bit8) return null;

                            // MemoryInformation memInfo2 = getMemoryInformation(a);
                            int val1 = vecxPanel.getVecXMem8(a);
                            int val2 = vecxPanel.getVecXMem8(a+1);
                            
                            tip +=adr+": contains...<BR>";
                            tip +="8bit:  " + "$"+String.format("%02X",val1&0xff)+", dec: "+(val1&0xff)+"<BR>";
                            tip +="16bit: " + "$"+String.format("%04X",((val1&0xff)*256+(val2&0xff)) )+", dec: "+((val1&0xff)*256+(val2&0xff)) +"<BR>";
                        }
                        if (model.convertViewToModel(colIndex) == 11)
                        {
                            // show complete comment as tt
                            tip+="<pre>";
                            for (int i = 0;i< memInfo.length; i++)
                            {
                                MemoryInformation next = model.getOrgData().memMap.get(memInfo.address+i);
                                for (int ii = 0;ii< next.comments.size(); ii++)
                                {
                                    tip += String.format("$%04X",memInfo.address+i )+"| "+next.comments.get(ii)+"\n";
                                }
                            }
                            tip+="</pre>";
                        }
                        
                        
                                
                    } 
                    catch (RuntimeException e1) 
                    {
                        //catch null pointer exception if mouse is over an empty line
                    }
                }
                tip += "</html>";
                return tip;
            }
        };       
        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);  
       return  table;
        
    }
    private void setupKeyEvents()
    {
        if (keyEventsAreSet) return;
        keyEventsAreSet = true;
        jTableSource.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0, false), "set Breakpoint");
        jTableSource.getActionMap().put("set Breakpoint", new AbstractAction() { public void actionPerformed(ActionEvent e) {  if (vecxPanel==null) return; vecxPanel.debugBreakpointAction(); }});
        jTableSource.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_R,0, false), "RUN");
        jTableSource.getActionMap().put("RUN", new AbstractAction() { public void actionPerformed(ActionEvent e) {  if (vecxPanel==null) return; vecxPanel.debugAction(); }});

        jTextFieldCommand.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,0, false), "command next");
        jTextFieldCommand.getActionMap().put("command next", new AbstractAction() { public void actionPerformed(ActionEvent e) {   commandHistoryNext(); }});
        jTextFieldCommand.getInputMap(WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP,0, false), "command previous");
        jTextFieldCommand.getActionMap().put("command previous", new AbstractAction() { public void actionPerformed(ActionEvent e) {  commandHistoryPrevious(); }});
    }   

    public void updateValues(boolean forceUpdate)
    {
        if (!forceUpdate)
            if (!updateEnabled) return;
        updateTableOnly();
    }
    public void setUpdateEnabled(boolean b)
    {
        updateEnabled = b;
    }
    
    public boolean hasBreakpoint(int adr)
    {
        MemoryInformation memInfo = dasm.myMemory.memMap.get(adr);
        if (memInfo == null) return false;
        return memInfo.hasBreakpoint();
    }

    public int getCurrentBank()
    {
        return dasm.myMemory.getCurrentBank();
    }
    public boolean hasBreakpoint(int adr, int bank)
    {
        MemoryInformation memInfo = dasm.myMemory.get(adr, bank);
        
        if (memInfo == null) return false;
        return memInfo.hasBreakpoint();
    }
    public void processHeyDissis()
    {
        Memory mem = dasm.myMemory;
        
        for (int i=0; i<65536; i++)
        {
            MemoryInformation memInfo = mem.memMap.get(i);
            if (memInfo == null) continue;
            for (String comment: memInfo.comments)
            {
                int hey = comment.toLowerCase().indexOf("hey dissi");
                if (hey <0) continue;
                addHeyDissi(i, mem.currentBank, comment);
            }
        }
    }
    private void addHeyDissi(int address, int bank, String hey)
    {
        // remove hey dissi
        int i = hey.toLowerCase().indexOf("hey dissi");
        hey = hey.substring(i+9).trim();

        // get what is in between quotes
        i=hey.indexOf("\""); // first quote
        if (i<0) return;
        hey = hey.substring(i+1).trim();
        i=hey.indexOf("\""); // second quote
        if (i>0) // if there is no second ", don'T bother its not THAT important!
            hey = hey.substring(0,i).trim();
        
        // in hey now our commands
        // they should be splitable and than "doable"
        String[] commands = hey.split(" ");
        int commandCounter = 0;
        for (;commandCounter<commands.length;)
        {
            if (commands[commandCounter].length()==0) continue;
            if (commands[commandCounter].toLowerCase().equals("print"))
            {
                addHeyDissiPrint(address, bank, commands, commandCounter+1);
                return; // nothing else can follow
            }
            if (commands[commandCounter].toLowerCase().equals("break"))
            {
                addHeyDissiBreak(address, bank, commands, commandCounter+1);
                return; // nothing else can follow
            }
            commandCounter++;
        }
        
    }
    
    void addHeyDissiPrint(int address, int bank, String[] commands, int parameterStart)
    {
        if (vecxPanel==null)printMessage("\"Hey dissi\" not processed, vecx not found!", MESSAGE_WARN);
            
        Breakpoint bp = new Breakpoint();
        bp.targetAddress = address;
        bp.targetType = Breakpoint.BP_TARGET_CPU;
        bp.targetBank = bank;
        bp.targetSubType = Breakpoint.BP_SUBTARGET_CPU_PC;
        bp.type = Breakpoint.BP_COMPARE | Breakpoint.BP_MULTI|Breakpoint.BP_INFO|Breakpoint.BP_HEY;
        bp.exitType = EMU_EXIT_BREAKPOINT_CONTINUE;
        
        // take $xxxx as memory location
        // take $label_name as label, seek memory location and take that
        // everything else is printed verbatim
        for (int i=parameterStart; i< commands.length; i++)
        {
            if (bp.printInfo==null) bp.printInfo = new ArrayList<String>();
            if (commands[i].trim().length()==0) continue;
            if (!commands[i].startsWith("$")) 
            {
                bp.printInfo.add(commands[i]); // print verbatim all strings which do not start with $
                continue;
            }

            int adr = DASM6809.toNumber(commands[i]); // is it a number?
            if ((adr & 0xffff) != 0) 
            {
                bp.printInfo.add(String.format("$%04X",adr&0xffff));
                continue;
            }
            
            if (commands[i].substring(1).trim().toLowerCase().equals("$intx"))
            {
                bp.printInfo.add("$intx"); 
                continue;
            }
            if (commands[i].substring(1).trim().toLowerCase().equals("$inty"))
            {
                bp.printInfo.add("$inty"); 
                continue;
            }
            
            
            
            // seek label!
            adr = getLabelAddr(commands[i].substring(1).trim());
            if (adr == 0) 
            {
                bp.printInfo.add(commands[i]); // take string verbatim, even with a $
                continue;
            }
            bp.printInfo.add(commands[i].substring(1).trim()+"="); 
            bp.printInfo.add(String.format("$%04X",adr&0xffff )); // translate label to hex address
        }
        vecxPanel.breakpointMemorySet(bp);
    }

    // if we do that more often, we should build a lookup table for labels as a hashmap!
    int getLabelAddr(String label)
    {
        Memory mem = dasm.myMemory;
        
        for (int i=0; i<65536; i++)
        {
            MemoryInformation memInfo = mem.memMap.get(i);
            for (String l: memInfo.labels)
            {
                if (l.equals(label)) 
                    return i;
            }
        }
        return 0; // not successull
        
    }
    void addHeyDissiBreak(int address, int bank, String[] commands, int parameterStart)
    {
        // no conditionals yet, just break
        Breakpoint bp = new Breakpoint();
        bp.targetAddress = address;
        bp.targetBank = bank;
        bp.targetType = Breakpoint.BP_TARGET_CPU;
        bp.targetSubType = Breakpoint.BP_SUBTARGET_CPU_PC;
        bp.type = Breakpoint.BP_COMPARE | Breakpoint.BP_MULTI|Breakpoint.BP_HEY;
        bp.exitType = EMU_EXIT_BREAKPOINT_BREAK;
        
        vecxPanel.breakpointMemorySet(bp);
    }
    
    void removeHeyDissi(MemoryInformation memInfo)
    {
        if (!memInfo.hasBreakpoint()) return;
        ArrayList<Breakpoint> tmp = (ArrayList<Breakpoint>) memInfo.getBreakpoints().clone(); // concurrency...
        for (Breakpoint bp: tmp)
        {
            if ((bp.type & Breakpoint.BP_HEY) == Breakpoint.BP_HEY)
            {
                vecxPanel.breakpointRemove(bp);
            }
        }
    }
    
    public void doHeyDissiBreakpoint(Breakpoint bp)
    {
        // for now only PRINT
        StringBuilder message = new StringBuilder();
        for (String c: bp.printInfo)
        {
            if (!c.startsWith("$")) 
            {
                message.append(c).append(" ");
                continue;
            }
            if (c.equals("$intx"))
            {
                message.append("Integrator X = "+vecxPanel.getXIntegratorValue());
                continue;
            }
            if (c.equals("$inty"))
            {
                message.append("Integrator Y = "+vecxPanel.getYIntegratorValue());
                continue;
            }
            int adr = DASM6809.toNumber(c); // is it a number?
            if (adr==0)     // can't interprete number
            {
                message.append(c).append(" ");
                continue;
            }
            
            int b = vecxPanel.getVecXMem8(adr)&0xff;
            message.append(String.format("$%04X",adr )).append("=").append(String.format("$%02X",b )).append(" ");
            
        }
        printMessage(message.toString(), MESSAGE_INFO);
    }
    public void printMessage(String s, String type)
    {
        try
        {
            jEditorPane1.getDocument().insertString(jEditorPane1.getDocument().getLength(), s+"\n", TokenStyles.getStyle(type));
        } catch (Throwable e) { }
        jEditorPane1.setCaretPosition(jEditorPane1.getDocument().getLength());
    }
    
    static class DissiStateInfo implements Serializable
    {
        int pos=0;
        Boolean[] columnVisible;
        int[]  columnWidth;
        int[] columnWidthSmall;
    }
    void rememberColumnsWidth()
    {
        MemoryInformationTableModel model = (MemoryInformationTableModel)jTableSource.getModel();
        for (int i = 0;i<jTableSource.getColumnCount(); i++)
        {
            TableColumn tcol = jTableSource.getColumnModel().getColumn(i);
            model.columnWidth[model.convertViewToModel(i)] = tcol.getWidth();
        }
    }
    public Serializable getAdditionalStateinfo()
    {
        DissiStateInfo sl = new DissiStateInfo();
        sl.pos = jSplitPane1.getDividerLocation();
        sl.columnVisible = new Boolean[MemoryInformationTableModel.columnVisible.length];
        sl.columnWidth = new int[MemoryInformationTableModel.columnWidth.length];
        sl.columnWidthSmall = new int[MemoryInformationTableModel.columnWidthSmall.length];
        for (int i=0;i<MemoryInformationTableModel.columnVisible.length; i++)
            sl.columnVisible[i]= MemoryInformationTableModel.columnVisible[i];
        for (int i=0;i<MemoryInformationTableModel.columnWidth.length; i++)
        {
            sl.columnWidth[i]= MemoryInformationTableModel.columnWidth[i];
        }
        for (int i=0;i<MemoryInformationTableModel.columnWidthSmall.length; i++)
            sl.columnWidthSmall[i]= MemoryInformationTableModel.columnWidthSmall[i];
        return sl;
    }
    public void setAdditionalStateinfo(Serializable ser)
    {
        DissiStateInfo sl = (DissiStateInfo) ser;
        jSplitPane1.setDividerLocation(sl.pos);
        MemoryInformationTableModel.columnVisible = new Boolean[sl.columnVisible.length];
        MemoryInformationTableModel.columnWidth = new int[sl.columnWidth.length];
        MemoryInformationTableModel.columnWidthSmall = new int[sl.columnWidthSmall.length];
        for (int i=0;i<sl.columnVisible.length; i++)
            MemoryInformationTableModel.columnVisible[i]= sl.columnVisible[i];
        for (int i=0;i<sl.columnWidth.length; i++)
        {
            MemoryInformationTableModel.columnWidth[i]= sl.columnWidth[i];
        }
        for (int i=0;i<sl.columnWidthSmall.length; i++)
            MemoryInformationTableModel.columnWidthSmall[i]= sl.columnWidthSmall[i];
        correctModel();
    }
    
    // receives the contents of the textfield after a return
    public void executeCommand(String command)
    {
       
        printMessage(command, MESSAGE_INFO);
        String[] parts = command.split(" ");
        if (parts.length==0) return;
        if (parts[0].length()==0) return;
        Command com = Command.getCommand(parts[0]);
        if (com == null)
        {
            printMessage("Syntax Error!", MESSAGE_ERR);
            return;
        }
        executeCommand(com, parts);
    }
    // executes given command with given parameters, param[0] is string of command invocation!
    public void executeCommand(Command command, String[] param)
    {
        if (vecxPanel==null) 
        {
            printMessage("Sorry, can't do that now! (emulation not started)", MESSAGE_WARN);
            return; 
        }
        switch (command.ID)
        {
            case Command.D_CMD_HELP:
            {
                printMessage(Command.getHelp(), MESSAGE_INFO);
                break;
            }
            case Command.D_CMD_GO:
            {
                if (param.length==2) 
                {
                    String param1 = param[1];
                    int num = DASM6809.toNumber(param1);
                    vecxPanel.setPC(num);
                }
                vecxPanel.run();
                break;
            }
            
            case Command.D_CMD_SOFTRESET:
            {
                vecxPanel.resetCurrent(true);
                vecxPanel.run();
                break;
            }
            case Command.D_CMD_RESET:
            {
                vecxPanel.resetCurrent();
                break;
            }
            case Command.D_CMD_CYCLES:
            {
                if (param.length==1) 
                {
                    vecxPanel.getCyclesRunning();
                    printMessage("Vecx works hard for "+vecxPanel.getCyclesRunning()+" cycles!", MESSAGE_INFO);
                    return;
                }
                String param1 = param[1];
                int num = DASM6809.toNumber(param1);
                vecxPanel.setCyclesRunning(num);
                printMessage("Vecx cycle count set to: "+vecxPanel.getCyclesRunning(), MESSAGE_INFO);
                break;
            }
            case Command.D_CMD_RUN_CYCLES:
            {
                if (param.length==1) 
                {
                    printMessage("More input needed...", MESSAGE_WARN);
                    return;
                }
                String param1 = param[1];
                int num = DASM6809.toNumber(param1, true);
                if (num==0) 
                {
                    printMessage("Running for 0 cycles uff - done!", MESSAGE_WARN);
                    return;
                }

                Breakpoint bp = new Breakpoint();
                bp.compareValue = num;
                
                bp.targetType = Breakpoint.BP_TARGET_CPU;
                bp.targetSubType = Breakpoint.BP_SUBTARGET_CPU_CYCLES;
                bp.name = "cycle run";
                bp.type = Breakpoint.BP_COMPARE | Breakpoint.BP_ONCE;
                vecxPanel.breakpointCyclesSet(bp);
                printMessage("Vecx one time breakpoint set after "+num+" cycles.", MESSAGE_INFO);
                if (param.length ==2)
                    vecxPanel.run();
                break;
            }
            
            case Command.D_CMD_CYCLES_MEASURE:
            {
                int startadr =0xF1A2;
                int endadr = 0xF192;
                if (param.length==1)
                {
                    ;
                }
                else if (param.length==2) 
                {
                    printMessage("Out of Data error!...", MESSAGE_WARN);
                    return;
                }
                else
                {
                    startadr = DASM6809.toNumber(param[1]);
                    endadr = DASM6809.toNumber(param[2]);
                }
                
                if ((startadr==0) ||(endadr==0))
                {
                    printMessage("You plan to execute adr $0000? - MURDER!", MESSAGE_WARN);
                    return;
                }

                Breakpoint bp = new Breakpoint();
                bp.targetAddress = startadr;
                bp.compareValue = endadr;
                bp.name = "cycle measure";
                bp.targetType = Breakpoint.BP_TARGET_CPU;
                bp.targetSubType = Breakpoint.BP_SUBTARGET_CPU_SPECIAL;
                bp.type = Breakpoint.BP_COMPARE | Breakpoint.BP_ONCE;
                vecxPanel.breakpointSet(bp);
                printMessage("Waiting for emulation to reach address "+String.format("$%04X", startadr ), MESSAGE_INFO);
                break;
            }
                    
            case Command.D_CMD_BREAKPOINT:
            {
                if (param.length<1) return;
                String param1 = param[1];
                int num = DASM6809.toNumber(param1);
                if (num != 0)
                {
                    Breakpoint bp = new Breakpoint();
                    bp.targetAddress = num;
                    bp.targetBank = getCurrentBank();
                    bp.targetType = Breakpoint.BP_TARGET_CPU;
                    bp.name = "Address: "+String.format("$%04X", num )+"["+bp.targetBank+"]";
                    bp.targetSubType = Breakpoint.BP_SUBTARGET_CPU_PC;
                    bp.type = Breakpoint.BP_COMPARE | Breakpoint.BP_MULTI;
                    vecxPanel.breakpointAddressToggle(bp);
                    printMessage("Breakpoint at address $"+String.format("$%04X", num )+", bank "+bp.targetBank+" toggled.", MESSAGE_INFO);
                    break;
                }
                else if (param1.toLowerCase().contains("bankswitch"))
                {
                    Breakpoint bp = new Breakpoint();
                    bp.targetBank = getCurrentBank();
                    bp.targetType = Breakpoint.BP_TARGET_CARTRIDGE;
                    bp.name = "Bankswitch";
                    bp.type = /*Breakpoint.BP_COMPARE |*/ Breakpoint.BP_MULTI|Breakpoint.BP_BANK;
                    boolean added = vecxPanel.breakpointBankToggle(bp);
                    if (added) printMessage("Breakpoint for bankswitching added.", MESSAGE_INFO);
                    else printMessage("Breakpoint for bankswitching removed.", MESSAGE_INFO);
                    break;
                }
                else if (param1.toLowerCase().contains("VIA_ORB".toLowerCase()))
                {
                    String param2 = param[2];
                    Breakpoint bp = new Breakpoint();
                    bp.compareValue = Math.abs(DASM6809.toNumber(param2)%8); // ensure bit0-7
                    bp.targetBank = getCurrentBank();
                    bp.name = "VIA_ORB bit "+bp.compareValue;
                    bp.targetType = Breakpoint.BP_TARGET_VIA;
                    bp.targetSubType = Breakpoint.BP_SUBTARGET_VIA_ORB;
                    bp.type = Breakpoint.BP_BITCOMPARE | Breakpoint.BP_MULTI;
                    boolean added = vecxPanel.breakpointVIAToggle(bp);
                    if (added) printMessage("Breakpoint for via orb bit "+bp.compareValue+" added.", MESSAGE_INFO);
                    else printMessage("Breakpoint for via orb bit "+bp.compareValue+" removed.", MESSAGE_INFO);
                    break;
                }
                
                printMessage("I don't know what you mean by that!", MESSAGE_WARN);
                break;
            }
            case Command.D_CMD_BREAKPOINT_RESET:
            {
                vecxPanel.breakpointClearAll();

                printMessage("Breakpoints cleared!", MESSAGE_INFO);
                printMessage("(even hey dissi breakpoints,  although comments were not altered!)", MESSAGE_INFO);
                break;
            }
            case Command.D_CMD_PRINT:
            {
                if (param.length<2) return;
                int address = DASM6809.toNumber(param[1]);
                String out = "";
                // if 0 than not a number
                // seek label!
                if (address == 0)
                {
                    if (param[1].startsWith("$")) param[1] = param[1].substring(1);
                    address = getLabelAddr(param[1].trim());
                    if (address == 0)
                    {
                        printMessage("Print not recognized.", MESSAGE_WARN);
                    }
                    else
                    {
                        out+="Contents of $"+param[1].trim()+" ("+String.format("$%04X", address)+")";
                    }
                }
                else
                {
                    out+="Contents of address "+String.format("$%04X", address );
                }
                if (address == 0)
                {
                    printMessage("Print not recognized.", MESSAGE_WARN);
                    return;
                }
                out +=": ";
                boolean bit8 = true;
                int value = 0;
                if (param.length>2)
                {
                    if (param[2].contains("16"))
                        bit8=false;
                }
                value += vecxPanel.getVecXMem8(address) & 0xff;
                if (!bit8)
                {
                    value = value*256;
                    value += (vecxPanel.getVecXMem8(address+1) & 0xff);
                    out += String.format("$%04X", value );
                }
                else
                {
                    out += String.format("$%02X", value );
                }
                printMessage(out, MESSAGE_INFO);
                break;
            }
            case Command.D_CMD_LABEL_RESET:
            {
                for (int m = 0; m<=0xffff; m++)
                {
                    MemoryInformation memInfo = getMemoryInformation(m, getCurrentBank());
                    for (String label: memInfo.labels)
                    {
                        String labtemp = "_"+String.format("%04X", (m & 0xFFFF));
                        if (label.equals(labtemp))
                        {
                            memInfo.labels.remove(label);
                            memInfo.reset();
                            break;
                        }
                    }
                }
                dasm.reDisassemble(true);
                updateTable();        
                printMessage("All autobuild labels were resetted, rebuild was executed (if configured).", MESSAGE_INFO);
                break;
            }
            case Command.D_CMD_REMOVE_DISSI_COMMENTS:
            {
                for (int b=0; b<getMemory().maxBank; b++)
                {
                    for (int a=0; a<65536; a++)
                    {
                        MemoryInformation memInfo = getMemory().get(a, b);
                        if (memInfo ==null) continue;
                        for (String comment: memInfo.comments)
                        {
                            if (comment.contains("DISSI")) 
                            {
                                memInfo.comments.remove(comment);
                                break;
                            }
                        }
                    }
                }
                printMessage("All comments containing \"DISSI\" have been removed!", MESSAGE_INFO);
                updateTable();
                break;
            }
            case Command.D_CMD_INFO:
            {
                Cartridge cart = vecxPanel.getCartridge();
                if (cart == null)
                {
                    printMessage("Sorry, can't do that now! (no cartridge loaded)", MESSAGE_WARN);
                    return;
                }

                printMessage("Cartridge name: "+cart.cartName, MESSAGE_INFO);
                printMessage("Cartridge banks# : "+cart.getBankCount(), MESSAGE_INFO);
                for (int i=0; i<cart.getBankCount(); i++)
                {
                    printMessage("bank size #"+i+": "+cart.getBankSize(i), MESSAGE_INFO);
                }
                printMessage("Cartridge CRC32: "+cart.getCRC()+" (over all banks)", MESSAGE_INFO);
                printMessage("Current bank (emulation): "+vecxPanel.getCurrentBank()+" (dissi: "+getMemory().currentBank+")", MESSAGE_INFO);
                break;
            }
            case Command.D_CMD_BANKSWITCH:
            {
                if (param.length<2) 
                {
                    return;
                }
                int bank = DASM6809.toNumber(param[1]);

                setDissiBank(bank);

                printMessage("Bank in dissi switched to: "+getCurrentBank()+" (bank may be changed automatically by emulation...)", MESSAGE_INFO);
                break;
            }
            case Command.D_CMD_BANKSWITCH_INFO:
            {
                bankswitchInfo = !bankswitchInfo;
                
                printMessage("Information display for bankswitches: "+(bankswitchInfo?"on":"off"), MESSAGE_INFO);
                break;
            }
            case Command.D_CMD_CLEAR_SCREEN:
            {
                jEditorPane1.setText("");
                break;
            }
            case Command.D_CMD_POKE:
            {
                if (param.length!= 3)
                {
                    printMessage("OUT OF DATA ERROR!", MESSAGE_ERR);
                    break;
                }
                String param1 = param[1];
                String param2 = param[2];
                int address = DASM6809.toNumber(param1)& 0xFFFF;
                int value = DASM6809.toNumber(param2) & 0xff;
                if (address == 53280)
                {
                    jEditorPane1.setBackground(new Color(00, 0x88,0xff ));
                    printMessage("Ooops, that was a C64 poke!", MESSAGE_INFO);
                    return;
                }
                
                int bank = vecxPanel.getCurrentBank();
                MemoryInformation memInfo = getMemoryInformation(address, bank);
                memInfo.content = (byte)value;

                int len = memInfo.length;
                for (int a=memInfo.address-5; a<memInfo.address+5; a++)
                {
                    dasm.myMemory.memMap.get(a).reset();
                }
                dasm.reDisassemble(memInfo.address>0xdff5);
                updateTable();        
                vecxPanel.poke(address, (byte)value);
                        
                        
                printMessage("I poked "+String.format("%02X", (value))+" to location: "+String.format("%04X", (address ))+" (current bank)", MESSAGE_INFO);
                break;
            }            
            default:
            {
                printMessage("Command not implemented yet.", MESSAGE_WARN);
            }
        }
    }
    
    
    public void varUpdate()
    {
        // labels
        // do complete disasm
        dasm.reDisassemble(true);
        updateTable();            
        vecxPanel.initLabels();
    }
    public void completeUpdate()
    {
        int end = 0;
        do
        {
            
            MemoryInformation memInfo = getMemoryInformation(end, getCurrentBank());
            
            int len = memInfo.length;
            for (int a=memInfo.address; a<memInfo.address+len; a++)
            {
                // join / ungroup makes no sense for NON code
                if (dasm.myMemory.memMap.get(a).disType <MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_1_LENGTH) continue;

                dasm.myMemory.memMap.get(a).disType = MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_GENERAL;
                dasm.myMemory.memMap.get(a).belongsToInstruction = null;
                dasm.myMemory.memMap.get(a).disassembledMnemonic = "";
                dasm.myMemory.memMap.get(a).disassembledOperand = "";
                dasm.myMemory.memMap.get(a).page = -1;
                dasm.myMemory.memMap.get(a).hexDump = "";
                dasm.myMemory.memMap.get(a).isInstructionByte = 0;
                dasm.myMemory.memMap.get(a).referingToAddress = -1;
                dasm.myMemory.memMap.get(a).referingAddressMode = -1;
                dasm.myMemory.memMap.get(a).length = 1;
                dasm.myMemory.memMap.get(a).done = false;
                dasm.myMemory.memMap.get(a).familyBytes.clear();
            }
            end = memInfo.address+len;
        } while (end < 65536);           
        dasm.reDisassemble(true);
        updateTable();            
        vecxPanel.initLabels();
    }    
    public boolean outputCNT()
    {
        
        if (loadedName==null) return false;
        if (loadedName.trim().length()==0) return false;
        StringBuilder s = new StringBuilder();
        
        for (int bank = 0; bank < getMemory().maxBank; bank++)
        {
            s.append("START_BANK");
            spaceTo(s, 0, TAB_MNEMONIC);
            s.append(String.format("$%02X",bank)).append("\n");
            
            int start = 0;
            int end = 0xdfff;
            for (int m = start; m<end; m++)
            {
                int c=0;
                // Labels
                MemoryInformation memInfo = getMemoryInformation(m, bank);
                for (String label: memInfo.immediateLabels)
                {
                    s.append("EQU");
                    spaceTo(s, c, TAB_MNEMONIC);
                    s.append(String.format("$%04X",m));
                    spaceTo(s, c, TAB_OP);
                    s.append(label).append("\n");
                }
                // labels 2
                for (String label: memInfo.labels)
                {
                    s.append("LABEL");
                    spaceTo(s, c, TAB_MNEMONIC);
                    s.append(String.format("$%04X",m));
                    spaceTo(s, c, TAB_OP);
                    s.append(label).append("\n");
                }
                // comments
                for (String comment: memInfo.comments)
                {
                    if (memInfo.disType<MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_1_LENGTH)
                    {
                        if (comment.contains("DISSI")) continue;
                        // don't keep all dissi error comments for data...
                    }
                    s.append("COMMENT");
                    spaceTo(s, c, TAB_MNEMONIC);
                    s.append(String.format("$%04X",m));
                    spaceTo(s, c, TAB_OP);
                    s.append(comment).append("\n");
                }
            }

            // DP
            int lastAddress = 0;
            int oldDP = -1;
            int currentDP=0;
            int dpStart = 0;
            for (int m = start; m<end; m++)
            {
                int c=0;
                MemoryInformation memInfo = getMemoryInformation(m, bank);
                currentDP = memInfo.directPageAddress;
                if (currentDP != oldDP)
                {
                    if (oldDP != -1)
                    {
                        // new dp range
                        // RANGE 1000-2000 DP D0
                        s.append("RANGE ").append(String.format("$%04X", (dpStart&0xffff))).append("-").append(String.format("$%04X", (lastAddress&0xffff))).append(" DP ").append(String.format("$%02X", (oldDP&0xff))).append("\n");
                    }
                    dpStart = m;
                }
                oldDP = currentDP;
                lastAddress = m;
            }        
            if (currentDP != -1)
            {
                // one more dp line
                s.append("RANGE ").append(String.format("$%04X", (dpStart&0xffff))).append("-").append(String.format("$%04X", (lastAddress&0xffff))).append(" DP ").append(String.format("$%02X", (oldDP&0xff))).append("\n");
            }

            // data / code
            // code / data
            for (int m = start; m<end; )
            {
                MemoryInformation memInfo = getMemoryInformation(m, bank);
                int type = memInfo.disType;
                if (type>=5) type = MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_GENERAL;

                int len = getConsecutiveType(m, bank, type);

                // out
                if ((type == MemoryInformation.DIS_TYPE_DATA_BYTE))
                {
                    s.append("RANGE ").append(String.format("$%04X", (m&0xffff))).append("-").append(String.format("$%04X", ((m+len)&0xffff))).append(" DB_DATA"+"\n");
                }
                else if ((type == MemoryInformation.DIS_TYPE_DATA_CHAR))
                {
                    s.append("RANGE ").append(String.format("$%04X", (m&0xffff))).append("-").append(String.format("$%04X", ((m+len)&0xffff))).append(" CHAR_DATA"+"\n");
                }
                else if ((type == MemoryInformation.DIS_TYPE_DATA_CHAR))
                {
                    s.append("RANGE ").append(String.format("$%04X", (m&0xffff))).append("-").append(String.format("$%04X", ((m+len)&0xffff))).append(" DW_DATA"+"\n");
                }
                else if ((type == MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_GENERAL))
                {
                    s.append("RANGE ").append(String.format("$%04X", (m&0xffff))).append("-").append(String.format("$%04X", ((m+len)&0xffff))).append(" CODE"+"\n");
                }
                m+= len;
            }        

            // direct labels
            // and all direct labels!
            Iterator it = getMemory().directLabels.entrySet().iterator();
            while (it.hasNext()) 
            {
                Map.Entry pair = (Map.Entry)it.next();
                int directPageHi = (int) pair.getKey();
                HashMap<Integer, MemoryInformation> entries = (HashMap<Integer, MemoryInformation>) pair.getValue();

                Iterator it2 = entries.entrySet().iterator();
                while (it2.hasNext()) 
                {
                    Map.Entry pair2 = (Map.Entry)it2.next();
                    int directPageLo = (int) pair2.getKey();
                    String directLabel = (String)pair2.getValue();

                    int value = directPageHi*256+directPageLo;
                    s.append("DIRECT_LABEL "+String.format("$%02X", ((directPageHi)&0xff))+String.format(" $%02X", (directPageLo&0xff))+" "+directLabel+"\n");
                }
            }
            s.append("END_BANK");
            spaceTo(s, 0, TAB_MNEMONIC);
            s.append(String.format("$%02X",bank)+"\n");
        }
        try
        {
            String name = loadedName;
            int li = name.lastIndexOf(".");
            if (li<0) return false;
            name = name.substring(0,li)+".cnt";

            PrintWriter out = new PrintWriter(name);
            out.println(s.toString());
            out.close();
            printMessage("DASM CNT output saved to \""+name+"\"", MESSAGE_INFO);
        }
        catch (Throwable e)
        {
            printMessage("Error saving DASM CNT file...", MESSAGE_ERR);
            return false;
        }        
        return true;
    }
    // returns the number of addresses which have the same type, staring at address
    int getConsecutiveType(int address, int bank, int type)
    {
        int len = 0;
        int newType;
        do
        {
            len++;
            address++;
            newType = getMemoryInformation(address, bank).disType;
            if (newType>=5) newType = MemoryInformation.DIS_TYPE_DATA_INSTRUCTION_GENERAL;
        }
        while  (type == newType);
        
        return len;
    }
    public void cartridgeChanged(CartridgeEvent e)
    {
        // called from emulation thread!
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                dasm.myMemory.setBank(e.newBank);
                if (bankswitchInfo)
                    printMessage("Bank was switched from: "+e.oldBank+" to "+e.newBank, MESSAGE_INFO);
//                updateTableOnly(); // if only this is used, multi banks accross multi files are not updated correctly in table (disassambled operand!
                correctModel();
                if (vecxPanel == null) return;
                vecxPanel.updateLabi();
                vecxPanel.updateVari();
            }
        });                    

    }
    public void setDissiBank(int bank)
    {
        dasm.myMemory.setBank(bank);
        correctModel();    
        if (vecxPanel==null) return;
        vecxPanel.updateLabi();
    }
    public void setUpTableColumns()
    {
        while (jTableSource.getColumnCount()>0)
        {
            jTableSource.getColumnModel().removeColumn(jTableSource.getColumnModel().getColumn(0));
        }
        for (int i=0; i< allColumns.size(); i++)
        {
            if (MemoryInformationTableModel.columnVisible[i])
                jTableSource.getColumnModel().addColumn(allColumns.get(i));
        }
    }
    
    public static void configChanged()
    {
        for (DissiPanel p: panels )
            p.setUpTableColumns();
    }
    void initOldBreakPoints()
    {
        
        dasm.myMemory.resetAllBreakPoints();
        ArrayList<Breakpoint>[] allBreakpoints = vecxPanel.getAllBreakpoints();
        int y = 0;
        ArrayList<Breakpoint> tmp = new ArrayList<Breakpoint>();
        for (ArrayList<Breakpoint> list: allBreakpoints)
        {
            for (Breakpoint bp: list)
            {
                if (bp.targetType == Breakpoint.BP_TARGET_CPU)
                {
                    if (bp.targetSubType == Breakpoint.BP_SUBTARGET_CPU_PC)
                    {
                        int address = bp.targetAddress;
                        int bank = bp.targetBank;
                        bp.memInfo = null;
                        MemoryInformation memInfo = getMemoryInformation(address, bank);
                        memInfo.removeBreakpoint(bp); // ensure no doubles
                        memInfo.addBreakpoint(bp);
                        bp.memInfo = memInfo;
                    }
                }
            }
        }        
        updateTableOnly();
    }
}