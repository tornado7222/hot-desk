package com.example.hotdesk.desk;

import com.example.hotdesk.common.service.GenericCrudService;
import com.example.hotdesk.desk.dto.*;
import com.example.hotdesk.desk.entity.Desk;
import com.example.hotdesk.room.RoomRepository;
import com.example.hotdesk.room.entity.Room;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@RequiredArgsConstructor
public class DeskService extends GenericCrudService<Desk, Integer, DeskCreateDto, DeskUpdateDto, DeskResponseDto, DeskPatchDto> {
    private final DeskDtoMapper mapper;
    private final DeskRepository repository;
    private final Class<Desk> entityClass = Desk.class;
    private final RoomRepository roomRepository;

    @Override
    protected Desk save(DeskCreateDto createDto) {
        Desk entity = mapper.toEntity(createDto);
        Room room = roomRepository.findById(createDto.getRoomId())
                .orElseThrow(() -> throwException(createDto.getRoomId()));
        entity.setRoom(room);
        return repository.save(entity);
    }
}
