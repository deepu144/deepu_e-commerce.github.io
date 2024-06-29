package com.deepu.productcatlogservice.util;

import com.deepu.productcatlogservice.entity.Product;
import com.deepu.productcatlogservice.request.ProductObject;

public class Mapper {

    public static ProductObject convertToProductObject(Product product){
        ProductObject productObject = new ProductObject();
        productObject.setUniqueId(product.getUniqueId());
        productObject.setName(product.getName());
        productObject.setDescription(product.getDescription());
        productObject.setCategory(product.getCategory());
        productObject.setPrice(product.getPrice());
        productObject.setProductDescriptionImageUrl(product.getProductDescriptionImageUrl());
        productObject.setStock(product.getStock());
        productObject.setIsStockAvailable(product.isStockAvailable());
        return productObject;
    }

    public static Product convertToProduct(ProductObject productObject){
        Product product = new Product();
        product.setName(productObject.getName());
        product.setDescription(productObject.getDescription());
        product.setCategory(productObject.getCategory());
        product.setPrice(productObject.getPrice());
        product.setProductDescriptionImageUrl(productObject.getProductDescriptionImageUrl());
        product.setStock(productObject.getStock());
        product.setStockAvailable(productObject.getIsStockAvailable());
        return product;
    }
}