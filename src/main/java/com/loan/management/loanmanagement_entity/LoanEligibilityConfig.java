package com.loan.management.loanmanagement_entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class LoanEligibilityConfig {

    @Id
    private Long id;

    private int creditScoreThreshold;
    private double incomeThreshold;

    // Getters and Setters
    public int getCreditScoreThreshold() {
        return creditScoreThreshold;
    }

    public void setCreditScoreThreshold(int creditScoreThreshold) {
        this.creditScoreThreshold = creditScoreThreshold;
    }

    public double getIncomeThreshold() {
        return incomeThreshold;
    }

    public void setIncomeThreshold(double incomeThreshold) {
        this.incomeThreshold = incomeThreshold;
    }
}
