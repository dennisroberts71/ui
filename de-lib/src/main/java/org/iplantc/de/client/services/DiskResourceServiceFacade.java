package org.iplantc.de.client.services;

import org.iplantc.de.client.models.HasPaths;
import org.iplantc.de.client.models.dataLink.DataLink;
import org.iplantc.de.client.models.dataLink.UpdateTicketResponse;
import org.iplantc.de.client.models.diskResources.DiskResource;
import org.iplantc.de.client.models.diskResources.DiskResourceAutoBeanFactory;
import org.iplantc.de.client.models.diskResources.DiskResourceExistMap;
import org.iplantc.de.client.models.diskResources.DiskResourceMetadataList;
import org.iplantc.de.client.models.diskResources.File;
import org.iplantc.de.client.models.diskResources.Folder;
import org.iplantc.de.client.models.diskResources.PathListRequest;
import org.iplantc.de.client.models.diskResources.MetadataCopyRequest;
import org.iplantc.de.client.models.diskResources.MetadataTemplate;
import org.iplantc.de.client.models.diskResources.MetadataTemplateInfo;
import org.iplantc.de.client.models.diskResources.RootFolders;
import org.iplantc.de.client.models.diskResources.TYPE;
import org.iplantc.de.client.models.diskResources.sharing.DataSharingRequestList;
import org.iplantc.de.client.models.diskResources.sharing.DataUnsharingRequestList;
import org.iplantc.de.client.models.diskResources.sharing.DataUserPermissionList;
import org.iplantc.de.client.models.services.DiskResourceMove;
import org.iplantc.de.client.models.viewer.InfoType;
import org.iplantc.de.shared.DECallback;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sencha.gxt.core.shared.FastMap;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;

import java.util.List;

/**
 * @author jstroot
 */
public interface DiskResourceServiceFacade {


    /**
     * Calledto reload the given folder's subfolders from the service. After refreshing the folder,
     * a global refresh event is fired so all views can update the folder's subfolders.
     *
     * @param folder The parent of the subfolders to refresh from the service.
     * @param callback executed when RPC call completes.
     */
    void refreshFolder(Folder folder, final DECallback<List<Folder>> callback);

    DiskResource combineDiskResources(DiskResource from, DiskResource into);

    Folder convertToFolder(DiskResource diskResource);

    /**
     * Call service to retrieve the root folder info for the current user
     * 
     * @param callback executed when RPC call completes.
     */
    void getRootFolders(DECallback<RootFolders> callback);

    /**
     * Called to retrieve the entire contents of a folder.
     * 
     * @param folder the folder whose contents are to be retrieved
     * @param infoTypeFilterList a list of <code>InfoType</code>s to filter the results by.
     * @param entityType used to specify if the results should contain only file, folders, or all.
     *            Defaults to all.
     * @param loadConfig the paged load config which contains all parameters necessary to construct a
     *            well-formed paged directory listing request
     * @param callback executed when RPC call completes.
     */
    void getFolderContents(Folder folder,
                           List<InfoType> infoTypeFilterList,
                           TYPE entityType,
                           FilterPagingLoadConfigBean loadConfig,
                           DECallback<Folder> callback);

    /**
     * Called to retrieve the contents of a folder without its file contents.
     * 
     * @param parent requested folder.
     * @param callback executed when RPC call completes.
     */
    void getSubFolders(final Folder parent, final DECallback<List<Folder>> callback);

    /**
     * Call service to create a new folder
     * 
     * @param parentFolder parent folder where the new folder will be created
     * @param callback executed when RPC call completes.
     */
    void createFolder(Folder parentFolder, final String newFolderName, DECallback<Folder> callback);

    /**
     * Check if a list of files or folders exist.
     * 
     * @param diskResourcePaths paths to desired resources.
     * @param callback callback executed when RPC call completes. On success, a map that maps resource
     *            paths to whether or not they exist.
     */
    void diskResourcesExist(HasPaths diskResourcePaths, DECallback<DiskResourceExistMap> callback);

    /**
     * Calls the move folder and move file services for the list of given disk resource ids.
     *
     * @param sourceFolder the source folder of disk resources to move.
     * @param destFolder the destination folder where the disk resources will be moved.
     * @param diskResources list of file and folder ids to move.
     */
    void moveDiskResources(final Folder sourceFolder,
                           final Folder destFolder,
                           final List<DiskResource> diskResources,
                           DECallback<DiskResourceMove> callback);

    /**
     * Calls the move folder and move file services for moving contents of a given folder.
     * 
     * @param sourceFolder the source folder of disk resources to move.
     * @param destFolder the destination folder where the disk resources will be moved.
     */
    void moveContents(final Folder sourceFolder,
                      final Folder destFolder,
                      DECallback<DiskResourceMove> callback);

    /**
     * Call service rename a file or folder.
     * 
     * @param src the disk resource to be renamed.
     * @param destName the new name.
     * @param callback service success/failure callback
     */
    void renameDiskResource(DiskResource src, String destName, DECallback<DiskResource> callback);

    /**
     * Call service to upload a file from a given URL.
     * 
     * @param url the URL to import from.
     * @param dest id of the destination folder.
     * @param callback service success/failure callback
     */
    void importFromUrl(String url, DiskResource dest, DECallback<String> callback);

    /**
     * @param path the path of the file to download.
     * @return the URL encoded simple download address for the given path.
     */
    String getEncodedSimpleDownloadURL(String path);

    /**
     * Call service to delete disk resources (i.e. {@link File}s and {@link Folder}s)
     * 
     * @param diskResources a set of <code>DiskResource</code>s to be deleted
     * @param callback callback executed when service call completes.
     */
    <T extends DiskResource> void deleteDiskResources(List<T> diskResources,
                                                      DECallback<HasPaths> callback);

    /**
     * Call service to delete disk resources in case user selects all items
     * 
     * @param selectedFolderId the folder whose contents will be deleted.
     */
    void deleteContents(String selectedFolderId, DECallback<HasPaths> callback);

    /**
     * Call service to delete disk resources (i.e. {@link File}s and {@link Folder}s)
     * 
     * @param diskResources a set of <code>DiskResource</code>s to be deleted
     * @param callback callback executed when service call completes.
     */
    void deleteDiskResources(HasPaths diskResources, DECallback<HasPaths> callback);

    /**
     * @param resource the <code>DiskResource</code> for which metadata will be retrieved.
     * @param callback callback executed when service call completes.
     */
    void getDiskResourceMetaData(DiskResource resource, AsyncCallback<DiskResourceMetadataList> callback);

    /**
     * Calls service to set disk resource metadata.
     * 
     * @param resource the <code>DiskResource</code> whose metadata will be updated
     * @param  mdList metadata list
     * @param callback executed when the service call completes.
     */
    void setDiskResourceMetaData(DiskResource resource,
                                 DiskResourceMetadataList mdList,
                                 AsyncCallback<String> callback);

    /**
     * 
     * Share a resource with give user with permission
     * 
     *  @param requestList - Post body in JSONObject format
     * @param callback callback object
     */
    void shareDiskResource(DataSharingRequestList requestList, AsyncCallback<String> callback);

    /**
     * UnShare a resource with give user with permission
     *  @param unsharingRequestList - Post body in JSONObject format
     * @param callback callback object
     */
    void unshareDiskResource(DataUnsharingRequestList unsharingRequestList, AsyncCallback<String> callback);

    /**
     * get user permission info on selected disk resources
     *  @param paths - Post body in JSONObject format
     * @param callback callback object
     */
    void getPermissions(HasPaths paths, DECallback<DataUserPermissionList> callback);

    /**
     * Get info about a selected file or folder
     * 
     * @param paths the paths to query
     * @param callback callback which returns a map of {@code DiskResource}s keyed by their paths
     */
    void getStat(final FastMap<TYPE> paths, final DECallback<FastMap<DiskResource>> callback);

    /**
     * empty user's trash
     * 
     * @param user the user whose trash will be emptied.
     */
    public void emptyTrash(String user, DECallback<String> callback);

    /**
     * Restore deleted disk resources.
     * 
     * @param request the disk resources to be restored.
     */
    public void restoreDiskResource(HasPaths request, DECallback<String> callback);

    /**
     * Creates a set of public data links for the given disk resources.
     * 
     * @param ticketIdList the id of the disk resource for which the ticket will be created.
     */
    public void createDataLinks(List<String> ticketIdList, DECallback<List<DataLink>> callback);

    /**
     * Requests a listing of all the tickets for the given disk resources.
     * 
     * @param diskResourceIds the disk resources whose tickets will be listed.
     */
    public void listDataLinks(List<String> diskResourceIds,
                              DECallback<FastMap<List<DataLink>>> callback);

    /**
     * Requests that the given Kif Share tickets will be deleted.
     * 
     * @param dataLinkIds the tickets which will be deleted.
     */
    public void deleteDataLinks(List<String> dataLinkIds, DECallback<UpdateTicketResponse> callback);

    /**
     * Get a list of files types recognized
     */
    void getInfoTypes(DECallback<List<InfoType>> callback);

    /**
     * Set type to a file
     * 
     * @param filePath the path of the file whose type will be set
     * @param type the type the file will be set to.
     */
    void setFileType(String filePath, String type, DECallback<String> callback);

    /**
     * Convenience method which returns a valid {@link DiskResourceAutoBeanFactory} instance.
     * 
     * @return a ready to use <code>DiskResourceAutoBeanFactory</code>
     */
    DiskResourceAutoBeanFactory getDiskResourceFactory();

    /**
     * Restore all items in trash to its original location.
     * 
     */
    void restoreAll(DECallback<String> callback);

    /**
     * Method used to retrieve list of metadata templates
     */
    void getMetadataTemplateListing(AsyncCallback<List<MetadataTemplateInfo>> callback);

    /**
     * Method used to retrieve a metadata template
     * 
     * @param templateId id of the template
     */
    void getMetadataTemplate(String templateId, AsyncCallback<MetadataTemplate> callback);

    /**
     * share with anonymous user selected file(s)
     * 
     * @param diskResourcePaths the paths to query
     * @param callback callback object
     */
    void shareWithAnonymous(final HasPaths diskResourcePaths, final DECallback<List<String>> callback);

    /**
     * Copy metadata to list of files / folders
     * 
     * @param srcUUID source DR's UUID
     * @param copyRequest IDs of disk resources to which metadata will be copied
     * @param callback callback object
     */

    void copyMetadata(final String srcUUID,
                      final MetadataCopyRequest copyRequest,
                      final AsyncCallback<String> callback);
            
            /**
     * save metadata to a file
     * 
     * @param srcUUID source DR's UUID
     * @param path where the file will be created
     * @param recursive should recursively store metadata of folder contents
     * @param callback callback object
     */
    void saveMetadata(final String srcUUID,
                      final String path,
                      boolean recursive,
                      final AsyncCallback<String> callback);

    /**
     * 
     * @param parentFolder parent folder under which new folders will be created
     * @param foldersToCreate an array of folder names to be created under parent folder
     * @param callback callback object
     */
    void createNcbiSraFolderStructure(Folder parentFolder,
                                      String[] foldersToCreate,
                                      DECallback<String> callback);

    /**
     * 
     * @param metadataFilePath path to CSV/ TSV metadata file
     * @param destFolder folder containing files to which metadata will be applied
     * @param callback callback object
     */
    void setBulkMetadataFromFile(String metadataFilePath,
                                 String destFolder,
                                 DECallback<String> callback);

    /**
     *
     * @param templateid
     * @return url to download template
     */
    String downloadTemplate(String templateid);

    /**
     *  A request to create HT Analysis path list file from selected folders and filters.
     * @param request
     * @param callback
     */
    void requestPathListFile(PathListRequest request, DECallback<File> callback);
}
