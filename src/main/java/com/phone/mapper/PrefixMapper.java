package com.phone.mapper;

import com.phone.dto.PrefixDtoRequest;
import com.phone.dto.PrefixDtoResponse;
import com.phone.model.Country;
import com.phone.model.PhoneNumberPrefix;
import com.phone.repository.CountryRepository;
import org.springframework.stereotype.Component;

@Component
public class PrefixMapper {

    private final CountryRepository countryRepository;

    public PrefixMapper(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public PrefixDtoResponse toDto(PhoneNumberPrefix entity) {
        PrefixDtoResponse dto = new PrefixDtoResponse();
        dto.setId(entity.getId());
        dto.setPrefix(entity.getPrefix());
        dto.setRegionName(entity.getRegionName());
        dto.setCountryCode(entity.getCountry().getCode());
        return dto;
    }

    public PhoneNumberPrefix toEntity(PrefixDtoRequest dto) {
        Country country = countryRepository.findById(dto.getCountryCode()).orElse(null);
        return new PhoneNumberPrefix(null, dto.getPrefix(), dto.getRegionName(), country);
    }
}
