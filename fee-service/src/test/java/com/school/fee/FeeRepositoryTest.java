package com.school.fee;

import com.school.fee.dto.FeeRequestDTO;
import com.school.fee.dto.StudentResponseDTO;
import com.school.fee.model.FeeReceipt;
import com.school.fee.model.FeeType;
import com.school.fee.repository.FeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class FeeRepositoryTest {

    private static final String STUDENT_ID = "STU-2026-001";

    @Autowired
    private FeeRepository feeRepository;
    private FeeReceipt feeReceipt;
    private FeeRequestDTO requestDTO;
    private StudentResponseDTO studentResponse;

    @BeforeEach
    void setUp() {
        feeRepository.deleteAll();

        requestDTO = FeeTestDataBuilder.buildFeeRequest(STUDENT_ID);
        studentResponse = FeeTestDataBuilder.buildStudentResponse(STUDENT_ID);
        feeReceipt = FeeTestDataBuilder
                .buildFeeReceipt(STUDENT_ID, requestDTO, studentResponse);
    }

    @Test
    void save_ShouldPersistFeeReceipt() {
        FeeReceipt saved = feeRepository.save(feeReceipt);
        assertNotNull(saved.getId());
        assertEquals("RCP-2026-00001", saved.getReceiptNumber());
        assertEquals(STUDENT_ID, saved.getStudentId());
        assertEquals(studentResponse.getStudentName(), saved.getStudentName());
        assertNotNull(saved.getPaymentDate());
    }

    @Test
    void findByReceiptNumber_ShouldReturn_WhenExists() {
        feeRepository.save(feeReceipt);
        Optional<FeeReceipt> found = feeRepository.findByReceiptNumber("RCP-2026-00001");
        assertTrue(found.isPresent());
        assertEquals("RCP-2026-00001", found.get().getReceiptNumber());
        assertEquals(STUDENT_ID, found.get().getStudentId());
    }

    @Test
    void findByReceiptNumber_ShouldReturnEmpty_WhenNotExists() {
        Optional<FeeReceipt> found = feeRepository.findByReceiptNumber("RCP-9999-99999");
        assertTrue(found.isEmpty());
    }

    @Test
    void findByStudentId_ShouldReturnPage_WhenExists() {
        feeRepository.save(feeReceipt);
        Pageable pageable = PageRequest.of(0, 10);
        Page<FeeReceipt> page = feeRepository.findByStudentId(STUDENT_ID, pageable);
        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        assertEquals(STUDENT_ID, page.getContent().get(0).getStudentId());
    }

    @Test
    void findByStudentId_ShouldReturnEmpty_WhenNoReceipts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<FeeReceipt> page = feeRepository.findByStudentId("STU-9999-999", pageable);
        assertNotNull(page);
        assertEquals(0, page.getTotalElements());
        assertTrue(page.getContent().isEmpty());
    }

    @Test
    void findDuplicate_ShouldReturn_WhenExists() {
        feeRepository.save(feeReceipt);

        Optional<FeeReceipt> found =
                feeRepository.findByStudentIdAndFeeTypeAndFeeMonthAndAcademicYear(
                        STUDENT_ID,
                        FeeType.TUITION,
                        "APRIL",
                        "2026-27");

        assertTrue(found.isPresent());
        assertEquals(STUDENT_ID, found.get().getStudentId());
    }

    @Test
    void findDuplicate_ShouldReturnEmpty_WhenNotExists() {
        Optional<FeeReceipt> found = feeRepository.findByStudentIdAndFeeTypeAndFeeMonthAndAcademicYear(
                STUDENT_ID,
                FeeType.TUITION,
                "MAY",
                "2026-27");
        assertTrue(found.isEmpty());
    }
}
