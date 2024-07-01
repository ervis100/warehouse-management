package com.lh.warehouse_management_system.order.model;

import com.lh.warehouse_management_system.common.model.JpaAuditable;
import com.lh.warehouse_management_system.delivery.Delivery;
import com.lh.warehouse_management_system.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends JpaAuditable<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_details_id", referencedColumnName = "id")
    private OrderDetails orderDetails;

    private LocalDate submittedDate;

    private LocalDate deadLine;

}
