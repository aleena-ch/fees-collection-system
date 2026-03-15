package com.school.fee.service;

import com.school.fee.client.StudentClient;
import com.school.fee.dto.FeeRequestDTO;
import com.school.fee.dto.ReceiptResponseDTO;
import com.school.fee.dto.StudentResponseDTO;
import com.school.fee.exception.DuplicateFeeException;
import com.school.fee.exception.FeeNotFoundException;
import com.school.fee.model.FeeReceipt;
import com.school.fee.model.PaymentStatus;
import com.school.fee.repository.FeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeeService {

    private static final String RECEIPT_PREFIX = "RCP";
    private final FeeRepository feeRepository;
    private final StudentClient studentClient;

    @Transactional
    public ReceiptResponseDTO collectFee(FeeRequestDTO feeRequestDto) {

        log.info("Collecting fee for student: {}", feeRequestDto.getStudentId());
        StudentResponseDTO studentResponse = studentClient.getStudent(feeRequestDto.getStudentId());
        log.info("Student found: {}", studentResponse.getStudentName());
        checkIfFeePaid(feeRequestDto);
        FeeReceipt receipt = buildReceipt(feeRequestDto, studentResponse);
        FeeReceipt savedFeeReceipt = feeRepository.save(receipt);
        String receiptNumber = generateReceiptNumber(savedFeeReceipt.getId());
        savedFeeReceipt.setReceiptNumber(receiptNumber);
        FeeReceipt updated = feeRepository.save(savedFeeReceipt);
        log.info("Fee collected. Receipt: {}", updated.getReceiptNumber());

        return toResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public ReceiptResponseDTO getReceiptByNumber(String receiptNumber) {

        log.info("Fetching receipt: {}", receiptNumber);
        FeeReceipt feeReceipt = feeRepository.findByReceiptNumber(receiptNumber)
                .orElseThrow(() -> new FeeNotFoundException("Receipt not found: " + receiptNumber));
        return toResponseDTO(feeReceipt);
    }

    @Transactional(readOnly = true)
    public Page<ReceiptResponseDTO> getReceiptsByStudentId(String studentId, Pageable pageable) {

        log.info("Fetching receipts for student Id : {}", studentId);
        return feeRepository.findByStudentId(studentId, pageable)
                .map(this::toResponseDTO);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return null;
        } else {
            return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
        }
    }

    private String generateReceiptNumber(Long id) {
        int year = LocalDate.now().getYear();
        return String.format("%s-%d-%05d",
                RECEIPT_PREFIX, year, id);
    }

    private void checkIfFeePaid(FeeRequestDTO feeRequestDto) {
        feeRepository.findByStudentIdAndFeeTypeAndFeeMonthAndAcademicYear(
                        feeRequestDto.getStudentId(), feeRequestDto.getFeeType(),
                        feeRequestDto.getFeeMonth(), feeRequestDto.getAcademicYear())
                .ifPresent(existingReceipt -> {
                    throw new DuplicateFeeException("Fee already paid for " + feeRequestDto.getFeeType() + " - " +
                            feeRequestDto.getFeeMonth() + " " + feeRequestDto.getAcademicYear() +
                            ". Receipt: " + existingReceipt.getReceiptNumber());
                });
    }

    private FeeReceipt buildReceipt(FeeRequestDTO feeRequestDto, StudentResponseDTO studentResponse) {
        return FeeReceipt.builder()
                .studentId(studentResponse.getStudentId())
                .studentName(studentResponse.getStudentName())
                .grade(studentResponse.getGrade())
                .schoolName(studentResponse.getSchoolName())
                .mobileNumber(studentResponse.getMobileNumber())
                .feeType(feeRequestDto.getFeeType())
                .feeDescription(feeRequestDto.getFeeDescription())
                .amount(feeRequestDto.getAmount())
                .totalAmount(feeRequestDto.getAmount())
                .currency(feeRequestDto.getCurrency())
                .paymentMode(feeRequestDto.getPaymentMode())
                .cardNumber(maskCardNumber(
                        feeRequestDto.getCardNumber()))
                .cardType(feeRequestDto.getCardType())
                .transactionReference(
                        feeRequestDto.getTransactionReference())
                .academicYear(feeRequestDto.getAcademicYear())
                .feeMonth(feeRequestDto.getFeeMonth())
                .collectedBy(feeRequestDto.getCollectedBy())
                .remarks(feeRequestDto.getRemarks())
                .paymentStatus(PaymentStatus.PAID)
                .build();
    }

    private ReceiptResponseDTO toResponseDTO(
            FeeReceipt receipt) {
        return ReceiptResponseDTO.builder()
                .receiptNumber(
                        receipt.getReceiptNumber())
                .paymentDate(
                        receipt.getPaymentDate())
                .studentId(receipt.getStudentId())
                .studentName(
                        receipt.getStudentName())
                .grade(receipt.getGrade())
                .schoolName(receipt.getSchoolName())
                .mobileNumber(
                        receipt.getMobileNumber())
                .feeType(receipt.getFeeType())
                .feeDescription(
                        receipt.getFeeDescription())
                .amount(receipt.getAmount())
                .totalAmount(
                        receipt.getTotalAmount())
                .currency(receipt.getCurrency())
                .paymentMode(
                        receipt.getPaymentMode())
                .cardNumber(
                        receipt.getCardNumber())
                .cardType(receipt.getCardType())
                .transactionReference(
                        receipt.getTransactionReference())
                .paymentStatus(receipt.getPaymentStatus())
                .academicYear(
                        receipt.getAcademicYear())
                .feeMonth(receipt.getFeeMonth())
                .collectedBy(
                        receipt.getCollectedBy())
                .remarks(receipt.getRemarks())
                .build();
    }
}
