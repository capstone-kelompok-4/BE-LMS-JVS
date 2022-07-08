package com.alterra.capstoneproject.domain.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SQLDelete(sql = "UPDATE addresses SET deleted = true WHERE user_id=?")
@Where(clause = "deleted = false")
public class Address {
    @Id
    @JsonIgnore
    @Column(name = "user_id")
    private Long id;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "country")
    private String country;

    @Column(name = "state_province")
    private String stateProvince;

    @Column(name = "city")
    private String city;

    @Column(name = "zip_code")
    private String zipCode;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User userAddress;

    @JsonIgnore
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;
}
