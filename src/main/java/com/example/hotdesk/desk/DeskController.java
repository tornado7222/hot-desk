package com.example.hotdesk.desk;

import com.example.hotdesk.desk.dto.DeskCreateDto;
import com.example.hotdesk.desk.dto.DeskResponseDto;
import com.example.hotdesk.desk.dto.DeskUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/desk")
@RequiredArgsConstructor
public class DeskController {
    private final DeskService deskService;

    @PostMapping
    public ResponseEntity<?> createDesk(@RequestBody DeskCreateDto deskCreateDto) {
        DeskResponseDto deskResponseDto = deskService.create(deskCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(deskResponseDto);
    }

    @GetMapping
    public ResponseEntity<?> get(Pageable pageable, @RequestParam String predicate) {
        Page<DeskResponseDto> all = deskService.getAll(pageable, predicate);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
        DeskResponseDto deskResponseDto = deskService.getById(id);
        return ResponseEntity.ok(deskResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody DeskUpdateDto dto) {
        DeskResponseDto deskResponseDto = deskService.update(id, dto);
        return ResponseEntity.ok(deskResponseDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        deskService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
