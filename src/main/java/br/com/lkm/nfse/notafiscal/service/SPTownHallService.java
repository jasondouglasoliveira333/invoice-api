package br.com.lkm.nfse.notafiscal.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import br.com.lkm.nfse.notafiscal.model.Certificate;
import br.com.lkm.nfse.notafiscal.repository.CertificateRepository;
import br.com.lkm.nfse.notafiscal.util.CertUtil;
import br.com.lkm.nfse.notafiscal.view.FeeKeyPair;
import br.gov.sp.prefeitura.nfe.ConsultaNFeRecebidasRequest;
import br.gov.sp.prefeitura.nfe.ConsultaNFeRecebidasResponse;
import br.gov.sp.prefeitura.nfe.LoteNFe;
import br.gov.sp.prefeitura.nfe.LoteNFeSoap;

@Service
public class SPTownHallService {
	
	private static Logger log = LoggerFactory.getLogger(SPTownHallService.class);
	
	private static String NFS_XML_TEMAPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PedidoConsultaNFePeriodo xmlns=\"http://www.prefeitura.sp.gov.br/nfe\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><Cabecalho xmlns=\"\" Versao=\"1\"><CPFCNPJRemetente><CNPJ>{{cnpj}}</CNPJ></CPFCNPJRemetente><CPFCNPJ><CNPJ>{{cnpj}}</CNPJ></CPFCNPJ><dtInicio>{{startDate}}</dtInicio><dtFim>{{endDate}}</dtFim><NumeroPagina>1</NumeroPagina></Cabecalho></PedidoConsultaNFePeriodo>";
	
	@Autowired
	private CertificateRepository certificateRepository;
	
	public static void main(String... args) {
		try {
			byte[] data = Files.readAllBytes(Paths.get("C:\\jason\\work\\atividades\\crawler_notarj\\nfs_sao_paulo_download\\nfs_sp_1633357352759.xml"));
			String xml = new String(data);
			SPTownHallService me = new SPTownHallService();
//			boolean valid = me.validate(xml);
//			System.out.println("valid:" + valid);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String findInvoices(String rootCnpj, String cnpj, LocalDate startDate, LocalDate endDate, String provinceCode, String townhallUrl) throws Exception {
		String nfsXml = NFS_XML_TEMAPLATE.replace("{{cnpj}}", cnpj).replace("{{startDate}}", startDate.toString()).
				replace("{{endDate}}", endDate.toString());
		
		log.info("XML Enviado:" + nfsXml);
		
		Certificate certificate = certificateRepository.findFirstByCnpj(rootCnpj);
		String nfsXmlSigned = sign(nfsXml, certificate);
//		log.info("nfsXmlSigned:" + nfsXmlSigned);
		
		CertUtil.configure(certificate);
		
		String searchResults = callConsultaNFS(nfsXmlSigned);
		
		return searchResults;
	}

	private static String callConsultaNFS(String xml) throws Exception {
		LoteNFe nfsWS = new LoteNFe();
		LoteNFeSoap nfPort = nfsWS.getLoteNFeSoap();
		ConsultaNFeRecebidasRequest consultaNFeRecebidasRequest = new ConsultaNFeRecebidasRequest();
		consultaNFeRecebidasRequest.setVersaoSchema(1);
		consultaNFeRecebidasRequest.setMensagemXML(xml);
		ConsultaNFeRecebidasResponse consultaNFeRecebidasResponse = nfPort.consultaNFeRecebidas(consultaNFeRecebidasRequest);
		String searchResults = consultaNFeRecebidasResponse.getRetornoXML(); 
		return searchResults;
	}
	
	
	public static String sign(String xml, Certificate certificate) throws Exception {
		//Get public and private key
		FeeKeyPair fkp = CertUtil.getKeyPair(certificate);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true); 
		DocumentBuilder builder = dbf.newDocumentBuilder();  
		Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
		DOMSignContext dsc = new DOMSignContext(fkp.getPk(), doc.getDocumentElement());
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
		//change the signute algoritm from sha256 to sha1 
		Reference ref = fac.newReference("", fac.newDigestMethod(DigestMethod.SHA1, null),
				Collections.singletonList(fac.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null)), null,
				null); 
		SignedInfo si = fac.newSignedInfo(fac.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS,
				(C14NMethodParameterSpec) null),fac.newSignatureMethod("http://www.w3.org/2000/09/xmldsig#rsa-sha1", null),
				Collections.singletonList(ref));
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		//Set the X509 data from cert 
		X509Data kv = kif.newX509Data(Arrays.asList(fkp.getCertificate()));
		KeyInfo ki = kif.newKeyInfo(Collections.singletonList(kv));
		XMLSignature signature = fac.newXMLSignature(si, ki); 
		signature.sign(dsc);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer trans = tf.newTransformer();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		trans.transform(new DOMSource(doc), new StreamResult(baos));
		String signed = new String(baos.toByteArray());
		return signed;
	}
}
