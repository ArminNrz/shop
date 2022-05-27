package com.shop.entity;

import com.shop.entity.enumartion.SaleStockStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "sale_stock", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class SaleStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_count", nullable = false)
    private Long stockCount;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

    @Column(name = "sale_stock_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private SaleStockStatus saleStockStatus;

    @Column(name = "created", nullable = false)
    private Long created;

    @Column(name = "updated")
    private Long updated;

    @PrePersist
    public void onPrePersist() {
        setCreated(System.currentTimeMillis());
        setUpdated(System.currentTimeMillis());
    }

    @PreUpdate
    public void onPreUpdated() {
        setUpdated(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SaleStock saleStock = (SaleStock) o;
        return id != null && Objects.equals(id, saleStock.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
