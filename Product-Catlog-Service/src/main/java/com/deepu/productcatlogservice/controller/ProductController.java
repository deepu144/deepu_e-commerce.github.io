package com.deepu.productcatlogservice.controller;

import com.deepu.productcatlogservice.request.ProductObject;
import com.deepu.productcatlogservice.response.CommonResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

public interface ProductController {
    @ApiResponse(description = "Fetch List of all the Product either by Filter or none")
    @GetMapping("")
    ResponseEntity<CommonResponse> getAllProducts(@RequestParam(required = false) String category,
                                                  @RequestParam(required = false) String name ,
                                                  @RequestParam(required = false) Double fromPrice ,
                                                  @RequestParam(required = false) Double toPrice ,
                                                  @RequestParam(required = false) Boolean isStockAvailable);

    @ApiResponse(description = "Fetch Product by uniqueId")
    @GetMapping("/{product-uniqueId}")
    ResponseEntity<CommonResponse> getProductByUniqueId(@PathVariable("product-uniqueId") String uniqueId);

    @ApiResponse(description = "Update Product by uniqueId")
    @PutMapping("/{product-uniqueId}")
    ResponseEntity<CommonResponse> updateProductByUniqueId(@PathVariable("product-uniqueId") String uniqueId, @RequestBody @Valid ProductObject productObject, BindingResult result);

    @ApiResponse(description = "Add New Product")
    @PostMapping("/add")
    ResponseEntity<CommonResponse> addProduct(@RequestBody @Valid ProductObject productObject, BindingResult result);

    @ApiResponse(description = "Delete Product By uniqueId")
    @DeleteMapping("/delete/{product-uniqueId}")
    ResponseEntity<CommonResponse> deleteProductByUniqueId(@PathVariable("product-uniqueId") String uniqueId);
}