package com.example.hotdesk.office.dto;

import com.example.hotdesk.address.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OfficeCreateDto {
    private String name;
    private Address address;
}
