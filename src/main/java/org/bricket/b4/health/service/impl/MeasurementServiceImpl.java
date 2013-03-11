package org.bricket.b4.health.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.core.service.B4ServiceImpl;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.repository.MeasurementRepository;
import org.bricket.b4.health.service.MeasurementService;
import org.bricket.b4.securityinren.entity.User;
import org.bricket.b4.securityinren.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.inren.frontend.common.dataprovider.RepositoryDataProvider;

/**
 * @author Ingo Renner
 * 
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class MeasurementServiceImpl extends B4ServiceImpl implements MeasurementService {
    @Autowired
    UserService userService;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Override
    protected void onInit() throws B4ServiceException {
        userService.init();
        if (measurementRepository.count() == 0L) {
            List<User> users = userService.loadAllUser();
            for (User u : users) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, -20);
                for (int i = 0; i < 20; i++) {
                    Measurement m = new Measurement();
                    m.setUid(u.getId());
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    m.setDate(cal.getTime());
                    m.setFat(30 + i);
                    m.setWater(40 + i);
                    m.setWeight(60 + i);
                    measurementRepository.save(m);
                }
            }
        }
        log.info("created auto generated measurement.");
    };
    
    @Override
    public Measurement saveMeasurement(Measurement measurement) throws B4ServiceException {
        try {
            if (measurement.getId() == null) {
                measurement.setDate(Calendar.getInstance().getTime());
            }
            measurementRepository.save(measurement);
            return measurement;
        } catch (Exception e) {
            log.error("error saving measurement: " + measurement, e);
            throw new B4ServiceException("MeasurementService", e);
        }
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
    }

}
