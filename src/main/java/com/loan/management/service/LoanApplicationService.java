package com.loan.management.service;

import java.util.Map;

public interface LoanApplicationService {

	String getEligibilityStatus(String customerName, int creditScore, double yearlyIncome, double totalDebt,
			String employmentStatus);

	Map<String, Double> calculateMaxLoanAmount(double yearlyIncome, int creditScore, double totalDebt);
}
