package com.example.hotdesk.office.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OfficeCreateDto {
    private String name;
    private AddressDto address;
}
