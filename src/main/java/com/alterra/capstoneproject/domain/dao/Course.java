package com.alterra.capstoneproject.domain.dao;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.alterra.capstoneproject.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SQLDelete(sql = "UPDATE courses SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "banner_url")
    private String bannerUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "rate")
    @Builder.Default
    private Double rate = 0.0;
    
    @ElementCollection
    @CollectionTable(name="target_learner")
    @Column(columnDefinition = "TEXT")
    private List<String> targetLearner;

    @ElementCollection
    @CollectionTable(name="objective_learner")
    @Column(columnDefinition = "TEXT")
    private List<String> objectiveLearner;

    @ElementCollection
    @CollectionTable(name="methodology_learnings")
    private List<MethodologyEnum> methodologyLearnings;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Specialization courseSpecialization;

    @OneToMany(mappedBy = "courseSection", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Section> sections;

    @OneToMany(mappedBy = "courseTake", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<CourseTaken> courseTakens;

    @JsonIgnore
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;
}
