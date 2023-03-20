package com.library.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {

    @NotNull
    @Size(min = 2, max =80,message = "name must not be null" )
    private String name;

    private Boolean builtIn;

    @NotNull(message = "isbn must not be null")
    @Pattern(regexp = "^[1-9]\\d{2}-\\d{2}-\\d{5}-\\d{2}-\\d{1}",
            message = "Please provide valid isbn")
    private String isbn;

    private Integer pageCount;

    private Integer publishDate;

    @NotNull(message = "shelfCode must not be null")
    @Pattern(regexp = "^[1-9]\\d{1}-\\d{3}",
            message = "Please provide valid shelf")
    private String shelfCode;

    @NotNull(message = "feature must not be null")
    private Boolean feature;

    @NotNull(message = "authorId must not be null")
    private Long authorId;

    @NotNull(message = "PublisherId must not be null")
    private Long publisherId;

    @NotNull(message = "CategorieId must not be null")
    private Long categorieId;

    private String imageId;
}