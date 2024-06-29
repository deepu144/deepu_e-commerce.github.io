package com.deepu.productcatlogservice.service.serviceImpl;

import com.deepu.productcatlogservice.constant.Constant;
import com.deepu.productcatlogservice.entity.Product;
import com.deepu.productcatlogservice.enumeration.ResponseStatus;
import com.deepu.productcatlogservice.repository.ProductRepository;
import com.deepu.productcatlogservice.request.ProductObject;
import com.deepu.productcatlogservice.response.CommonResponse;
import com.deepu.productcatlogservice.response.ListResponse;
import com.deepu.productcatlogservice.service.ProductService;
import com.deepu.productcatlogservice.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public CommonResponse getAllProducts(String category,String name,Double fromPrice,Double toPrice,Boolean isStockAvailable) {
        List<Product> products = productRepository.findProductsByCriteria(name,category,fromPrice,toPrice,isStockAvailable);
        List<ProductObject> productObjects = new ArrayList<>();
        for(Product product : products){
            productObjects.add(Mapper.convertToProductObject(product));
        }
        ListResponse listResponse = new ListResponse(productObjects.size(), productObjects);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(listResponse);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.ALL_PRODUCT_FETCH_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse getProductByUniqueId(String uniqueId) {
        Product product = productRepository.findByUniqueId(uniqueId);
        ProductObject productObject = Mapper.convertToProductObject(product);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(productObject);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.PRODUCT_FETCH_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse updateProductByUniqueId(String uniqueId, ProductObject productObject) {
        Product product = productRepository.findByUniqueId(uniqueId);
        product.setName(productObject.getName());
        product.setDescription(productObject.getDescription());
        product.setDescription(productObject.getDescription());
        product.setPrice(productObject.getPrice());
        product.setProductDescriptionImageUrl(productObject.getProductDescriptionImageUrl());
        product.setStock(productObject.getStock());
        product.setStockAvailable(productObject.getIsStockAvailable());
        productRepository.save(product);
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(200);
        commonResponse.setData(productObject);
        commonResponse.setStatus(ResponseStatus.SUCCESS);
        commonResponse.setSuccessMessage(Constant.PRODUCT_UPDATE_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse addProduct(ProductObject productObject) {
        Product product = Mapper.convertToProduct(productObject);
        product.setUniqueId(UUID.randomUUID().toString());
        productRepository.save(product);
        productObject.setUniqueId(product.getUniqueId());
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(201);
        commonResponse.setData(productObject);
        commonResponse.setStatus(ResponseStatus.CREATED);
        commonResponse.setSuccessMessage(Constant.PRODUCT_ADDED_SUCCESS);
        return commonResponse;
    }

    @Override
    public CommonResponse deleteProductByUniqueId(String uniqueId) throws NoSuchObjectException {
        Product product = productRepository.findByUniqueId(uniqueId);
        if(product!=null){
            ProductObject productObject = Mapper.convertToProductObject(product);
            productRepository.deleteByUniqueId(uniqueId);
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(200);
            commonResponse.setData(productObject);
            commonResponse.setStatus(ResponseStatus.SUCCESS);
            commonResponse.setSuccessMessage(Constant.PRODUCT_DELETED_SUCCESS);
            return commonResponse;
        }else{
            throw new NoSuchObjectException(Constant.PRODUCT_NOT_EXIST);
        }
    }
}