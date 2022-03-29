package br.com.lkm.nfse.notafiscal;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InvoiceApplication {
	
	public static void main(String... args) {
		System.out.println(System.getProperties());
		System.out.println("currentDir:" + new File(".").getAbsolutePath());
		SpringApplication.run(InvoiceApplication.class);
	}
}
