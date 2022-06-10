package com.shop.entity;

import com.shop.entity.enumartion.AcceptanceSaleStockStatus;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "acceptance_sale_stock", indexes = {
        @Index(name = "id_seller_idx", columnList = "id, seller_id")
})
@Data
public class AcceptanceSaleStock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "propose_buy_stock_id", insertable = false, updatable = false)
    private Long proposeBuyStockId;

    @OneToOne
    @JoinColumn(name = "propose_buy_stock_id", referencedColumnName = "id")
    private ProposeBuyStock proposeBuyStock;

    @Column(name = "seller_id", insertable = false, updatable = false)
    private Long sellerId;

    @OneToOne
    @JoinColumn(name = "seller_id", referencedColumnName = "id")
    private AppUser seller;

    @Column(name = "buyer_id", insertable = false, updatable = false)
    private Long buyerId;

    @OneToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private AppUser buyer;

    @Column(name = "sell_time", nullable = false)
    private Long sellTime;

    @Column(name = "sell_location", nullable = false, length = 2000)
    private String sellLocation;

    @Column(name = "created", nullable = false)
    private Long created;

    @Column(name = "updated", nullable = false)
    private Long updated;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AcceptanceSaleStockStatus status;

    @PrePersist
    public void onPrePersist() {
        setCreated(System.currentTimeMillis());
        setUpdated(System.currentTimeMillis());
    }

    @PreUpdate
    public void onPreUpdated() {
        setUpdated(System.currentTimeMillis());
    }
}
