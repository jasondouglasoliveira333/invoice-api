package br.com.lkm.nfse.notafiscal.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	public static String extractTagValue(String xml, String tag) {
		int idx = xml.indexOf("<" + tag + ">");
		if (idx != -1) {
			int startV = idx+tag.length()+2;
			int end = xml.indexOf("</" + tag, startV); 
			if (end != -1) {
				return xml.substring(startV, end);
			}
		}
		return null;
	}

	public static List<String> extractListTagValue(String xml, String tag) {
		List<String> values = new ArrayList<>();
		int idx = 0;
		while ((idx = xml.indexOf(tag, idx)) != -1) {
			int startV = idx+tag.length()+1;
			int end = xml.indexOf("</" + tag, startV); 
			if (end != -1) {
				values.add(xml.substring(startV, end));
				idx = end+tag.length()+1;
			}
		}
		return values;
		
	}

	

	
	public static void main(String[] args) {
		String xml = "<root><ultNSU>HI FOLKS</ultNSU><maxNSU>YOU</maxNSU><maxNSU>YOU2</maxNSU><maxNSU>YOU3</maxNSU></root>";
		System.out.println(extractTagValue(xml, "ultNSU"));
		System.out.println(extractTagValue(xml, "maxNSU"));
		System.out.println(extractListTagValue(xml, "maxNSU"));
	}

}
