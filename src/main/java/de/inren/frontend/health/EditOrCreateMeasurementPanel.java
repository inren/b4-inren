/**
 * 
 */
package de.inren.frontend.health;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.bricket.b4.core.service.B4ServiceException;
import org.bricket.b4.health.entity.Measurement;
import org.bricket.b4.health.service.MeasurementService;

import de.inren.frontend.common.manage.IWorktopManageDelegate;
import de.inren.frontend.common.panel.ABasePanel;
import de.inren.frontend.common.panel.IAdminPanel;

/**
 * @author Ingo Renner
 * 
 */
public class EditOrCreateMeasurementPanel extends ABasePanel implements IAdminPanel {

    @SpringBean
    private MeasurementService measurementService;

    private IWorktopManageDelegate<Measurement> delegate;

    public EditOrCreateMeasurementPanel(String id, IModel<Measurement> m, IWorktopManageDelegate<Measurement> delegate) {
        super(id, (m!=null && m.getObject() != null) ? new CompoundPropertyModel<Measurement>(m.getObject()) : new CompoundPropertyModel<Measurement>(new Measurement()));
        this.delegate = delegate;
    }

    @Override
    protected void initGui() {

        Form<Measurement> form = new Form<Measurement>("form");
        form.setDefaultModel(getDefaultModel());

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
                delegate.switchToComponent(target, delegate.getManagePanel());
            }
        });

        form.add(new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                try {
                    Measurement u = measurementService.saveMeasurement((Measurement) form.getModelObject());
                    form.info(new StringResourceModel("feedback.success", EditOrCreateMeasurementPanel.this, null).getString());
                    delegate.switchToComponent(target, delegate.getManagePanel());
                } catch (B4ServiceException e) {
                    form.error(new StringResourceModel(e.getKey(), EditOrCreateMeasurementPanel.this, null).getString());
                    target.add(getFeedback());
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                FeedbackPanel f = getFeedback();
                if (target != null && f != null) {
                    target.add(f);
                }
            }
        });

        add(form);
    }
}
