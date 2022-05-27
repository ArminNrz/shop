package com.shop.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user_stock_manager", indexes = {
        @Index(name = "user_id_idx", columnList = "user_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserStocksManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

    @Column(name = "current", nullable = false)
    private Long current; // current stock = total - forSale

    @Column(name = "for_sale", nullable = false)
    private Long forSale; // stock that want to sale

    @Column(name = "total", nullable = false)
    private Long total; // current + forSale

    @Column(name = "will_buy", nullable = false)
    private Long willBuy; // stock wants to buy in future

    @Column(name = "created", nullable = false)
    private Long created;

    @Column(name = "updated")
    private Long updated;

    @OneToMany(mappedBy = "stocksManager")
    private Set<AppUserStockManagerLog> logs;

    @PrePersist
    public void onPrePersist() {
        setCreated(System.currentTimeMillis());
        setUpdated(System.currentTimeMillis());
    }

    @PreUpdate
    public void onPreUpdate() {
        setUpdated(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AppUserStocksManager that = (AppUserStocksManager) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String toString() {
        return "AppUserStocksManager(id=" + this.getId() + ", current=" + this.getCurrent() + ", forSale=" + this.getForSale() + ", total=" + this.getTotal() + ", willBuy=" + this.getWillBuy() + ")";
    }
}
