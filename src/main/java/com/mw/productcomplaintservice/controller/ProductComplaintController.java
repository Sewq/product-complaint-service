package com.mw.productcomplaintservice.controller;

import com.mw.productcomplaintservice.api.ProductComplaintApi;
import com.mw.productcomplaintservice.dto.ProductComplaintAddRequest;
import com.mw.productcomplaintservice.dto.ProductComplaintResponse;
import com.mw.productcomplaintservice.dto.ProductComplaintUpdateRequest;
import com.mw.productcomplaintservice.service.ProductComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/productComplaint")
@RequiredArgsConstructor
public class ProductComplaintController implements ProductComplaintApi {

    private final ProductComplaintService service;

    @Override
    public ResponseEntity<ProductComplaintResponse> addComplaint(ProductComplaintAddRequest productComplaintRequest,
                                                                 HttpServletRequest httpRequest) {

        ProductComplaintResponse productComplaintResponse = service.addComplaint(productComplaintRequest, httpRequest.getRemoteAddr());
        return ResponseEntity.ok(productComplaintResponse);
    }

    @Override
    public ResponseEntity<ProductComplaintResponse> updateComplaintDescription(ProductComplaintUpdateRequest productComplaintUpdateRequest) {
        return ResponseEntity.ok(service.updateComplaint(productComplaintUpdateRequest));
    }

    public ResponseEntity<List<ProductComplaintResponse>> getAllComplains() {
        return ResponseEntity.ok(service.findAll());
    }

}
