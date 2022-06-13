package com.alterra.capstoneproject.domain.dao;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "course")
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SQLDelete(sql = "UPDATE course SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "specialization_id", referencedColumnName = "id")
    @JsonManagedReference
    private Specialization specialization;

    @OneToMany(mappedBy = "courseSection")
    @JsonBackReference
    private List<Section> sections;

    @OneToMany(mappedBy = "courseTake")
    @JsonBackReference
    private List<CourseTaken> courseTakens;

    @JsonIgnore
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;
}
