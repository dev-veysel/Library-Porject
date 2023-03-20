package com.library.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull
    @Size(min = 2, max =30)
    private String firstName;

    @NotNull
    @Size(min = 2, max =30)
    private String lastName;

    @NotNull
    @Size(min = 10, max =100)
    private String address;

    @NotNull
    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //1234567890
            message = "Please provide valid phone number")
    private String phone;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
    private LocalDate birthDate;

    @NotNull
    @Size(min = 10, max =80)
    @Email
    private String email;

    @NotNull
    private String password;
}