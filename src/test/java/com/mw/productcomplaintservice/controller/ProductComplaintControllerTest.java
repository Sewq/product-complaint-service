package com.mw.productcomplaintservice.controller;

import com.mw.productcomplaintservice.dto.ProductComplaintAddRequest;
import com.mw.productcomplaintservice.dto.ProductComplaintResponse;
import com.mw.productcomplaintservice.service.ProductComplaintService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductComplaintControllerTest {

    @Inject
    private TestRestTemplate restTemplate;

    @MockitoBean
    private ProductComplaintService service;

    private static final String BASE_URL = "/productComplaint";

    @Test
    public void addComplaint_shouldReturnHttpStatus200_whenCorrectDataSent() {
        ProductComplaintAddRequest request = createRequest();
        ProductComplaintResponse response = ProductComplaintResponse.builder().build();

        when(service.addComplaint(any(), anyString())).thenReturn(response);

        ResponseEntity<ProductComplaintResponse> result = restTemplate.exchange(
                BASE_URL, HttpMethod.PUT, new HttpEntity<>(request), ProductComplaintResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void addComplaint_shouldReturnHttpStatus400_whenInvalidDataSent() {
        ProductComplaintAddRequest request = ProductComplaintAddRequest.builder()
                .build();
        ProductComplaintResponse response = ProductComplaintResponse.builder().build();

        when(service.addComplaint(any(), anyString())).thenReturn(response);

        ResponseEntity<ProductComplaintResponse> result = restTemplate.exchange(
                BASE_URL, HttpMethod.PUT, new HttpEntity<>(request), ProductComplaintResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateComplaintDescription_shouldReturnHttpStatus200_whenCorrectDataSent() {
        ProductComplaintAddRequest request = createRequest();

        ProductComplaintResponse response = ProductComplaintResponse.builder().build();

        when(service.updateComplaint(any(), any())).thenReturn(response);

        ResponseEntity<ProductComplaintResponse> result = restTemplate.exchange(
                BASE_URL, HttpMethod.PUT, new HttpEntity<>(request), ProductComplaintResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void updateComplaintDescription_shouldReturnHttpStatus400_whenInvalidDataSent() {
        ProductComplaintAddRequest request = ProductComplaintAddRequest.builder()
                .build();

        ProductComplaintResponse response = ProductComplaintResponse.builder().build();

        when(service.updateComplaint(any(), any())).thenReturn(response);

        ResponseEntity<ProductComplaintResponse> result = restTemplate.exchange(
                BASE_URL, HttpMethod.PUT, new HttpEntity<>(request), ProductComplaintResponse.class);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllComplaints_shouldReturnHttpStatus200_whenCorrectDataSent() {
        when(service.findAll()).thenReturn(List.of());

        ResponseEntity<List<ProductComplaintResponse>> result = restTemplate.getForEntity(
                BASE_URL, null, new ParameterizedTypeReference<List<ProductComplaintResponse>>() {
                });

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private ProductComplaintAddRequest createRequest() {
        return ProductComplaintAddRequest.builder()
                .productComplaint("complaint")
                .productId("id")
                .complainerName("name")
                .build();
    }
}
