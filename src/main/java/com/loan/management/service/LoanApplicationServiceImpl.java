package com.loan.management.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loan.management.loanmanagement_entity.LoanApplicationEntity;
import com.loan.management.loanmanagement_entity.LoanApplicationHistory;
import com.loan.management.loanmanagement_entity.LoanEligibilityConfig;
import com.loan.management.loanmanagement_repo.LoanApplicationHistoryRepo;
import com.loan.management.loanmanagement_repo.LoanApplicationRepo;
import com.loan.management.loanmanagement_repo.LoanEligibilityConfigRepo;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

	private final LoanApplicationRepo loanApplicationRepository;
	private final LoanEligibilityConfigRepo loanEligibilityConfigRepo;
	private final LoanApplicationHistoryRepo loanApplicationHistoryRepo;

	@Autowired
	public LoanApplicationServiceImpl(LoanApplicationRepo loanApplicationRepository,
			LoanEligibilityConfigRepo loanEligibilityConfigRepo,
			LoanApplicationHistoryRepo loanApplicationHistoryRepo) {
		this.loanApplicationRepository = loanApplicationRepository;
		this.loanEligibilityConfigRepo = loanEligibilityConfigRepo;
		this.loanApplicationHistoryRepo = loanApplicationHistoryRepo;
	}

	@Override
	public String getEligibilityStatus(String customerName, int creditScore, double yearlyIncome, double totalDebt,
			String employmentStatus) {
		LoanApplicationEntity loanApplication = loanApplicationRepository.findByCustomerName(customerName);

		if (loanApplication == null) {
			return "No loan application found for customer: " + customerName;
		}

		LoanEligibilityConfig config = loanEligibilityConfigRepo.findById(1L)
				.orElseThrow(() -> new RuntimeException("Configuration not found"));

		List<String> rejectionReasons = new ArrayList<>();

		checkRejectionConditions(config, creditScore, yearlyIncome, totalDebt, employmentStatus, rejectionReasons);

		if (rejectionReasons.isEmpty()) {
			loanApplication.setEligibilityStatus("Eligible");
			loanApplication.setLoanStatus("Approved");
			saveLoanApplicationHistory(loanApplication);
			return "Customer " + customerName + " is eligible for the loan.";
		} else {
			loanApplication.setEligibilityStatus("Not Eligible");
			loanApplication.setLoanStatus("Rejected");
			saveLoanApplicationHistory(loanApplication);
			return "Customer " + customerName + " is not eligible: " + String.join(", ", rejectionReasons);
		}
	}

	private void checkRejectionConditions(LoanEligibilityConfig config, int creditScore, double yearlyIncome,
			double totalDebt, String employmentStatus, List<String> rejectionReasons) {
		Map<String, Boolean> rejectionConditions = Map.of("Credit Score too low.",
				creditScore < config.getCreditScoreThreshold(), "Income too low.",
				yearlyIncome < config.getIncomeThreshold(), "Existing loans exceed the $10,000 limit.",
				totalDebt > 10000, "Income-to-Debt Ratio (IDR) is too high.", (totalDebt / yearlyIncome) > 0.4,
				"Customer is unemployed.", employmentStatus.equalsIgnoreCase("Unemployed"));

		rejectionConditions.forEach((reason, condition) -> {
			if (condition) {
				rejectionReasons.add(reason);
			}
		});
	}

	@Override
	public Map<String, Double> calculateMaxLoanAmount(double yearlyIncome, int creditScore, double totalDebt) {
		Map<String, Double> result = new HashMap<>();

		if (creditScore < 650) {
			result.put("maxLoanAmount", 0.0);
			return result;
		}
		double maxLoanAmount = yearlyIncome * 0.4;
		result.put("maxLoanAmount", maxLoanAmount);
		return result;
	}

	private void saveLoanApplicationHistory(LoanApplicationEntity loanApplication) {
		LoanApplicationHistory history = new LoanApplicationHistory();
		history.setLoanAmount(loanApplication.getLoanAmount());
		history.setStatus(loanApplication.getLoanStatus());
		history.setApplicationDate(new java.sql.Timestamp(System.currentTimeMillis()));
		loanApplicationHistoryRepo.save(history);
	}
}
