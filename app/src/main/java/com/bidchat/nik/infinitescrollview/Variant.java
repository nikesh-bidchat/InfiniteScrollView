
package com.bidchat.nik.infinitescrollview;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Variant {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("product_id")
    @Expose
    private Long productId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("position")
    @Expose
    private Integer position;
    @SerializedName("grams")
    @Expose
    private Integer grams;
    @SerializedName("inventory_policy")
    @Expose
    private String inventoryPolicy;
    @SerializedName("compare_at_price")
    @Expose
    private Object compareAtPrice;
    @SerializedName("fulfillment_service")
    @Expose
    private String fulfillmentService;
    @SerializedName("inventory_management")
    @Expose
    private Object inventoryManagement;
    @SerializedName("option1")
    @Expose
    private String option1;
    @SerializedName("option2")
    @Expose
    private Object option2;
    @SerializedName("option3")
    @Expose
    private Object option3;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("taxable")
    @Expose
    private Boolean taxable;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("image_id")
    @Expose
    private Long imageId;
    @SerializedName("inventory_quantity")
    @Expose
    private Integer inventoryQuantity;
    @SerializedName("weight")
    @Expose
    private Double weight;
    @SerializedName("weight_unit")
    @Expose
    private String weightUnit;
    @SerializedName("old_inventory_quantity")
    @Expose
    private Integer oldInventoryQuantity;
    @SerializedName("requires_shipping")
    @Expose
    private Boolean requiresShipping;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getGrams() {
        return grams;
    }

    public void setGrams(Integer grams) {
        this.grams = grams;
    }

    public String getInventoryPolicy() {
        return inventoryPolicy;
    }

    public void setInventoryPolicy(String inventoryPolicy) {
        this.inventoryPolicy = inventoryPolicy;
    }

    public Object getCompareAtPrice() {
        return compareAtPrice;
    }

    public void setCompareAtPrice(Object compareAtPrice) {
        this.compareAtPrice = compareAtPrice;
    }

    public String getFulfillmentService() {
        return fulfillmentService;
    }

    public void setFulfillmentService(String fulfillmentService) {
        this.fulfillmentService = fulfillmentService;
    }

    public Object getInventoryManagement() {
        return inventoryManagement;
    }

    public void setInventoryManagement(Object inventoryManagement) {
        this.inventoryManagement = inventoryManagement;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public Object getOption2() {
        return option2;
    }

    public void setOption2(Object option2) {
        this.option2 = option2;
    }

    public Object getOption3() {
        return option3;
    }

    public void setOption3(Object option3) {
        this.option3 = option3;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getTaxable() {
        return taxable;
    }

    public void setTaxable(Boolean taxable) {
        this.taxable = taxable;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Integer getInventoryQuantity() {
        return inventoryQuantity;
    }

    public void setInventoryQuantity(Integer inventoryQuantity) {
        this.inventoryQuantity = inventoryQuantity;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Integer getOldInventoryQuantity() {
        return oldInventoryQuantity;
    }

    public void setOldInventoryQuantity(Integer oldInventoryQuantity) {
        this.oldInventoryQuantity = oldInventoryQuantity;
    }

    public Boolean getRequiresShipping() {
        return requiresShipping;
    }

    public void setRequiresShipping(Boolean requiresShipping) {
        this.requiresShipping = requiresShipping;
    }

}
