package com.school.fee.dto;

import com.school.fee.model.FeeType;
import com.school.fee.model.PaymentMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeRequestDTO {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotNull(message = "Fee type is required")
    private FeeType feeType;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotBlank(message = "Fee description is required")
    private String feeDescription;

    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;

    private String cardNumber;

    private String cardType;

    private String transactionReference;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @NotBlank(message = "Fee month is required")
    private String feeMonth;

    @NotBlank(message = "Collected by is required")
    private String collectedBy;

    private String remarks;
}
