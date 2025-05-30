package com.phone.dto;

import java.util.List;
import lombok.Data;

@Data
public class CountryDtoResponse {
    private String code;
    private String name;
    private String phoneCode;
    private List<PrefixDtoResponse> prefixes;
}
