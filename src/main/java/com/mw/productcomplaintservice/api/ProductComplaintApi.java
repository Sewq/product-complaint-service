package com.mw.productcomplaintservice.api;

import com.mw.productcomplaintservice.dto.ProductComplaintAddRequest;
import com.mw.productcomplaintservice.dto.ProductComplaintResponse;
import com.mw.productcomplaintservice.dto.ProductComplaintUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "ProductComplaints", description = "API for managing Product Complaints")
public interface ProductComplaintApi {

    @PutMapping
    @Operation(summary = "Add new complaint for product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added new complaint"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Complaint not found on conflict"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    ResponseEntity<ProductComplaintResponse> addComplaint(@Valid @RequestBody ProductComplaintAddRequest productComplaintAddRequest,
                                                          HttpServletRequest request);

    @PutMapping("/{id}")
    @Operation(summary = "Update existing complaint description for product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated complaint description"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Complaint not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    ResponseEntity<ProductComplaintResponse> updateComplaintDescription(@PathVariable UUID id, @Valid @RequestBody ProductComplaintUpdateRequest productComplaintUpdateRequest);

    @GetMapping
    @Operation(summary = "Get all complaints")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched all complaints"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    ResponseEntity<List<ProductComplaintResponse>> getAllComplains();
}
