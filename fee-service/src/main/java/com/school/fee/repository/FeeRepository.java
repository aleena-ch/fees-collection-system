package com.school.fee.repository;

import com.school.fee.model.FeeReceipt;
import com.school.fee.model.FeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeRepository extends JpaRepository<FeeReceipt, Long> {

    Optional<FeeReceipt> findByReceiptNumber(String receiptNumber);

    List<FeeReceipt> findByStudentId(String studentId);

    Optional<FeeReceipt> findByStudentIdAndFeeTypeAndFeeMonthAndAcademicYear(
            String studentId, FeeType feeType, String feeMonth, String academicYear);
}
