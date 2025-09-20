package com.gov.docmanagement.repo;

import com.gov.docmanagement.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByDeptIdAndStatus(Long departmentId, String status);

    Optional<Document> findByrefnumber(String refNumber);

    @Query("SELECT COUNT(d) FROM Document d WHERE DATE(d.createdDate) = CURRENT_DATE")
    Long countDocumentsUploadedToday();

    @Query("SELECT COUNT(d) FROM Document d WHERE d.createdDate BETWEEN :startOfWeek AND :endOfWeek")
    Long countDocumentsUploadedThisWeek(@Param("startOfWeek") LocalDateTime startOfWeek,
                                        @Param("endOfWeek") LocalDateTime endOfWeek);

    @Query("SELECT COUNT(d) FROM Document d WHERE MONTH(d.createdDate) = MONTH(CURRENT_DATE) AND YEAR(d.createdDate) = YEAR(CURRENT_DATE)")
    Long countDocumentsUploadedThisMonth();

    @Query("SELECT COUNT(d) FROM Document d WHERE d.createdDate BETWEEN :startDate AND :endDate")
    Long countDocumentsUploadedBetween(@Param("startDate") LocalDateTime startDate,
                                       @Param("endDate") LocalDateTime endDate);

    boolean existsByRefnumber(String refnumber);
}
