import { Component } from '@angular/core';
import { CustomerService } from 'src/app/services/customer.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  name: string = '';
  creditScore: number = 700; // Default value
  yearlyIncome: number = 50000; // Default value
  totalDebt: number = 5000; // Default value
  employmentStatus: string = 'Employed'; // Default value

  eligibilityStatus: string = ''; // Eligibility status message
  rejectionReasons: string[] = []; // List of rejection reasons (if any)
  message: string = ''; // Message from backend (additional details)
  loanAmount: number = 0; // Max loan amount returned by backend

  constructor(private customerService: CustomerService) {}

  // Submit function to check eligibility
  checkEligibility() {
    if (this.name.trim()) {
      this.customerService
        .getEligibilityStatus(
          this.name,
          this.creditScore,
          this.yearlyIncome,
          this.totalDebt,
          this.employmentStatus
        )
        .subscribe(
          (response: string) => {
            // Now, response is plain text (the eligibility status message)
            this.eligibilityStatus = response;  // The response is the eligibility message from backend
            this.rejectionReasons = []; // Clear any previous rejection reasons
            this.message = ''; // Clear the message if eligibility is provided
          },
          (error) => {
            console.error('Error fetching eligibility status:', error);
            this.eligibilityStatus = 'Error fetching eligibility status!';
            this.message = ''; // Clear the message on error
            this.rejectionReasons = [];
          }
        );

      // Call the loan calculator method
      this.calculateLoanAmount();
    } else {
      this.eligibilityStatus = 'Please enter a valid name.';
      this.message = ''; // Clear the message if no name is entered
      this.rejectionReasons = [];
      this.loanAmount = 0; // Reset loan amount
    }
  }

  // Method to calculate max loan amount
  calculateLoanAmount() {
    this.customerService
      .calculateMaxLoanAmount(this.creditScore, this.yearlyIncome, this.totalDebt)
      .subscribe(
        (response: { maxLoanAmount: number }) => {
          this.loanAmount = response.maxLoanAmount; // Get max loan amount from response
        },
        (error) => {
          console.error('Error calculating max loan amount:', error);
          this.loanAmount = 0; // Reset loan amount on error
        }
      );
  }
}
