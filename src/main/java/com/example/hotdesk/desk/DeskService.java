package com.example.hotdesk.desk;

import com.example.hotdesk.common.service.GenericCrudService;
import com.example.hotdesk.desk.dto.DeskCreateDto;
import com.example.hotdesk.desk.dto.DeskPatchDto;
import com.example.hotdesk.desk.dto.DeskResponseDto;
import com.example.hotdesk.desk.dto.DeskUpdateDto;
import com.example.hotdesk.desk.entity.Desk;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Getter
@Service
@RequiredArgsConstructor
public class DeskService extends GenericCrudService<Desk,Integer, DeskCreateDto, DeskUpdateDto, DeskResponseDto, DeskPatchDto> {
    private final DeskDtoMapper mapper;
    private final DeskRepository repository;
    private final Class<Desk> entityClass = Desk.class;
}
