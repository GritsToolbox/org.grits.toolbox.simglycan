package org.grits.toolbox.simglycan.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.grits.toolbox.simglycan.data.ColumnLabel;
import org.grits.toolbox.simglycan.data.SimGlycanData;
import org.grits.toolbox.simglycan.data.SimGlycanInfoCSV;
import org.grits.toolbox.simglycan.util.CsvParsingException;
import org.grits.toolbox.simglycan.util.SimGlycanCsvListener;

public class SimGlycanCSV
{
    private String m_separator = "\t";
    private CSVFileReader m_reader = null;
    private SimGlycanCsvListener m_listener = null;
    private int m_lineNumber = 0;
    private SimGlycanImporter m_importer = null;

    public SimGlycanCSV()
    {
        super();
    }

    public SimGlycanCSV(String a_separator)
    {
        super();
        this.m_separator = a_separator;
    }

    public SimGlycanInfoCSV analyze(String a_fileName) throws IOException
    {
        SimGlycanInfoCSV t_result = new SimGlycanInfoCSV();
        CSVFileReader t_reader = new CSVFileReader(new File(a_fileName));
        t_reader.setRemoveQuotes(true);
        t_reader.setSeparator(this.m_separator);
        // parse the input file line by line
        String[] line;
        int t_lineNumber = 0;
        boolean t_foundHeader = false;
        while ((line = t_reader.readLine()) != null) 
        {
            t_lineNumber++;
            // find file name
            if ( line.length > 3 )
            {
                if ( line[0].trim().equals("Scan No") )
                {
                    if ( t_foundHeader == true )
                    {
                        // more than one header
                        throw new IOException("File has several headers lines.");
                    }
                    else
                    {
                        SimGlycanImporter40 t_reader40 = new SimGlycanImporter40();
                        t_result = t_reader40.analyzeHeadline(line);
                        t_result.setVersion("4.0");
                        t_foundHeader = true;
                    }
                }
                else if ( line[0].trim().equals("File Name@Scan No") )
                {
                    if ( t_foundHeader == true )
                    {
                        // more than one header
                        throw new IOException("File has several headers lines.");
                    }
                    else
                    {
                        SimGlycanImporter45 t_reader45 = new SimGlycanImporter45();
                        t_result = t_reader45.analyzeHeadline(line);
                        t_result.setVersion("4.5");
                        t_foundHeader = true;
                    }
                }
            }
        }
        t_result.setLineNumber(t_lineNumber);
        return t_result;
    }

    public void openFile(String a_fileName, HashMap<ColumnLabel, Integer> a_header, SimGlycanCsvListener a_listener, String a_version) throws IOException
    {
        this.m_reader = new CSVFileReader(new File(a_fileName));
        this.m_reader.setRemoveQuotes(true);
        this.m_reader.setSeparator(this.m_separator);
        this.m_listener = a_listener;
        this.m_lineNumber = 0;
        if ( a_version.equals("4.0") )
        {
            this.m_importer = new SimGlycanImporter40(a_header);
        }
        else if ( a_version.equals("4.5") )
        {
            this.m_importer = new SimGlycanImporter45(a_header);
        }
        else
        {
            throw new IOException("SimGlycan CSV version (" + a_version + ") is not supported.");
        }
        this.m_importer.newData();
    }

    public boolean nextLine() throws IOException
    {
        // parse the input file line by line
        String[] line = this.m_reader.readLine();
        if ( line == null )
        {
            return false;
        }
        this.m_lineNumber++;
        this.m_listener.parseLine(this.m_lineNumber);
        try
        {
            if ( !this.m_importer.parseLine(line, this.m_reader.getLastLine()) )
            {
                this.m_listener.invalideLine(this.m_lineNumber, this.m_reader.getLastLine());
            }
        }
        catch (CsvParsingException e)
        {
            this.m_listener.lineError(this.m_lineNumber,e);
        }
        return true;
    }

    public SimGlycanData closeFile() throws IOException
    {
        this.m_reader.close();
        return this.m_importer.finalizeData();
    }

}
