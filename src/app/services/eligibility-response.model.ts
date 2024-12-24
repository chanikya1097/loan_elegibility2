export interface EligibilityResponse {
  status: string;  // Eligibility status (Eligible/Not Eligible)
  message: string;  // The message returned from the backend
  rejectionReasons?: string[];  // List of rejection reasons (only if not eligible)
}
