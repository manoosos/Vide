/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FirePanel.java
 *
 * Created on 08.03.2010, 12:21:17
 */

package de.malban.gui.panels;

import de.malban.Global;
import de.malban.sound.PlayMP3;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.io.File;

/**
 *
 * @author Malban
 */
public class FirePanel3 extends javax.swing.JPanel implements Runnable{
    int backShade =00;
    int notShownBottomLines=4;
    int animDelay = 30;

    Thread animator = null;
    boolean doRun = false;
    int index=0;
    Image im = null;
    MemoryImageSource MemImageSrc = null;
    int backbuffer[] = null;
    int bufferLR[] = null;

    int bufferG[] = null;
    int bufferB[] = null;
    int bufferA[] = null;
    int bufferR[] = null;

    int pixels[] = null;
    int fireWidth = 200;
    int fireHeight = 200;
    boolean deinit=false;
    PlayMP3 play = null;

    public void setDelay(int d)
    {
        animDelay=d;
    }

    /** Creates new form FirePanel */
    public FirePanel3() {
        super.setVisible(false);
        initComponents();
    }

    public void deinit()
    {
        setVisible(false);
        removeAll();
        im = null;
        MemImageSrc = null;
        backbuffer = null;
        bufferLR = null;
        bufferG = null;
        bufferB = null;
        bufferA = null;
        bufferR = null;
        pixels = null;
        deinit = true;
        if (play != null)
        {
            play.close();
            play.deinit();
            play = null;
        }
    }

    @Override public void setVisible(boolean b)
    {
        if (deinit) return;
        if (b==isVisible()) return;
        super.setVisible(b);

        if ( b )
        {
            if (animator == null)
            {
                
                if (!de.malban.config.Configuration.getConfiguration().isSoundQuiet())
                {
                    play = new PlayMP3(Global.mainPathPrefix+"sound"+File.separator+"burningFire.mp3");
                    play.play();
                }
                animator = new Thread(this);
                doRun = true;

                fireHeight=getHeight();
                fireWidth=getWidth();

            	init();

                animator.start();
            }
        }
        else
        {
            if (animator != null)
            {
                synchronized (this)
                {
                    doRun = false;
                    animator.interrupt();
                    animator = null;
                    if (play != null)
                    {
                        play.close();
                        play.deinit();
                        play = null;
                    }
                }
            }
        }
    }

    // Run the animation thread.
    // First wait for the background image to fully load
    // and paint.  Then wait for all of the animation
    // frames to finish loading. Finally, loop and
    // increment the animation frame index.
    @Override
    public void run()
    {
        if (deinit) return;
        while (doRun)
        {
            try
            {
                Thread.sleep(animDelay);
            }
            catch (InterruptedException e)
            {
                break;
            }
            synchronized (this)
            {
            }
            repaint();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setOpaque(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    int RGB ( int a, int r, int g, int b )
    {
        return ( a << 24 ) | ( r << 16) | ( g << 8 ) | b;
    }

    public void init ()
    {
        if (deinit) return;
        backbuffer = new int[fireWidth * (fireHeight+notShownBottomLines)];
        bufferR = new int[fireWidth * (fireHeight+notShownBottomLines)];
        bufferLR = new int[fireWidth * (fireHeight+notShownBottomLines)];
        bufferG = new int[fireWidth * (fireHeight+notShownBottomLines)];
        bufferB = new int[fireWidth * (fireHeight+notShownBottomLines)];
        bufferA = new int[fireWidth * (fireHeight+notShownBottomLines)];
        pixels = new int[fireWidth * (fireHeight+notShownBottomLines)];

        MemImageSrc = new MemoryImageSource( fireWidth, fireHeight , pixels, 0, fireWidth);
        MemImageSrc.setAnimated ( true );
        im = createImage ( MemImageSrc );
        firstUpdate = true;
        for (int i=0; i < 50; i++)
            update ();
    }

    boolean firstUpdate = true;
    private void update ()
    {
        int index = 0;
        int color = 0;

        System.arraycopy ( bufferLR, 0, backbuffer, 0, fireWidth * (fireHeight+notShownBottomLines) );

        int flameHeight=3;
        int flameDieSpeed = 3;
        int flameDieThreshold =10;

        for (int cc=0; cc<flameHeight;cc++)
            for ( int i = 0; i < fireWidth; i++ )
                if ( (int)( Math.random () * 2) == 1 )
                {
                    backbuffer[((fireHeight+notShownBottomLines)-1-cc)*fireWidth + i] = 255;
                }

        int start = 1;
        if ((!firstUpdate) && (fireHeight>90)) start =90;
        for ( int y = start; y < (fireHeight+notShownBottomLines)-1; y++ )
            for ( int x = 1; x < fireWidth-1; x++ )
            {
                index = y * fireWidth + x;
                color = backbuffer[index];
                color += backbuffer[index-1];
                color += backbuffer[index+1];
                color += backbuffer[index + fireWidth];
                color += backbuffer[index - fireWidth];
                color = (int)( (double)color / 5.0 );

                if (y<(fireHeight+notShownBottomLines)-1-flameDieThreshold)
                {
                    if ( color > flameDieSpeed )
                        color-=flameDieSpeed;
                    else
                        color=0;
                }
                else
                {
                    boolean aBigOne=false;
                    if ( backbuffer[index]==255)aBigOne=true;
                    if ( backbuffer[index-1]==255)aBigOne=true;
                    if ( backbuffer[index+1]==255)aBigOne=true;
                    if ((aBigOne) && ( (int)( Math.random () *5) == 1 ))
                    {
                        color = 255;
                    }
                 }
                bufferA[index-fireWidth] = (int)(color*0.8);
                bufferR[index-fireWidth] = color;
                bufferG[index-fireWidth] = color-100;
                if (bufferA[index-fireWidth]<backShade) bufferA[index-fireWidth] = backShade;
                if (bufferG[index-fireWidth]<0)bufferG[index-fireWidth]=0;
            }
            System.arraycopy ( bufferR, 0, bufferLR, 0, fireWidth * (fireHeight+notShownBottomLines) );
    }

    public void draw ( Graphics g )
    {
        update ();
        for ( int i = 0; i < fireWidth * fireHeight; i++ )
        {
            pixels[i] = RGB ( bufferA[i], bufferR[i], bufferG[i], 0 );
        }

        MemImageSrc.newPixels ( 0, 0, fireWidth, fireHeight );
        g.drawImage ( im, 0, 0, null );
    }

    @Override public void paintComponent(Graphics g)
    {
        if (deinit) return;
        g.setColor (Color.red);
        draw (g);
    }

}