package org.grits.toolbox.simglycan.data;

public enum ColumnLabel
{
    scan_no("Scan No"),
    precursor( "Precursor ion m/z"),
    charge( "Charge"),
    deconvoluted_mass( "Deconvoluted Mass (Mass + Na)"),
    intensity( "Intensity"),
    glycan_rank( "Glycan Rank"),
    composition_score( "Composition Score"),
    branching_score( "Branching Score"),
    glycan_id( "Glycan ID"),
    composition( "Composition"),
    sequence( "Sequence"),
    glycan_mass( "Glycan Mass"),
    peptide_mass( "Peptide Mass"),
    carbohydrate_mass( "Carbohydrate Mass"),
    name( "Name"),
    classes( "Class"),
    reaction( "Reaction"),
    pathway( "Pathway"),
    enzyme( "Enzyme"),
    other_db( "OtherDB"),
    single_glycosidic_match( "Single Glycosidic % Match:"),
    single_crossring_match( "Single CrossRing % Match:"),
    glycosidic_glycosidic_match( "Glycosidic-Glycosidic % Match:"),
    crossring_glycosidic_match( "Single CrossRing-Glycosidic % Match:"),
    ms_level ( "MS Level"),
    retention_time ( "Retention Time"),
    sequence_peptide ( "Peptide Sequence"),
    glycosylation_position ( "Glycosylation Position"),
    proximity_score ("Proximity Score"),
    theoretical_mz ("Theoretical Precursor m/z"),
    peptide_match( "Peptide Match");
        
    private String m_strSimglycanName;
    
    private ColumnLabel( String simglycan )
    {
        this.m_strSimglycanName = simglycan;
    }

    public String getSimglycanName() 
    {  
        return this.m_strSimglycanName;  
    }

    public static ColumnLabel forSimGlycanName(String a_string)
    {
        for ( ColumnLabel a : ColumnLabel.values() )
        {
            if ( a_string.equals(a.m_strSimglycanName) )
            {
                return a;
            }
        }
        return null;
    }
}