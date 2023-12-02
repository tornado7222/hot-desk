package com.example.hotdesk.office;

import com.example.hotdesk.common.service.GenericCrudService;
import com.example.hotdesk.office.dto.OfficeCreateDto;
import com.example.hotdesk.office.dto.OfficeResponseDto;
import com.example.hotdesk.office.dto.OfficeUpdateDto;
import com.example.hotdesk.office.entity.Office;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
@RequiredArgsConstructor
@Transactional
public class OfficeService extends GenericCrudService<Office, Integer, OfficeCreateDto, OfficeUpdateDto, OfficeResponseDto, OfficeUpdateDto> {
    private final OfficeDtoMapper mapper;
    private final OfficeRepository repository;
    private final Class<Office> entityClass = Office.class;

    @Override
    protected Office save(OfficeCreateDto officeCreateDto) {
        Office entity = mapper.toEntity(officeCreateDto);
        return repository.save(entity);
    }

    @Override
    protected Office updateEntity(OfficeUpdateDto officeUpdateDto, Office office) {
        mapper.update(officeUpdateDto, office);
        return repository.save(office);
    }

}
