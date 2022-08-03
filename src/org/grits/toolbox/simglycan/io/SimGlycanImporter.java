package org.grits.toolbox.simglycan.io;

import java.util.ArrayList;
import java.util.HashMap;

import org.grits.toolbox.simglycan.data.ColumnLabel;
import org.grits.toolbox.simglycan.data.SimGlycanData;
import org.grits.toolbox.simglycan.data.SimGlycanInfoCSV;
import org.grits.toolbox.simglycan.data.SimGlycanScan;
import org.grits.toolbox.simglycan.util.CsvParsingException;

public abstract class SimGlycanImporter
{
    protected SimGlycanData m_data = null;
    protected HashMap<Integer, SimGlycanScan> m_hashScans = new HashMap<Integer, SimGlycanScan>();
    protected HashMap<ColumnLabel, Integer> m_header = new HashMap<ColumnLabel, Integer>();

    public SimGlycanInfoCSV analyzeHeadline(String[] a_aParts)
    {
        SimGlycanInfoCSV t_result = new SimGlycanInfoCSV();
        int t_counter = 0;
        HashMap<ColumnLabel, Integer> t_map = new HashMap<ColumnLabel, Integer>();
        ArrayList<String> t_unknwonNames = new ArrayList<String>();
        for (String t_string : a_aParts)
        {
            ColumnLabel t_label = this.columnForName(t_string.trim());
            if ( t_label == null )
            {
                t_unknwonNames.add(t_string.trim());
            }
            else
            {
                t_map.put(t_label,t_counter);
            }
            t_counter++;
        }
        t_result.setColumnHeader(t_map);
        t_result.setUnknownColumn(t_unknwonNames);
        return t_result;
    }

    protected abstract ColumnLabel columnForName(String a_trim);

    public abstract boolean parseLine(String[] a_line, String a_completeLine) throws CsvParsingException;

    public void newData()
    {
        this.m_data = new SimGlycanData();
        this.m_hashScans.clear();
    }

    public SimGlycanData finalizeData()
    {
        for (Integer t_scanNo : this.m_hashScans.keySet())
        {
            this.m_data.addScan(this.m_hashScans.get(t_scanNo));
        }
        return this.m_data;
    }

    protected Integer getInteger(boolean a_mandatory, ColumnLabel a_column, String[] a_line, String a_lastLine) throws CsvParsingException
    {
        Integer t_columnNumber = this.m_header.get(a_column);
        String t_part = this.getString(a_line,t_columnNumber);
        if ( t_part != null )
        {
            t_part = t_part.trim();
            if ( t_part.length() == 0 )
            {
                if ( a_mandatory )
                {
                    throw new CsvParsingException(a_lastLine, a_column.getSimglycanName() + " - missing value");
                }
            }
            else
            {
                try
                {
                    return Integer.parseInt(t_part);
                } 
                catch (Exception e)
                {
                    throw new CsvParsingException(a_lastLine, a_column.getSimglycanName() + " - value is not a number: " + t_part);
                }
            }
        }
        else
        {
            if ( a_mandatory )
            {
                throw new CsvParsingException(a_lastLine, a_column.getSimglycanName() + " - value is missing");
            }
        }
        return null;
    }

    protected String getString(String[] a_line, Integer a_columnNumber)
    {
        if ( a_columnNumber == null )
        {
            return null;
        }
        if ( a_line.length <= a_columnNumber )
        {
            return null;
        }
        return a_line[a_columnNumber].trim();
    }

    protected Double getDouble(boolean a_mandatory, ColumnLabel a_column, String[] a_line, String a_lastLine) throws CsvParsingException
    {
        Integer t_columnNumber = this.m_header.get(a_column);
        String t_part = this.getString(a_line,t_columnNumber);
        if ( t_part != null )
        {
            t_part = t_part.trim();
            if ( t_part.length() == 0 )
            {
                if ( a_mandatory )
                {
                    throw new CsvParsingException(a_lastLine, a_column.getSimglycanName() + " - missing value");
                }
            }
            else
            {
                try
                {
                    return Double.parseDouble(t_part);
                } 
                catch (Exception e)
                {
                    throw new CsvParsingException(a_lastLine, a_column.getSimglycanName() + " - value is not a float number: " + t_part);
                }
            }
        }
        else
        {
            if ( a_mandatory )
            {
                throw new CsvParsingException(a_lastLine, a_column.getSimglycanName() + " - value is missing");
            }
        }
        return null;
    }

    protected String getString(boolean a_mandatory, ColumnLabel a_column, String[] a_line, String a_lastLine) throws CsvParsingException
    {
        Integer t_columnNumber = this.m_header.get(a_column);
        String t_part = this.getString(a_line,t_columnNumber);
        if ( t_part == null )
        {
            if ( a_mandatory )
            {
                throw new CsvParsingException(a_lastLine, a_column.getSimglycanName() + " - value is missing");
            }
            return null;
        }
        return t_part.trim();
    }

}
