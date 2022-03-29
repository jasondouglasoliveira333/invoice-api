package br.com.lkm.nfse.notafiscal.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.lkm.nfse.notafiscal.enums.ProvinceCodeEnum;

@Service
public class InvoiceService {

	@Autowired
	private SPTownHallService spTownHallService;
	
	@Autowired
	private RJTownHallService rjTownHallService;


	public String findInvoices(String rootCnpj, String cnpj, LocalDate startDate, LocalDate endDate, String provinceCode, String townhallUrl) throws Exception {
		ProvinceCodeEnum pc = ProvinceCodeEnum.parse(provinceCode);
		if (pc.equals(ProvinceCodeEnum.SAOPAULO)) {
			return spTownHallService.findInvoices(rootCnpj, cnpj, startDate, endDate, provinceCode, townhallUrl);
		}else if (pc.equals(ProvinceCodeEnum.RIOJANEIRO)) {
			return rjTownHallService.findInvoices(rootCnpj, cnpj, startDate, endDate, provinceCode, townhallUrl);
		}else {
			return null;
		}
	}

}
