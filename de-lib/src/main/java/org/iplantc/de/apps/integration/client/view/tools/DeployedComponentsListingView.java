/**
 * 
 */
package org.iplantc.de.apps.integration.client.view.tools;

import org.iplantc.de.client.models.tool.Tool;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

import java.util.List;

/**
 * @author sriram
 *
 */
public interface DeployedComponentsListingView extends IsWidget,
                                                       SelectionChangedEvent.HasSelectionChangedHandlers<Tool> {
    public interface Presenter extends org.iplantc.de.commons.client.presenter.Presenter,
                                       SelectionChangedEvent.HasSelectionChangedHandlers<Tool> {

        Tool getSelectedDC();
    }

    interface DeployedComponentsListingViewAppearance {

        String nameColumnHeader();

        String versionColumnHeader();

        String pathColumnHeader();

        String attributionLabel();

        String descriptionLabel();

        String loadingMask();

        String searchEmptyText();

        SafeHtml detailsRenderer();

        String newToolReq();

        ImageResource add();
    }

    public Tool getSelectedDC();

    public void loadDC(List<Tool> list);

    public void mask();

    public void showInfo(Tool dc);

    public void unmask();

}
