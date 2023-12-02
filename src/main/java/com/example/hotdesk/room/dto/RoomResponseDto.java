package com.example.hotdesk.room.dto;

import com.example.hotdesk.room.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomResponseDto {
    private Integer id;
    private String number;
    private RoomType roomType;
    private Integer floorNumber;
    private Integer officeId;
}
