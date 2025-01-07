import { Component } from '@angular/core';
import { CustomerService } from 'src/app/services/customer.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent {
  name: string = '';
  creditScore: string = '750'; // Credit score as string for better control over input
  yearlyIncome: number = 50000; // Default value
  totalDebt: number = 5000; // Default value
  employmentStatus: string = 'Employed'; // Default value

  eligibilityStatus: string = ''; // Eligibility status message
  rejectionReasons: string[] = []; // List of rejection reasons (if any)
  message: string = ''; // Message from backend (additional details)
  loanAmount: number = 0; // Max loan amount returned by backend

  errorMessages: { creditScore?: string; yearlyIncome?: string; totalDebt?: string } = {};

  constructor(private customerService: CustomerService) {}

  validatePositive(field: "creditScore" | "yearlyIncome" | "totalDebt"): void {
    const value = field === "creditScore" ? Number(this.creditScore) : this[field];
  
    if (value < 0) {
      this.errorMessages[field] = "Negative value is not allowed";
    } else {
      this.errorMessages[field] = "";
    }
  }
  blockSpecialCharactersAndNumbers(event: KeyboardEvent): void {
    const regex = /^[a-zA-Z\s]*$/; // Allow letters and spaces only
    const key = event.key;
  
    // Block any input that does not match the regex
    if (!regex.test(key)) {
      event.preventDefault();
    }
  }
  
  
  limitToSixFigures(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = input.value.replace(/[^0-9]/g, ''); // Remove non-numeric characters
  
    if (value.length > 6) {
      input.value = value.slice(0, 6); // Limit input to 6 digits
    } else {
      input.value = value; // Valid input
    }
  }
  
  limitCreditScore(event: Event): void {
    const input = event.target as HTMLInputElement;
    input.value = input.value.replace(/[^0-9]/g, ''); // Remove any non-numeric characters
    this.creditScore = input.value; // Update the component's creditScore property
  }
  
  

  // Check if there are validation errors
  hasValidationErrors(): boolean {
    return Object.values(this.errorMessages).some((msg) => msg !== '');
  }

  // Submit function to check eligibility
  checkEligibility() {
    if (this.hasValidationErrors()) {
      this.eligibilityStatus = 'Please correct errors before submitting.';
      return;
    }

    const numericCreditScore = Number(this.creditScore);
    if (isNaN(numericCreditScore) || numericCreditScore < 350 || numericCreditScore > 850) {
      this.eligibilityStatus = 'Please enter a valid credit score.';
      return;
    }

    if (this.name.trim()) {
      this.customerService
        .getEligibilityStatus(
          this.name,
          numericCreditScore,
          this.yearlyIncome,
          this.totalDebt,
          this.employmentStatus
        )
        .subscribe(
          (response: string) => {
            this.eligibilityStatus = response;
            this.rejectionReasons = [];
            this.message = '';
          },
          (error) => {
            console.error('Error fetching eligibility status:', error);
            this.eligibilityStatus = 'Error fetching eligibility status!';
            this.rejectionReasons = [];
          }
        );

      // Call the loan calculator method
      this.calculateLoanAmount(numericCreditScore);
    } else {
      this.eligibilityStatus = 'Please enter a valid name.';
      this.rejectionReasons = [];
    }
  }

  // Calculate loan amount
  calculateLoanAmount(creditScore: number) {
    if (this.hasValidationErrors()) {
      console.error('Validation errors detected. Cannot calculate loan amount.');
      return;
    }

    this.customerService
      .calculateMaxLoanAmount(creditScore, this.yearlyIncome, this.totalDebt)
      .subscribe(
        (response: { maxLoanAmount: number }) => {
          this.loanAmount = response.maxLoanAmount;
        },
        (error) => {
          console.error('Error calculating max loan amount:', error);
          this.loanAmount = 0;
        }
      );
  }
}
