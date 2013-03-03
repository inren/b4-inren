package org.bricket.b4.health.service;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.health.entity.Measurement;

/**
 * @author Ingo Renner
 *
 */
public interface MeasurementService extends Serializable {

    Measurement saveMeasurement(Measurement measurement) throws B4ServiceException;

    List<Measurement> findAll();

    ISortableDataProvider<Measurement, String> getDataProvider();
}
