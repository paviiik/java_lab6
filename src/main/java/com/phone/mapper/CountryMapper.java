package com.phone.mapper;

import com.phone.dto.CountryDtoRequest;
import com.phone.dto.CountryDtoResponse;
import com.phone.dto.PrefixDtoResponse;
import com.phone.model.Country;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {

    private final PrefixMapper prefixMapper;

    public CountryMapper(PrefixMapper prefixMapper) {
        this.prefixMapper = prefixMapper;
    }

    public CountryDtoResponse toDto(Country entity) {
        CountryDtoResponse dto = new CountryDtoResponse();
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setPhoneCode(entity.getPhoneCode());

        List<PrefixDtoResponse> prefixDtos = entity.getPrefixes() != null
                ? entity.getPrefixes().stream()
                .map(prefixMapper::toDto)
                .collect(Collectors.toList())
                : List.of();

        dto.setPrefixes(prefixDtos);
        return dto;
    }

    public Country toEntity(CountryDtoRequest dto) {
        return new Country(dto.getCode(), dto.getName(), dto.getPhoneCode(), null);
    }
}
