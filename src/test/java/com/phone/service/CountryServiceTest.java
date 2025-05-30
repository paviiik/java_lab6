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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CountryServiceTest {

    @InjectMocks
    private CountryService countryService;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private PhoneNumberPrefixRepository prefixRepository;

    @Mock
    private CountryMapper countryMapper;

    @Mock
    private PrefixCache prefixCache;

    @Mock
    private CountryDtoRequest request;

    @Mock
    private CountryDtoResponse response;

    @Mock
    private Country country;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllShouldReturnList() {
        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(countryMapper.toDto(country)).thenReturn(response);

        List<CountryDtoResponse> result = countryService.getAll();

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    @Test
    void saveShouldSaveNewCountry() {
        when(request.getCode()).thenReturn("US");
        when(countryRepository.existsById("US")).thenReturn(false);
        when(countryMapper.toEntity(request)).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(country);
        when(countryMapper.toDto(country)).thenReturn(response);

        CountryDtoResponse result = countryService.save(request);

        assertEquals(response, result);
    }

    @Test
    void saveShouldThrowWhenCountryExists() {
        when(request.getCode()).thenReturn("US");
        when(countryRepository.existsById("US")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> countryService.save(request));
    }

    @Test
    void getByCodeShouldReturnCountry() {
        when(countryRepository.findById("US")).thenReturn(Optional.of(country));
        when(countryMapper.toDto(country)).thenReturn(response);

        CountryDtoResponse result = countryService.getByCode("US");

        assertEquals(response, result);
    }

    @Test
    void getByCodeShouldThrowWhenNotFound() {
        when(countryRepository.findById("US")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> countryService.getByCode("US"));
    }

    @Test
    void updateShouldUpdateCountry() {
        when(countryRepository.findById("US")).thenReturn(Optional.of(country));
        when(countryRepository.save(country)).thenReturn(country);
        when(countryMapper.toDto(country)).thenReturn(response);

        CountryDtoResponse result = countryService.update("US", request);

        assertEquals(response, result);
    }

    @Test
    void updateShouldThrowWhenNotFound() {
        when(countryRepository.findById("US")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> countryService.update("US", request));
    }

    @Test
    void lookupShouldReturnCountryWhenMatchedByCode() {
        when(country.getCode()).thenReturn("DE");
        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(countryMapper.toDto(country)).thenReturn(response);

        CountryDtoResponse result = countryService.lookup("de");

        assertEquals(response, result);
    }

    @Test
    void lookupShouldReturnCountryWhenMatchedByName() {
        when(country.getCode()).thenReturn("ZZ");
        when(country.getName()).thenReturn("Germany");
        when(country.getPhoneCode()).thenReturn("000");
        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(countryMapper.toDto(country)).thenReturn(response);

        CountryDtoResponse result = countryService.lookup("GERMANY");

        assertEquals(response, result);
    }

    @Test
    void lookupShouldReturnCountryWhenMatchedByPhoneCode() {
        when(country.getCode()).thenReturn("ZZ");
        when(country.getName()).thenReturn("Unknown");
        when(country.getPhoneCode()).thenReturn("+49");
        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(countryMapper.toDto(country)).thenReturn(response);

        CountryDtoResponse result = countryService.lookup("+49");

        assertEquals(response, result);
    }

    @Test
    void deleteShouldRemoveCountryAndCache() {
        PhoneNumberPrefix prefix = mock(PhoneNumberPrefix.class);
        when(prefix.getId()).thenReturn(1L);
        when(prefixRepository.findByCountryCode("US")).thenReturn(List.of(prefix));

        countryService.delete("US");

        verify(countryRepository).deleteById("US");
        verify(prefixCache).remove(1L);
    }

    @Test
    void lookupShouldReturnCountryWhenMatched() {
        when(country.getCode()).thenReturn("US");
        when(countryRepository.findAll()).thenReturn(List.of(country));
        when(countryMapper.toDto(country)).thenReturn(response);

        CountryDtoResponse result = countryService.lookup("US");

        assertEquals(response, result);
    }

    @Test
    void lookupShouldReturnNullWhenNoMatch() {
        when(countryRepository.findAll()).thenReturn(Collections.emptyList());

        CountryDtoResponse result = countryService.lookup("ZZ");

        assertNull(result);
    }

    @Test
    void saveAllShouldSaveCountries() {
        List<CountryDtoRequest> requests = List.of(request);

        when(request.getCode()).thenReturn("US");
        when(countryRepository.existsById("US")).thenReturn(false);
        when(countryMapper.toEntity(request)).thenReturn(country);
        when(countryRepository.save(country)).thenReturn(country);
        when(countryMapper.toDto(country)).thenReturn(response);

        List<CountryDtoResponse> results = countryService.saveAll(requests);

        assertEquals(1, results.size());
        assertEquals(response, results.get(0));
    }

    @Test
    void saveAllShouldThrowWhenCountryExists() {
        when(request.getCode()).thenReturn("US");
        when(countryRepository.existsById("US")).thenReturn(true);
        List<CountryDtoRequest> requests = List.of(request);

        assertThrows(IllegalArgumentException.class, () -> countryService.saveAll(requests));
    }
}
