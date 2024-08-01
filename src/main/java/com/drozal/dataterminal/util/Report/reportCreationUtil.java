package com.drozal.dataterminal.util.Report;

import java.security.SecureRandom;

public class reportCreationUtil {
	
	public static String generateReportNumber() {
		int num_length = 7;
		StringBuilder DeathReportNumber = new StringBuilder();
		for (int i = 0; i < num_length; i++) {
			SecureRandom RANDOM = new SecureRandom();
			int digit = RANDOM.nextInt(10);
			DeathReportNumber.append(digit);
		}
		return DeathReportNumber.toString();
	}
}