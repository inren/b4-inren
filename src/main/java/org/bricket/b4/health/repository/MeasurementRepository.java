package org.bricket.b4.health.repository;

import org.bricket.b4.health.entity.Measurement;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ingo Renner
 * 
 */
@Repository
public interface MeasurementRepository extends PagingAndSortingRepository<Measurement, Long> {

}
