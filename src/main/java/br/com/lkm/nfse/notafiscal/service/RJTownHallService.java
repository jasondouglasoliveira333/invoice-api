package br.com.lkm.nfse.notafiscal.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.lkm.nfse.notafiscal.model.Certificate;
import br.com.lkm.nfse.notafiscal.repository.CertificateRepository;
import br.com.lkm.nfse.notafiscal.util.RegistryWriter;
import br.com.lkm.nfse.notafiscal.util.StringUtil;

/**
 *
 */
@SuppressWarnings("unused")
@Service
public class RJTownHallService {
	
	private static Logger log = LoggerFactory.getLogger(RJTownHallService.class);
	
	@Autowired	
	private CertificateRepository certificateRepository;
	
	@Value("${lkm.invoice.rjtownhall.download.dir}")
	private String rjDownloadDir;

	public static void main(String[] args) {
		try {
			String rootCnpj = "57029431002737";
			String cnpj = "57029431002737";
			RJTownHallService s = new RJTownHallService();
			String result = s.findInvoices(rootCnpj, cnpj, LocalDate.of(2021, 9, 1), LocalDate.of(2021, 9, 30), null, null);
			System.out.println("result:" + result);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String findInvoices(String rootCnpj, String cnpj, LocalDate startDate, LocalDate endDate, String provinceCode, String townhallUrl) throws Exception {
		log.info("rootCnpj:" + rootCnpj);
		Certificate certificate = certificateRepository.findFirstByCnpj(rootCnpj);
//		Certificate certificate = new Certificate();
//		certificate.setCnpj(rootCnpj);
//		certificate.setName("57029431AtlasCopco.pfx");
//		certificate.setPassword("1009880547");
		RegistryWriter.write(certificate);
		RemoteWebDriver driver = initDriver();
		login(driver);
		searchEmittedFee(driver, startDate, endDate);
		Thread.sleep(5000);//wait the download
		driver.quit();
		String content = getDownloadedContent();
		return content;
	}
	
	private String getDownloadedContent() throws Exception {
		File f = new File(rjDownloadDir).listFiles()[0];
		byte[] data = Files.readAllBytes(f.toPath());
		f.delete();
		return new String(data);
	}

	private RemoteWebDriver initDriver() {
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		ChromeOptions co = new ChromeOptions();
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("profile.default_content_settings.popups", 0);
		chromePrefs.put("download.default_directory", rjDownloadDir);
		co.setExperimentalOption("prefs", chromePrefs);
//		co.addArguments("--window-size=200,200", "--window-position=-200,-200");
		ChromeDriver driver = new ChromeDriver(co);
		return driver;
	}

	private static void login(RemoteWebDriver driver) {
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.get("https://notacarioca.rio.gov.br/senhaweb/loginICP.aspx");
		WebElement button = driver.findElementById("ctl00_cphCabMenu_btAcesso");
		System.out.println("button.toString():" + button);
		button.click();
	}

	private static void searchEmittedFee(RemoteWebDriver driver, LocalDate startDate , LocalDate endDate) throws Exception {
		int year = endDate.getYear();
		String mounth = getMounthName(endDate.getMonthValue());
		String mounthYear = mounth + "/" + year;
		driver.get("https://notacarioca.rio.gov.br/contribuinte/exportaarquivo.aspx");
		Select selectTomador = new Select(driver.findElementById("ctl00_cphCabMenu_ddlPrestador"));
		selectTomador.selectByIndex(2);
		WebElement notaRecebida = driver.findElementById("ctl00_cphCabMenu_rbNotasRecebidas");
		notaRecebida.click();
		WebElement dataInicio = driver.findElementById("ctl00_cphCabMenu_tbInicio");
		WebElement dataFim = driver.findElementById("ctl00_cphCabMenu_tbFim");
		DateTimeFormatter DTF_ddMMyyyy = DateTimeFormatter.ofPattern("ddMMyyyy");
		dataInicio.clear();
		dataInicio.sendKeys(DTF_ddMMyyyy.format(startDate));
		dataFim.clear();
		dataFim.sendKeys(DTF_ddMMyyyy.format(endDate));
		Select selectCompetenciaEmissao = new Select(driver.findElementById("ctl00_cphCabMenu_ddlTipoArquivo"));
		selectCompetenciaEmissao.selectByIndex(3);
		WebElement pesquisar = driver.findElementById("ctl00_cphCabMenu_btGerar");
		pesquisar.click();
	}
	
	public static String getMounthName(int mounth) {
		List<String> mounths = Arrays.asList("jan", "fev", "mar", "abr", "mai", "jun", "jul", "ago", "set", "out", "nov", "dev");
		return mounths.get(mounth-1);
	}
	
}
