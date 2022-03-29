package br.com.lkm.nfse.notafiscal.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import br.com.lkm.nfse.notafiscal.model.Certificate;
import br.com.lkm.nfse.notafiscal.view.FeeKeyPair;

public class CertUtil {
	
	public static final String BASE_CERT_PATH = "./cert/";

	public static void configure(Certificate certificate) throws Exception {
        System.setProperty("javax.net.ssl.keyStore", BASE_CERT_PATH + certificate.getName());
        System.setProperty("javax.net.ssl.keyStorePassword", certificate.getPassword());
        System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

	}
	
	public static void main(String[] args) {
		try {
			TrustManager[] trustCerts = new TrustManager[]{
			    new X509TrustManager() {
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			            return null;
			        }
			        public void checkClientTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			        public void checkServerTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    }
			};
			
			SSLContext sc = SSLContext.getInstance("TLSv1.2");
			// Init the SSLContext with a TrustManager[] and SecureRandom()
			sc.init(null, trustCerts, new java.security.SecureRandom()); 
			
			Certificate certificate = new Certificate();
			certificate.setName("57029431AtlasCopco.pfx");
			certificate.setPassword("1009880547");
			
			CertUtil.configure(certificate);

			URL url = new URL("https://notacarioca.rio.gov.br/WSNacional/nfse.asmx?WSDL");
			HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
			urlConn.setSSLSocketFactory(sc.getSocketFactory());
			InputStream is = urlConn.getInputStream();
			byte[] data = IOUtil.readAllBytes(is);
			String s = new String(data);
			System.out.println("s:" + s);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static FeeKeyPair getKeyPair(Certificate certificate) throws Exception {
		//Get public and private key
		KeyStore ks = KeyStore.getInstance("pkcs12");
        try (FileInputStream fis = new FileInputStream(BASE_CERT_PATH + certificate.getName())){ //ensure close
        	ks.load(fis, certificate.getPassword().toCharArray());
        }
        String alias = "";
        Enumeration<String> aliasesEnum = ks.aliases();
        while (aliasesEnum.hasMoreElements() && !ks.isKeyEntry(alias = aliasesEnum.nextElement())) {
        }
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
        PrivateKey pk = (PrivateKey) ks.getKey(alias, certificate.getPassword().toCharArray());
        FeeKeyPair fkp = new FeeKeyPair(cert, pk);
        return fkp;
	}

}
