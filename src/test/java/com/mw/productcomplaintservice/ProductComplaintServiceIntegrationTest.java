package com.mw.productcomplaintservice;

import com.mw.productcomplaintservice.dto.ProductComplaintAddRequest;
import com.mw.productcomplaintservice.dto.ProductComplaintResponse;
import com.mw.productcomplaintservice.dto.ProductComplaintUpdateRequest;
import com.mw.productcomplaintservice.model.ProductComplaint;
import com.mw.productcomplaintservice.repository.ProductComplaintRepository;
import com.mw.productcomplaintservice.service.IpCountryLocationService;
import com.mw.productcomplaintservice.service.ProductComplaintService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductComplaintServiceIntegrationTest {

    private static final String COUNTRY_POLAND = "Poland";
    private static final String MISSING_PARTS_COMPLAINT = "Product arrived with missing parts";
    private static final String PRODUCT_DAMAGED_COMPLAINT = "Product arrived damaged";
    private static final String PAN_PIOTR = "Pan Piotr";
    private final String IP = "192.168.1.1";

    @Inject
    private ProductComplaintService productComplaintService;
    @Inject
    private ProductComplaintRepository productComplaintRepository;
    @MockitoBean
    private IpCountryLocationService ipToCountryLocator;

    @BeforeEach
    void setUp() {
        productComplaintRepository.deleteAll();

        when(ipToCountryLocator.getCountryFromIp(anyString())).thenReturn(COUNTRY_POLAND);
    }

    @Test
    void addComplaint_shouldCreateNewComplaint() {
        var addComplaintRequest = createAddComplaintRequest();
        ProductComplaintResponse response = productComplaintService.addComplaint(addComplaintRequest, IP);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();

        assertThat(response.getProductId()).isEqualTo(addComplaintRequest.getProductId());
        assertThat(response.getComplainerName()).isEqualTo(addComplaintRequest.getComplainerName());
        assertThat(response.getProductComplaint()).isEqualTo(addComplaintRequest.getProductComplaint());
        assertThat(response.getCountry()).isEqualTo(COUNTRY_POLAND);

        List<ProductComplaint> complaints = productComplaintRepository.findAll();
        assertThat(complaints.size()).isEqualTo(1);
    }

    @Test
    void addComplaint_shouldIncrementCounter_whenAddingSameComplaintTwice() {
        var addComplaintRequest = createAddComplaintRequest();
        submitMultipleComplaintForSameProduct(addComplaintRequest);

        List<ProductComplaint> complaints = productComplaintRepository.findAll();
        assertThat(complaints.size()).isEqualTo(1);
        assertThat(complaints.getFirst().getCounter()).isEqualTo(1);
    }

    private void submitMultipleComplaintForSameProduct(ProductComplaintAddRequest addComplaintRequest) {
        productComplaintService.addComplaint(addComplaintRequest, IP);
        productComplaintService.addComplaint(addComplaintRequest, IP);
    }

    @Test
    void findAll_shouldReturnAllComplaints() {
        var addComplaintRequest = createAddComplaintRequest();
        productComplaintService.addComplaint(addComplaintRequest, IP);

        var secondRequest = createSecondAddComplaintRequest();
        productComplaintService.addComplaint(secondRequest, IP);

        List<ProductComplaintResponse> responses = productComplaintService.findAll();

        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void updateComplaint_shouldUpdateExistingComplaint() {
        var addComplaintRequest = createAddComplaintRequest();
        ProductComplaintResponse productComplaintResponse = productComplaintService.addComplaint(addComplaintRequest, IP);
        assertThat(productComplaintResponse.getProductComplaint()).isEqualTo(addComplaintRequest.getProductComplaint());

        var updateRequest = createUpdateRequest();
        ProductComplaintResponse response = productComplaintService.updateComplaint(updateRequest);

        assertThat(response.getProductComplaint()).isEqualTo(MISSING_PARTS_COMPLAINT);

        Optional<ProductComplaint> updatedEntity = productComplaintRepository
                .findByProductIdAndComplainerName(updateRequest.getProductId(), updateRequest.getComplainerName());

        assertThat(updatedEntity.isPresent()).isTrue();
        assertThat(updatedEntity.get().getCounter()).isEqualTo(0);
        assertThat(updatedEntity.get().getProductComplaint()).isEqualTo(MISSING_PARTS_COMPLAINT);
    }

    @Test
    void updateComplaint_shouldThrowException_whenComplaintDoesNotExist_ShouldThrowException() {
        var nonExistentRequest = createUpdateRequest();

        assertThatThrownBy(() -> productComplaintService.updateComplaint(nonExistentRequest)).isInstanceOf(EntityNotFoundException.class);
    }

    private ProductComplaintAddRequest createAddComplaintRequest() {
        return ProductComplaintAddRequest.builder()
                .productId("456")
                .complainerName(PAN_PIOTR)
                .productComplaint(PRODUCT_DAMAGED_COMPLAINT)
                .build();
    }

    private ProductComplaintAddRequest createSecondAddComplaintRequest() {
        return ProductComplaintAddRequest.builder()
                .productId("12552")
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

}