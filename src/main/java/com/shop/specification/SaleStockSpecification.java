package com.shop.specification;

import com.shop.entity.SaleStock;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.JoinFetch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@JoinFetch(paths = {"user"})
@And({
        @Spec(path = "user.phoneNumber", params = "userPhoneNumber.equals", spec = Equal.class),
        @Spec(path = "user.firstName", params = "firstName.equals", spec = Like.class),
        @Spec(path = "user.lastName", params = "lastName.equals", spec = Like.class),
        @Spec(path = "id", params = "id.equals", spec = Equal.class),
        @Spec(path = "saleStockStatus", params = "status.in", spec = In.class),
        @Spec(path = "unitPrice", params = "unitPrice.graterThan", spec = GreaterThanOrEqual.class),
        @Spec(path = "unitPrice", params = "unitPrice.lessThan", spec = LessThanOrEqual.class),
        @Spec(path = "created", params = {"created.from", "created.to"}, spec = Between.class)
})
public interface SaleStockSpecification extends Specification<SaleStock> {
}
