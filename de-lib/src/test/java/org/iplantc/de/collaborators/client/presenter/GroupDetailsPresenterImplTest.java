package org.iplantc.de.collaborators.client.presenter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.client.models.groups.Group;
import org.iplantc.de.client.models.groups.GroupAutoBeanFactory;
import org.iplantc.de.client.models.groups.UpdateMemberResult;
import org.iplantc.de.client.services.GroupServiceFacade;
import org.iplantc.de.collaborators.client.GroupDetailsView;
import org.iplantc.de.collaborators.client.ManageCollaboratorsView;
import org.iplantc.de.collaborators.client.events.AddGroupMemberSelected;
import org.iplantc.de.collaborators.client.events.DeleteMembersSelected;
import org.iplantc.de.collaborators.client.events.GroupSaved;
import org.iplantc.de.collaborators.client.views.dialogs.RetainPermissionsDialog;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.web.bindery.autobean.shared.AutoBean;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author aramsey
 */
@RunWith(GwtMockitoTestRunner.class)
public class GroupDetailsPresenterImplTest {

    @Mock GroupDetailsView viewMock;
    @Mock GroupServiceFacade serviceFacadeMock;
    @Mock GroupAutoBeanFactory factoryMock;
    @Mock ManageCollaboratorsView.Appearance appearanceMock;
    @Mock Group groupMock;
    @Mock Group newGroupMock;
    @Mock AutoBean<Group> groupAutoBeanMock;
    @Mock List<Subject> subjectListMock;
    @Mock HandlerManager handlerManagerMock;
    @Mock UpdateMemberResult updateResultMock;
    @Mock List<UpdateMemberResult> updateMemberResultsMock;
    @Mock List<UpdateMemberResult> failedUpdateResultsMock;
    @Mock Stream<UpdateMemberResult> updateMemberResultStreamMock;
    @Mock IplantAnnouncer announcerMock;
    @Mock Subject subjectMock;
    @Mock Iterator<Subject> collaboratorIteratorMock;
    @Mock Iterator<UpdateMemberResult> resultIteratorMock;
    @Mock UserInfo userInfoMock;
    @Mock AsyncProviderWrapper<RetainPermissionsDialog> retainPermsDialogProvider;
    @Mock RetainPermissionsDialog retainPermissionsDialogMock;

    @Captor ArgumentCaptor<AsyncCallback<List<Subject>>> collabListCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Group>> groupCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<List<UpdateMemberResult>>> updateMembersCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<Void>> voidCallbackCaptor;
    @Captor ArgumentCaptor<AsyncCallback<RetainPermissionsDialog>> retainPermsDlgCaptor;

    private GroupDetailsPresenterImpl uut;

    @Before
    public void setUp() {

        when(factoryMock.getGroup()).thenReturn(groupAutoBeanMock);
        when(groupAutoBeanMock.as()).thenReturn(newGroupMock);
        when(subjectListMock.size()).thenReturn(2);
        when(subjectListMock.iterator()).thenReturn(collaboratorIteratorMock);
        when(collaboratorIteratorMock.hasNext()).thenReturn(true, true, false);
        when(collaboratorIteratorMock.next()).thenReturn(subjectMock, subjectMock);
        when(failedUpdateResultsMock.size()).thenReturn(2);
        when(failedUpdateResultsMock.iterator()).thenReturn(resultIteratorMock);
        when(resultIteratorMock.hasNext()).thenReturn(true, true, false);
        when(resultIteratorMock.next()).thenReturn(updateResultMock, updateResultMock);

        uut = new GroupDetailsPresenterImpl(viewMock,
                                            serviceFacadeMock,
                                            factoryMock,
                                            appearanceMock) {

            @Override
            Group getGroupCopy(Group group) {
                return groupMock;
            }

            @Override
            HandlerManager ensureHandlers() {
                return handlerManagerMock;
            }

            @Override
            List<Subject> wrapSubjectInList(Subject subject) {
                return subjectListMock;
            }
        };
        uut.announcer = announcerMock;
        uut.originalGroup = groupMock;
        uut.userInfo = userInfoMock;
        uut.permissionsDlgProvider = retainPermsDialogProvider;
    }

    @Test
    public void getGroupMembers_editGroup() {
        uut.mode = GroupDetailsView.MODE.EDIT;

        /** CALL METHOD UNDER TEST **/
        uut.getGroupMembers(groupMock);

        verify(serviceFacadeMock).getListMembers(eq(groupMock), collabListCallbackCaptor.capture());

        collabListCallbackCaptor.getValue().onSuccess(subjectListMock);
        verify(viewMock).addMembers(eq(subjectListMock));
        verify(viewMock).edit(eq(groupMock), eq(GroupDetailsView.MODE.EDIT));
    }

    @Test
    public void getGroupMembers_newGroup() {
        uut.mode = GroupDetailsView.MODE.ADD;

        /** CALL METHOD UNDER TEST **/
        uut.getGroupMembers(null);
        
        verify(viewMock).edit(eq(newGroupMock), eq(GroupDetailsView.MODE.ADD));
    }

    @Test
    public void saveGroupSelected_newGroup() {
        when(viewMock.getGroup()).thenReturn(groupMock);
        uut.mode = GroupDetailsView.MODE.ADD;
        GroupDetailsPresenterImpl spy = Mockito.spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.saveGroupSelected();

        verify(spy).addGroup(eq(groupMock));
    }

    @Test
    public void addGroup() {

        /** CALL METHOD UNDER TEST **/
        uut.addGroup(groupMock);
        verify(serviceFacadeMock).addList(eq(groupMock), groupCallbackCaptor.capture());
        groupCallbackCaptor.getValue().onSuccess(groupMock);

        verify(handlerManagerMock).fireEvent(isA(GroupSaved.class));

    }

    @Test
    public void updateGroupMembers_withFailures() {
        when(subjectListMock.isEmpty()).thenReturn(false);
        when(updateMemberResultsMock.stream()).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.filter(any())).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.collect(any())).thenReturn(failedUpdateResultsMock);
        when(appearanceMock.unableToAddMembers(any())).thenReturn("announcement");
        /** CALL METHOD UNDER TEST **/
        uut.addGroupMembers(groupMock, subjectListMock);

        verify(serviceFacadeMock).addMembersToList(eq(groupMock),
                                                   eq(subjectListMock),
                                                   updateMembersCallbackCaptor.capture());

        updateMembersCallbackCaptor.getValue().onSuccess(updateMemberResultsMock);
        verify(appearanceMock).unableToAddMembers(eq(failedUpdateResultsMock));
        verify(announcerMock).schedule(isA(ErrorAnnouncementConfig.class));
        verify(viewMock).unmask();
    }

    @Test
    public void onAddGroupMemberSelected() {
        AddGroupMemberSelected eventMock = mock(AddGroupMemberSelected.class);
        when(eventMock.getGroup()).thenReturn(groupMock);
        when(eventMock.getSubject()).thenReturn(subjectMock);
        when(subjectMock.getName()).thenReturn("name");
        when(userInfoMock.getUsername()).thenReturn("someid");
        when(subjectMock.getId()).thenReturn("id");
        when(updateMemberResultsMock.stream()).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.filter(any())).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.collect(any())).thenReturn(null);
        when(appearanceMock.unableToAddMembers(any())).thenReturn("announcement");
        when(groupMock.getName()).thenReturn("name");

        /** CALL METHOD UNDER TEST **/
        uut.onAddGroupMemberSelected(eventMock);

        verify(serviceFacadeMock).addMembersToList(eq(groupMock),
                                                   eq(subjectListMock),
                                                   updateMembersCallbackCaptor.capture());

        updateMembersCallbackCaptor.getValue().onSuccess(updateMemberResultsMock);
        verify(viewMock).addMembers(subjectListMock);
    }

    @Test
    public void updateGroup() {
        when(viewMock.getMembers()).thenReturn(subjectListMock);
        when(groupMock.getName()).thenReturn("original");
        GroupDetailsPresenterImpl spy = Mockito.spy(uut);

        /** CALL METHOD UNDER TEST **/
        spy.updateGroup(groupMock);
        verify(serviceFacadeMock).updateList(eq("original"), eq(groupMock), groupCallbackCaptor.capture());
        groupCallbackCaptor.getValue().onSuccess(groupMock);

        verify(handlerManagerMock).fireEvent(isA(GroupSaved.class));
    }

    @Test
    public void onDeleteMembersSelectedTest() {
        DeleteMembersSelected eventMock = mock(DeleteMembersSelected.class);
        when(eventMock.getSubjects()).thenReturn(subjectListMock);
        when(eventMock.getGroup()).thenReturn(groupMock);
        when(subjectListMock.isEmpty()).thenReturn(false);
        uut.mode = GroupDetailsView.MODE.EDIT;

        /** CALL METHOD UNDER TEST **/
        uut.onDeleteMembersSelected(eventMock);

        verify(retainPermsDialogProvider).get(retainPermsDlgCaptor.capture());

        retainPermsDlgCaptor.getValue().onSuccess(retainPermissionsDialogMock);
        verify(retainPermissionsDialogMock).show();
        verify(retainPermissionsDialogMock).addDialogHideHandler(any());
    }

    @Test
    public void deleteMember() {
        when(appearanceMock.memberDeleteFail(updateMemberResultsMock)).thenReturn("fail");
        when(updateMemberResultsMock.stream()).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.filter(any())).thenReturn(updateMemberResultStreamMock);
        when(updateMemberResultStreamMock.collect(any())).thenReturn(null);

        /** CALL METHOD UNDER TEST **/
        uut.deleteMembers(subjectListMock, groupMock, true);

        verify(serviceFacadeMock).deleteListMembers(eq(groupMock), eq(subjectListMock), eq(true), updateMembersCallbackCaptor.capture());

        updateMembersCallbackCaptor.getValue().onSuccess(updateMemberResultsMock);
        verify(viewMock).deleteMembers(subjectListMock);
    }
}
