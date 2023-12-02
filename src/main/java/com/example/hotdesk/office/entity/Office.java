package com.example.hotdesk.office.entity;

import com.example.hotdesk.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    @OneToMany(mappedBy = "office")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Room> rooms;
    private String name;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne( cascade = CascadeType.ALL)
    private Address address;

}
