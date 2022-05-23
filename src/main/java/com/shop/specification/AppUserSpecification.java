package com.shop.specification;

import com.shop.entity.AppUser;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And(
        @Spec(path = "phoneNumber", params = "phoneNumber.equal", spec = Equal.class)
)
public interface AppUserSpecification extends Specification<AppUser> {
}
