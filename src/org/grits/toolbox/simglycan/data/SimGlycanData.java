package org.grits.toolbox.simglycan.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "data")
public class SimGlycanData
{
    private List<SimGlycanScan> m_scans = new ArrayList<SimGlycanScan>();
    private String m_fileName = null;
    
    @XmlElement(name="scan")
    public List<SimGlycanScan> getScans()
    {
        return m_scans;
    }
    public void setScans(List<SimGlycanScan> a_scans)
    {
        m_scans = a_scans;
    }
    
    @XmlAttribute(name="file")
    public String getFileName()
    {
        return m_fileName;
    }
    public void setFileName(String a_fileName)
    {
        m_fileName = a_fileName;
    }
    public boolean addScan(SimGlycanScan a_scan)
    {
        return this.m_scans.add(a_scan);
    }
}
