package org.bricket.b4.health.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
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
            Random r = new Random();
            for (User u : users) {
                Calendar cal = Calendar.getInstance();
                final int max = 200;
                cal.add(Calendar.DAY_OF_YEAR, -max);
                int fat = 30;
                int water = 40;
                int weight = 120;
                
                for (int i = 0; i < max; i++) {
                    Measurement m = new Measurement();
                    int pm = r.nextBoolean() ? 1 : -1;
                    fat = fat + pm * r.nextInt(2);
                    water = water + pm * r.nextInt(2);
                    weight = weight + pm * r.nextInt(5);
                    m.setUid(u.getId());
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                    m.setDate(new Date(cal.getTime().getTime()));
                    m.setFat(fat);
                    m.setWater(water);
                    m.setWeight(weight);
                    saveMeasurement(m);
                }
            }
        }
        log.info("created auto generated measurement.");
    };
    
    @Override
    public Measurement saveMeasurement(Measurement measurement) throws B4ServiceException {
        try {
            if (measurement.getDate() == null) {
                measurement.setDate(Calendar.getInstance().getTime());
            }
            adjustDeltas(measurement);
            measurementRepository.save(measurement);
            return measurement;
        } catch (Exception e) {
            log.error("error saving measurement: " + measurement, e);
            throw new B4ServiceException("MeasurementService", e);
        }
    }

    private void adjustDeltas(Measurement measurement) {
        // TODO Sorry, this is a quick one, needs a rewrite.
        Order o = new Order("date");
        List<Measurement> all = measurementRepository.findByUid(measurement.getUid(), new Sort(o));
        Measurement prev = null;
        for (Measurement m2 : all) {
            if (m2.getDate().before(measurement.getDate())) {
                prev = m2;
            } else {
                break;
            }
        }
        if (prev!=null) {
            measurement.setWeightDelta(measurement.getWeight() - prev.getWeight());  
            measurement.setFatDelta(measurement.getFat() - prev.getFat());  
            measurement.setWaterDelta(measurement.getWater() - prev.getWater());
        } else {
            measurement.setWeightDelta(0);  
            measurement.setFatDelta(0);  
            measurement.setWaterDelta(0);
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
