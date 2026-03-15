package com.school.fee;

import com.school.fee.dto.FeeRequestDTO;
import com.school.fee.dto.ReceiptResponseDTO;
import com.school.fee.dto.StudentResponseDTO;
import com.school.fee.model.FeeReceipt;
import com.school.fee.model.FeeType;
import com.school.fee.model.PaymentMode;
import com.school.fee.model.PaymentStatus;
import net.datafaker.Faker;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FeeTestDataBuilder {

    private static final Faker faker = new Faker();

    public static FeeRequestDTO buildFeeRequest(
            String studentId) {
        return FeeRequestDTO.builder()
                .studentId(studentId)
                .feeType(FeeType.TUITION)
                .feeDescription("Tuition Fees - " +
                        faker.educator().course())
                .amount(BigDecimal.valueOf(
                        faker.number()
                                .numberBetween(100, 5000)))
                .currency("AED")
                .paymentMode(PaymentMode.CARD)
                .cardNumber("1234-5678-1236-0081")
                .cardType("MASTERCARD")
                .transactionReference(
                        faker.number().digits(12))
                .academicYear("2026-27")
                .feeMonth("APRIL")
                .collectedBy("Admin")
                .build();
    }

    public static StudentResponseDTO
    buildStudentResponse(
            String studentId) {
        return StudentResponseDTO.builder()
                .studentId(studentId)
                .studentName(faker.name().fullName())
                .grade(faker.number()
                        .numberBetween(1, 12) + "-A")
                .mobileNumber("+9715" +
                        faker.number().digits(8))
                .schoolName(
                        faker.university().name())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static FeeReceipt buildFeeReceipt(
            String studentId,
            FeeRequestDTO request,
            StudentResponseDTO student) {
        return FeeReceipt.builder()
                .receiptNumber("RCP-2026-00001")
                .studentId(studentId)
                .studentName(student.getStudentName())
                .grade(student.getGrade())
                .schoolName(student.getSchoolName())
                .mobileNumber(
                        student.getMobileNumber())
                .feeType(request.getFeeType())
                .feeDescription(
                        request.getFeeDescription())
                .amount(request.getAmount())
                .totalAmount(request.getAmount())
                .currency(request.getCurrency())
                .paymentMode(request.getPaymentMode())
                .cardNumber("**** **** **** 0081")
                .cardType(request.getCardType())
                .transactionReference(
                        request.getTransactionReference())
                .academicYear(
                        request.getAcademicYear())
                .feeMonth(request.getFeeMonth())
                .collectedBy(request.getCollectedBy())
                .paymentStatus(PaymentStatus.PAID)
                .paymentDate(LocalDateTime.now())
                .build();
    }

    public static FeeReceipt buildFeeReceiptWithId(
            String studentId,
            FeeRequestDTO request,
            StudentResponseDTO student) {
        return FeeReceipt.builder()
                .id(1L)
                .receiptNumber(null)
                .studentId(studentId)
                .studentName(student.getStudentName())
                .grade(student.getGrade())
                .schoolName(student.getSchoolName())
                .mobileNumber(student.getMobileNumber())
                .feeType(request.getFeeType())
                .feeDescription(request.getFeeDescription())
                .amount(request.getAmount())
                .totalAmount(request.getAmount())
                .currency(request.getCurrency())
                .paymentMode(request.getPaymentMode())
                .cardNumber("**** **** **** 0081")
                .cardType(request.getCardType())
                .transactionReference(
                        request.getTransactionReference())
                .academicYear(request.getAcademicYear())
                .feeMonth(request.getFeeMonth())
                .collectedBy(request.getCollectedBy())
                .paymentStatus(PaymentStatus.PAID)
                .paymentDate(LocalDateTime.now())
                .build();
    }


    public static ReceiptResponseDTO buildReceiptResponse(FeeReceipt receipt) {
        return ReceiptResponseDTO.builder()
                .receiptNumber(receipt.getReceiptNumber())
                .paymentDate(receipt.getPaymentDate())
                .studentId(receipt.getStudentId())
                .studentName(receipt.getStudentName())
                .grade(receipt.getGrade())
                .schoolName(receipt.getSchoolName())
                .mobileNumber(receipt.getMobileNumber())
                .feeType(receipt.getFeeType())
                .feeDescription(receipt.getFeeDescription())
                .amount(receipt.getAmount())
                .totalAmount(receipt.getTotalAmount())
                .currency(receipt.getCurrency())
                .paymentMode(receipt.getPaymentMode())
                .cardNumber(receipt.getCardNumber())
                .cardType(receipt.getCardType())
                .transactionReference(receipt.getTransactionReference())
                .paymentStatus(receipt.getPaymentStatus())
                .academicYear(receipt.getAcademicYear())
                .feeMonth(receipt.getFeeMonth())
                .collectedBy(receipt.getCollectedBy())
                .build();
    }}
