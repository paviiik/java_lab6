package com.phone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CountryDtoRequest {
    @NotBlank(message = "Код не должен быть пустым")
    @Size(max = 10, message = "Код слишком длинный")
    private String code;

    @NotBlank(message = "Название не должно быть пустым")
    @Size(max = 50, message = "Название слишком длинное")
    private String name;

    @NotBlank(message = "Телефонный код не должен быть пустым")
    @Size(max = 10, message = "Телефонный код слишком длинный")
    private String phoneCode;
}
