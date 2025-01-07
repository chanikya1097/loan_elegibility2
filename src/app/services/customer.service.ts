import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  // private apiUrl = 'http://localhost:8080/loan-application/check-eligibility'; 
  // private loanCalculatorUrl = 'http://localhost:8080/loan-application/loan-calculator';
  private apiUrl = 'https://3921-47-184-104-147.ngrok-free.app/api/loan-application/check-eligibility';
private loanCalculatorUrl = 'https://3921-47-184-104-147.ngrok-free.app/api/loan-application/loan-calculator';


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
