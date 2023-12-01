package com.example.hotdesk.office.entity;

import com.example.hotdesk.address.entity.Address;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String name;
    @OneToOne(mappedBy = "address")
    private Address address;
}
