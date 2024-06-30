package com.lh.warehouse_management_system.truck;

import com.lh.warehouse_management_system.delivery.Delivery;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 17, max = 17)
    private String chassisNumber;

    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 7)
    private String licensePlate;

    @ManyToMany(mappedBy = "trucks")
    private Set<Delivery> deliveries;
}
