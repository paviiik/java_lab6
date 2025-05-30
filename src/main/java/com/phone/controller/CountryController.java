package com.phone.controller;

import com.phone.dto.CountryDtoRequest;
import com.phone.dto.CountryDtoResponse;
import com.phone.service.CountryService;
import com.phone.service.VisitCounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Страны", description = "API для управления странами")
@RestController
@RequestMapping("/countries")
public class CountryController {

    private final CountryService service;
    private final VisitCounterService visitCounterService;

    public CountryController(CountryService service, VisitCounterService visitCounterService) {
        this.service = service;
        this.visitCounterService = visitCounterService;
    }

    @Operation(summary = "Получить список всех стран")
    @GetMapping
    public List<CountryDtoResponse> getAll() {
        visitCounterService.increment();
        return service.getAll();
    }

    @Operation(summary = "Получить страну по коду")
    @GetMapping("/{code}")
    public CountryDtoResponse getByCode(
            @Parameter(description = "Код страны") @PathVariable String code) {
        visitCounterService.increment();
        return service.getByCode(code);
    }

    @Operation(summary = "Создать новую страну")
    @PostMapping("/save")
    public CountryDtoResponse save(
            @Parameter(description = "Данные новой страны")
            @RequestBody @Valid CountryDtoRequest request) {
        visitCounterService.increment();
        return service.save(request);
    }

    @Operation(summary = "Обновить страну")
    @PutMapping("/{code}")
    public CountryDtoResponse update(
            @Parameter(description = "Код страны") @PathVariable String code,
            @Parameter(description = "Обновленные данные страны")
            @RequestBody @Valid CountryDtoRequest request) {
        visitCounterService.increment();
        return service.update(code, request);
    }

    @Operation(summary = "Удалить страну")
    @DeleteMapping("/{code}")
    public void delete(
            @Parameter(description = "Код страны") @PathVariable String code) {
        visitCounterService.increment();
        service.delete(code);
    }

    @Operation(summary = "Поиск страны по значению (код, имя или телефонный код)")
    @GetMapping("/lookup")
    public CountryDtoResponse lookup(
            @Parameter(description = "Значение для поиска") @RequestParam String value) {
        visitCounterService.increment();
        return service.lookup(value);
    }

    @Operation(summary = "Создать несколько стран",
            description = "Создает список новых стран и возвращает их данные")
    @PostMapping("/saveAll")
    public ResponseEntity<List<CountryDtoResponse>> saveAll(
            @Parameter(description = "Список стран для создания")
            @Valid @RequestBody List<CountryDtoRequest> requests) {
        visitCounterService.increment();
        List<CountryDtoResponse> result = service.saveAll(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
