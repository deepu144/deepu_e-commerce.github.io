package com.deepu.productcatlogservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document("product")
public class Product {
    @Id
    private String id;
    private String uniqueId;
    private String name;
    private List<String> productDescriptionImageUrl;
    private String description;
    private String category;
    private double price;
    private boolean isStockAvailable;
    private long stock;
}