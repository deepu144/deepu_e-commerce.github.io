package com.deepu.productcatlogservice.repository;

import com.deepu.productcatlogservice.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product,String> , ProductRepositoryCustom{
    Product findByUniqueId(String uniqueId);
    void deleteByUniqueId(String uniqueId);
}