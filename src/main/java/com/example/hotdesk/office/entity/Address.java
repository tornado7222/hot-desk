package com.example.hotdesk.office.entity;


import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String country;
    private String city;
    private String street;
    private String building;
    @OneToOne(mappedBy = "address")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Office office;
}

