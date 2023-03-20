package com.library.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.library.dto.BookDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoanResponse {

    private Long id;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime loanDate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime expireDate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime returnDate;

    private Long userId;

    private BookDTO book;
}