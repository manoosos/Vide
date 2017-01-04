/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * LogPanel.java
 *
 * Created on 08.02.2010, 14:03:47
 */

package de.malban.gui.panels;
import de.malban.config.Configuration;
import de.malban.config.LogListener;
import de.malban.config.Logable;
import de.malban.config.TinyLogInterface;
import de.malban.gui.CSAMainFrame;
import de.malban.gui.Windowable;
import de.malban.gui.components.CSAView;
import java.io.*;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Malban
 */
public class LogPanel extends javax.swing.JPanel implements Windowable, Logable, TinyLogInterface{

    public static boolean logToStd = false;
    public static final int MAX_LOG_LEN = 30000;
    private CSAView mParent = null;
    private javax.swing.JMenuItem mParentMenuItem = null;
    private String mTitel = "Log Window";
    private String mLogName;
    private boolean mFileTracking=true;
    private boolean mTimeTracking=false;
    private boolean mFileOnly=false;
    private boolean loggingControl = true; // if true logging on, if flse than off
                                           // set via string in log §stream"

    public static int ERROR = 0;
    public static int WARN = 1;
    public static int INFO = 2;
    public static int VERBOSE = 3;

    public static String[] LEVEL ={"ERROR", "WARN", "INFO", "VERBOSE"};
    
    @Override
    public void closing(){}
    @Override
    public void setDebugFileOnly(boolean b)
    {
        mFileOnly=b;
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
    public void setTrackTime(boolean b)
    {
        mTimeTracking=b;
    }
    @Override
    public void setTrackInFile(boolean b)
    {
        mFileTracking=b;
    }
    /** Creates new form LogPanel */
    public LogPanel() {
        initComponents();
        String time = de.malban.util.UtilityDate.dateToStringGermanClock(new Date());
        time = de.malban.util.UtilityString.replace(time, ".", "_");
        time = de.malban.util.UtilityString.replace(time, ":", "_");
        time = de.malban.util.UtilityString.replace(time, " ", "_");
        mLogName = time;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneLog = new javax.swing.JTextPane();

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTextPaneLog.setName("jTextPaneLog"); // NOI18N
        jScrollPane1.setViewportView(jTextPaneLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPaneLog;
    // End of variables declaration//GEN-END:variables

    public void setTitle(String t)
    {
        mTitel = t;
        mLogName = mTitel + mLogName;
        mLogName = de.malban.util.UtilityString.replace(mLogName, " ", "_");
    }

    @Override
    public void setFiletracking(boolean b)
    {
        mFileTracking = b;
    }

    public void fileAppend(String text)
    {
        if (!mFileTracking) return;
        try
        {
            FileWriter fstream = new FileWriter("logs"+File.separator+mLogName+".log",true);
            PrintWriter pw = new PrintWriter(fstream);
            pw.print(text);
            pw.flush();
            pw.close();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
            System.out.println("LOG could not be created!");
        }
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
        mParentMenuItem.setText(mTitel);
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

    String lineHead="";
    @Override
    public void setLog(String text, int level)
    {
        lineHead="";
        if (!interestedInLog(level)) return;
        setLog(lineHead+": "+text);
    }

    private String correctLoggingText(String text)
    {
        int noLogOff = text.indexOf("//++NO_LOG_OFF");
        int noLogOn = text.indexOf("//++NO_LOG_ON");
        
        if ((noLogOff != -1) && (noLogOn != -1))
        {
            if (noLogOff > noLogOn)
            {
                if (!loggingControl)
                {
                    // sowiso off, delete all to log start
                    text = text.substring(noLogOff);
                }
                else // zwischendruch ein stueck rausschneiden
                {
                    String h = text.substring(0, noLogOn);
                    h += text.substring(noLogOff);
                    text = h;
                }
                loggingControl = true;
                // am ende wird geloggt!
            }
            else
            {
                if (!loggingControl) // am anfang wird nicht geloggt 
                {
                    // einen teil aus der mitte nehmen
                    text = text.substring(noLogOff, noLogOn);
                }
                else // 
                {
                    // dann alles bis zum no log nehmen
                    text = text.substring(0, noLogOn);
                }
                loggingControl = false;
                // am ende wird nicht geloggt!
            }
        }
        else if (noLogOff != -1) 
        {
            if (!loggingControl)
            {
                text = text.substring(noLogOff);
                loggingControl = true;
            }
        }
        else if (noLogOn != -1)
        {
            if (loggingControl)
            {
                text = text.substring(0, noLogOn);
                loggingControl = false;
            }
        }
        else
        {
            if (!loggingControl) text = "";
        }
        return text;
    }

    @Override
    public void setLog(String text)
    {
        text = correctLoggingText(text);
        if (text.length()>0)
            setToLog(text);
    }
    private void setToLog(String text)
    {
        if (mTimeTracking)
            text = de.malban.util.UtilityDate.dateToStringGermanClock(new Date())+": "+text;

        if (!mFileOnly)
        {
            jTextPaneLog.setText(text);
            jTextPaneLog.setCaretPosition(jTextPaneLog.getDocument().getLength());
        }
        fileAppend(text);
    }
    @Override
    public void addLog(String text, int level)
    {
        lineHead="";
        if (!interestedInLog(level)) return;
        addLog(lineHead+": "+text);
    }

    @Override
    public void addLog(String text)
    {
        text = correctLoggingText(text);
        if (text.length()>0)
            addToLog(text);
    }
    private void addToLog(String text)
    {
        if (mTimeTracking)
            text = de.malban.util.UtilityDate.dateToStringGermanClock(new Date())+": "+text;
        if (!mFileOnly)
        {
            try
            {
                jTextPaneLog.replaceSelection(text+"\n");
                jTextPaneLog.setCaretPosition(jTextPaneLog.getDocument().getLength());
            }
            catch(Throwable e){} // clipboard errors!!!
        }
        if (logToStd)
        {
            System.out.println("LogOut:\n"+text+"");
        }
        fileAppend(text+"\n");
        fireLogAdded(text);
        gc();
    }

    int cc = 0;
    private void gc()
    {
        cc++;
        if (cc==50)
        {
            cc=0;
            String t = jTextPaneLog.getText();
            if (t.length() >MAX_LOG_LEN)
            {
                jTextPaneLog.setText(t.substring(t.length()/4));
            }
        }
    }

    String add="";
    @Override
     public void addLog(Throwable e, int level)
     {
         lineHead="";
         // if (Configuration.getConfiguration().getDebugLevel() < level) return;
         addLog(e);
     }
    @Override
     public void addLog(Throwable e)
     {
         add="Exception!!! \n";
         PrintStream  p= new PrintStream(
          new OutputStream()
          {
            @Override
            public void write( int b )
            {
                char[] c = {(char)b};
                String s = new String(c);
                add += s;
            }
          }
        );
         try
         {
        e.printStackTrace(p);
             
         }
         catch (Throwable ex)
         {
System.out.println("Strange");             
         }
        p.flush();
        addLog(lineHead=": \n"+add);
     }

    @Override
    public boolean saveLog()
    {
        return false;
    }
    @Override
    public String getLog()
    {
        return jTextPaneLog.getText();
    }
    @Override
    public void clearLog()
    {
        // file ist not cleared!
        jTextPaneLog.setText("");
        addLog("----");
        addLog("LOG CLEARED!");
        addLog("----");
        jTextPaneLog.setText("");
    }

    private Vector<String> filesOfInterest = new Vector<String>();
    private Vector<String> classesOfInterest = new Vector<String>();
    private Vector<String> methodsOfInterest = new Vector<String>();
    private boolean enhancedDebug = false;

    @Override
    public void setInterestedClasses(String c)
    {
        classesOfInterest.clear();
        enhancedDebug = ! ((classesOfInterest.isEmpty()) && (filesOfInterest.isEmpty())&& (methodsOfInterest.isEmpty()));
        if (c.trim().length()==0) return;
        String classes[] = c.split(",");
        for (int i = 0; i < classes.length; i++)
        {
            String string = classes[i];
            classesOfInterest.addElement(string.trim());
        }
        enhancedDebug = ! ((classesOfInterest.isEmpty()) && (filesOfInterest.isEmpty())&& (methodsOfInterest.isEmpty()));
    }
    @Override
    public void setInterestedMethods(String m)
    {
        methodsOfInterest.clear();
        enhancedDebug = ! ((classesOfInterest.isEmpty()) && (filesOfInterest.isEmpty())&& (methodsOfInterest.isEmpty()));
        if (m.trim().length()==0) return;
        String methods[] = m.split(",");
        for (int i = 0; i < methods.length; i++)
        {
            String string = methods[i];
            methodsOfInterest.addElement(string.trim());
        }
        enhancedDebug = ! ((classesOfInterest.isEmpty()) && (filesOfInterest.isEmpty())&& (methodsOfInterest.isEmpty()));
    }
    @Override
    public void setInterestedFiles(String f)
    {
        filesOfInterest.clear();
        enhancedDebug = ! ((classesOfInterest.isEmpty()) && (filesOfInterest.isEmpty())&& (methodsOfInterest.isEmpty()));
        if (f.trim().length()==0) return;
        String files[] = f.split(",");
        for (int i = 0; i < files.length; i++)
        {
            String string = files[i];
            filesOfInterest.addElement(string.trim());
        }
        enhancedDebug = ! ((classesOfInterest.isEmpty()) && (filesOfInterest.isEmpty())&& (methodsOfInterest.isEmpty()));
    }

    private boolean interestedInLog(int level)
    {
        if (enhancedDebug)
        {
            Thread T = Thread.currentThread();
            StackTraceElement[] tracers =  Thread.getAllStackTraces().get(T);
            for (int i=0; i <tracers.length;i++)
            {
                StackTraceElement traceElement = tracers[i];
                String _file = traceElement.getFileName();
                String _class = traceElement.getClassName();
                String _method = traceElement.getMethodName();
                _S="";
                if (vectorElementsFoundInString(filesOfInterest, _file)) {lineHead = ""+_S;return true;}
                if (vectorElementsFoundInString(classesOfInterest, _class)) {lineHead = ""+_S;return true;}
                if (vectorElementsFoundInString(methodsOfInterest, _method)) {lineHead = ""+_S;return true;}
            }
        }
        if (mDebugLevel < level) return false;
        lineHead = ""+LEVEL[level]+"";
        return true;
    }
    private boolean vectorElementsFoundInString(Vector<String> search, String where)
    {
        if (where==null) return false;
        for (int i = 0; i < search.size(); i++)
        {
            String string = search.elementAt(i);
            if (string.trim().length()==0) continue;
            _S=string;
            if (where.indexOf(string)!=-1) return true;
        }
        return false;
    }
    private int mDebugLevel=0;
    @Override
    public void setDebugLevel(int l)
    {
        mDebugLevel = l;
    }
    String _S="";
    private Vector<LogListener> mLogListener= new Vector<LogListener>();
    @Override
    public void addLogListener(LogListener listener)
    {
        mLogListener.removeElement(listener);
        mLogListener.addElement(listener);
    }

    @Override
    public void removeLogListener(LogListener listener)
    {
        mLogListener.removeElement(listener);
    }
    public void fireLogAdded(String addTest)
    {
        for (int i=0; i<mLogListener.size(); i++)
        {
            mLogListener.elementAt(i).logAddedChanged(addTest);
        }
    }
    public void printMessage(String s)
    {
        addLog(s, INFO);
    }
    public void printWarning(String s)
    {
        addLog(s, WARN);
    }
    public void printError(String s)
    {
        addLog(s, ERROR);
    }
    public void printMessageSU(String s)
    {
        addLog(s, INFO);
    }
    public void printWarningSU(String s)
    {
        addLog(s, WARN);
    }
    public void printErrorSU(String s)
    {
        addLog(s, ERROR);
    }
}
