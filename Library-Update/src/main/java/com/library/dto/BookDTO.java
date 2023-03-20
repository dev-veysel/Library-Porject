package com.library.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    private Long id;

    private String name;

    private Boolean builtIn;

    private String isbn;

    private Integer pageCount;

    private Integer publishDate;

    private Boolean loanable;

    private String shelfCode;

    private Boolean active;

    private Boolean feature;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDateTime createDate;

    private String authorName;

    private String publisherName;

    private String categoryName;

    private String imageId;
}