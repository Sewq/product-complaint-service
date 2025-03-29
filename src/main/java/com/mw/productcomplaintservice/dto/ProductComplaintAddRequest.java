package com.mw.productcomplaintservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductComplaintAddRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String complainerName;

    @NotBlank(message = "Product ID is required")
    @Size(max = 100, message = "Product ID exceed 100 characters")
    private String productId;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "description cannot exceed 500 characters")
    private String productComplaint;
}
