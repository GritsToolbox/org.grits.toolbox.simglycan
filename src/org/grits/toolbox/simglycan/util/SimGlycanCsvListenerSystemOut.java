package org.grits.toolbox.simglycan.util;


public class SimGlycanCsvListenerSystemOut implements SimGlycanCsvListener
{

    public void invalideLine(int a_lineNumber,String a_lastLine)
    {
        System.out.println(Integer.toString(a_lineNumber) + " - Invalide line format - " + a_lastLine);
    }

    public void parseLine(int a_number)
    {
        
    }

    public void lineError(int a_lineNumber, CsvParsingException a_exception)
    {
        System.out.println(Integer.toString(a_lineNumber) + " - " + a_exception.getMessage() + " - " + a_exception.getLastLine());
    }

}
