/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.malban.vide.vedi.panels;

import de.malban.config.TinyLogInterface;
import de.malban.vide.vedi.VEdiFoundationPanel;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author malban
 */
public class ImagePanel extends javax.swing.JPanel {

    VEdiFoundationPanel parent;
    public void setParent(VEdiFoundationPanel p)
    {
        parent = p;
    }
    
    TinyLogInterface tinyLog = null;
    private String filename = "";
    boolean initError = false;

    /**
     * Creates new form ImagePanel
     */
    public ImagePanel() {
        initComponents();
    }

    public ImagePanel(String fn, TinyLogInterface tl)
    {
        initComponents();
        filename = fn;
        try
        {
            setup();
        }
        catch (Throwable e)
        {
            initError = true;
        }
        if (initError)
        {
            deinit();
        }        
    }
    public void deinit()
    {
    }
    public boolean isInitError()
    {
        return initError;
    }
    public void setup()
    {
        singleImagePanel1.setImage(filename, true);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        singleImagePanel1 = new de.malban.graphics.SingleImagePanel();

        javax.swing.GroupLayout singleImagePanel1Layout = new javax.swing.GroupLayout(singleImagePanel1);
        singleImagePanel1.setLayout(singleImagePanel1Layout);
        singleImagePanel1Layout.setHorizontalGroup(
            singleImagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        singleImagePanel1Layout.setVerticalGroup(
            singleImagePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(singleImagePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(singleImagePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.malban.graphics.SingleImagePanel singleImagePanel1;
    // End of variables declaration//GEN-END:variables
}
