package com.alterra.capstoneproject.domain.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.alterra.capstoneproject.domain.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "course_takens")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@SQLDelete(sql = "UPDATE course_takens SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class CourseTaken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "certificate_code")
    private String certificateCode;

    @Column(name = "progress")
    @Builder.Default
    private Integer progress = 0;

    @Column(name = "rate")
    @Min(value = 0) @Max(value = 5)
    private Double rate;

    @Column(name = "request_type")
    private RequestEnum requestType;

    @Column(name = "request_detail", columnDefinition = "TEXT")
    private String requestDetail;

    @Column(name = "status")
    @Builder.Default
    private StatusEnum status = StatusEnum.PENDING;

    @Column(name = "taken_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime takenAt;

    @Column(name = "last_access_course")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastAccessCourse;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id", nullable = false)
    @JsonManagedReference
    private Course courseTake;

    @OneToMany(mappedBy = "courseTaken", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Report> reports;

    @JsonIgnore
    @Builder.Default
    private Boolean deleted = Boolean.FALSE;
}
