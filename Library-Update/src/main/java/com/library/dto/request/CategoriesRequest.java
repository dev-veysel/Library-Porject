package com.library.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesRequest {

    @Size(min = 2, max =80)
    @NotNull
    private String name;

    private Integer sequence;

    private Boolean builtIn;
}