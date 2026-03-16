package com.school.fee.repository;

import com.school.fee.model.FeeReceipt;
import com.school.fee.model.FeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for FeeReceipt entity.
 * Provides data access for fee receipt records.
 */
@Repository
public interface FeeRepository extends JpaRepository<FeeReceipt, Long> {

    /**
     * Finds receipt by receipt number.
     *
     * @param receiptNumber Unique receipt number
     * @return Optional containing receipt if found
     */
    Optional<FeeReceipt> findByReceiptNumber(String receiptNumber);

    /**
     * Finds all receipts for a student
     * with pagination.
     *
     * @param studentId Student identifier
     * @param pageable  Pagination parameters
     * @return Page of fee receipts
     */
    Page<FeeReceipt> findByStudentId(String studentId, Pageable pageable);

    /**
     * Checks for duplicate fee payment.
     * Used to prevent same fee being paid twice
     * for the same month and academic year.
     *
     * @param studentId    Student identifier
     * @param feeType      Type of fee
     * @param feeMonth     Month of fee
     * @param academicYear Academic year
     * @return Optional containing receipt
     * if duplicate found
     */
    Optional<FeeReceipt> findByStudentIdAndFeeTypeAndFeeMonthAndAcademicYear(
            String studentId, FeeType feeType, String feeMonth, String academicYear);
}
