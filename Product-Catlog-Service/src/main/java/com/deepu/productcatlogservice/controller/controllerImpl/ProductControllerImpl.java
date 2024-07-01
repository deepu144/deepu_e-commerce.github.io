package com.deepu.productcatlogservice.controller.controllerImpl;

import com.deepu.productcatlogservice.constant.Constant;
import com.deepu.productcatlogservice.controller.ProductController;
import com.deepu.productcatlogservice.enumeration.ResponseStatus;
import com.deepu.productcatlogservice.request.ProductObject;
import com.deepu.productcatlogservice.response.CommonResponse;
import com.deepu.productcatlogservice.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequestMapping("/product")
public class ProductControllerImpl implements ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductControllerImpl.class);

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Override
    @GetMapping("/")
    public ResponseEntity<CommonResponse> getAllProducts(@RequestParam(required = false) String category,
                                                         @RequestParam(required = false) String name ,
                                                         @RequestParam(required = false) Double fromPrice ,
                                                         @RequestParam(required = false) Double toPrice ,
                                                         @RequestParam(required = false) Boolean isStockAvailable){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts(category,name,fromPrice,toPrice,isStockAvailable));
        }catch (Exception e){
            log.error("** getAllProducts: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    @Override
    @GetMapping("/{product-uniqueId}")
    public ResponseEntity<CommonResponse> getProductByUniqueId(@PathVariable("product-uniqueId") String uniqueId) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productService.getProductByUniqueId(uniqueId));
        }catch (Exception e){
            log.error("** getProductByUniqueId: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @PutMapping("/{product-uniqueId}")
    public ResponseEntity<CommonResponse> updateProductByUniqueId(@PathVariable("product-uniqueId") String uniqueId,
                                                                  @RequestBody @Valid ProductObject productObject,
                                                                  BindingResult result){
        if(result.hasErrors()){
            log.error("** updateProductByUniqueId: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setData(null);
            commonResponse.setErrorMessage(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productService.updateProductByUniqueId(uniqueId,productObject));
        }catch (Exception e){
            log.error("** updateProductByUniqueId: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @PostMapping("/add")
    public ResponseEntity<CommonResponse> addProduct(@RequestBody @Valid ProductObject productObject,
                                                     BindingResult result){
        if(result.hasErrors()){
            log.error("** addProduct: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            CommonResponse commonResponse = new CommonResponse();
            commonResponse.setCode(400);
            commonResponse.setStatus(ResponseStatus.FAILED);
            commonResponse.setData(null);
            commonResponse.setErrorMessage(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponse);
        }
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productService.addProduct(productObject));
        }catch (Exception e){
            log.error("** addProduct: {}", Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    @DeleteMapping("/delete/{product-uniqueId}")
    public ResponseEntity<CommonResponse> deleteProductByUniqueId(@PathVariable("product-uniqueId") String uniqueId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(productService.deleteProductByUniqueId(uniqueId));
        }catch (Exception e){
            log.error("** deleteProductByUniqueId: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(setServerError(e));
        }
    }

    public CommonResponse setServerError(Exception e){
        CommonResponse commonResponse = new CommonResponse();
        commonResponse.setCode(500);
        commonResponse.setStatus(ResponseStatus.FAILED);
        commonResponse.setData(e.getMessage());
        commonResponse.setErrorMessage(Constant.SERVER_ERROR_MESSAGE);
        return commonResponse;
    }
}