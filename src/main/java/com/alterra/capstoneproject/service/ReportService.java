package com.alterra.capstoneproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.CourseTaken;
import com.alterra.capstoneproject.domain.dao.Material;
import com.alterra.capstoneproject.domain.dao.Report;
import com.alterra.capstoneproject.domain.dao.Section;
import com.alterra.capstoneproject.repository.ReportRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    public void postReport(CourseTaken request) {
        try {
            log.info("Add report {}", request.getCourseTake().getId());
            List<Section> sections = request.getCourseTake().getSections();

            log.info("Get material");
            sections.forEach(getMaterial -> {
                List<Material> materials = getMaterial.getMaterials();
                materials.forEach(inputMaterial -> {
                    Report report = new Report();
                    report.setCourseTaken(request);
                    report.setSectionReport(getMaterial);
                    report.setMaterial(inputMaterial);
                    reportRepository.save(report);
                });
            });
        } catch (Exception e) {
            log.error("Post report error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
