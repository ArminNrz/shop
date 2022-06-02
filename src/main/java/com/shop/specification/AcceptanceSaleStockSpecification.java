package com.shop.specification;

import com.shop.entity.AcceptanceSaleStock;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.JoinFetch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@JoinFetch(paths = {"seller"})
@JoinFetch(paths = {"buyer"})
@And({
        @Spec(path = "seller.phoneNumber", params = "sellerPhoneNumber.equals", spec = Equal.class),
        @Spec(path = "buyer.phoneNumber", params = "buyerPhoneNumber.equals", spec = Equal.class),
        @Spec(path = "sellTime", params = {"sellTimeAfter", "sellTimeBefore"}, spec = Between.class),
        @Spec(path = "status", params = "status.in", spec = In.class)
})
public interface AcceptanceSaleStockSpecification extends Specification<AcceptanceSaleStock> {
}
