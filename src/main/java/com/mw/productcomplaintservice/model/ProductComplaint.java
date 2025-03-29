package com.mw.productcomplaintservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ProductComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private String complainerName;

    @NotNull
    private String productId;

    @NotNull
    private String productComplaint;

    @CreatedDate
    private Instant complaintDate;

    @NotNull
    private String country;

    private int counter;

}
