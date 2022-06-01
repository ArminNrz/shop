package com.shop.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_stock_manager_log", indexes = {
        @Index(name = "stock_manager_idx", columnList = "stock_manager_id")
})
@Getter
@Setter
@RequiredArgsConstructor
public class AppUserStockManagerLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_manager_id")
    private AppUserStocksManager stocksManager;

    @Column(name = "created", nullable = false)
    private Long created;

    @Column(name = "last_current")
    private Long lastCurrent;

    @Column(name = "last_for_sale")
    private Long lastForSale;

    @Column(name = "last_total")
    private Long lastTotal;

    @Column(name = "last_will_buy")
    private Long lastWillBuy;

    @Column(name = "description")
    private String description;

    @Column(name = "modifier")
    private Long modifier;

    @PrePersist
    public void onPrePersist() {
        setCreated(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUserStockManagerLog that = (AppUserStockManagerLog) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String toString() {
        return "AppUserStockManagerLog(id=" + this.getId() + ", created=" + this.getCreated() + ", lastCurrent=" + this.getLastCurrent() + ", lastForSale=" + this.getLastForSale() + ", lastTotal=" + this.getLastTotal() + ", lastWillBuy=" + this.getLastWillBuy() + ")";
    }
}
