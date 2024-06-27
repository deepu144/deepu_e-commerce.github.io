package com.deepu.productcatlogservice.repository.repositoryImpl;

import com.deepu.productcatlogservice.entity.Product;
import com.deepu.productcatlogservice.repository.ProductRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Product> findProductsByCriteria(String name, String category, Double fromPrice, Double toPrice, Boolean isStockAvailable) {
        Query query = new Query();
        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }
        if (category != null && !category.isEmpty()) {
            query.addCriteria(Criteria.where("category").is(category));
        }
        if (fromPrice != null || toPrice != null) {
            Criteria priceCriteria;
            if (fromPrice != null && toPrice != null) {
                priceCriteria = Criteria.where("price").gte(fromPrice).lte(toPrice);
            } else if (fromPrice != null) {
                priceCriteria = Criteria.where("price").gte(fromPrice);
            } else {
                priceCriteria = Criteria.where("price").lte(toPrice);
            }
            query.addCriteria(priceCriteria);
        }
        if (isStockAvailable != null) {
            query.addCriteria(Criteria.where("isStockAvailable").is(isStockAvailable));
        }

        return mongoTemplate.find(query, Product.class);
    }
}