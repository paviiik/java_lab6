package com.phone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PrefixDtoRequest {
    @NotBlank(message = "Префикс не должен быть пустым")
    @Size(max = 10, message = "Префикс слишком длинный")
    private String prefix;

    @NotBlank(message = "Название региона не должно быть пустым")
    @Size(max = 100, message = "Название региона слишком длинное")
    private String regionName;

    @NotBlank(message = "Код страны не должен быть пустым")
    @Size(max = 10, message = "Код страны слишком длинный")
    private String countryCode;
}
