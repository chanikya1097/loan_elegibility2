package com.loan.management.loanmanagement_repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.loan.management.loanmanagement_entity.LoanApplicationHistory;

@Repository
public interface LoanApplicationHistoryRepo extends JpaRepository<LoanApplicationHistory, Long> {

	List<LoanApplicationHistory> findByStatus(String status);

	List<LoanApplicationHistory> findByCustomerId(Long customerId);

	@Query("SELECT l FROM LoanApplicationHistory l WHERE l.applicationDate BETWEEN :startDate AND :endDate")
	List<LoanApplicationHistory> findLoanApplicationsBetweenDates(Date startDate, Date endDate);

}
