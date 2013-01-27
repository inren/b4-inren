package org.bricket.b4.health.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.bricket.b4.core.wicket.dataprovider.RepositoryDataProvider;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.repository.MeasurementRepository;
import org.bricket.b4.health.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Ingo Renner
 * 
 */
@Service
@Transactional(readOnly = true)
public class MeasurementServiceImpl implements MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Override
    public Measurement saveMeasurement(Measurement measurement) {
	if (measurement.getId() == null) {
	    measurement.setDate(Calendar.getInstance().getTime());
	}
	measurementRepository.save(measurement);
	return measurement;
    }

    @Override
    public List<Measurement> findAll() {
	List<Measurement> res = new ArrayList<Measurement>();
	Iterator<Measurement> iterator = measurementRepository.findAll().iterator();
	while (iterator.hasNext()) {
	    res.add(iterator.next());
	}
	return res;
    }
    
    @Override
    public ISortableDataProvider<Measurement, String> getDataProvider() {
	return new RepositoryDataProvider<Measurement>(measurementRepository);
    };
}
