package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.DELETE;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;

import org.iplantc.de.client.models.HasId;
import org.iplantc.de.client.models.HasUUIDs;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.notifications.NotificationAutoBeanFactory;
import org.iplantc.de.client.models.notifications.NotificationCategory;
import org.iplantc.de.client.models.notifications.NotificationList;
import org.iplantc.de.client.services.MessageServiceFacade;
import org.iplantc.de.client.services.PermIdRequestUserServiceFacade;
import org.iplantc.de.client.services.callbacks.NotificationCallbackWrapper;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.client.services.converters.NotificationCallbackConverter;
import org.iplantc.de.client.util.DiskResourceUtil;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.base.Strings;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;

import java.util.Arrays;
import java.util.List;

/**
 * Provides access to remote services to acquire messages and notifications.
 *
 * @author amuir
 *
 */
public class MessageServiceFacadeImpl implements MessageServiceFacade {

    private final NotificationAutoBeanFactory notesFactory;
    private final DiscEnvApiService deServiceFacade;
    private final UserInfo userInfo;
    private final String NOTIFICATIONS = "org.iplantc.services.notifications";
    @Inject DiskResourceUtil diskResourceUtil;

    @Inject
    public MessageServiceFacadeImpl(final DiscEnvApiService deServiceFacade,
                                    final NotificationAutoBeanFactory notesFactory,
                                    final UserInfo userInfo) {
        this.deServiceFacade = deServiceFacade;
        this.notesFactory = notesFactory;
        this.userInfo = userInfo;
    }

    @Override
    public void getNotifications(int limit, int offset, String filter, String sortDir, NotificationCallbackWrapper callback) {
        String address = NOTIFICATIONS;

        StringBuilder builder = new StringBuilder("/messages?limit=" + limit + "&offset="
                                                      + offset);
        if (!Strings.isNullOrEmpty(filter) && !filter.equals("All")) {
            builder.append("&filter=").append(URL.encodeQueryString(filter));
        }

        if (sortDir != null && !sortDir.isEmpty() && !sortDir.equalsIgnoreCase("NONE")) {
            builder.append("&sortDir=").append(URL.encodeQueryString(sortDir.toUpperCase()));
        }

        address = address + builder.toString();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void getRecentMessages(AsyncCallback<NotificationList> callback) {
        String address = NOTIFICATIONS + "/last-ten-messages"; //$NON-NLS-1$
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);

        deServiceFacade.getServiceData(wrapper, new NotificationCallbackConverter(callback, notesFactory));
    }

    @Override
    public void markAsSeen(HasId id, DECallback<String> callback) {
        List<String> nids = Arrays.asList(id.getId());
        HasUUIDs hasUUIDs = notesFactory.getHasUUIDs().as();
        hasUUIDs.setUUIDs(nids);
        markAsSeen(hasUUIDs, callback);
    }

    @Override
    public void markAsSeen(final HasUUIDs seenIds, DECallback<String> callback) {
        String address = NOTIFICATIONS + "/seen"; //$NON-NLS-1$
        Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(seenIds));

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, encode.getPayload());

        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void deleteMessages(HasUUIDs deleteIds, DECallback<String> callback) {
        String address = NOTIFICATIONS + "/delete"; //$NON-NLS-1$

        Splittable encode = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(deleteIds));
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address,
                                                            encode.getPayload());

        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void deleteAll(NotificationCategory category, DECallback<String> callback) {
        String address = NOTIFICATIONS + "/delete-all"; //$NON-NLS-1$

        if (NotificationCategory.ALL != category) {
            address += "?filter=" + URL.encodeQueryString(category.toString().toLowerCase());
        }

        ServiceCallWrapper wrapper = new ServiceCallWrapper(DELETE, address);

        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void markAllNotificationsSeen(DECallback<Void> callback) {
        String address = NOTIFICATIONS + "/mark-all-seen"; //$NON-NLS-1$

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, "{}");

        deServiceFacade.getServiceData(wrapper, new DECallbackConverter<String, Void>(callback) {
            @Override
            protected Void convertFrom(String object) {
                return null;
            }
        });
    }

    @Override
    public void getPermanentIdRequestStatusHistory(String id, DECallback<String> callback) {
        String address = PermIdRequestUserServiceFacade.PERMID_REQUEST + "/" + id;
        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);
        deServiceFacade.getServiceData(wrapper, callback);
    }

}
