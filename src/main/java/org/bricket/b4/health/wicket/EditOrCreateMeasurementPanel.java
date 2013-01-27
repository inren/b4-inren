/**
 * 
 */
package org.bricket.b4.health.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.service.MeasurementService;

/**
 * @author Ingo Renner
 *
 */
public class EditOrCreateMeasurementPanel extends Panel {
	
	@SpringBean
	private MeasurementService measurementService;
    
	public EditOrCreateMeasurementPanel(String id, IModel<Measurement> model) {
		super(id, model.getObject() != null 
				? new CompoundPropertyModel<Measurement>(model.getObject())
				: new CompoundPropertyModel<Measurement>(new Measurement()));
	}

    @Override
	protected void onInitialize() {
		super.onInitialize();

        Form<Measurement> form = new Form<Measurement>("form");

        StringResourceModel lDate = new StringResourceModel("date.label", EditOrCreateMeasurementPanel.this, null);
        form.add(new Label("date.label", lDate));
        form.add(new TextField<String>("date", String.class).setRequired(true).setLabel(lDate));

        StringResourceModel lWeight = new StringResourceModel("weight.label", EditOrCreateMeasurementPanel.this, null);
        form.add(new Label("weight.label", lWeight));
        form.add(new TextField<String>("weight", String.class).setRequired(true).setLabel(lWeight));

        StringResourceModel lFat = new StringResourceModel("fat.label", EditOrCreateMeasurementPanel.this, null);
        form.add(new Label("fat.label", lFat));
        form.add(new TextField<String>("fat", String.class).setRequired(true).setLabel(lFat));

        StringResourceModel lWater = new StringResourceModel("water.label", EditOrCreateMeasurementPanel.this, null);
        form.add(new Label("water.label", lWater));
        form.add(new TextField<String>("water", String.class).setRequired(true).setLabel(lWater));

        form.add(new AjaxLink<Void>("cancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                getSession().getFeedbackMessages().clear();
            }
        });

        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                Measurement u = measurementService.saveMeasurement((Measurement) form.getModelObject());
                form.info(new StringResourceModel("feedback.success", EditOrCreateMeasurementPanel.this, null).getString());
            }

        });

        add(form);
    }
	
	
}
