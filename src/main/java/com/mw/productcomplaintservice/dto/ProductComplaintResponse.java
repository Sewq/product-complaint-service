package com.mw.productcomplaintservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
public class ProductComplaintResponse {
    private UUID id;

    private String complainerName;

    private String productId;

    private String productComplaint;

    private Instant complaintDate;

    private String country;

    private int counter;

}
