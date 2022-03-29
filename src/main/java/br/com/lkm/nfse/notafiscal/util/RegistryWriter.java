package br.com.lkm.nfse.notafiscal.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.Registry;
import java.security.cert.X509Certificate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.lkm.nfse.notafiscal.model.Certificate;
import br.com.lkm.nfse.notafiscal.view.FeeKeyPair;

public class RegistryWriter {
	
	private static Logger log = LoggerFactory.getLogger(RegistryWriter.class);
	
	private static String REGISTRY_TEMPLATE = "Windows Registry Editor Version 5.00\r\n"
			+ "\r\n"
			+ "[HKEY_LOCAL_MACHINE\\SOFTWARE\\Policies\\Google\\Chrome\\AutoSelectCertificateForUrls]\r\n"
			+ "\"2\"=\"{\\\"pattern\\\":\\\"[*.]notacarioca.rio.gov.br\\\",\\\"filter\\\":{\\\"SUBJECT\\\":{\\\"CN\\\":\\\"{{CN_NAME}}\\\"}}}\"\r\n"
			+ "\r\n";
	
	public static void write(Certificate certificate) throws Exception {
		FeeKeyPair fkp = CertUtil.getKeyPair(certificate);
		X509Certificate cert = fkp.getCertificate();
		String subjectName = cert.getSubjectX500Principal().getName();
		log.info("subjectName:" + subjectName);
		int idx = subjectName.indexOf("CN=");
		int endIdx = subjectName.indexOf(",", idx);
		String cn = subjectName.substring(idx+3, endIdx); 
		log.info(">>>cn:" + cn);
		String registryContent = REGISTRY_TEMPLATE.replace("{{CN_NAME}}", cn);
		Files.write(Paths.get("auto_select_cert.reg"), registryContent.getBytes());
		try {
			Runtime.getRuntime().exec("regedit /S auto_select_cert.reg");
		}catch (Exception e) {
			log.error("Erro executando o registro", e);
		}
	}

}
