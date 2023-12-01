package com.example.hotdesk.address.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressCreateDto {
    private String country;
    private String city;
    private String street;
    private String building;
}
