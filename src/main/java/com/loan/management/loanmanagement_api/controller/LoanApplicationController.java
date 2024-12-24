package com.loan.management.loanmanagement_api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loan.management.service.LoanApplicationService;

@RestController
@RequestMapping("/loan-application")
//@CrossOrigin(origins = { "http://localhost:4200", "https://63a5-47-184-104-147.ngrok-free.app","https://empty-cows-slide.loca.lt" })
@CrossOrigin(origins = "*")
public class LoanApplicationController {

	private final LoanApplicationService loanApplicationService;

	@Autowired
	public LoanApplicationController(LoanApplicationService loanApplicationService) {
		this.loanApplicationService = loanApplicationService;
	}

	// Endpoint to check loan eligibility based on customer name and other
	// parameters
	@GetMapping("/check-eligibility")
	public String getEligibilityStatus(@RequestParam String name, @RequestParam int creditScore,
			@RequestParam double yearlyIncome, @RequestParam double totalDebt, @RequestParam String employmentStatus) {

		return loanApplicationService.getEligibilityStatus(name, creditScore, yearlyIncome, totalDebt,
				employmentStatus);
	}

	// Endpoint to calculate the maximum loan amount based on credit score and IDR
	// (Income-to-Debt Ratio)
	@GetMapping("/loan-calculator")
	public Map<String, Double> calculateMaxLoanAmount(@RequestParam("yearlyIncome") double yearlyIncome,
			@RequestParam("creditScore") int creditScore, @RequestParam("totalDebt") double totalDebt) {
		return loanApplicationService.calculateMaxLoanAmount(yearlyIncome, creditScore, totalDebt);
	}

}