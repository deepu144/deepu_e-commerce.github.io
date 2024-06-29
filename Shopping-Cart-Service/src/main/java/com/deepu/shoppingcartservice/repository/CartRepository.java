package com.deepu.shoppingcartservice.repository;

import com.deepu.shoppingcartservice.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<Cart,String> {
    Cart findByUniqueId(String uniqueId);

    List<Cart> findByEmail(String email);
}
