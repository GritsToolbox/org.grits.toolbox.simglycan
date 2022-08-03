package org.grits.toolbox.simglycan.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SimGlycanInfoCSV
{
    private int m_lineNumber = 0;
    private HashMap<ColumnLabel, Integer> m_columnHeader = new HashMap<ColumnLabel, Integer>();
    private List<String> m_unknownColumn = new ArrayList<String>();
    private String m_version = null;
    
    public int getLineNumber()
    {
        return m_lineNumber;
    }

    public void setLineNumber(int lineNumber)
    {
        m_lineNumber = lineNumber;
    }

    public HashMap<ColumnLabel, Integer> getColumnHeader()
    {
        return m_columnHeader;
    }

    public void setColumnHeader(HashMap<ColumnLabel, Integer> columnHeader)
    {
        m_columnHeader = columnHeader;
    }

    public List<String> getUnknownColumn()
    {
        return m_unknownColumn;
    }

    public void setUnknownColumn(List<String> unknownColumn)
    {
        m_unknownColumn = unknownColumn;
    }

    public String getVersion()
    {
        return m_version;
    }

    public void setVersion(String a_version)
    {
        m_version = a_version;
    }

}
