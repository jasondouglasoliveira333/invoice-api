package br.com.lkm.nfse.notafiscal.enums;

public enum ProvinceCodeEnum {
	SAOPAULO("35"), RIOJANEIRO("33"); 

	private String value; 
	
	private ProvinceCodeEnum(String value) {
		this.value = value;
	}
	
	public static ProvinceCodeEnum parse(String value) {
		for (ProvinceCodeEnum v :  values()) {
			if (v.value.equals(value)) {
				return v;
			}
		}
		return null;
	}
}
