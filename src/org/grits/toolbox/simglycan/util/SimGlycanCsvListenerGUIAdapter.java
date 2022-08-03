package org.grits.toolbox.simglycan.util;

import org.apache.log4j.Logger;
import org.grits.toolbox.core.utilShare.processDialog.ProgressDialog;

public class SimGlycanCsvListenerGUIAdapter implements SimGlycanCsvListener
{
    private static final Logger logger = Logger.getLogger(SimGlycanCsvListenerGUIAdapter.class);
    private ProgressDialog m_progressDialog = null;
    private String m_filename = null;
    
    public SimGlycanCsvListenerGUIAdapter(ProgressDialog a_progressBarDialog, String a_csvFile)
    {
        this.m_filename = a_csvFile;
        this.m_progressDialog = a_progressBarDialog;
    }

    public void invalideLine(int a_lineNumber, String a_lastLine)
    {
        logger.warn("Invalid line in SimGlycan CSV " + this.m_filename + " line " + Integer.toString(a_lineNumber) + ": " + a_lastLine);
    }

    public void parseLine(int a_number)
    {
        this.m_progressDialog.updateProgresBar("Line " + Integer.toString(a_number));
    }

    public void lineError(int a_lineNumber, CsvParsingException a_exception)
    {
        logger.warn("Parsing error in SimGlycan CSV " + this.m_filename + " line " + Integer.toString(a_lineNumber), a_exception);
    }

}
