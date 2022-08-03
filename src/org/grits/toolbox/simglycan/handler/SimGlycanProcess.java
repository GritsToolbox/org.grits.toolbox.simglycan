package org.grits.toolbox.simglycan.handler;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarExporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.io.simglycan.SugarImporterSimGlycan;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;
import org.grits.toolbox.core.utilShare.processDialog.ProgressDialog;
import org.grits.toolbox.core.utilShare.processDialog.ProgressDialogThread;
import org.grits.toolbox.simglycan.data.SimGlycanAnnotation;
import org.grits.toolbox.simglycan.data.SimGlycanData;
import org.grits.toolbox.simglycan.data.SimGlycanInfoCSV;
import org.grits.toolbox.simglycan.data.SimGlycanScan;
import org.grits.toolbox.simglycan.io.SimGlycanCSV;
import org.grits.toolbox.simglycan.util.SimGlycanCsvListenerGUIAdapter;

import edu.uga.ccrc.simiantools.ms.om.data.GlycanAnnotation;

public class SimGlycanProcess extends ProgressDialogThread{

    //log4J Logger
    private static final Logger logger = Logger.getLogger(SimGlycanProcess.class);
    private volatile boolean m_isCanceled = false;
    //    private SimianData m_simGlycanData = null;
    //    private ConversionParameter m_parameter = null;
    //    private String projectFolder = null;
    private String m_csvFile = null;
    //    private String entryId = null;
    //    private String simGlycanFolder = null;
    //    private Integer numberOfResult = null;
    private Integer m_annotationId = 0;
    private HashMap<String, GlycanAnnotation> m_annotationRegistry = new HashMap<String, GlycanAnnotation>(); 
    
    @Override
    public void cancelWork()
    {
        this.m_isCanceled = true;
    }

    
    public boolean threadStart(ProgressDialog a_progressBarDialog) throws Exception
    {
        try
        {
            // 1. load CSV file and analyze headline + get line numbers
            a_progressBarDialog.setMax(1);
            a_progressBarDialog.setProcessMessageLabel("1. Read CSV file");
            SimGlycanInfoCSV t_csvInfo = null;
            SimGlycanCSV t_csvReader = new SimGlycanCSV();
            try
            {
                t_csvInfo = t_csvReader.analyze(this.m_csvFile);
            }
            catch (Exception t_excpetion)
            {
                logger.debug("Unable to get information of SimGlycanCSV file - Invalid file", t_excpetion);
                throw new Exception("Unable to get information of SimGlycanCSV file - Invalid file");
            }
            if(t_csvInfo.getVersion() == null )
            {
                logger.error("Invalid SimGlycan File: " + this.m_csvFile);
                throw new Exception("Invalid SimGlycan File: " + this.m_csvFile);
            }
            // 2. load the data from the CSV
            a_progressBarDialog.setMax(t_csvInfo.getLineNumber());
            a_progressBarDialog.setProcessMessageLabel("2.Load the data from the CSV");
            t_csvReader.openFile(this.m_csvFile,t_csvInfo.getColumnHeader(), new SimGlycanCsvListenerGUIAdapter(a_progressBarDialog,this.m_csvFile),t_csvInfo.getVersion());
            while(t_csvReader.nextLine())
            {
                //check for cancellation
                if(this.m_isCanceled)
                {
                    return false;
                }
            }
            SimGlycanData t_simGlycanData = t_csvReader.closeFile();

            // Fill GRITS OM
            a_progressBarDialog.setMax(t_simGlycanData.getScans().size());
            a_progressBarDialog.setProcessMessageLabel("3.Convert to SimGlycan Data");

            for (SimGlycanScan t_scan : t_simGlycanData.getScans())
            {
                if(this.m_isCanceled)
                {
                    return false;
                }
                
            }
//            SimianConverter t_converter = new SimianConverter(t_gwbUtil, new SimianConverterListenerGUI(a_progressBarDialog),
//                    this.m_parameter, new ImageProviderMemory());			
//            // process all scans
//            for (SimianScan t_scan : this.m_simGlycanData.getScans())
//            {
//                if(isCanceled)
//                {
//                    return false;
//                }
//                t_converter.process(t_scan);
//            }
//
//            //and write to xml file
//            writeToXml();
        }
        catch(Exception e)
        {
            logger.error(e.getMessage(), e);
            throw e;
        }
        return true;
    }


    public GlycanAnnotation toAnnotation(SimGlycanAnnotation a_annoSG)
    {
        GlycanAnnotation t_annotation = this.m_annotationRegistry.get(a_annoSG.getGlycanId());
        if ( t_annotation != null )
        {
            return t_annotation;
        }
        t_annotation = new GlycanAnnotation();
        t_annotation.setId(this.m_annotationId++);
        t_annotation.setSequence(this.toGlycoCT(a_annoSG));
        t_annotation.setSequenceFormat(GlycanAnnotation.SEQ_FORMAT_GLYCOCT_CONDENSED);
        t_annotation.setGlycanId(a_annoSG.getGlycanId());
        t_annotation.setSequenceGWB(this.toGWS(t_annotation.getSequence()));
        this.m_annotationRegistry.put(a_annoSG.getGlycanId(), t_annotation);
        return t_annotation;
    }


    private String toGlycoCT(SimGlycanAnnotation a_annoSG) throws IOException
    {
        if ( a_annoSG.getSequence() == null )
        {
            throw new IOException("Missing sequence for " + a_annoSG.getGlycanId());
        }
        try
        {
            SugarImporterSimGlycan t_importer = new SugarImporterSimGlycan();
            Sugar t_sugar = t_importer.parse(a_annoSG.getSequence());
            SugarExporterGlycoCTCondensed t_exporter = new SugarExporterGlycoCTCondensed();
            t_exporter.start(t_sugar);
            return t_exporter.getHashCode();
        }
        catch (SugarImporterException e)
        {
            
        } 
        catch (GlycoVisitorException e)
        {
            
        }
    }
}
