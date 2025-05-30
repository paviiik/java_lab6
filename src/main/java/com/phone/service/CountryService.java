package com.phone.service;

import com.phone.cache.PrefixCache;
import com.phone.dto.CountryDtoRequest;
import com.phone.dto.CountryDtoResponse;
import com.phone.exception.NotFoundException;
import com.phone.mapper.CountryMapper;
import com.phone.model.Country;
import com.phone.model.PhoneNumberPrefix;
import com.phone.repository.CountryRepository;
import com.phone.repository.PhoneNumberPrefixRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class CountryService {

    private final CountryRepository repository;
    private final CountryMapper mapper;
    private PhoneNumberPrefixRepository prefixRepository;
    private PrefixCache cache;

    public List<CountryDtoResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    public CountryDtoResponse save(CountryDtoRequest request) {
        if (repository.existsById(request.getCode())) {
            throw new IllegalArgumentException("Страна с кодом '"
                    + request.getCode() + "' уже существует");
        }
        Country saved = repository.save(mapper.toEntity(request));
        return mapper.toDto(saved);
    }

    public CountryDtoResponse getByCode(String code) {
        return repository.findById(code)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("Country with code '"
                        + code + "' not found"));
    }

    public CountryDtoResponse update(String code, CountryDtoRequest request) {
        return repository.findById(code)
                .map(existing -> {
                    existing.setName(request.getName());
                    existing.setPhoneCode(request.getPhoneCode());
                    return mapper.toDto(repository.save(existing));
                })
                .orElseThrow(() -> new NotFoundException("Country with code '"
                        + code + "' not found"));
    }

    public void delete(String code) {
        List<PhoneNumberPrefix> prefixes = prefixRepository.findByCountryCode(code);
        repository.deleteById(code);
        for (PhoneNumberPrefix prefix : prefixes) {
            cache.remove(prefix.getId());
        }
    }

    public CountryDtoResponse lookup(String value) {
        return repository.findAll().stream()
                .filter(c -> c.getCode().equalsIgnoreCase(value)
                        || c.getName().equalsIgnoreCase(value)
                        || c.getPhoneCode().equals(value))
                .map(mapper::toDto)
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public List<CountryDtoResponse> saveAll(List<CountryDtoRequest> requests) {
        return requests.stream()
                .map(request -> {
                    if (repository.existsById(request.getCode())) {
                        throw new IllegalArgumentException(
                                "Страна с кодом '" + request.getCode() + "' уже существует");
                    }
                    Country entity = mapper.toEntity(request);
                    Country saved = repository.save(entity);
                    return mapper.toDto(saved);
                })
                .toList();
    }
}
