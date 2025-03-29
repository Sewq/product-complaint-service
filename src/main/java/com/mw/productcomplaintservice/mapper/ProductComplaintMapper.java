package com.mw.productcomplaintservice.mapper;

import com.mw.productcomplaintservice.dto.ProductComplaintAddRequest;
import com.mw.productcomplaintservice.dto.ProductComplaintResponse;
import com.mw.productcomplaintservice.model.ProductComplaint;

public class ProductComplaintMapper {

    public static ProductComplaint toEntity(ProductComplaintAddRequest productComplaint) {
        ProductComplaint pc = new ProductComplaint();
        pc.setProductId(productComplaint.getProductId());
        pc.setProductComplaint(productComplaint.getProductComplaint());
        pc.setComplainerName(productComplaint.getComplainerName());
        return pc;
    }

    public static ProductComplaintResponse toResponse(ProductComplaint productComplaint) {
        return ProductComplaintResponse.builder()
                .complainerName(productComplaint.getComplainerName())
                .productId(productComplaint.getProductId())
                .complaintDate(productComplaint.getComplaintDate())
                .country(productComplaint.getCountry())
                .id(productComplaint.getId())
                .productComplaint(productComplaint.getProductComplaint())
                .build();

    }
}
