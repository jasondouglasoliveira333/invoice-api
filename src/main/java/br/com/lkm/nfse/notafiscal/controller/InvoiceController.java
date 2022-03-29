package br.com.lkm.nfse.notafiscal.controller;

import java.time.LocalDate;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.lkm.nfse.notafiscal.service.CertificateService;
import br.com.lkm.nfse.notafiscal.service.InvoiceService;
import br.com.lkm.nfse.notafiscal.service.OCRLKMService;
import br.com.lkm.nfse.notafiscal.view.CertificateDTO;
import br.com.lkm.nfse.notafiscal.view.PageResponse;

@RestController
@CrossOrigin
@RequestMapping("invoice/api/v1")
public class InvoiceController {
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private OCRLKMService ocrlkmService;
	
	@Autowired
	private CertificateService certificateService;
	
	private static Logger log = LoggerFactory.getLogger(InvoiceController.class); 
	
	@GetMapping("ping")
	public String ping() {
		return "Hi FOLKS";
	}
	
	@GetMapping("invoices")
	public ResponseEntity<?> findInvoices(@RequestParam("rootCnpj") String rootCnpj, @RequestParam("cnpj") String cnpj, 
			@RequestParam("startDate") String startDateS, @RequestParam("endDate") String endDateS, 
			@RequestParam("provicneCode") String provinceCode, @RequestParam("townhallUrl") String townhallUrl){
		log.info("in findInvoices: startDate" + startDateS);
		try {
			String invoices = invoiceService.findInvoices(rootCnpj, cnpj, LocalDate.parse(startDateS), LocalDate.parse(endDateS), 
					provinceCode, townhallUrl);
			return ResponseEntity.ok().body(invoices);
		}catch (Exception e) {
			log.error("Erro procurando as notas fiscais", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
//	@PostMapping("validate")
//	public ResponseEntity<?> validateFee(@RequestParam("xml") String xml){
//		try {
//			log.info("xml:" + xml);
//			boolean valid = invoiceService.validate(xml);
//			return ResponseEntity.ok().body(valid);
//		}catch (Exception e) {
//			log.error("Erro validando a nota fiscal", e);
//			return ResponseEntity.badRequest().build();
//		}
//	}
	
	@PostMapping("ocr")
	public ResponseEntity<?> ocr(@RequestParam("file") MultipartFile file){
		try {
			byte[] contentEncoded = Base64.getEncoder().encode(file.getBytes());
			ocrlkmService.sendPDF(contentEncoded, file.getOriginalFilename());
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			log.error("Erro fazendo a ocr", e);
			return ResponseEntity.badRequest().build();
		}
	}
			
	
	@GetMapping("certificates")
	public ResponseEntity<?> listCertificates(@RequestParam(name="page", defaultValue = "0") Integer page, 
			@RequestParam(name="size", defaultValue = "10") Integer size){
		try {
			PageRequest pageRequest = PageRequest.of(page, size);
			PageResponse<CertificateDTO> pr = certificateService.findAll(pageRequest);
			return ResponseEntity.ok(pr);
		}catch (Exception e) {
			log.error("Erro listando os certificados ", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("certificates/{cnpj}")
	public ResponseEntity<?> getByCNPJ(@PathVariable("cnpj") String cnpj){
		try {
			System.out.println("cnpj:" + cnpj);
			CertificateDTO cDTO = certificateService.findFirstByCnpj(cnpj);
			return ResponseEntity.ok(cDTO);
		}catch (Exception e) {
			log.error("Erro listando os certificados ", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping("certificates")
	public ResponseEntity<?> saveCertificate(@RequestParam("file") MultipartFile file, CertificateDTO cDTO){
		try {
			certificateService.save(cDTO, file.getOriginalFilename(), file.getBytes());
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			log.error("Erro validando a nota fiscal", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("certificates/{cnpj}")
	public ResponseEntity<?> saveCertificate(@PathVariable("cnpj") String cnpj, @RequestParam("password") String password){
		try {
			certificateService.updatePassword(cnpj, password);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			log.error("Erro validando a nota fiscal", e);
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("certificates/{cnpj}")
	@Transactional
	public ResponseEntity<?> deleteCertificate(@PathVariable("cnpj") String cnpj){
		try {
			System.out.println("delete:cnpj:" + cnpj);
			certificateService.deleteByCnpj(cnpj);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			log.error("Erro validando a nota fiscal", e);
			return ResponseEntity.badRequest().build();
		}
	}


//	

}
