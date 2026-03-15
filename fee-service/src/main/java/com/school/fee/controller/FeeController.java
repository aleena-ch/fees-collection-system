package com.school.fee.controller;

import com.school.fee.dto.FeeRequestDTO;
import com.school.fee.dto.ReceiptResponseDTO;
import com.school.fee.service.FeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fees")
@RequiredArgsConstructor
@Slf4j
public class FeeController {

    private final FeeService feeService;

    @PostMapping("/collect")
    public ResponseEntity<ReceiptResponseDTO> collectFee(@Valid @RequestBody FeeRequestDTO feeRequestDto) {
        log.info("Collecting fees for student: {}", feeRequestDto.getStudentId());
        ReceiptResponseDTO response = feeService.collectFee(feeRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/receipt/{receiptNumber}")
    public ResponseEntity<ReceiptResponseDTO> getReceiptByNumber(@PathVariable String receiptNumber) {
        log.info("Getting receipt by number: {}", receiptNumber);
        ReceiptResponseDTO response = feeService.getReceiptByNumber(receiptNumber);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/receipt/student/{studentId}")
    public ResponseEntity<Page<ReceiptResponseDTO>> getReceiptsByStudentId(
            @RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int size, @PathVariable String studentId) {
        log.info("Getting receipts for student Id: {}", studentId);
        Pageable pageable = Pageable.ofSize(size).withPage(pageNumber);
        return ResponseEntity.ok(feeService.getReceiptsByStudentId(studentId, pageable));
    }
}