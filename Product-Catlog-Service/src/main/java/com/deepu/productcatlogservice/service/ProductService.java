package com.deepu.productcatlogservice.service;

import com.deepu.productcatlogservice.request.ProductObject;
import com.deepu.productcatlogservice.response.CommonResponse;
import java.rmi.NoSuchObjectException;

public interface ProductService {
    CommonResponse getAllProducts(String category,String name,Double fromPrice,Double toPrice,Boolean isStockAvailable);

    CommonResponse getProductByUniqueId(String uniqueId);

    CommonResponse updateProductByUniqueId(String uniqueId, ProductObject productObject);

    CommonResponse addProduct(ProductObject productObject);

    CommonResponse deleteProductByUniqueId(String uniqueId) throws NoSuchObjectException;
}