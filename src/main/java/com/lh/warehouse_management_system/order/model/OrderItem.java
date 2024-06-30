package com.lh.warehouse_management_system.order.model;

import com.lh.warehouse_management_system.inventory.Item;
import com.lh.warehouse_management_system.order.exception.InsufficientItemsException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;


@Entity
@Table(name = "order_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @EmbeddedId
    private OrderItemId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer quantity;

    public void setQuantity(Integer quantity) {
        if (quantity > item.getQuantity()) {
            throw new InsufficientItemsException(item.getName());
        }
        this.quantity = quantity;
    }

    public OrderItem(Order order, Item item, Integer quantity) {
        if (quantity > item.getQuantity()) {
            throw new InsufficientItemsException(item.getName());
        }
        this.order = order;
        this.item = item;
        this.id = new OrderItemId(order.getId(), item.getId());
        this.quantity = quantity;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrderItem orderItem = (OrderItem) o;
        return getId() != null && Objects.equals(getId(), orderItem.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
