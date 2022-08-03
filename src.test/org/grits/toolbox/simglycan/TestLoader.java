package org.grits.toolbox.simglycan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.grits.toolbox.simglycan.data.SimGlycanAnnotation;
import org.grits.toolbox.simglycan.data.SimGlycanData;
import org.grits.toolbox.simglycan.data.SimGlycanInfoCSV;
import org.grits.toolbox.simglycan.data.SimGlycanScan;
import org.grits.toolbox.simglycan.handler.SimGlycanProcess;
import org.grits.toolbox.simglycan.io.SimGlycanCSV;
import org.grits.toolbox.simglycan.util.SimGlycanCsvListenerSystemOut;
import org.systemsbiology.jrap.extension.ExtMSXMLParser;
import org.systemsbiology.jrap.extension.ExtScan;

import edu.uga.ccrc.simiantools.ms.om.data.Data;
import edu.uga.ccrc.simiantools.ms.om.data.DataHeader;
import edu.uga.ccrc.simiantools.ms.om.data.GlycanAnnotation;
import edu.uga.ccrc.simiantools.ms.om.data.Method;
import edu.uga.ccrc.simiantools.ms.om.data.Scan;
import edu.uga.ccrc.simiantools.ms.om.data.ScanFeatures;
import edu.uga.ccrc.simiantools.ms.om.io.xml.AnnotationWriter;


public class TestLoader 
{
    public static void main(String[] args) throws IOException, JAXBException 
    {
        SimGlycanProcess t_process = new SimGlycanProcess();
        String t_fileName = "testfile/29.csv";
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
        // 3. write of GRITS OM
        Data t_data = new Data();
        Method t_methode = new Method();
        DataHeader t_header = new DataHeader();
        // 3.1 fill method object


        t_header.setMethod(t_methode);
        t_data.setDataHeader(t_header);
        // 3.2 fill scans, annotations, and features
        HashMap<Integer, Scan> t_scanList = TestLoader.getScanHierachy("./testfile/29.mzXML");
        for (SimGlycanScan t_scanSimGlyan : t_simGlycanData.getScans())
        {
            Scan t_scan = t_scanList.get(t_scanSimGlyan.getScanNo());
            if ( t_scan == null )
            {
                throw new IOException("Unable to find scan " + t_scanSimGlyan.getScanNo().toString() + " in raw data file.");
            }
            else
            {
                Integer t_parentScanID = t_scan.getParentScan();
                if ( t_parentScanID == null || t_scan.getMsLevel() != 2 )
                {
                    throw new IOException("Scan " + t_scanSimGlyan.getScanNo().toString() + " does not have a parent scan in raw data file or is not a MS2 scan.");
                }
                else
                {
                    Scan t_scanParent = t_scanList.get(t_parentScanID);
                    if ( t_scanParent == null )
                    {
                        throw new IOException("Unable to find parent scan of scan " + t_scanSimGlyan.getScanNo().toString() + " in raw data file.");
                    }
                    else
                    {
                        for (SimGlycanAnnotation t_annoSG : t_scanSimGlyan.getAnnotations())
                        {
                            GlycanAnnotation t_annotation = t_process.toAnnotation(t_annoSG);
                            
                            
                        }
                    }
                }
            }
        }
        // writing the file
        AnnotationWriter t_writer = new AnnotationWriter();
        ZipOutputStream compressedFile = t_writer.getZipOutputStream("d:\\1.zip");
        t_writer.writeDataHeaderToZip(t_data.getDataHeader(), compressedFile);
        t_writer.writeMethodToZip(t_data.getDataHeader().getMethod(), compressedFile);
        t_writer.writeDataToZip(t_data, compressedFile);
        TestLoader.writeScanFeaturesToZip(t_data, compressedFile);
        compressedFile.close();
    }

    public static void writeScanFeaturesToZip(Data data, ZipOutputStream compressedFile) throws JAXBException, IOException
    {
        JAXBContext jaxbContextAnn = JAXBContext.newInstance(ScanFeatures.class);
        Marshaller jaxbMarshallerAnn = jaxbContextAnn.createMarshaller();
        if( data.getScans() != null )
        {
            for(Integer scanId : data.getScans().keySet())
            {
                compressedFile.putNextEntry(new ZipEntry(scanId+".xml"));
                jaxbMarshallerAnn.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                jaxbMarshallerAnn.marshal(data.getScanFeatures().get(scanId),compressedFile);
                compressedFile.closeEntry();
            }
        }
    }

    public static HashMap<Integer, Scan> getScanHierachy(String a_mzXmlFile) throws IOException
    {
        HashMap<Integer, Scan> t_scans= new HashMap<Integer,Scan>();
        HashMap<Integer, List<Integer>> t_childScans = new HashMap<Integer, List<Integer>>();
        // open MZXML file 
        ExtMSXMLParser t_parser=new ExtMSXMLParser(a_mzXmlFile);
        // get scanID and ScanList
        int count = t_parser.getScanCount();
        for (int i = 1; i <= count; i++)
        {
            ExtScan t_jrapScan = t_parser.rap(i);
            Scan t_scan = new Scan();
            t_scan.setScanNo(t_jrapScan.getNum());
            t_scan.setRetentionTime(t_jrapScan.getDoubleRetentionTime());
            // parent scan
            int t_scanNo = t_jrapScan.getPrecursorScanNum();
            if ( t_scanNo != -1 )
            {
                t_scan.setParentScan(t_scanNo);
                List<Integer> t_childList = t_childScans.get(t_scanNo);
                if ( t_childList == null )
                {
                    t_childList = new ArrayList<Integer>();
                    t_childScans.put(t_scanNo, t_childList);
                }
                t_childList.add(t_scan.getScanNo());
            }
            t_scan.setScanStart((double) t_jrapScan.getLowMz());
            t_scan.setScanEnd((double) t_jrapScan.getHighMz());
            t_scan.setMsLevel(t_jrapScan.getMsLevel());
            // polarity
            String t_attribute = t_jrapScan.getPolarity();
            if ( t_attribute != null && t_attribute.equals("+")) 
            {
                t_scan.setPolarity(true);
            } 
            else 
            {
                t_scan.setPolarity(false);
            }
            t_scan.setActivationMethode(t_jrapScan.getActivationMethod());
            t_scans.put(t_scan.getScanNo(), t_scan);
        }
        // fix the subscans
        for (Integer t_scanNo : t_childScans.keySet())
        {
            Scan t_scan = t_scans.get(t_scanNo);
            if ( t_scan == null )
            {
                throw new IOException("Missing scan " + t_scanNo.toString() + " that is parent scan of another scan.");
            }
            t_scan.setSubScans(t_childScans.get(t_scanNo));
        }
        return t_scans;
    }
}
