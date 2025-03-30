package com.mw.productcomplaintservice.service;

import com.mw.productcomplaintservice.dto.ProductComplaintAddRequest;
import com.mw.productcomplaintservice.dto.ProductComplaintResponse;
import com.mw.productcomplaintservice.dto.ProductComplaintUpdateRequest;
import com.mw.productcomplaintservice.mapper.ProductComplaintMapper;
import com.mw.productcomplaintservice.model.ProductComplaint;
import com.mw.productcomplaintservice.repository.ProductComplaintRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductComplaintService {
    private final ProductComplaintRepository repository;
    private final IpCountryLocationService ipToCountryLocator;

    @Transactional
    public ProductComplaintResponse addComplaint(ProductComplaintAddRequest productComplaint, String remoteAddr) {

        Optional<UUID> existingEntity = repository.findUuidByProductIdAndComplainerName(productComplaint.getProductId(),
                productComplaint.getComplainerName());

        return existingEntity
                .map(this::incrementCounterOnConflict)
                .orElseGet(() -> createNewComplaint(productComplaint, remoteAddr));
    }

    private ProductComplaintResponse incrementCounterOnConflict(UUID uuid) {
        repository.incrementExisting(uuid);
        return ProductComplaintMapper.toResponse(repository.findById(uuid)
                .orElseThrow(EntityNotFoundException::new));
    }

    private ProductComplaintResponse createNewComplaint(ProductComplaintAddRequest productComplaint, String remoteAddr) {
        ProductComplaint entity = ProductComplaintMapper.toEntity(productComplaint);
        String countryFromIp = ipToCountryLocator.getCountryFromIp(remoteAddr);
        entity.setCountry(countryFromIp);
        return ProductComplaintMapper.toResponse(repository.save(entity));
    }

    public List<ProductComplaintResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(ProductComplaintMapper::toResponse)
                .toList();
    }

    @Transactional
    public ProductComplaintResponse updateComplaint(UUID id, ProductComplaintUpdateRequest productComplaintUpdateRequest) {

        return repository.findById(id)
                .map(productComplaint ->
                        updateProductComplaintDescription(productComplaintUpdateRequest, productComplaint))
                .orElseThrow(EntityNotFoundException::new);

    }

    private ProductComplaintResponse updateProductComplaintDescription(ProductComplaintUpdateRequest productComplaintUpdateRequest,
                                                                       ProductComplaint productComplaint) {
        productComplaint.setProductComplaint(productComplaintUpdateRequest.getProductComplaint());
        return ProductComplaintMapper.toResponse(repository.save(productComplaint));
    }
}
