package com.shop.specification;

import com.shop.entity.AppUserStocksManager;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.JoinFetch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@JoinFetch(paths = {"user"})
@And({
        @Spec(path = "user.id", params = "userId.equals", spec = Equal.class),
        @Spec(path = "user.phoneNumber", params = "phoneNumber.equals", spec = Equal.class),
        @Spec(path = "user.firstName", params = "firstName.Like", spec = Like.class),
        @Spec(path = "user.lastName", params = "lastName.like", spec = Like.class)
})
public interface AppUserStockManagerSpecification extends Specification<AppUserStocksManager> {
}
