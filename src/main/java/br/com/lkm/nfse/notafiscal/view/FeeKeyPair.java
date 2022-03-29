package br.com.lkm.nfse.notafiscal.view;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public class FeeKeyPair {
	private X509Certificate certificate;
	private PrivateKey pk;

	public FeeKeyPair() {
	}

	public FeeKeyPair(X509Certificate certificate, PrivateKey pk) {
		this.certificate = certificate;
		this.pk = pk;
	}

	public X509Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(X509Certificate certificate) {
		this.certificate = certificate;
	}

	public PrivateKey getPk() {
		return pk;
	}

	public void setPk(PrivateKey pk) {
		this.pk = pk;
	}

}
