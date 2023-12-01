package com.example.hotdesk.address;

import com.example.hotdesk.address.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;
    @PostMapping
    public ResponseEntity<AddressResponseDto> createDesk(@RequestBody AddressCreateDto addressCreateDto) {
        AddressResponseDto addressResponseDto = addressService.create(addressCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<AddressResponseDto>> get(Pageable pageable, @RequestParam String predicate) {
        Page<AddressResponseDto> all = addressService.getAll(pageable, predicate);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponseDto> getById(@PathVariable("id") Integer id) {
        AddressResponseDto addressResponseDto = addressService.getById(id);
        return ResponseEntity.ok(addressResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressResponseDto> update(@PathVariable("id") Integer id, @RequestBody AddressUpdateDto dto) {
        AddressResponseDto addressResponseDto = addressService.update(id, dto);
        return ResponseEntity.ok(addressResponseDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        addressService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
