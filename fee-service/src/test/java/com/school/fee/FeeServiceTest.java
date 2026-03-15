package com.school.fee;

import com.school.fee.client.StudentClient;
import com.school.fee.dto.FeeRequestDTO;
import com.school.fee.dto.ReceiptResponseDTO;
import com.school.fee.dto.StudentResponseDTO;
import com.school.fee.exception.DuplicateFeeException;
import com.school.fee.exception.FeeNotFoundException;
import com.school.fee.model.FeeReceipt;
import com.school.fee.repository.FeeRepository;
import com.school.fee.service.FeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeeServiceTest {

    private static final String
            STUDENT_ID = "STU-2026-001";
    @Mock
    private FeeRepository feeRepository;
    @Mock
    private StudentClient studentClient;
    @InjectMocks
    private FeeService feeService;
    private FeeRequestDTO requestDTO;
    private StudentResponseDTO studentResponse;
    private FeeReceipt feeReceipt;
    private FeeReceipt feeReceiptWithId;

    @BeforeEach
    void setUp() {
        requestDTO = FeeTestDataBuilder.buildFeeRequest(STUDENT_ID);
        studentResponse = FeeTestDataBuilder.buildStudentResponse(STUDENT_ID);
        feeReceipt = FeeTestDataBuilder.buildFeeReceipt(STUDENT_ID, requestDTO, studentResponse);
        feeReceiptWithId = FeeTestDataBuilder.buildFeeReceiptWithId(STUDENT_ID, requestDTO, studentResponse);
    }

    @Test
    void collectFee_ShouldReturnReceipt_WhenValid() {

        when(studentClient.getStudent(STUDENT_ID)).thenReturn(studentResponse);
        when(feeRepository.findByStudentIdAndFeeTypeAndFeeMonthAndAcademicYear(any(), any(), any(), any()))
                .thenReturn(Optional.empty());

        when(feeRepository.save(any(FeeReceipt.class)))
                .thenReturn(feeReceiptWithId)
                .thenReturn(feeReceipt);

        ReceiptResponseDTO response = feeService.collectFee(requestDTO);

        assertNotNull(response);
        assertEquals("RCP-2026-00001", response.getReceiptNumber());
        assertEquals(STUDENT_ID, response.getStudentId());
        verify(feeRepository, times(2)).save(any(FeeReceipt.class));
    }

    @Test
    void collectFee_ShouldThrowException_WhenDuplicate() {
        when(studentClient.getStudent(STUDENT_ID)).thenReturn(studentResponse);
        when(feeRepository
                .findByStudentIdAndFeeTypeAndFeeMonthAndAcademicYear(any(), any(), any(), any()))
                .thenReturn(Optional.of(feeReceipt));

        DuplicateFeeException exception = assertThrows(DuplicateFeeException.class,
                () -> feeService.collectFee(requestDTO));

        assertTrue(exception.getMessage().contains("Fee already paid"));
        verify(feeRepository, never()).save(any());
    }

    @Test
    void getReceiptByNumber_ShouldReturn_WhenExists() {
        when(feeRepository.findByReceiptNumber("RCP-2026-00001")).thenReturn(Optional.of(feeReceipt));

        ReceiptResponseDTO response = feeService.getReceiptByNumber("RCP-2026-00001");

        assertNotNull(response);
        assertEquals("RCP-2026-00001", response.getReceiptNumber());
        verify(feeRepository, times(1)).findByReceiptNumber("RCP-2026-00001");
    }

    @Test
    void getReceiptByNumber_ShouldThrow_WhenNotFound() {

        when(feeRepository.findByReceiptNumber("RCP-9999-99999")).thenReturn(Optional.empty());

        FeeNotFoundException exception =
                assertThrows(FeeNotFoundException.class,
                        () -> feeService.getReceiptByNumber("RCP-9999-99999"));

        assertTrue(exception.getMessage()
                .contains("Receipt not found"));
    }

    // ─── getReceiptsByStudentId Tests ─────

    @Test
    void getReceiptsByStudentId_ShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<FeeReceipt> page = new PageImpl<>(List.of(feeReceipt));
        when(feeRepository.findByStudentId(STUDENT_ID, pageable)).thenReturn(page);

        Page<ReceiptResponseDTO> response = feeService.getReceiptsByStudentId(STUDENT_ID, pageable);

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals(STUDENT_ID, response.getContent().get(0).getStudentId());
    }

    @Test
    void getReceiptsByStudentId_ShouldReturnEmpty() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<FeeReceipt> emptyPage = new PageImpl<>(List.of());
        when(feeRepository.findByStudentId(STUDENT_ID, pageable)).thenReturn(emptyPage);

        Page<ReceiptResponseDTO> response = feeService.getReceiptsByStudentId(STUDENT_ID, pageable);

        assertNotNull(response);
        assertEquals(0, response.getTotalElements());
        assertTrue(response.getContent().isEmpty());
    }
}
