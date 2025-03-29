package com.mw.productcomplaintservice.service;

import com.mw.productcomplaintservice.dto.ProductComplaintAddRequest;
import com.mw.productcomplaintservice.dto.ProductComplaintResponse;
import com.mw.productcomplaintservice.dto.ProductComplaintUpdateRequest;
import com.mw.productcomplaintservice.model.ProductComplaint;
import com.mw.productcomplaintservice.repository.ProductComplaintRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductComplaintServiceTest {
    private static final String MISSING_PARTS_COMPLAINT = "Product arrived with missing parts";
    private static final String PRODUCT_DAMAGED_COMPLAINT = "Product arrived damaged";
    private static final String PAN_PIOTR = "Pan Piotr";
    private static final String IP_ADDRESS = "192.168.1.1";
    private static final String COUNTRY_POLAND = "Poland";

    @Mock
    private ProductComplaintRepository repository;

    @Mock
    private IpCountryLocationService ipToCountryLocator;

    @InjectMocks
    private ProductComplaintService service;

    @Test
    void addComplaint_shouldCreateNew_whenNoExistingComplaint() {

        var addRequest = createAddComplaintRequest();
        var savedComplaint = buildProductComplaint(UUID.randomUUID());

        when(repository.findUuidByProductIdAndComplainerName(anyString(), anyString())).thenReturn(Optional.empty());
        when(ipToCountryLocator.getCountryFromIp(IP_ADDRESS)).thenReturn(COUNTRY_POLAND);
        when(repository.save(any(ProductComplaint.class))).thenReturn(savedComplaint);

        ProductComplaintResponse response = service.addComplaint(addRequest, IP_ADDRESS);

        assertThat(response).isNotNull();
        assertThat(response.getProductId()).isEqualTo(savedComplaint.getProductId());
        assertThat(response.getComplainerName()).isEqualTo(savedComplaint.getComplainerName());
        assertThat(response.getProductComplaint()).isEqualTo(savedComplaint.getProductComplaint());
        assertThat(response.getCountry()).isEqualTo(savedComplaint.getCountry());

        verify(repository).findUuidByProductIdAndComplainerName(addRequest.getProductId(), addRequest.getComplainerName());
        verify(ipToCountryLocator).getCountryFromIp(IP_ADDRESS);
        verify(repository).save(any(ProductComplaint.class));
    }

    @Test
    void addComplaint_shouldIncrementCounter_whenExistingComplaint() {
        UUID uuid = UUID.randomUUID();

        var addRequest = createAddComplaintRequest();
        var existingComplaint = buildProductComplaint(uuid);

        when(repository.findUuidByProductIdAndComplainerName(anyString(), anyString())).thenReturn(Optional.of(uuid));
        doNothing().when(repository).incrementExisting(uuid);
        when(repository.findById(uuid)).thenReturn(Optional.of(existingComplaint));

        ProductComplaintResponse response = service.addComplaint(addRequest, IP_ADDRESS);

        assertThat(response).isNotNull();

        verify(repository).findUuidByProductIdAndComplainerName(addRequest.getProductId(), addRequest.getComplainerName());
        verify(repository).incrementExisting(uuid);
        verify(repository).findById(uuid);
        verify(repository, never()).save(any(ProductComplaint.class));
    }

    @Test
    void findAll_shouldReturnAllComplaints() {
        UUID uuid = UUID.randomUUID();
        var existingComplaint = buildProductComplaint(uuid);
        List<ProductComplaint> complaints = List.of(existingComplaint);

        when(repository.findAll()).thenReturn(complaints);

        List<ProductComplaintResponse> responses = service.findAll();

        assertThat(responses).isNotNull().hasSize(1);
        assertThat(responses.getFirst().getProductId()).isEqualTo(existingComplaint.getProductId());

        verify(repository).findAll();
    }

    @Test
    void updateComplaint_shouldUpdateDescription_whenComplaintExists() {
        UUID uuid = UUID.randomUUID();
        var updateRequest = createUpdateRequest();
        var existingComplaint = buildProductComplaint(uuid);

        var updatedComplaint = buildProductComplaint(uuid);
        updatedComplaint.setProductComplaint("Updated complaint description");

        when(repository.findByProductIdAndComplainerName(anyString(), anyString())).thenReturn(Optional.of(existingComplaint));
        when(repository.save(any(ProductComplaint.class))).thenReturn(updatedComplaint);

        ProductComplaintResponse response = service.updateComplaint(updateRequest);

        assertThat(response).isNotNull();
        assertThat(response.getProductComplaint()).isEqualTo("Updated complaint description");

        verify(repository).findByProductIdAndComplainerName(updateRequest.getProductId(), updateRequest.getComplainerName());
        verify(repository).save(any(ProductComplaint.class));
    }

    @Test
    void updateComplaint_shouldThrowException_whenComplaintDoesNotExist() {
        var updateRequest = createUpdateRequest();

        when(repository.findByProductIdAndComplainerName(anyString(), anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateComplaint(updateRequest))
                .isInstanceOf(EntityNotFoundException.class);

        verify(repository).findByProductIdAndComplainerName(updateRequest.getProductId(), updateRequest.getComplainerName());
        verify(repository, never()).save(any(ProductComplaint.class));
    }


    private ProductComplaintAddRequest createAddComplaintRequest() {
        return ProductComplaintAddRequest.builder()
                .productId("456")
                .complainerName(PAN_PIOTR)
                .productComplaint(PRODUCT_DAMAGED_COMPLAINT)
                .build();
    }

    private ProductComplaintUpdateRequest createUpdateRequest() {
        return ProductComplaintUpdateRequest.builder()
                .productId("456")
                .complainerName(PAN_PIOTR)
                .productComplaint(MISSING_PARTS_COMPLAINT)
                .build();
    }

    private ProductComplaint buildProductComplaint(UUID id) {
        ProductComplaint complaint = new ProductComplaint();
        complaint.setId(id);
        complaint.setProductId("44123");
        complaint.setComplainerName(PAN_PIOTR);
        complaint.setProductComplaint(PRODUCT_DAMAGED_COMPLAINT);
        complaint.setCountry(COUNTRY_POLAND);
        complaint.setCounter(0);
        return complaint;
    }
}