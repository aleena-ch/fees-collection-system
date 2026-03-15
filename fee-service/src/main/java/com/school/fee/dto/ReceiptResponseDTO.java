package com.school.fee.dto;

import com.school.fee.model.FeeType;
import com.school.fee.model.PaymentMode;
import com.school.fee.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponseDTO {

    private String receiptNumber;
    private LocalDateTime paymentDate;

    private String studentId;
    private String studentName;
    private String grade;
    private String schoolName;
    private String mobileNumber;

    private FeeType feeType;
    private String feeDescription;
    private BigDecimal amount;
    private BigDecimal totalAmount;
    private String currency;

    private PaymentMode paymentMode;
    private String cardNumber;
    private String cardType;
    private String transactionReference;
    private PaymentStatus paymentStatus;

    private String academicYear;
    private String feeMonth;
    private String collectedBy;
    private String remarks;
}
