package com.loan.management.loanmanagement_repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loan.management.loanmanagement_entity.LoanApplicationEntity;

@Repository
public interface LoanApplicationRepo extends JpaRepository<LoanApplicationEntity, Long> {

	LoanApplicationEntity findByCustomerName(String customerName);

}
