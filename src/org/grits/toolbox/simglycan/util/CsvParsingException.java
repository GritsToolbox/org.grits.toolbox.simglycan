package org.grits.toolbox.simglycan.util;

public class CsvParsingException extends Exception
{
    private static final long serialVersionUID = 1L;
    private String m_lastLine = null;
    
    public CsvParsingException(String a_lastLine, String a_message)
    {
        super(a_message);
        this.m_lastLine = a_lastLine;
    }

    public String getLastLine()
    {
        return this.m_lastLine;
    }
}
