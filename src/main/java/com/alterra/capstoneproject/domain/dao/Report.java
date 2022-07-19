package com.alterra.capstoneproject.domain.dao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.alterra.capstoneproject.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SQLDelete(sql = "UPDATE reports SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Builder.Default
    private Boolean completed = false;

    @Builder.Default
    private Integer score = 10;

    @ManyToOne
    @JoinColumn(name = "course_taken_id", referencedColumnName = "id")
    @JsonBackReference
    private CourseTaken courseTaken;

    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    @JsonManagedReference
    private Section sectionReport;

    @ManyToOne
    @JoinColumn(name = "material_id", referencedColumnName = "id")
    @JsonManagedReference
    private Material material;

    @JsonIgnore
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;
}
