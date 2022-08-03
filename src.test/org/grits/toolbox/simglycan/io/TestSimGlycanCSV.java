package org.grits.toolbox.simglycan.io;

import java.io.File;
import java.io.IOException;

import org.grits.toolbox.simglycan.data.SimGlycanData;
import org.grits.toolbox.simglycan.data.SimGlycanInfoCSV;
import org.grits.toolbox.simglycan.util.SimGlycanCsvListenerSystemOut;

public class TestSimGlycanCSV
{

    public static void main(String[] args) throws IOException
    {
        String t_fileName = "testfiles/Mindy/jurkat-unstim.csv";
        // 1. load CSV file and analyse headline + get line numbers
        SimGlycanCSV t_importer = new SimGlycanCSV();
        SimGlycanInfoCSV t_info = t_importer.analyze(t_fileName);
        // number of lines in the file
        t_info.getLineNumber();
        // columns in the file HashMap<Integer,ColumnLabel>
        t_info.getColumnHeader();
        // columns that could not be identified
        t_info.getUnknownColumn();

        // 2. load the data from the CSV
        t_importer.openFile(t_fileName,t_info.getColumnHeader(), new SimGlycanCsvListenerSystemOut(),t_info.getVersion());
        while(t_importer.nextLine())
        {}
        SimGlycanData t_simGlycanData = t_importer.closeFile();

        // Test: Write the CSV file again to see if it worked
        t_importer.writeData(new File(t_fileName + ".out"),t_info.getColumnHeader(),t_simGlycanData, t_info.getVersion());
    }

}
