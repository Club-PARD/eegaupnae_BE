package com.picpick.scan;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScanRepository extends JpaRepository<Scan, Long> {
    @EntityGraph(attributePaths = { "user", "mart", "gemini" })
    List<Scan> findAllByUser_Id(Long userId);

    Optional<Scan> findFirstByScanNameOrderByScannedAtDesc(String scanName);
}
