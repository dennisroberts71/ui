package org.iplantc.de.admin.desktop.client.toolAdmin.service.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PATCH;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.admin.desktop.client.toolAdmin.service.ToolAdminServiceFacade;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.models.tool.ToolAutoBeanFactory;
import org.iplantc.de.client.models.tool.ToolList;
import org.iplantc.de.client.models.tool.ToolType;
import org.iplantc.de.client.models.tool.ToolTypeList;
import org.iplantc.de.client.services.converters.AsyncCallbackConverter;
import org.iplantc.de.client.services.converters.StringToVoidCallbackConverter;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.List;

/**
 * @author aramsey
 */
public class ToolAdminServiceFacadeImpl implements ToolAdminServiceFacade {

    private final String TOOLS = "org.iplantc.services.tools";
    private final String TOOLS_ADMIN = "org.iplantc.services.admin.tools";
    private final String APP_ELEMENTS = "org.iplantc.services.apps.elements";

    @Inject
    public ToolAdminServiceFacadeImpl() {
    }

    @Inject
    private ToolAutoBeanFactory factory;
    @Inject
    private DiscEnvApiService deService;

    @Override
    public void getTools(String searchTerm, AsyncCallback<List<Tool>> callback) {
        String address = TOOLS_ADMIN + "?search=" + URL.encodeQueryString(searchTerm);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new ToolListCallbackConverter(callback, factory));
    }

    @Override
    public void getToolDetails(String toolId, AsyncCallback<Tool> callback) {
        String address = TOOLS_ADMIN + "/" + toolId;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new ToolCallbackConverter(callback, factory));
    }

    @Override
    public void addTool(ToolList toolList, AsyncCallback<Void> callback) {
        String address = TOOLS_ADMIN;

        toolList.getToolList().stream().forEach(tool -> {
            final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool));
            nullUnwantedValues(encode);
        });

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(toolList));
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public void updateTool(Tool tool, boolean overwrite, AsyncCallback<Void> callback) {

        String toolId = tool.getId();
        String address;
        if (overwrite) {
            address = TOOLS_ADMIN + "/" + toolId + "?overwrite-public=true";
        } else {
            address = TOOLS_ADMIN + "/" + toolId;
        }

        final Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool));
        nullUnwantedValues(encode);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address, encode.getPayload());
        deService.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    private void nullUnwantedValues(Splittable encode) {
        Splittable.NULL.assign(encode, "is_public");
        Splittable.NULL.assign(encode, "permission");
    }

    @Override
    public void deleteTool(String toolId, AsyncCallback<Void> callback) {
        String address = TOOLS_ADMIN + "/" + toolId;

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);
        deService.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public void publishTool(Tool tool, AsyncCallback<Void> callback) {
        String address = TOOLS_ADMIN + "/" + tool.getId() + "/publish";

        //null out values not needed by service
        Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(tool));
        nullUnwantedValues(encode);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());
        deService.getServiceData(wrapper, new StringToVoidCallbackConverter(callback));
    }

    @Override
    public void getToolTypes(AsyncCallback<List<ToolType>> callback) {
        String address = APP_ELEMENTS + "/tool-types";

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deService.getServiceData(wrapper, new AsyncCallbackConverter<String, List<ToolType>>(callback) {
            @Override
            protected List<ToolType> convertFrom(String object) {
                ToolTypeList typeList = AutoBeanCodex.decode(factory, ToolTypeList.class, object).as();
                return typeList.getToolTypes();
            }
        });
    }
}

