package org.iplantc.de.diskResource.client.views.widgets;

import static org.iplantc.de.client.models.diskResources.PermissionValue.*;
import org.iplantc.de.client.models.UserInfo;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.diskResource.client.events.DiskResourceSelectionChangedEvent;

import com.google.common.collect.Lists;
import com.google.gwtmockito.GxtMockitoTestRunner;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

@RunWith(GxtMockitoTestRunner.class)
public class DiskResourceViewToolbar_onDiskResourceSelectionChangedTest {

    // File Menu
    @Mock public MenuItem mockNewWindow,
            mockNewWindowAtLoc,
            mockNewFolder,
            mockDuplicate,
            mockAddToSideBar,
            mockNewPlainTextFile,
            mockNewTabularFile,
            mockMoveToTrash;
    // Trash Menu
    @Mock public MenuItem mockOpenTrash,
            mockRestore,
            mockEmptyTrash;
    // Edit Menu
    @Mock public MenuItem mockRename,
            mockMove,
            mockDelete,
            mockEditFile,
            mockEditComments,
            mockEditInfoType,
            mockMetadata;
    // Share Menu
    @Mock public MenuItem mockShareWithCollabs,
            mockCreatePublicLink,
            mockSendToCoge,
            mockSendToEnsembl,
            mockSendToTreeViewer;
    // Download Menu
    @Mock public MenuItem mockSimpleDownload,
            mockBulkDownload;
    // Upload Menu
    @Mock public MenuItem mockSimpleUpload,
            mockBulkUpload,
            mockImportFromUrl;
    @Mock public TextButton mockUploadMenu,
            mockFileMenu,
            mockEditMenu,
            mockDownloadMenu,
            mockShareMenu,
            mockRefreshButton,
            mockTrashMenu;
    private boolean containsFile = false;
    private boolean containsOnlyFolders = false;
    private boolean isReadable = true;
    private boolean isSelectionInTrash = false;
    private boolean isSelectionOwner = true;
    private DiskResourceViewToolbarImpl uut;

    @Before public void setup() {
        uut = new DiskResourceViewToolbarImpl(mock(UserInfo.class)){
            boolean containsFile(List<DiskResource> selection) {
                return containsFile;
            }

            @Override
            boolean isOwner(List<DiskResource> selection) {
                return isSelectionOwner;
            }

            @Override
            boolean isReadable(DiskResource item) {
                return isReadable;
            }

            @Override
            boolean isSelectionInTrash(List<DiskResource> selection) {
                return isSelectionInTrash;
            }

            boolean containsOnlyFolders(List<DiskResource> selection) {
                return containsOnlyFolders;
            }
        };
        mockMenuItems(uut);
    }

    /**
     * Selection is empty
     */
    @Test public void testOnDiskResourceSelectionChanged_emptySelection(){
        this.isSelectionInTrash = false;
        this.containsFile = true;
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        when(mockEvent.getSelection()).thenReturn(selection);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(false);
        verify(mockBulkDownload).setEnabled(false);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one file;
     * with read perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_file_notInTrash_read() {
        this.isSelectionInTrash = false;
        this.containsFile = true;
        File mockFile = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Read ====================*/
        this.isSelectionOwner = false;
        when(mockFile.getPermission()).thenReturn(read);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(true);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(true);
        verify(mockSendToEnsembl).setEnabled(true);
        verify(mockSendToTreeViewer).setEnabled(true);


        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one file;
     * with write perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_file_notInTrash_write() {
        this.isSelectionInTrash = false;
        this.containsFile = true;
        File mockFile = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*==================== Write ==================*/
        this.isSelectionOwner = false;
        when(mockFile.getPermission()).thenReturn(write);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(true);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(true);
        verify(mockSendToEnsembl).setEnabled(true);
        verify(mockSendToTreeViewer).setEnabled(true);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one file;
     * with own perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_file_notInTrash_own() {
        this.isSelectionInTrash = false;
        this.containsFile = true;
        File mockFile = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*==================== Own ====================*/
        this.isSelectionOwner = true;
        when(mockFile.getPermission()).thenReturn(own);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(true);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(true);

        // Edit Menu Items
        verify(mockRename).setEnabled(true);
        verify(mockMove).setEnabled(true);
        verify(mockDelete).setEnabled(true);
        verify(mockEditFile).setEnabled(true);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(true);
        verify(mockMetadata).setEnabled(true);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(true);
        verify(mockCreatePublicLink).setEnabled(true);
        verify(mockSendToCoge).setEnabled(true);
        verify(mockSendToEnsembl).setEnabled(true);
        verify(mockSendToTreeViewer).setEnabled(true);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one file;
     * in trash with read perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_file_inTrash_read() {
        this.isSelectionInTrash = true;
        this.containsFile = true;
        File mockFile = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Read ====================*/
        this.isSelectionOwner = false;
        when(mockFile.getPermission()).thenReturn(read);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one file;
     * in trash with write perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_file_inTrash_write() {
        this.isSelectionInTrash = true;
        this.containsFile = true;
        File mockFile = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*==================== Write ==================*/
        this.isSelectionOwner = false;
        when(mockFile.getPermission()).thenReturn(write);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one file;
     * in trash with own perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_file_inTrash_own(){
        this.isSelectionInTrash = true;
        this.containsFile = true;
        File mockFile = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*==================== Own ====================*/
        this.isSelectionOwner = true;
        when(mockFile.getPermission()).thenReturn(own);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(true);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(true);
    }


    /**
     * Selection contains one folder;
     * with read perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_folder_notInTrash_read(){
        this.isSelectionInTrash = false;
        Folder mockFolder = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Read ====================*/
        this.isSelectionOwner = false;
        when(mockFolder.getPermission()).thenReturn(read);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one folder;
     * with write perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_folder_notInTrash_write(){
        this.isSelectionInTrash = false;
        Folder mockFolder = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Write ====================*/
        this.isSelectionOwner = false;
        this.containsOnlyFolders = true;
        when(mockFolder.getPermission()).thenReturn(write);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(true);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one folder;
     * with owner perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_folder_notInTrash_own(){
        this.isSelectionInTrash = false;
        Folder mockFolder = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Own ====================*/
        this.isSelectionOwner = true;
        this.containsOnlyFolders = true;
        when(mockFolder.getPermission()).thenReturn(own);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(true);
        verify(mockAddToSideBar).setEnabled(true);
        verify(mockMoveToTrash).setEnabled(true);

        // Edit Menu Items
        verify(mockRename).setEnabled(true);
        verify(mockMove).setEnabled(true);
        verify(mockDelete).setEnabled(true);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(true);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one folder;
     * in trash with read perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_folder_inTrash_read(){
        this.isSelectionInTrash = true;
        Folder mockFolder = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Read ====================*/
        this.isSelectionOwner = false;
        when(mockFolder.getPermission()).thenReturn(read);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one folder;
     * in trash with write perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_folder_inTrash_write(){
        this.isSelectionInTrash = true;
        Folder mockFolder = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Write ====================*/
        this.isSelectionOwner = false;
        this.containsOnlyFolders = true;
        when(mockFolder.getPermission()).thenReturn(write);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains one folder;
     * in trash with owner perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_folder_inTrash_own(){
        this.isSelectionInTrash = true;
        Folder mockFolder = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Own ====================*/
        this.isSelectionOwner = true;
        this.containsOnlyFolders = true;
        when(mockFolder.getPermission()).thenReturn(own);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(true);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(true);
    }



    /**
     * Selection contains multiple files;
     * with read perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFiles_notInTrash_read() {
        this.isSelectionInTrash = false;
        this.containsFile = true;
        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile1);
        selection.add(mockFile2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Read ====================*/
        this.isSelectionOwner = false;
        when(mockFile1.getPermission()).thenReturn(read);
        when(mockFile2.getPermission()).thenReturn(read);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple files;
     * with write perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFiles_notInTrash_write() {
        this.isSelectionInTrash = false;
        this.containsFile = true;
        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile1);
        selection.add(mockFile2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*==================== Write ==================*/
        this.isSelectionOwner = false;
        when(mockFile1.getPermission()).thenReturn(write);
        when(mockFile2.getPermission()).thenReturn(write);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple files;
     * with own perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFiles_notInTrash_own() {
        this.isSelectionInTrash = false;
        this.containsFile = true;
        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile1);
        selection.add(mockFile2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*==================== Own ====================*/
        this.isSelectionOwner = true;
        when(mockFile1.getPermission()).thenReturn(own);
        when(mockFile2.getPermission()).thenReturn(own);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(true);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(true);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(true);
        verify(mockDelete).setEnabled(true);
        verify(mockEditFile).setEnabled(true);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(true);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(true);
        verify(mockCreatePublicLink).setEnabled(true);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple files;
     * with mixed perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFiles_notInTrash_mixedPerms() {

    }

    /**
     * Selection contains multiple files;
     * in trash with read perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFiles_inTrash_read() {
        this.isSelectionInTrash = true;
        this.containsFile = true;
        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile1);
        selection.add(mockFile2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Read ====================*/
        this.isSelectionOwner = false;
        when(mockFile1.getPermission()).thenReturn(read);
        when(mockFile2.getPermission()).thenReturn(read);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple files;
     * in trash with write perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFiles_inTrash_write() {
        this.isSelectionInTrash = true;
        this.containsFile = true;
        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile1);
        selection.add(mockFile2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*==================== Write ==================*/
        this.isSelectionOwner = false;
        when(mockFile1.getPermission()).thenReturn(write);
        when(mockFile2.getPermission()).thenReturn(write);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple files;
     * in trash with own perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFiles_inTrash_own(){
        this.isSelectionInTrash = true;
        this.containsFile = true;
        File mockFile1 = mock(File.class);
        File mockFile2 = mock(File.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFile1);
        selection.add(mockFile2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*==================== Own ====================*/
        this.isSelectionOwner = true;
        when(mockFile1.getPermission()).thenReturn(own);
        when(mockFile2.getPermission()).thenReturn(own);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(true);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(true);
    }

    /**
     * Selection contains multiple files;
     * in trash with mixed perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFiles_inTrash_mixedPerms(){

    }

    /**
     * Selection contains multiple folders;
     * with read perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFolders_notInTrash_read(){
        this.isSelectionInTrash = false;
        Folder mockFolder1 = mock(Folder.class);
        Folder mockFolder2 = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder1);
        selection.add(mockFolder2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Read ====================*/
        this.isSelectionOwner = false;
        when(mockFolder1.getPermission()).thenReturn(read);
        when(mockFolder2.getPermission()).thenReturn(read);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple folders;
     * with write perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFolders_notInTrash_write(){
        this.isSelectionInTrash = false;
        Folder mockFolder1 = mock(Folder.class);
        Folder mockFolder2 = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder1);
        selection.add(mockFolder2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Write ====================*/
        this.isSelectionOwner = false;
        this.containsOnlyFolders = true;
        when(mockFolder1.getPermission()).thenReturn(write);
        when(mockFolder2.getPermission()).thenReturn(write);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(true);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple folders;
     * with owner perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFolders_notInTrash_own(){
        this.isSelectionInTrash = false;
        Folder mockFolder1 = mock(Folder.class);
        Folder mockFolder2 = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder1);
        selection.add(mockFolder2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Own ====================*/
        this.isSelectionOwner = true;
        this.containsOnlyFolders = true;
        when(mockFolder1.getPermission()).thenReturn(own);
        when(mockFolder2.getPermission()).thenReturn(own);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(true);
        verify(mockAddToSideBar).setEnabled(true);
        verify(mockMoveToTrash).setEnabled(true);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(true);
        verify(mockDelete).setEnabled(true);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(true);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple folders;
     * with mixed perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFolders_notInTrash_mixedPerms(){
    }

    /**
     * Selection contains multiple folders;
     * in trash with read perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFolders_inTrash_read(){
        this.isSelectionInTrash = true;
        Folder mockFolder1 = mock(Folder.class);
        Folder mockFolder2 = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder1);
        selection.add(mockFolder2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Read ====================*/
        this.isSelectionOwner = false;
        when(mockFolder1.getPermission()).thenReturn(read);
        when(mockFolder2.getPermission()).thenReturn(read);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple folders;
     * in trash with write perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFolders_inTrash_write(){
        this.isSelectionInTrash = true;
        Folder mockFolder1 = mock(Folder.class);
        Folder mockFolder2 = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder1);
        selection.add(mockFolder2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Write ====================*/
        this.isSelectionOwner = false;
        this.containsOnlyFolders = true;
        when(mockFolder1.getPermission()).thenReturn(write);
        when(mockFolder2.getPermission()).thenReturn(write);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(false);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(false);
    }

    /**
     * Selection contains multiple folders;
     * in trash with owner perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFolders_inTrash_own(){
        this.isSelectionInTrash = true;
        Folder mockFolder1 = mock(Folder.class);
        Folder mockFolder2 = mock(Folder.class);
        // Setup mock event
        DiskResourceSelectionChangedEvent mockEvent = mock(DiskResourceSelectionChangedEvent.class);
        final ArrayList<DiskResource> selection = Lists.newArrayList();
        selection.add(mockFolder1);
        selection.add(mockFolder2);
        when(mockEvent.getSelection()).thenReturn(selection);

        /*=================== Own ====================*/
        this.isSelectionOwner = true;
        this.containsOnlyFolders = true;
        when(mockFolder1.getPermission()).thenReturn(own);
        when(mockFolder2.getPermission()).thenReturn(own);
        uut.onDiskResourceSelectionChanged(mockEvent);
        verifyOnDiskResourceSelectionChangedNeverUsedItems();

        // File Menu Items
        verify(mockDuplicate).setEnabled(false);
        verify(mockAddToSideBar).setEnabled(false);
        verify(mockMoveToTrash).setEnabled(false);

        // Edit Menu Items
        verify(mockRename).setEnabled(false);
        verify(mockMove).setEnabled(false);
        verify(mockDelete).setEnabled(true);
        verify(mockEditFile).setEnabled(false);
        verify(mockEditComments).setEnabled(false);
        verify(mockEditInfoType).setEnabled(false);
        verify(mockMetadata).setEnabled(false);

        // Download Menu Items
        verify(mockSimpleDownload).setEnabled(true);
        verify(mockBulkDownload).setEnabled(true);

        // Share Menu Items
        verify(mockShareWithCollabs).setEnabled(false);
        verify(mockCreatePublicLink).setEnabled(false);
        verify(mockSendToCoge).setEnabled(false);
        verify(mockSendToEnsembl).setEnabled(false);
        verify(mockSendToTreeViewer).setEnabled(false);

        // Trash Menu Items
        verify(mockRestore).setEnabled(true);
    }

    /**
     * Selection contains multiple folders;
     * in trash with mixed perms,
     */
    @Test public void testOnDiskResourceSelectionChanged_multiFolders_inTrash_mixedPerms(){
    }

    void verifyOnDiskResourceSelectionChangedNeverUsedItems(){
        // Top level menu items
        verify(mockUploadMenu, never()).setEnabled(anyBoolean());
        verify(mockFileMenu, never()).setEnabled(anyBoolean());
        verify(mockEditMenu, never()).setEnabled(anyBoolean());
        verify(mockDownloadMenu, never()).setEnabled(anyBoolean());
        verify(mockShareMenu, never()).setEnabled(anyBoolean());
        verify(mockTrashMenu, never()).setEnabled(anyBoolean());

        // Upload Menu items
        verify(mockSimpleUpload, never()).setEnabled(anyBoolean());
        verify(mockBulkUpload, never()).setEnabled(anyBoolean());
        verify(mockImportFromUrl, never()).setEnabled(anyBoolean());

        // File Menu Items
        verify(mockNewWindow, never()).setEnabled(anyBoolean());
        verify(mockNewWindowAtLoc, never()).setEnabled(anyBoolean());
        verify(mockNewFolder, never()).setEnabled(anyBoolean());
        verify(mockNewPlainTextFile, never()).setEnabled(anyBoolean());
        verify(mockNewTabularFile, never()).setEnabled(anyBoolean());

        // Refresh Menu item
        verify(mockRefreshButton, never()).setEnabled(anyBoolean());

        // Trash Menu Items
        verify(mockOpenTrash, never()).setEnabled(anyBoolean());
        verify(mockEmptyTrash, never()).setEnabled(anyBoolean());
    }

    private void mockMenuItems(DiskResourceViewToolbarImpl uut) {
        // Upload Menu
        uut.uploadMenu = mockUploadMenu;
        uut.simpleUploadMi = mockSimpleUpload;
        uut.bulkUploadMi = mockBulkUpload;
        uut.importFromUrlMi = mockImportFromUrl;

        // File Menu
        uut.fileMenu = mockFileMenu;
        uut.newWindowMi = mockNewWindow;
        uut.newWindowAtLocMi = mockNewWindowAtLoc;
        uut.newFolderMi = mockNewFolder;
        uut.duplicateMi = mockDuplicate;
        uut.addToSideBarMi = mockAddToSideBar;
        uut.newPlainTextFileMi = mockNewPlainTextFile;
        uut.newTabularDataFileMi = mockNewTabularFile;
        uut.moveToTrashMi = mockMoveToTrash;

        // Edit Menu
        uut.editMenu = mockEditMenu;
        uut.renameMi = mockRename;
        uut.moveMi = mockMove;
        uut.deleteMi = mockDelete;
        uut.editFileMi = mockEditFile;
        uut.editCommentsMi = mockEditComments;
        uut.editInfoTypeMi = mockEditInfoType;
        uut.metadataMi = mockMetadata;

        // Download Menu
        uut.downloadMenu = mockDownloadMenu;
        uut.simpleDownloadMi = mockSimpleDownload;
        uut.bulkDownloadMi = mockBulkDownload;

        // Share Menu
        uut.shareMenu = mockShareMenu;
        uut.shareWithCollaboratorsMi = mockShareWithCollabs;
        uut.createPublicLinkMi = mockCreatePublicLink;
        uut.sendToCogeMi = mockSendToCoge;
        uut.sendToEnsemblMi = mockSendToEnsembl;
        uut.sendToTreeViewerMi = mockSendToTreeViewer;

        // Refresh
        uut.refreshButton = mockRefreshButton;

        // Trash Menu
        uut.trashMenu = mockTrashMenu;
        uut.openTrashMi = mockOpenTrash;
        uut.restoreMi = mockRestore;
        uut.emptyTrashMi = mockEmptyTrash;
    }

}