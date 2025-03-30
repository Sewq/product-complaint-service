package com.mw.productcomplaintservice.repository;

import com.mw.productcomplaintservice.model.ProductComplaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProductComplaintRepository extends JpaRepository<ProductComplaint, UUID> {

    @Query("SELECT p.id FROM ProductComplaint p WHERE p.productId = :productId AND p.complainerName = :complainerName")
    Optional<UUID> findUuidByProductIdAndComplainerName(String productId, String complainerName);

    @Modifying
    @Query("UPDATE ProductComplaint e SET e.counter = e.counter + 1 WHERE e.id = :id")
    void incrementExisting(UUID id);

}
