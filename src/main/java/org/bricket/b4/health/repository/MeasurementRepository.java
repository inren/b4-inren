package org.bricket.b4.health.repository;

import java.util.List;

import org.bricket.b4.health.entity.Measurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ingo Renner
 * 
 */
@Repository
public interface MeasurementRepository extends PagingAndSortingRepository<Measurement, Long> {
    
    Page<Measurement> findByUid(Long uid, Pageable pageable);

    List<Measurement> findByUid(Long uid);

    List<Measurement> findByUid(Long uid, Sort sort);
}
