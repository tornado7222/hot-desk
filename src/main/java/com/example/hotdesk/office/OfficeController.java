package com.example.hotdesk.office;

import com.example.hotdesk.office.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/office")
public class OfficeController {
    private final OfficeService officeService;
    @PostMapping
    public ResponseEntity<OfficeResponseDto> createDesk(@RequestBody @Valid OfficeCreateDto officeCreateDto) {
        OfficeResponseDto officeResponseDto = officeService.create(officeCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(officeResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<OfficeResponseDto>> get(Pageable pageable, @RequestParam(required = false) String predicate) {
        Page<OfficeResponseDto> all = officeService.getAll(pageable, predicate);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> getById(@PathVariable("id") Integer id) {
        OfficeResponseDto officeResponseDto = officeService.getById(id);
        return ResponseEntity.ok(officeResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfficeResponseDto> update(@PathVariable("id") Integer id, @RequestBody OfficeUpdateDto dto) {
        OfficeResponseDto officeResponseDto = officeService.update(id, dto);
        return ResponseEntity.ok(officeResponseDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        officeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
