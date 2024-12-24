import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  private apiUrl = 'https://wide-dryers-knock.loca.lt/loan-application/check-eligibility'; // Backend API for eligibility
  private loanCalculatorUrl = 'https://wide-dryers-knock.loca.lt/loan-application/loan-calculator'; // Backend API for loan calculator

  constructor(private http: HttpClient) {}

  // Method to fetch eligibility status (Handle plain text response)
  getEligibilityStatus(
    name: string,
    creditScore: number,
    yearlyIncome: number,
    totalDebt: number,
    employmentStatus: string
  ): Observable<string> {
    const url = `${this.apiUrl}?name=${name}&creditScore=${creditScore}&yearlyIncome=${yearlyIncome}&totalDebt=${totalDebt}&employmentStatus=${employmentStatus}`;
    return this.http.get(url, { responseType: 'text' }).pipe(
      catchError(this.handleError)  // Improved error handling
    );
  }

  // Method to calculate max loan amount based on credit score, yearly income, and total debt
  calculateMaxLoanAmount(
    creditScore: number,
    yearlyIncome: number,
    totalDebt: number
  ): Observable<{ maxLoanAmount: number }> {
    const url = `${this.loanCalculatorUrl}?creditScore=${creditScore}&yearlyIncome=${yearlyIncome}&totalDebt=${totalDebt}`;
    return this.http.get<{ maxLoanAmount: number }>(url).pipe(
      catchError(this.handleError)  // Improved error handling
    );
  }

  // Custom error handler for HTTP requests
  private handleError(error: any) {
    console.error('Error occurred:', error); // For debugging purposes
    return throwError('Error occurred! Please try again later.');
  }
}
