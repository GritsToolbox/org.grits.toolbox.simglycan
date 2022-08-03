package org.grits.toolbox.simglycan.io;

import java.util.HashMap;

import org.grits.toolbox.simglycan.data.ColumnLabel;
import org.grits.toolbox.simglycan.data.SimGlycanAnnotation;
import org.grits.toolbox.simglycan.data.SimGlycanScan;
import org.grits.toolbox.simglycan.util.CsvParsingException;

public class SimGlycanImporter40 extends SimGlycanImporter
{

    public SimGlycanImporter40()
    {
        super();
    }

    public SimGlycanImporter40(HashMap<ColumnLabel, Integer> a_header)
    {
        super();
        this.m_header = a_header;
    }

    protected ColumnLabel columnForName(String a_trim)
    {
        if ( a_trim.equals("Scan No") )
        {
            return ColumnLabel.scan_no;
        }
        if ( a_trim.equals("Precursor ion m/z") )
        {
            return ColumnLabel.precursor;
        }
        if ( a_trim.equals("Charge") )
        {
            return ColumnLabel.charge;
        }
        if ( a_trim.equals("Deconvoluted Mass (Mass + Na)") )
        {
            return ColumnLabel.deconvoluted_mass;
        }
        if ( a_trim.equals("Intensity") )
        {
            return ColumnLabel.intensity;
        }
        if ( a_trim.equals("Glycan Rank") )
        {
            return ColumnLabel.glycan_rank;
        }
        if ( a_trim.equals("Composition Score") )
        {
            return ColumnLabel.composition_score;
        }
        if ( a_trim.equals("Branching Score") )
        {
            return ColumnLabel.branching_score;
        }
        if ( a_trim.equals("Glycan ID") )
        {
            return ColumnLabel.glycan_id;
        }
        if ( a_trim.equals("Composition") )
        {
            return ColumnLabel.composition;
        }
        if ( a_trim.equals("Sequence") )
        {
            return ColumnLabel.sequence;
        }
        if ( a_trim.equals("Glycan Mass") )
        {
            return ColumnLabel.glycan_mass;
        }
        if ( a_trim.equals("Peptide Mass") )
        {
            return ColumnLabel.peptide_mass;
        }
        if ( a_trim.equals("Carbohydrate Mass") )
        {
            return ColumnLabel.carbohydrate_mass;
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
        if ( a_trim.equals("Single Glycosidic % Match:") )
        {
            return ColumnLabel.single_glycosidic_match;
        }
        if ( a_trim.equals("Single CrossRing % Match:") )
        {
            return ColumnLabel.single_crossring_match;
        }
        if ( a_trim.equals("Glycosidic-Glycosidic % Match:") )
        {
            return ColumnLabel.glycosidic_glycosidic_match;
        }
        if ( a_trim.equals("Single CrossRing-Glycosidic % Match:") )
        {
            return ColumnLabel.crossring_glycosidic_match;
        }
        if ( a_trim.equals("Peptide Match") )
        {
            return ColumnLabel.peptide_match;
        }
        return null;
    }

    public boolean parseLine(String[] a_line, String a_completeLine) throws CsvParsingException
    {
        // find file name
        if ( a_line.length > 2 && a_line[0].trim().startsWith("Profile Name") )
        {
            this.m_data.setFileName(a_line[2].trim());
        }
        else if ( a_line.length == 1 )
        {
            // empty line
        }
        else if ( a_line.length > 3 && a_line[0].trim().equals("Scan No") )
        {
            // headline
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

        if ( t_part.indexOf("@") != -1 )
        {
            t_part = t_part.substring(0, t_part.indexOf("@"));
            t_part = t_part.replace("Scan", "").trim();
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
            t_scan.setDeconvolutedMass(this.getDouble(true,ColumnLabel.deconvoluted_mass,a_line,a_completeLine));
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
            t_annotation.setCompositionScore(this.getInteger(false, ColumnLabel.composition_score, a_line, a_completeLine));
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
            t_annotation.setSingleGlycosidicMatch(this.getDouble(false, ColumnLabel.single_glycosidic_match, a_line, a_completeLine));
            t_annotation.setSingleCrossringMatch(this.getDouble(false, ColumnLabel.single_crossring_match, a_line, a_completeLine));
            t_annotation.setGlycosidicGlycosidicMatch(this.getDouble(false, ColumnLabel.glycosidic_glycosidic_match, a_line, a_completeLine));
            t_annotation.setCrossringGlycosidicMatch(this.getDouble(false, ColumnLabel.crossring_glycosidic_match, a_line, a_completeLine));
            t_annotation.setPeptideMatch(this.getDouble(false, ColumnLabel.peptide_match, a_line, a_completeLine));

            t_scan.addAnnotation(t_annotation);
        }
    }

}