package com.example.hotdesk.room.entity;

import com.example.hotdesk.desk.entity.Desk;
import com.example.hotdesk.office.entity.Office;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String number;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    @Column(nullable = false)
    private Integer floorNumber;
    @OneToMany(mappedBy = "room")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Desk> desks;
    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Office office;
}
