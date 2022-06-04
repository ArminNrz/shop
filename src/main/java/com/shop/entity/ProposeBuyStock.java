package com.shop.entity;

import com.shop.entity.enumartion.ProposeBuyStockStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "propose_buy_stock", indexes = {
        @Index(name = "sale_stock_idx", columnList = "sale_stock_id")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProposeBuyStock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private AppUser user;

    @Column(name = "sale_stock_id", insertable = false, updatable = false)
    private Long saleStockId;

    @ManyToOne()
    @JoinColumn(name = "sale_stock_id")
    private SaleStock saleStock;

    @Column(name = "propose_count", nullable = false)
    private Long proposeCount;

    @Column(name = "propose_unit_cost", nullable = false)
    private BigDecimal proposeUnitCost;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProposeBuyStockStatus status;

    @OneToOne(mappedBy = "proposeBuyStock")
    private AcceptanceSaleStock acceptanceSaleStock;

    @Column(name = "created", nullable = false)
    private Long created;

    @PrePersist
    public void onPrePersist() {
        setCreated(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProposeBuyStock that = (ProposeBuyStock) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
