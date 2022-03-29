package br.com.lkm.nfse.notafiscal.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lkm.nfse.notafiscal.converter.CertificateConverter;
import br.com.lkm.nfse.notafiscal.model.Certificate;
import br.com.lkm.nfse.notafiscal.repository.CertificateRepository;
import br.com.lkm.nfse.notafiscal.util.CertUtil;
import br.com.lkm.nfse.notafiscal.util.OSUtil;
import br.com.lkm.nfse.notafiscal.view.CertificateDTO;
import br.com.lkm.nfse.notafiscal.view.PageResponse;

@Service
public class CertificateService {
	
	@Autowired
	private CertificateRepository certificateRepository; 

	public PageResponse<CertificateDTO> findAll(PageRequest pageRequest) {
		Page<Certificate> pCertificate = certificateRepository.findAll(pageRequest);
		List<CertificateDTO> list = pCertificate.getContent().stream().map(CertificateConverter::convert).collect(Collectors.toList());
		PageResponse<CertificateDTO> pr = new PageResponse<>();
		pr.setContent(list);
		pr.setTotalPages(pCertificate.getTotalPages());
		return pr;
	}


	public void deleteByCnpj(String cnpj) {
		certificateRepository.deleteByCnpj(cnpj);
	}

	public void save(CertificateDTO cDTO, String fileName, byte[] content) throws Exception {
		Path certFile = Paths.get(CertUtil.BASE_CERT_PATH + fileName);
		Files.write(certFile, content);
		cDTO.setName(fileName);
		Certificate cModel = CertificateConverter.convert(cDTO);
		OSUtil.installCert(certFile, cDTO.getPassword());
		certificateRepository.save(cModel);
	}


	@Transactional
	public void updatePassword(String cnpj, String password) {
		certificateRepository.updatePassword(cnpj, password);
	}


	public CertificateDTO findFirstByCnpj(String cnpj) {
		return CertificateConverter.convert(certificateRepository.findFirstByCnpj(cnpj));
	}

}
