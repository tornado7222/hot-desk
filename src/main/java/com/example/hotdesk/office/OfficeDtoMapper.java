package com.example.hotdesk.office;

import com.example.hotdesk.common.service.GenericDtoMapper;
import com.example.hotdesk.office.dto.OfficeCreateDto;
import com.example.hotdesk.office.dto.OfficeResponseDto;
import com.example.hotdesk.office.dto.OfficeUpdateDto;
import com.example.hotdesk.office.entity.Office;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfficeDtoMapper extends GenericDtoMapper<Office, OfficeCreateDto, OfficeUpdateDto, OfficeResponseDto> {
    private final ModelMapper mapper;

    @Override
    public Office toEntity(OfficeCreateDto officeCreateDto) {
        return mapper.map(officeCreateDto, Office.class);
    }

    @Override
    public OfficeResponseDto toResponseDto(Office office) {
        return mapper.map(office, OfficeResponseDto.class);
    }

    @Override
    public void update(OfficeUpdateDto officeUpdateDto, Office office) {
        mapper.map(officeUpdateDto, office);
    }
}
