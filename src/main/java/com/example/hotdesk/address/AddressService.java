package com.example.hotdesk.address;

import com.example.hotdesk.address.dto.AddressCreateDto;
import com.example.hotdesk.address.dto.AddressPatchDto;
import com.example.hotdesk.address.dto.AddressResponseDto;
import com.example.hotdesk.address.dto.AddressUpdateDto;
import com.example.hotdesk.address.entity.Address;
import com.example.hotdesk.common.service.GenericCrudService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class AddressService extends GenericCrudService<Address, Integer, AddressCreateDto, AddressUpdateDto, AddressResponseDto, AddressPatchDto> {
    private final ModelMapper mapper;
    private final AddressRepository repository;
    private final Class<Address> entityClass = Address.class;

    @Override
    protected Address save(AddressCreateDto entity) {
        return repository.save(mapper.map(entity, Address.class));
    }
}
