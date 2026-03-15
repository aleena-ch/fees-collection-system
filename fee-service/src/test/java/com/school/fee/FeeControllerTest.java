package com.school.fee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.fee.controller.FeeController;
import com.school.fee.dto.FeeRequestDTO;
import com.school.fee.dto.ReceiptResponseDTO;
import com.school.fee.dto.StudentResponseDTO;
import com.school.fee.exception.DuplicateFeeException;
import com.school.fee.exception.FeeNotFoundException;
import com.school.fee.exception.GlobalExceptionHandler;
import com.school.fee.model.FeeReceipt;
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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FeeControllerTest {

    private static final String
            STUDENT_ID = "STU-2026-001";
    private static final String
            RECEIPT_NUMBER = "RCP-2026-00001";
    @Mock
    private FeeService feeService;
    @InjectMocks
    private FeeController feeController;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private FeeRequestDTO requestDTO;
    private ReceiptResponseDTO responseDTO;
    private StudentResponseDTO studentResponse;
    private FeeReceipt feeReceipt;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(feeController)
                .setControllerAdvice(
                        new GlobalExceptionHandler())
                .setCustomArgumentResolvers(
                        new PageableHandlerMethodArgumentResolver())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        requestDTO = FeeTestDataBuilder.buildFeeRequest(STUDENT_ID);
        studentResponse = FeeTestDataBuilder.buildStudentResponse(STUDENT_ID);
        feeReceipt = FeeTestDataBuilder
                .buildFeeReceipt(
                        STUDENT_ID,
                        requestDTO,
                        studentResponse);
        responseDTO = FeeTestDataBuilder.buildReceiptResponse(feeReceipt);
    }

    @Test
    void collectFee_ShouldReturn201_WhenValid() throws Exception {
        when(feeService.collectFee(any(FeeRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/fees/collect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.receiptNumber")
                        .value(RECEIPT_NUMBER))
                .andExpect(jsonPath("$.studentId")
                        .value(STUDENT_ID));

        verify(feeService, times(1)).collectFee(any(FeeRequestDTO.class));
    }

    @Test
    void collectFee_ShouldReturn400_WhenInvalid() throws Exception {
        FeeRequestDTO invalid = FeeRequestDTO.builder().studentId("").build();

        mockMvc.perform(post("/api/v1/fees/collect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verify(feeService, never()).collectFee(any());
    }

    @Test
    void collectFee_ShouldReturn409_WhenDuplicate() throws Exception {
        when(feeService.collectFee(any(FeeRequestDTO.class)))
                .thenThrow(new DuplicateFeeException("Fee already paid"));

        mockMvc.perform(post("/api/v1/fees/collect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode")
                        .value("DUPLICATE_FEE"));

        verify(feeService, times(1)).collectFee(any());
    }

    @Test
    void getReceipt_ShouldReturn200_WhenExists() throws Exception {
        when(feeService.getReceiptByNumber(RECEIPT_NUMBER)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/fees/receipt/" + RECEIPT_NUMBER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receiptNumber")
                        .value(RECEIPT_NUMBER));

        verify(feeService, times(1)).getReceiptByNumber(RECEIPT_NUMBER);
    }

    @Test
    void getReceipt_ShouldReturn404_WhenNotFound() throws Exception {
        when(feeService.getReceiptByNumber("RCP-9999-99999"))
                .thenThrow(new FeeNotFoundException("Receipt not found: " + "RCP-9999-99999"));

        mockMvc.perform(get("/api/v1/fees/receipt/" + "RCP-9999-99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode")
                        .value("FEE_NOT_FOUND"));

        verify(feeService, times(1)).getReceiptByNumber("RCP-9999-99999");
    }

    @Test
    void getStudentReceipts_ShouldReturn200() throws Exception {
        Page<ReceiptResponseDTO> page = new PageImpl<>(
                List.of(responseDTO),
                PageRequest.of(0, 10),
                1);

        when(feeService.getReceiptsByStudentId(any(), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/fees/receipt/student/" + STUDENT_ID)
                        .param("pageNumber", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(feeService, times(1)).getReceiptsByStudentId(any(), any());
    }

    @Test
    void getStudentReceipts_ShouldReturn200_WhenEmpty() throws Exception {
        Page<ReceiptResponseDTO> emptyPage = new PageImpl<>(List.of(),
                        PageRequest.of(0, 10),
                        0);

        when(feeService.getReceiptsByStudentId(any(), any())).thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/fees/receipt/student/" + STUDENT_ID))
                .andExpect(status().isOk());

        verify(feeService, times(1)).getReceiptsByStudentId(any(), any());
    }
}
