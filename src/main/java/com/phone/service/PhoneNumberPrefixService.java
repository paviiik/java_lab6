package com.phone.service;

import com.phone.cache.PrefixCache;
import com.phone.dto.PrefixDtoRequest;
import com.phone.dto.PrefixDtoResponse;
import com.phone.exception.NotFoundException;
import com.phone.mapper.PrefixMapper;
import com.phone.repository.PhoneNumberPrefixRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class PhoneNumberPrefixService {
    private final PhoneNumberPrefixRepository repository;
    private final PrefixMapper mapper;
    private final PrefixCache cache;

    public List<PrefixDtoResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PrefixDtoResponse getById(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Некорректный ID");
        }

        PrefixDtoResponse cached = cache.get(id);
        if (cached != null) {
            return cached;
        }

        return repository.findById(id)
                .map(prefix -> {
                    PrefixDtoResponse dto = mapper.toDto(prefix);
                    cache.put(id, dto);
                    return dto;
                })
                .orElseThrow(() -> new NotFoundException("Prefix with id '" + id + "' not found"));
    }

    public PrefixDtoResponse update(Long id, PrefixDtoRequest request) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Некорректный ID");
        }

        return repository.findById(id)
                .map(existing -> {
                    existing.setPrefix(request.getPrefix());
                    existing.setRegionName(request.getRegionName());
                    PrefixDtoResponse updated = mapper.toDto(repository.save(existing));
                    cache.put(id, updated);
                    return updated;
                })
                .orElseThrow(() -> new NotFoundException("Prefix with id '" + id + "' not found"));
    }

    public void delete(Long id) {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("Некорректный ID");
        }

        if (!repository.existsById(id)) {
            throw new NotFoundException("Prefix with id '" + id + "' not found");
        }
        repository.deleteById(id);
        cache.remove(id);
    }

    public List<PrefixDtoResponse> getByCountry(String countryCode) {
        return repository.findByCountryCode(countryCode)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<PrefixDtoResponse> getByCountryName(String name) {
        return repository.findByCountryName(name)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PrefixDtoResponse save(PrefixDtoRequest request) {
        if (repository.existsByPrefixAndCountryCode(request.getPrefix(),
                request.getCountryCode())) {
            throw new IllegalArgumentException("Префикс '" + request.getPrefix()
                    + "' уже существует для страны '" + request.getCountryCode() + "'");
        }
        return mapper.toDto(repository.save(mapper.toEntity(request)));
    }
}
