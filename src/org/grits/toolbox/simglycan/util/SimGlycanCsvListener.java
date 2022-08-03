package org.grits.toolbox.simglycan.util;


public interface SimGlycanCsvListener
{

    public void invalideLine(int a_lineNumber, String a_lastLine);

    public void parseLine(int a_number);

    public void lineError(int a_lineNumber, CsvParsingException a_exception);
    
}
