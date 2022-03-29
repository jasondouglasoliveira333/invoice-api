
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
 *         &lt;element name="VersaoSchema" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="MensagemXML" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "versaoSchema",
    "mensagemXML"
})
@XmlRootElement(name = "ConsultaInformacoesLoteRequest")
public class ConsultaInformacoesLoteRequest {

    @XmlElement(name = "VersaoSchema")
    protected int versaoSchema;
    @XmlElement(name = "MensagemXML")
    protected String mensagemXML;

    /**
     * Obtém o valor da propriedade versaoSchema.
     * 
     */
    public int getVersaoSchema() {
        return versaoSchema;
    }

    /**
     * Define o valor da propriedade versaoSchema.
     * 
     */
    public void setVersaoSchema(int value) {
        this.versaoSchema = value;
    }

    /**
     * Obtém o valor da propriedade mensagemXML.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensagemXML() {
        return mensagemXML;
    }

    /**
     * Define o valor da propriedade mensagemXML.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensagemXML(String value) {
        this.mensagemXML = value;
    }

}
