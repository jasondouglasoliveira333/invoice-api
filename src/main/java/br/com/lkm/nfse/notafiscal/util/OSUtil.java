package br.com.lkm.nfse.notafiscal.util;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OSUtil {
	
	private static Logger log = LoggerFactory.getLogger(OSUtil.class);
	
	private static String INSTALL_CERT_TEMPLATE = "certutil -importpfx -user -p {{password}} \"{{certPath}}\"";

	public static void installCert(Path certFile, String password) throws Exception{
		String installCertInstruction = INSTALL_CERT_TEMPLATE.replace("{{password}}", password).replace("{{certPath}}", certFile.toString());
		Runtime.getRuntime().exec(installCertInstruction);
		log.info("certificado instalado");
	}

}
