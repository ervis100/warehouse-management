package com.lh.warehouse_management_system.delivery;

import com.lh.warehouse_management_system.order.model.Order;
import com.lh.warehouse_management_system.truck.Truck;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "delivery")
    private Set<Order> orders = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "delivery_truck",
            joinColumns = @JoinColumn(name = "delivery_id"),
            inverseJoinColumns = @JoinColumn(name = "truck_id"))
    private Set<Truck> trucks = new HashSet<>();

    private LocalDate deliveryDate;
}
