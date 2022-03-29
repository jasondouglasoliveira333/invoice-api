package br.com.lkm.nfse.notafiscal.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.lkm.nfse.notafiscal.util.IOUtil;

@Service
public class OCRLKMService {
	
	@Value("${lkm.invoice.ocrservice.url}")
	private String ocrServiceUrl = "http://192.168.197.21/ocr_async_qas/ocr.asmx";
	
	private static String SEND_TEMPLATE_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
			+ "<soap:Envelope\r\n"
			+ "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n"
			+ "	xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\r\n"
			+ "	xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n"
			+ "	<soap:Body>\r\n"
			+ "		<EnviarArquivo xmlns=\"http://ardfe.lkm.com.br\">\r\n"
			+ "			<arqConteudo>{{contentPDF}}</arqConteudo>\r\n"
			+ "			<arqNome>{{fileName}}</arqNome>\r\n"
			+ "			<arqDiret>BTP</arqDiret>\r\n"
			+ "			<arqUrl></arqUrl>\r\n"
			+ "			<arqEmailRem>renato_martins@lkm.com</arqEmailRem>\r\n"
			+ "			<arqEmailDthr>20191224123100</arqEmailDthr>\r\n"
			+ "			<identCliente>LKM Interno</identCliente>\r\n"
			+ "			<solicAmb>QAS</solicAmb>\r\n"
			+ "			<solicOrig>P</solicOrig>\r\n"
			+ "		</EnviarArquivo>\r\n"
			+ "	</soap:Body>\r\n"
			+ "</soap:Envelope>";
	
	public static void main(String... args) {
		try {
			byte[] data = Files.readAllBytes(Paths.get("C:\\jason\\work\\atividades\\ocr_btp\\CONTA-VIVO-LKM.pdf"));
			byte[] contentEncoded = Base64.getEncoder().encode(data);
			System.out.println("content:" + new String(contentEncoded));
			OCRLKMService o = new OCRLKMService();
			o.sendPDF(contentEncoded, "CONTA-VIVO-LKM.pdf");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void sendPDF(byte[] data, String fileName) {
		OutputStream os = null;
		InputStream is = null;
		try {
			URLConnection urlCon = new URL(ocrServiceUrl).openConnection();
			urlCon.setDoInput(true);
			urlCon.setDoOutput(true);
			urlCon.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//			Files.write(Path.of("C:\\jason\\work\\atividades\\crawler_nota\\ocr_output\\pdf_" + System.currentTimeMillis() + ".txt"), data);
			String xmlFilled = SEND_TEMPLATE_XML.replace("{{contentPDF}}", new String(data, "UTF-8")).replace("{{fileName}}", fileName);
			os = urlCon.getOutputStream();
			os.write(xmlFilled.getBytes());
			os.flush();
			is = urlCon.getInputStream();
			byte[] returnOCR = IOUtil.readAllBytes(is);
			System.out.println("returnOCR:" + new String(returnOCR));
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (os != null ) { try { os.close(); } catch (Exception e) {}}
			if (is != null ) { try { is.close(); } catch (Exception e) {}}
		}
		
	}
	
}
