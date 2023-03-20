package com.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TotalReportResponse {
   private Long totalBook;
   private Long totalAuthor;
   private Long totalCategory;
   private Long totalPublisher;
   private Long totalLoan;
   private Long totalUser;
   private Long unReturnedBooks;
   private Long expiredBooks;
}
