package br.com.lkm.nfse.notafiscal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.lkm.nfse.notafiscal.model.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Integer>{
	
	public Certificate findFirstByCnpj(String cnpj);

	@Query("update Certificate c set c.password = :password where c.cnpj = :cnpj")
	@Modifying
	public void updatePassword(@Param("cnpj") String cnpj, @Param("password") String password);

	@Modifying
	public void deleteByCnpj(String cnpj);

}
