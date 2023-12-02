package com.example.hotdesk.room;


import com.example.hotdesk.room.dto.RoomCreateDto;
import com.example.hotdesk.room.dto.RoomResponseDto;
import com.example.hotdesk.room.dto.RoomUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;
    @PostMapping
    public ResponseEntity<RoomResponseDto> createDesk(@RequestBody @Valid RoomCreateDto roomCreateDto) {
        RoomResponseDto roomResponseDto = roomService.create(roomCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(roomResponseDto);
    }

    @GetMapping
    public ResponseEntity<Page<RoomResponseDto>> get(Pageable pageable, @RequestParam(required = false) String predicate) {
        Page<RoomResponseDto> all = roomService.getAll(pageable, predicate);
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponseDto> getById(@PathVariable("id") Integer id) {
        RoomResponseDto roomResponseDto = roomService.getById(id);
        return ResponseEntity.ok(roomResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> update(@PathVariable("id") Integer id, @RequestBody RoomUpdateDto dto) {
        RoomResponseDto roomResponseDto = roomService.update(id, dto);
        return ResponseEntity.ok(roomResponseDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        roomService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
