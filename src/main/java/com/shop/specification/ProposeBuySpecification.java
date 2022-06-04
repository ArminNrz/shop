package com.shop.specification;

import com.shop.entity.ProposeBuyStock;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.In;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "status", params = "status.in", spec = In.class),
        @Spec(path = "created", params = {"created.from", "created.to"}, spec = Between.class),
        @Spec(path = "proposeUnitCost", params = {"cost.from", "cost.to"}, spec = Between.class)
})
public interface ProposeBuySpecification extends Specification<ProposeBuyStock> {
}
