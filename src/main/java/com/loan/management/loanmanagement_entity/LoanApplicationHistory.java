package com.loan.management.loanmanagement_entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class LoanApplicationHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long loanApplicationId;

	@Column(name = "loan_amount")
	private BigDecimal loanAmount;

	@Column(name = "status")
	private String status;

	@Column(name = "application_date")
	private Timestamp applicationDate; // Timestamp field

	@Column(name = "customer_id") // Add customer_id column
	private Long customerId; // New field to store customer_id

	// Getters and Setters
	public Long getLoanApplicationId() {
		return loanApplicationId;
	}

	public void setLoanApplicationId(Long loanApplicationId) {
		this.loanApplicationId = loanApplicationId;
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal bigDecimal) {
		this.loanAmount = bigDecimal;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(Timestamp applicationDate) {
		this.applicationDate = applicationDate;
	}

	public Long getCustomerId() {
		return customerId; // Getter for customerId
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId; // Setter for customerId
	}
}
