package com.phone.service;

import com.phone.cache.PrefixCache;
import com.phone.dto.PrefixDtoRequest;
import com.phone.dto.PrefixDtoResponse;
import com.phone.exception.NotFoundException;
import com.phone.mapper.PrefixMapper;
import com.phone.model.PhoneNumberPrefix;
import com.phone.repository.PhoneNumberPrefixRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PhoneNumberPrefixServiceTest {

    @InjectMocks
    private PhoneNumberPrefixService service;

    @Mock
    private PhoneNumberPrefixRepository repository;

    @Mock
    private PrefixMapper mapper;

    @Mock
    private PrefixCache cache;

    @Mock
    private PrefixDtoRequest request;

    @Mock
    private PrefixDtoResponse response;

    @Mock
    private PhoneNumberPrefix entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(response);

        List<PrefixDtoResponse> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    @Test
    void getByIdShouldReturnFromCache() {
        when(cache.get(1L)).thenReturn(response);

        PrefixDtoResponse result = service.getById(1L);

        assertEquals(response, result);
        verify(repository, never()).findById(any());
    }

    @Test
    void getByIdShouldReturnFromRepoAndCacheIt() {
        when(cache.get(1L)).thenReturn(null);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(response);

        PrefixDtoResponse result = service.getById(1L);

        assertEquals(response, result);
        verify(cache).put(1L, response);
    }

    @Test
    void getByIdShouldThrowIfInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> service.getById(-1L));
    }

    @Test
    void getByIdShouldThrowIfNotFound() {
        when(cache.get(1L)).thenReturn(null);
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(1L));
    }

    @Test
    void updateShouldUpdateEntity() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(response);

        PrefixDtoResponse result = service.update(1L, request);

        assertEquals(response, result);
        verify(cache).put(1L, response);
    }

    @Test
    void updateShouldThrowIfInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> service.update(-1L, request));
    }

    @Test
    void updateShouldThrowIfNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.update(1L, request));
    }

    @Test
    void deleteShouldDeleteAndRemoveFromCache() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository).deleteById(1L);
        verify(cache).remove(1L);
    }

    @Test
    void deleteShouldThrowIfInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> service.delete(-1L));
    }

    @Test
    void deleteShouldThrowIfNotFound() {
        when(repository.existsById(1L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> service.delete(1L));
    }

    @Test
    void getByCountryShouldReturnList() {
        when(repository.findByCountryCode("RU")).thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(response);

        List<PrefixDtoResponse> result = service.getByCountry("RU");

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    @Test
    void getByCountryNameShouldReturnList() {
        when(repository.findByCountryName("Russia")).thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(response);

        List<PrefixDtoResponse> result = service.getByCountryName("Russia");

        assertEquals(1, result.size());
        assertEquals(response, result.get(0));
    }

    @Test
    void saveShouldSaveAndReturn() {
        when(request.getPrefix()).thenReturn("495");
        when(request.getCountryCode()).thenReturn("RU");
        when(repository.existsByPrefixAndCountryCode("495", "RU")).thenReturn(false);
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(response);

        PrefixDtoResponse result = service.save(request);

        assertEquals(response, result);
    }

    @Test
    void saveShouldThrowIfExists() {
        when(request.getPrefix()).thenReturn("495");
        when(request.getCountryCode()).thenReturn("RU");
        when(repository.existsByPrefixAndCountryCode("495", "RU")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.save(request));
    }
}
