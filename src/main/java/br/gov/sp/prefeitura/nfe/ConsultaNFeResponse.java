
package br.gov.sp.prefeitura.nfe;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RetornoXML" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "retornoXML"
})
@XmlRootElement(name = "ConsultaNFeResponse")
public class ConsultaNFeResponse {

    @XmlElement(name = "RetornoXML")
    protected String retornoXML;

    /**
     * Obtém o valor da propriedade retornoXML.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRetornoXML() {
        return retornoXML;
    }

    /**
     * Define o valor da propriedade retornoXML.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRetornoXML(String value) {
        this.retornoXML = value;
    }

}
