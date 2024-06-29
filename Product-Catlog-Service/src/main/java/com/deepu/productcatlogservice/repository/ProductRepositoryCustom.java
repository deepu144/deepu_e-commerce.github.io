package com.deepu.productcatlogservice.repository;

import com.deepu.productcatlogservice.entity.Product;
import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findProductsByCriteria(String name, String category, Double fromPrice, Double toPrice, Boolean isStockAvailable);
}