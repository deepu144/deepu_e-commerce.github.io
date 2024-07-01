package com.deepu.shoppingcartservice.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductObject {
    private String uniqueId;
    private String name;
    private List<String> productDescriptionImageUrl;
    private String description;
    private String category;
    private Double price;
    private Boolean isStockAvailable;
    private long stock;
}