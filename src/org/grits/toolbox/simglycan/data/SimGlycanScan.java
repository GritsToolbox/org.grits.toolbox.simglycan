package org.grits.toolbox.simglycan.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"m_scanNo", "m_precursor", "m_charge", "m_deconvolutedMass", "m_intensity"})
public class SimGlycanScan
{
	@XmlAttribute(name="no")
    private Integer m_scanNo = null;
	@XmlAttribute(name="precursor")
    private Double m_precursor = null;
	@XmlAttribute(name="charge")
    private Integer m_charge = null;
	@XmlAttribute(name="deconMass")
    private Double m_deconvolutedMass = null;
	@XmlAttribute(name="int")
    private Double m_intensity = null;
	@XmlElement(name="annotation")
    private List<SimGlycanAnnotation> m_annotations = new ArrayList<SimGlycanAnnotation>();

	public List<SimGlycanAnnotation> getAnnotations()
    {
        return m_annotations;
    }
    public void setAnnotations(List<SimGlycanAnnotation> a_annotations)
    {
        m_annotations = a_annotations;
    }
    public Integer getScanNo()
    {
        return m_scanNo;
    }
    public void setScanNo(Integer a_scanNo)
    {
        m_scanNo = a_scanNo;
    }
    public Double getPrecursor()
    {
        return m_precursor;
    }
    public void setPrecursor(Double a_precursor)
    {
        m_precursor = a_precursor;
    }
    public Integer getCharge()
    {
        return m_charge;
    }
    public void setCharge(Integer a_charge)
    {
        m_charge = a_charge;
    }
    public Double getDeconvolutedMass()
    {
        return m_deconvolutedMass;
    }
    public void setDeconvolutedMass(Double a_deconvolutedMass)
    {
        m_deconvolutedMass = a_deconvolutedMass;
    }
    public Double getIntensity()
    {
        return m_intensity;
    }
    public void setIntensity(Double a_intensity)
    {
        m_intensity = a_intensity;
    }
    public void addAnnotation(SimGlycanAnnotation a_annotation)
    {
        this.m_annotations.add(a_annotation);
    }
}
