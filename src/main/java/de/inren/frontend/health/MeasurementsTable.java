package de.inren.frontend.health;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.CSVDataExporter;
import org.apache.wicket.extensions.markup.html.repeater.data.table.export.ExportToolbar;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.repository.MeasurementRepository;
import org.bricket.b4.health.service.MeasurementService;

import de.agilecoders.wicket.markup.html.bootstrap.table.TableBehavior;
import de.inren.frontend.common.dataprovider.RepositoryDataProvider;

/**
 * @author Ingo Renner
 * 
 */
public class MeasurementsTable extends Panel {
    @SpringBean
    private MeasurementRepository measurementRepository;
    
    @SpringBean
    private MeasurementService measurementService;

    public MeasurementsTable(String id) {
	super(id);
    }

    @Override
    protected void onInitialize() {
	super.onInitialize();
	if (!hasBeenRendered()) {
	    initGui();
	}
    }

    private void initGui() {

	List<IColumn<Measurement, String>> columns = new ArrayList<IColumn<Measurement, String>>();

	columns.add(new HealthColumn<Measurement>(new Model<String>("weight"), "weight", "weight"));
	columns.add(new HealthColumn<Measurement>(new Model<String>("fat"), "fat", "fat"));
	columns.add(new HealthColumn<Measurement>(new Model<String>("water"), "water","water"));

	ISortableDataProvider<Measurement, String> dataProvider = new RepositoryDataProvider<Measurement>(measurementRepository);
	
	AjaxFallbackDefaultDataTable<Measurement, String> table = new AjaxFallbackDefaultDataTable<Measurement, String>("datatable", columns, dataProvider, 3);
	table.addBottomToolbar(new ExportToolbar(table).addDataExporter(new CSVDataExporter()));
	table.add(new TableBehavior().bordered().condensed().striped());
	add(table);
    }

}
