package com.phone.controller;

import com.phone.dto.PrefixDtoRequest;
import com.phone.dto.PrefixDtoResponse;
import com.phone.service.PhoneNumberPrefixService;
import com.phone.service.VisitCounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Префиксы", description = "API для управления телефонными префиксами")
@RestController
@RequestMapping("/prefixes")
public class PhoneNumberPrefixController {

    private final PhoneNumberPrefixService service;
    private final VisitCounterService visitCounterService;

    public PhoneNumberPrefixController(PhoneNumberPrefixService service, VisitCounterService visitCounterService) {
        this.service = service;
        this.visitCounterService = visitCounterService;
    }

    @Operation(summary = "Получить префиксы по названию страны")
    @GetMapping("/by-country-name")
    public List<PrefixDtoResponse> getByCountryName(
            @Parameter(description = "Название страны") @RequestParam String name) {
        visitCounterService.increment();
        return service.getByCountryName(name);
    }

    @Operation(summary = "Получить все префиксы")
    @GetMapping
    public List<PrefixDtoResponse> getAll() {

        visitCounterService.increment();
        return service.getAll();
    }

    @Operation(summary = "Получить префиксы по коду страны")
    @GetMapping("/country/{code}")
    public List<PrefixDtoResponse> getByCountry(
            @Parameter(description = "Код страны") @PathVariable String code) {
        visitCounterService.increment();
        return service.getByCountry(code);
    }

    @Operation(summary = "Получить префикс по ID")
    @GetMapping("/{id}")
    public PrefixDtoResponse getById(
            @Parameter(description = "ID префикса") @PathVariable Long id) {
        visitCounterService.increment();
        return service.getById(id);
    }

    @Operation(summary = "Создать новый префикс")
    @PostMapping("/save")
    public PrefixDtoResponse save(
            @Parameter(description = "Данные нового префикса")
            @RequestBody @Valid PrefixDtoRequest request) {
        visitCounterService.increment();
        return service.save(request);
    }

    @Operation(summary = "Обновить префикс")
    @PutMapping("/{id}")
    public PrefixDtoResponse update(
            @Parameter(description = "ID префикса") @PathVariable Long id,
            @Parameter(description = "Обновленные данные префикса")
            @RequestBody @Valid PrefixDtoRequest request) {
        visitCounterService.increment();
        return service.update(id, request);
    }

    @Operation(summary = "Удалить префикс")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID префикса") @PathVariable Long id) {
        visitCounterService.increment();
        service.delete(id);
    }
}
