package org.iplantc.de.diskResource.client.presenters.callbacks;

import org.iplantc.de.client.models.IsMaskable;
import org.iplantc.de.client.models.errors.diskResources.DiskResourceErrorAutoBeanFactory;
import org.iplantc.de.client.models.errors.diskResources.ErrorDiskResourceMove;
import org.iplantc.de.client.models.services.DiskResourceMove;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * @author jstroot
 */
public class DiskResourceMoveCallback extends DiskResourceServiceCallback<DiskResourceMove> {

    private final DiskResourceCallbackAppearance appearance = GWT.create(DiskResourceCallbackAppearance.class);

    public DiskResourceMoveCallback(final IsMaskable maskedCaller) {
        super(maskedCaller);
    }

    @Override
    public void onSuccess(DiskResourceMove result) {
        unmaskCaller();

        String successMsg = appearance.diskResourceMoveSuccess();
        IplantAnnouncer.getInstance().schedule(new SuccessAnnouncementConfig(successMsg));
    }

    @Override
    public void onFailure(Integer statusCode, Throwable caught){
        unmaskCaller();
        try {
            DiskResourceErrorAutoBeanFactory factory = GWT.create(DiskResourceErrorAutoBeanFactory.class);
            AutoBean<ErrorDiskResourceMove> errorBean =
                    AutoBeanCodex.decode(factory, ErrorDiskResourceMove.class, caught.getMessage());

            ErrorHandler.post(errorBean.as(), caught);
        } catch (Exception e) {
            ErrorHandler.post(caught);
        }
    }

    @Override
    protected String getErrorMessageDefault() {
        return appearance.moveFailed();
    }

}
