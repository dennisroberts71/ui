package org.iplantc.de.client.models.toolRequests;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

public interface ToolRequestList {

    @PropertyName("tool_requests")
    List<ToolRequest> getToolRequests();

}
