package com.school.fee.repository;

import com.school.fee.model.FeeReceipt;
import com.school.fee.model.FeeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeRepository extends JpaRepository<FeeReceipt, Long> {

    Optional<FeeReceipt> findByReceiptNumber(String receiptNumber);

    Page<FeeReceipt> findByStudentId(String studentId, Pageable pageable);

    Optional<FeeReceipt> findByStudentIdAndFeeTypeAndFeeMonthAndAcademicYear(
            String studentId, FeeType feeType, String feeMonth, String academicYear);
}
