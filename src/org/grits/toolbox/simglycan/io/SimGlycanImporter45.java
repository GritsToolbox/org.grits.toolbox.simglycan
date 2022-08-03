package org.grits.toolbox.simglycan.io;

import java.util.HashMap;

import org.grits.toolbox.simglycan.data.ColumnLabel;
import org.grits.toolbox.simglycan.data.SimGlycanAnnotation;
import org.grits.toolbox.simglycan.data.SimGlycanScan;
import org.grits.toolbox.simglycan.util.CsvParsingException;

public class SimGlycanImporter45 extends SimGlycanImporter
{
    public SimGlycanImporter45()
    {
        super();
    }

    public SimGlycanImporter45(HashMap<ColumnLabel, Integer> a_header)
    {
        this.m_header = a_header;
    }

    protected ColumnLabel columnForName(String a_trim)
    {
        if ( a_trim.equals("File Name@Scan No") )
        {
            return ColumnLabel.scan_no;
        }
        if ( a_trim.equals("MS Level") )
        {
            return ColumnLabel.ms_level;
        }
        if ( a_trim.equals("Retention Time") )
        {
            return ColumnLabel.retention_time;
        }
        if ( a_trim.equals("Precursor m/z") )
        {
            return ColumnLabel.precursor;
        }
        if ( a_trim.equals("Charge") )
        {
            return ColumnLabel.charge;
        }
        if ( a_trim.equals("Precursor Intensity") )
        {
            return ColumnLabel.intensity;
        }
        if ( a_trim.equals("Glycan ID") )
        {
            return ColumnLabel.glycan_id;
        }
        if ( a_trim.equals("Composition") )
        {
            return ColumnLabel.composition;
        }
        if ( a_trim.equals("Glycan Mass") )
        {
            return ColumnLabel.glycan_mass;
        }
        if ( a_trim.equals("Carbohydrate Mass") )
        {
            return ColumnLabel.carbohydrate_mass;
        }
        if ( a_trim.equals("Peptide Mass") )
        {
            return ColumnLabel.peptide_mass;
        }
        if ( a_trim.equals("Theoretical Precursor m/z") )
        {
            return ColumnLabel.theoretical_mz;
        }
        if ( a_trim.equals("Glycan Sequence") )
        {
            return ColumnLabel.sequence;
        }
        if ( a_trim.equals("Peptide Sequence") )
        {
            return ColumnLabel.sequence_peptide;
        }
        if ( a_trim.equals("Glycosylation Site") )
        {
            return ColumnLabel.glycosylation_position;
        }
        if ( a_trim.equals("Single Glycosidic") )
        {
            return ColumnLabel.single_glycosidic_match;
        }
        if ( a_trim.equals("Single Cross ring") )
        {
            return ColumnLabel.single_crossring_match;
        }
        if ( a_trim.equals("Glycosidic/Glycosidic") )
        {
            return ColumnLabel.glycosidic_glycosidic_match;
        }
        if ( a_trim.equals("Cross ring/Glycosidic") )
        {
            return ColumnLabel.crossring_glycosidic_match;
        }
        if ( a_trim.equals("Peptide Match") )
        {
            return ColumnLabel.peptide_match;
        }
        if ( a_trim.equals("Glycan Rank") )
        {
            return ColumnLabel.glycan_rank;
        }
        if ( a_trim.equals("Proximity Score") )
        {
            return ColumnLabel.proximity_score;
        }
        if ( a_trim.equals("Name") )
        {
            return ColumnLabel.name;
        }
        if ( a_trim.equals("Class") )
        {
            return ColumnLabel.classes;
        }
        if ( a_trim.equals("Reaction") )
        {
            return ColumnLabel.reaction;
        }
        if ( a_trim.equals("Pathway") )
        {
            return ColumnLabel.pathway;
        }
        if ( a_trim.equals("Enzyme") )
        {
            return ColumnLabel.enzyme;
        }
        if ( a_trim.equals("OtherDB") )
        {
            return ColumnLabel.other_db;
        }
        return null;
    }

    public boolean parseLine(String[] a_line, String a_completeLine) throws CsvParsingException
    {
        if ( a_line.length == 1 )
        {
            // empty line
        }
        else if ( a_line.length > 3 && a_line[0].trim().equals("File Name@Scan No") )
        {
            // headline
        }
        else if ( a_completeLine.contains("Percentage Match") || a_completeLine.contains("Intensity (Isotopic cluster)") )
        {
            // line above the headline
        }
        else if ( a_line.length > 3 )
        {
            this.parseDataLine(a_line, a_completeLine);
        }
        else
        {
            return false;
        }
        return true;
    }

    private void parseDataLine(String[] a_line, String a_completeLine) throws CsvParsingException
    {
        Integer t_columnNumber = this.m_header.get(ColumnLabel.scan_no);
        String t_part = this.getString(a_line,t_columnNumber);
        if ( t_part == null )
        {
            throw new CsvParsingException(a_completeLine,"Unable to find the scan number");
        }
        int t_index = t_part.indexOf("@");
        if ( t_index != -1 )
        {
            String t_filename = t_part.substring(0, t_index).trim();
            if ( t_filename.length() == 0 )
            {
                throw new CsvParsingException(a_completeLine,"Unable to find the file name");
            }
            if ( this.m_data.getFileName() != null )
            {
                if ( !this.m_data.getFileName().equals(t_filename) )
                {
                    throw new CsvParsingException(a_completeLine,"Unable to load data that origins from several different files (" + t_filename + ", " + this.m_data.getFileName() + ")");
                }
            }
            else
            {
                this.m_data.setFileName(t_filename);
            }
            if ( t_part.indexOf("@", t_index+1) == - 1 )
            {
                throw new CsvParsingException(a_completeLine,"Unable to find the scan number");
            }
            t_part = t_part.substring(t_index+1, t_part.indexOf("@", t_index+1));
            t_part = t_part.replace("Scan", "").trim();
        }
        else
        {
            throw new CsvParsingException(a_completeLine,"Unable to find the scan number and file name");
        }
        Integer t_scanNo = null;
        try
        {
            t_scanNo = Integer.parseInt(t_part);
        } 
        catch (Exception t_tException)
        {
            throw new CsvParsingException(a_completeLine,"Not a scan number: " + t_part);
        }
        SimGlycanScan t_scan = this.m_hashScans.get(t_scanNo);
        if ( t_scan  == null )
        {
            t_scan = new SimGlycanScan();
            t_scan.setScanNo(t_scanNo);
            // fill the scan information
            t_scan.setPrecursor(this.getDouble(false,ColumnLabel.precursor,a_line,a_completeLine));
            t_scan.setCharge(this.getInteger(true,ColumnLabel.charge,a_line,a_completeLine));
            t_scan.setIntensity(this.getDouble(false,ColumnLabel.intensity,a_line,a_completeLine));
            this.m_hashScans.put(t_scanNo, t_scan);
        }
        // now add the annotation data if available
        t_part = this.getString(true,ColumnLabel.glycan_id,a_line,a_completeLine);
        if ( !t_part.equals("No Results Found") )
        {
            SimGlycanAnnotation t_annotation = new SimGlycanAnnotation();
            t_annotation.setGlycanId(t_part);

            t_annotation.setGlycanRank(this.getInteger(false, ColumnLabel.glycan_rank, a_line, a_completeLine));
            t_annotation.setProximityScore(this.getDouble(false, ColumnLabel.proximity_score, a_line, a_completeLine));
            t_annotation.setBranchingScore(this.getInteger(false, ColumnLabel.branching_score, a_line, a_completeLine));
            t_annotation.setComposition(this.getString(false, ColumnLabel.composition, a_line, a_completeLine));
            t_annotation.setSequence(this.getString(false, ColumnLabel.sequence, a_line, a_completeLine));
            t_annotation.setGlycanMass(this.getString(false, ColumnLabel.glycan_mass, a_line, a_completeLine));
            t_annotation.setPeptideMass(this.getDouble(false, ColumnLabel.peptide_mass, a_line, a_completeLine));
            t_annotation.setCarbohydrateMass(this.getDouble(false, ColumnLabel.carbohydrate_mass, a_line, a_completeLine));
            t_annotation.setName(this.getString(false, ColumnLabel.name, a_line, a_completeLine));
            t_annotation.setClasses(this.getString(false, ColumnLabel.classes, a_line, a_completeLine));
            t_annotation.setReaction(this.getString(false, ColumnLabel.reaction, a_line, a_completeLine));
            t_annotation.setPathway(this.getString(false, ColumnLabel.pathway, a_line, a_completeLine));
            t_annotation.setEnzyme(this.getString(false, ColumnLabel.enzyme, a_line, a_completeLine));
            t_annotation.setOtherDb(this.getString(false, ColumnLabel.other_db, a_line, a_completeLine));
            t_annotation.setSingleGlycosidicMatch(this.getDoubleNA(false, ColumnLabel.single_glycosidic_match, a_line, a_completeLine));
            t_annotation.setSingleCrossringMatch(this.getDoubleNA(false, ColumnLabel.single_crossring_match, a_line, a_completeLine));
            t_annotation.setGlycosidicGlycosidicMatch(this.getDoubleNA(false, ColumnLabel.glycosidic_glycosidic_match, a_line, a_completeLine));
            t_annotation.setCrossringGlycosidicMatch(this.getDoubleNA(false, ColumnLabel.crossring_glycosidic_match, a_line, a_completeLine));
            t_annotation.setPeptideMatch(this.getDouble(false, ColumnLabel.peptide_match, a_line, a_completeLine));

            t_scan.addAnnotation(t_annotation);
        }
    }

    protected Double getDoubleNA(boolean a_mandatory, ColumnLabel a_column, String[] a_line, String a_lastLine) throws CsvParsingException
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
                if ( t_part.equals("N/A") )
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

}
