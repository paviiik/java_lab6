package com.phone.dto;

import lombok.Data;

@Data
public class PrefixDtoResponse {
    private Long id;
    private String prefix;
    private String regionName;
    private String countryCode;
}