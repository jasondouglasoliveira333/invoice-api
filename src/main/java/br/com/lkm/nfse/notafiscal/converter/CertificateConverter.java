package br.com.lkm.nfse.notafiscal.converter;

import br.com.lkm.nfse.notafiscal.model.Certificate;
import br.com.lkm.nfse.notafiscal.view.CertificateDTO;

public class CertificateConverter {
	public static CertificateDTO convert(Certificate c) {
		CertificateDTO cDTO = new CertificateDTO();
		cDTO.setId(c.getId());
		cDTO.setCnpj(c.getCnpj());
		cDTO.setName(new String(c.getName()));
		cDTO.setPassword(c.getPassword());
		return cDTO;
	}

	public static Certificate convert(CertificateDTO cDTO) {
		Certificate c = new Certificate();
		c.setId(cDTO.getId());
		c.setCnpj(cDTO.getCnpj());
		c.setName(cDTO.getName());
		c.setPassword(cDTO.getPassword());
		return c;
	}
}
