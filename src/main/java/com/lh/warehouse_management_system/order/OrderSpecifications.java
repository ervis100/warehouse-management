package com.lh.warehouse_management_system.order;

import com.lh.warehouse_management_system.order.model.Order;
import com.lh.warehouse_management_system.order.model.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecifications {
    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Order> hasCustomerId(Long customerId) {
        return (root, query, criteriaBuilder) ->
                customerId == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("createdBy"), customerId);
    }

    public static Specification<Order> orderBySubmittedDateDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("submittedDate")));
            return criteriaBuilder.conjunction();
        };
    }
}
