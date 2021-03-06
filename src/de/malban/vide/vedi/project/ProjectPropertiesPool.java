package de.malban.vide.vedi.project;

import static de.malban.util.Utility.makeGlobalAbsolute;
import java.io.File;
import javax.swing.JOptionPane;
import java.util.*;
public class  ProjectPropertiesPool
{
	public static final String DEFAULT_XML_NAME = new String("ProjectProperties.xml");
	private String mFileName = DEFAULT_XML_NAME;
        private String pathName=null;
	private HashMap<String, ProjectProperties> mProjectProperties = new HashMap<String, ProjectProperties>();
	private HashMap<String, String> mKlassenMap = new HashMap<String, String>();
	public ProjectPropertiesPool(String p, String name)
	{
                pathName = p;
		mFileName = name;
		init();
	}
	public ProjectPropertiesPool()
	{
		init();
	}
	public void setFilename(String n)
	{
		mFileName=n;
	}
	private boolean init()
	{
		try
		{
			return load();
		}
		catch (Throwable e)
		{
			JOptionPane.showMessageDialog(null, e.toString() ,"Load Error ProjectProperties...",  JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
	}
        
        
	public boolean load()
	{
            java.io.File f;
            if (pathName==null)
                f = new java.io.File(makeGlobalAbsolute(mFileName) );
            else
                    f = new java.io.File(makeGlobalAbsolute(pathName)+File.separator+mFileName);
            if (!f.exists()) return false;
            if (pathName == null)
                mProjectProperties = ProjectProperties.getHashMapFromXML(mFileName);
            else
                mProjectProperties = ProjectProperties.getHashMapFromXML(mFileName, makeGlobalAbsolute(pathName));
            return true;
	}
	public void save()
	{
            if (pathName==null)
		ProjectProperties.saveCollectionAsXML(mFileName, mProjectProperties.values());
            else
		ProjectProperties.saveCollectionAsXML(makeGlobalAbsolute(pathName), mFileName, mProjectProperties.values());
            buildKlassenMap();
	}
	public void remove(ProjectProperties st)
	{
		mProjectProperties.remove(st.mName);
	}
	public void put(ProjectProperties st)
	{
		mProjectProperties.remove(st.mName);
		mProjectProperties.put(st.mName, st);
	}
	public void putAsNew(ProjectProperties st)
	{
		mProjectProperties.put(st.mName, st);
	}
	public ProjectProperties get(String key)
	{
		return mProjectProperties.get(key);
	}
	public HashMap<String, ProjectProperties> getHashMap()
	{
		return mProjectProperties;
	}
	private void buildKlassenMap()
	{
		mKlassenMap = new HashMap<String, String>();
		Set entries = mProjectProperties.entrySet();
		Iterator it = entries.iterator();
		while (it.hasNext())
		{
			Map.Entry entry = (Map.Entry) it.next();
			ProjectProperties value = (ProjectProperties) entry.getValue();
			mKlassenMap.put(value.mClass, value.mClass);
		}
	}
	public HashMap<String, String> getKlassenHashMap()
	{
		buildKlassenMap();
		return mKlassenMap;
	}
	public HashMap<String, ProjectProperties> getMapForKlasse(String klasse)
	{
		HashMap<String, ProjectProperties> ret = new HashMap<String, ProjectProperties>();
		Set entries = mProjectProperties.entrySet();
		Iterator it = entries.iterator();
		while (it.hasNext())
		{
			Map.Entry entry = (Map.Entry) it.next();
			ProjectProperties value = (ProjectProperties) entry.getValue();
			if (value.mClass.equalsIgnoreCase(klasse))
			{
				ret.put(value.mName, value);
			}
		}
		 return ret;
	}
}
