package org.grits.toolbox.simglycan.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class CSVFileReader
{
    private boolean m_removeQuotes = true;
    private String m_separator = ",";
    private LineNumberReader m_lineReader = null;
    private String m_lastLine = null;
    
    public CSVFileReader(File a_file) throws FileNotFoundException
    {
        this.m_lineReader = new LineNumberReader(new FileReader(a_file));
    }
    
    public boolean isRemoveQuotes()
    {
        return this.m_removeQuotes;
    }
    public void setRemoveQuotes(boolean a_removeQuotes)
    {
        this.m_removeQuotes = a_removeQuotes;
    }
    public String getSeparator()
    {
        return this.m_separator;
    }
    public void setSeparator(String a_separator)
    {
        this.m_separator = a_separator;
    }

    public void close() throws IOException
    {        
        this.m_lineReader.close();
    }

    /**
     * @return null for last line in file otherwise the parts split by separator
     * @throws IOException 
     */
    public String[] readLine() throws IOException
    {
        this.m_lastLine = this.m_lineReader.readLine();
        if ( this.m_lastLine == null )
        {
            // past last line
            return null;
        }
        // do the split
        this.m_lastLine = this.m_lastLine.trim();
        String[] t_parts = this.m_lastLine.split(this.m_separator);
        
        for (int i = 0; i < t_parts.length; i++)
        {
            t_parts[i] = this.cleanup(t_parts[i]);
        }
        return t_parts;
    }

    private String cleanup(String a_string)
    {
        String t_str = a_string.trim();
        if ( this.m_removeQuotes )
        {
        	if ( t_str.startsWith("\"") && t_str.endsWith("\"") )
        	{
        		t_str = t_str.substring(1, t_str.length()-1).trim();
        	}
        }
        return t_str;
    }

    public void setLastLine(String lastLine)
    {
        this.m_lastLine = lastLine;
    }

    public String getLastLine()
    {
        return m_lastLine;
    }
    
}
