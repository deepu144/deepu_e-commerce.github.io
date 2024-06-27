package com.deepu.productcatlogservice.request;

import com.deepu.productcatlogservice.constant.Constant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductObject {
    private String uniqueId;
    @NotNull(message = Constant.PRODUCT_NAME_NOT_NULL)
    private String name;
    private List<String> productDescriptionImageUrl;
    @NotNull(message = Constant.PRODUCT_DESCRIPTION_NOT_NULL)
    @Size(min = 15,message = Constant.PRODUCT_DESCRIPTION_SIZE)
    private String description;
    @NotNull(message = Constant.PRODUCT_CATEGORY_NOT_NULL)
    private String category;
    @NotNull(message = Constant.PRODUCT_PRICE_NOT_NULL)
    @Min(value = 0,message = Constant.PRODUCT_PRIZE_NON_NEGATIVE)
    private Double price;
    @NotNull(message = Constant.PRODUCT_STOCK_AVAILABILITY_PROVIDED)
    private Boolean isStockAvailable;
    private long stock;
}