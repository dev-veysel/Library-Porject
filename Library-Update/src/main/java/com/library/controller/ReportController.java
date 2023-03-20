package com.library.controller;

import com.library.dto.BookDTO;
import com.library.dto.response.TotalReportResponse;
import com.library.dto.response.UserResponse;
import com.library.service.ReportService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Resource> getUserReport() {
        String fileName = "total.xlsx";
        ByteArrayInputStream bais = reportService.getTotalReport();
        InputStreamResource file = new InputStreamResource(bais);
        return ResponseEntity.ok().
                header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName).
                contentType(MediaType.parseMediaType("application/vmd.ms-excel")).
                body(file);
    }

    @GetMapping("/total")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<TotalReportResponse> getTotalRaport(){
        TotalReportResponse totals=reportService.getTotalRaports();
        return ResponseEntity.ok(totals);
    }


    @GetMapping("/unreturned-books")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Page<BookDTO>> getUnReturnedBooks(@RequestParam(required = false, value = "page", defaultValue = "0") int page,
                                                            @RequestParam(required = false, value = "size", defaultValue = "20") int size,
                                                            @RequestParam(required = false, value = "sort", defaultValue = "id") String prop,
                                                            @RequestParam(required = false, value = "type", defaultValue = "ASC") Sort.Direction type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));
        Page<BookDTO> bookDTOS=reportService.getUnReturnedBooks(pageable);
        return ResponseEntity.ok(bookDTOS);
    }


    @GetMapping("/expired-books")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Page<BookDTO>> getExpiredBooks(@RequestParam(required = false, value = "page", defaultValue = "0") int page,
                                                         @RequestParam(required = false, value = "size", defaultValue = "20") int size,
                                                         @RequestParam(required = false, value = "sort", defaultValue = "id") String prop,
                                                         @RequestParam(required = false, value = "type", defaultValue = "ASC") Sort.Direction type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));
        Page<BookDTO> bookDTOS=reportService.getExpiredBooks(pageable);
        return ResponseEntity.ok(bookDTOS);
    }


    @GetMapping("/most-borrowers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF')")
    public ResponseEntity<Page<UserResponse>> getMostBorrowers(@RequestParam(required = false,value="page",defaultValue ="0") int page,
                                                               @RequestParam(required = false,value="size",defaultValue ="20") int size,
                                                               @RequestParam(required = false,value="sort",defaultValue ="bookCount") String prop,
                                                               @RequestParam(required = false,value="type",defaultValue ="DESC") Sort.Direction type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(type, prop));
        Page<UserResponse> users = reportService.getMostBorrowers(pageable);
        return ResponseEntity.ok(users);
    }
}