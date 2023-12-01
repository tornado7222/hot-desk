package com.example.hotdesk.address;

import com.example.hotdesk.address.dto.AddressCreateDto;
import com.example.hotdesk.address.dto.AddressResponseDto;
import com.example.hotdesk.address.dto.AddressUpdateDto;
import com.example.hotdesk.address.entity.Address;
import com.example.hotdesk.common.service.GenericDtoMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressDtoMapper extends GenericDtoMapper<Address, AddressCreateDto, AddressUpdateDto, AddressResponseDto> {
    private final ModelMapper mapper;
    @Override
    public Address toEntity(AddressCreateDto addressCreateDto) {
        return mapper.map(addressCreateDto, Address.class);
    }

    @Override
    public AddressResponseDto toResponseDto(Address address) {
        return mapper.map(address, AddressResponseDto.class);
    }

    @Override
    public void update(AddressUpdateDto addressUpdateDto, Address address) {
        mapper.map(addressUpdateDto, address);
    }
}
