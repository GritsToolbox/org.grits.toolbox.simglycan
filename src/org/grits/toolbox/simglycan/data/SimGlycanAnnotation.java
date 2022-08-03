package org.grits.toolbox.simglycan.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SimGlycanAnnotation
{
	@XmlAttribute(name="glycanRank")
    private Integer m_glycanRank = null;
	@XmlAttribute(name="compositionScore")
    private Integer m_compositionScore = null;
	@XmlAttribute(name="branchingScore")
    private Integer m_branchingScore = null;
	@XmlAttribute(name="glycanId")
    private String m_glycanId = null;
	@XmlAttribute(name="composition")
    private String m_composition = null;
	@XmlElement(name="sequence")
    private String m_sequence = null;
	@XmlAttribute(name="glycanMass")
    private String m_glycanMass = null;
	@XmlAttribute(name="peptideMass")
    private Double m_peptideMass = null;
	@XmlAttribute(name="carbohydrateMass")
    private Double m_carbohydrateMass = null;
	@XmlAttribute(name="name")
    private String m_name = null;
	@XmlAttribute(name="classes")
    private String m_classes = null;
	@XmlAttribute(name="reaction")
    private String m_reaction = null;
	@XmlAttribute(name="pathway")
    private String m_pathway = null;
	@XmlAttribute(name="enzyme")
    private String m_enzyme = null;
	@XmlAttribute(name="otherDb")
    private String m_otherDb = null;
	@XmlAttribute(name="singleGlycosidicMatch")
    private Double m_singleGlycosidicMatch = null;
	@XmlAttribute(name="singleCrossringMatch")
    private Double m_singleCrossringMatch = null;
	@XmlAttribute(name="glycosidicGlycosidicMatch")
    private Double m_glycosidicGlycosidicMatch = null;
	@XmlAttribute(name="crossringGlycosidicMatch")
    private Double m_crossringGlycosidicMatch = null;
	@XmlAttribute(name="peptideMatch")
    private Double m_peptideMatch = null;
	@XmlAttribute(name="proximityScore")
    private Double m_proximityScore = null;
	
    public Double getProximityScore()
    {
        return m_proximityScore;
    }
    public void setProximityScore(Double a_proximityScore)
    {
        m_proximityScore = a_proximityScore;
    }
    public Integer getGlycanRank()
    {
        return m_glycanRank;
    }
    public void setGlycanRank(Integer a_glycanRank)
    {
        m_glycanRank = a_glycanRank;
    }
    public Integer getCompositionScore()
    {
        return m_compositionScore;
    }
    public void setCompositionScore(Integer a_compositionScore)
    {
        m_compositionScore = a_compositionScore;
    }
    public Integer getBranchingScore()
    {
        return m_branchingScore;
    }
    public void setBranchingScore(Integer a_branchingScore)
    {
        m_branchingScore = a_branchingScore;
    }
    public String getGlycanId()
    {
        return m_glycanId;
    }
    public void setGlycanId(String a_glycanId)
    {
        m_glycanId = a_glycanId;
    }
    public String getComposition()
    {
        return m_composition;
    }
    public void setComposition(String a_composition)
    {
        m_composition = a_composition;
    }
    public String getSequence()
    {
        return m_sequence;
    }
    public void setSequence(String a_sequence)
    {
        m_sequence = a_sequence;
    }
    public String getGlycanMass()
    {
        return m_glycanMass;
    }
    public void setGlycanMass(String a_glycanMass)
    {
        m_glycanMass = a_glycanMass;
    }
    public Double getPeptideMass()
    {
        return m_peptideMass;
    }
    public void setPeptideMass(Double a_peptideMass)
    {
        m_peptideMass = a_peptideMass;
    }
    public Double getCarbohydrateMass()
    {
        return m_carbohydrateMass;
    }
    public void setCarbohydrateMass(Double a_carbohydrateMass)
    {
        m_carbohydrateMass = a_carbohydrateMass;
    }
    public String getName()
    {
        return m_name;
    }
    public void setName(String a_name)
    {
        m_name = a_name;
    }
    public String getClasses()
    {
        return m_classes;
    }
    public void setClasses(String a_classes)
    {
        m_classes = a_classes;
    }
    public String getReaction()
    {
        return m_reaction;
    }
    public void setReaction(String a_reaction)
    {
        m_reaction = a_reaction;
    }
    public String getPathway()
    {
        return m_pathway;
    }
    public void setPathway(String a_pathway)
    {
        m_pathway = a_pathway;
    }
    public String getEnzyme()
    {
        return m_enzyme;
    }
    public void setEnzyme(String a_enzyme)
    {
        m_enzyme = a_enzyme;
    }
    public String getOtherDb()
    {
        return m_otherDb;
    }
    public void setOtherDb(String a_otherDb)
    {
        m_otherDb = a_otherDb;
    }
    public Double getSingleGlycosidicMatch()
    {
        return m_singleGlycosidicMatch;
    }
    public void setSingleGlycosidicMatch(Double a_singleGlycosidicMatch)
    {
        m_singleGlycosidicMatch = a_singleGlycosidicMatch;
    }
    public Double getSingleCrossringMatch()
    {
        return m_singleCrossringMatch;
    }
    public void setSingleCrossringMatch(Double a_singleCrossringMatch)
    {
        m_singleCrossringMatch = a_singleCrossringMatch;
    }
    public Double getGlycosidicGlycosidicMatch()
    {
        return m_glycosidicGlycosidicMatch;
    }
    public void setGlycosidicGlycosidicMatch(Double a_glycosidicGlycosidicMatch)
    {
        m_glycosidicGlycosidicMatch = a_glycosidicGlycosidicMatch;
    }
    public Double getCrossringGlycosidicMatch()
    {
        return m_crossringGlycosidicMatch;
    }
    public void setCrossringGlycosidicMatch(Double a_crossringGlycosidicMatch)
    {
        m_crossringGlycosidicMatch = a_crossringGlycosidicMatch;
    }
    public Double getPeptideMatch()
    {
        return m_peptideMatch;
    }
    public void setPeptideMatch(Double a_peptideMatch)
    {
        m_peptideMatch = a_peptideMatch;
    }
}
