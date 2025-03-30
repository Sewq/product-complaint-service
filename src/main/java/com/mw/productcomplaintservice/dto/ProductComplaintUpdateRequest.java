package com.mw.productcomplaintservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductComplaintUpdateRequest {

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "description cannot exceed 500 characters")
    private String productComplaint;
}
