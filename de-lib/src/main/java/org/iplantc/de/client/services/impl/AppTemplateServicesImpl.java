package org.iplantc.de.client.services.impl;

import static org.iplantc.de.shared.ServiceFacadeLoggerConstants.ANALYSIS_ID;
import static org.iplantc.de.shared.ServiceFacadeLoggerConstants.APP_EVENT;
import static org.iplantc.de.shared.ServiceFacadeLoggerConstants.APP_ID;
import static org.iplantc.de.shared.ServiceFacadeLoggerConstants.METRIC_TYPE_KEY;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.GET;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PATCH;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.POST;
import static org.iplantc.de.shared.services.BaseServiceCallWrapper.Type.PUT;

import org.iplantc.de.client.DEClientConstants;
import org.iplantc.de.client.models.HasQualifiedId;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.client.models.apps.integration.ArgumentGroup;
import org.iplantc.de.client.models.apps.integration.ArgumentType;
import org.iplantc.de.client.models.apps.integration.DataSource;
import org.iplantc.de.client.models.apps.integration.DataSourceList;
import org.iplantc.de.client.models.apps.integration.FileInfoType;
import org.iplantc.de.client.models.apps.integration.FileInfoTypeList;
import org.iplantc.de.client.models.apps.integration.JobExecution;
import org.iplantc.de.client.models.apps.integration.SelectionItem;
import org.iplantc.de.client.models.apps.integration.SelectionItemGroup;
import org.iplantc.de.client.models.apps.refGenome.ReferenceGenome;
import org.iplantc.de.client.models.apps.refGenome.ReferenceGenomeList;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.services.AppBuilderMetadataServiceFacade;
import org.iplantc.de.client.services.AppTemplateServices;
import org.iplantc.de.client.services.converters.AppTemplateCallbackConverter;
import org.iplantc.de.client.services.converters.DECallbackConverter;
import org.iplantc.de.client.services.impl.models.AnalysisSubmissionResponse;
import org.iplantc.de.client.util.AnalysisSubmissionUtil;
import org.iplantc.de.client.util.AppTemplateUtils;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.intercom.client.IntercomFacade;
import org.iplantc.de.intercom.client.TrackingEventType;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.DECallback;
import org.iplantc.de.shared.DEProperties;
import org.iplantc.de.shared.ServiceFacadeLoggerConstants;
import org.iplantc.de.shared.services.DiscEnvApiService;
import org.iplantc.de.shared.services.ServiceCallWrapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

public class AppTemplateServicesImpl implements AppTemplateServices, AppBuilderMetadataServiceFacade {

    private final String APPS = "org.iplantc.services.apps";
    private final String DATA_SOURCES = "org.iplantc.services.apps.elements.dataSources";
    private final String FILE_INFO_TYPES = "org.iplantc.services.apps.elements.infoTypes";
    private final String REFERENCE_GENOMES = "org.iplantc.services.referenceGenomes";
    private final String ANALYSES = "org.iplantc.services.analyses";

    private final AppTemplateAutoBeanFactory factory;
    private static final Queue<DECallback<List<DataSource>>> dataSourceQueue = Lists.newLinkedList();
    private static final Queue<DECallback<List<FileInfoType>>> fileInfoTypeQueue = Lists.newLinkedList();
    private static final Queue<DECallback<List<ReferenceGenome>>> refGenQueue = Lists.newLinkedList();
    private final List<DataSource> dataSourceList = Lists.newArrayList();
    private final List<FileInfoType> fileInfoTypeList = Lists.newArrayList();

    private final List<ReferenceGenome> refGenList = Lists.newArrayList();
    private final DiscEnvApiService deServiceFacade;
    private final DEProperties deProperties;
    private final AppTemplateUtils appTemplateUtils;
    private final DEClientConstants deClientConstants;
    private final JsonUtil jsonUtil;

    private static final Logger LOG = Logger.getLogger(AppTemplateServicesImpl.class.getName());

    @Inject
    public AppTemplateServicesImpl(final DiscEnvApiService deServiceFacade,
                                   final DEProperties deProperties,
                                   final AppTemplateAutoBeanFactory factory,
                                   final AppTemplateUtils appTemplateUtils,
                                   final DEClientConstants deClientConstants,
                                   final JsonUtil jsonUtil) {
        this.deServiceFacade = deServiceFacade;
        this.deProperties = deProperties;
        this.factory = factory;
        this.appTemplateUtils = appTemplateUtils;
        this.deClientConstants = deClientConstants;
        this.jsonUtil = jsonUtil;
    }

    @Override
    public void cmdLinePreview(AppTemplate at, DECallback<String> callback) {
        String address = APPS + "/" + deClientConstants.deSystemId() + "/arg-preview";

        AppTemplate cleaned = doCmdLinePreviewCleanup(at);
        // SS: Service wont accept string values for dates
        cleaned.setEditedDate(null);
        Splittable split = appTemplateToSplittable(cleaned);
        String payload = split.getPayload();
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, payload);
        deServiceFacade.getServiceData(wrapper, callback);
    }

    @Override
    public void getAppTemplate(HasQualifiedId appId, DECallback<AppTemplate> callback) {
        String address = APPS + "/" + appId.getSystemId() + "/" + appId.getId();
        HashMap<String, String> mdcMap = Maps.newHashMap();
        mdcMap.put(METRIC_TYPE_KEY, APP_EVENT);
        mdcMap.put(APP_ID, appId.getId());
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deServiceFacade.getServiceData(wrapper,
                                       mdcMap,
                                       new AppTemplateCallbackConverter(factory, callback));
    }

    @Override
    public AppTemplateAutoBeanFactory getAppTemplateFactory() {
        return factory;
    }

    @Override
    public void getAppTemplateForEdit(HasQualifiedId appId, DECallback<AppTemplate> callback) {
        String address = APPS + "/" + appId.getSystemId() + "/" + appId.getId() + "/ui";
        HashMap<String, String> mdcMap = Maps.newHashMap();
        mdcMap.put(METRIC_TYPE_KEY, APP_EVENT);
        mdcMap.put(APP_ID, appId.getId());
        ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
        deServiceFacade.getServiceData(wrapper,
                                       mdcMap,
                                       new AppTemplateCallbackConverter(factory, callback));
    }

    @Override
    public void getDataSources(DECallback<List<DataSource>> callback) {
        if (!dataSourceList.isEmpty()) {
            callback.onSuccess(dataSourceList);
        } else {
            enqueueDataSourceCallback(callback);
        }
    }

    @Override
    public void getFileInfoTypes(DECallback<List<FileInfoType>> callback) {
        if (!fileInfoTypeList.isEmpty()) {
            callback.onSuccess(fileInfoTypeList);
        } else {
            enqueueFileInfoTypeCallback(callback);
        }
    }

    @Override
    public void getReferenceGenomes(DECallback<List<ReferenceGenome>> callback) {
        if (!refGenList.isEmpty()) {
            callback.onSuccess(refGenList);
        } else {
            enqueueRefGenomeCallback(callback);
        }
    }

    @Override
    public void launchAnalysis(AppTemplate at, JobExecution je, DECallback<AnalysisSubmissionResponse> callback) {
        String address = ANALYSES;
        Splittable assembledPayload =
                AnalysisSubmissionUtil.assembleLaunchAnalysisPayload(appTemplateUtils, at, je);
        HashMap<String, String> mdcMap = Maps.newHashMap();
        mdcMap.put(METRIC_TYPE_KEY, APP_EVENT);
        mdcMap.put(APP_ID, at.getId());
        mdcMap.put(ServiceFacadeLoggerConstants.ANALYSIS_NAME, je.getName());
        mdcMap.put(ServiceFacadeLoggerConstants.ANALYSIS_OUTPUT_DIR, je.getOutputDirectory());

        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, assembledPayload.getPayload());
        deServiceFacade.getServiceData(wrapper, mdcMap, new DECallbackConverter<String, AnalysisSubmissionResponse>(callback) {
            @Override
            protected AnalysisSubmissionResponse convertFrom(String payload) {
                return AutoBeanCodex.decode(factory, AnalysisSubmissionResponse.class, payload).as();
            }
        });
        IntercomFacade.trackEvent(TrackingEventType.JOB_LAUNCHED,assembledPayload);
    }

    @Override
    public void rerunAnalysis(String analysisId, String appId, DECallback<AppTemplate> callback) {
        String address = ANALYSES + "/" + analysisId + "/relaunch-info";
        HashMap<String, String> mdcMap = Maps.newHashMap();
        mdcMap.put(METRIC_TYPE_KEY, APP_EVENT);
        mdcMap.put(APP_ID, appId);
        mdcMap.put(ANALYSIS_ID, analysisId);

        ServiceCallWrapper wrapper = new ServiceCallWrapper(GET, address);

        deServiceFacade.getServiceData(wrapper,
                                       mdcMap,
                                       new AppTemplateCallbackConverter(factory, callback));
    }

    @Override
    public void saveAndPublishAppTemplate(AppTemplate at, DECallback<AppTemplate> callback) {
        String address = APPS + "/" + at.getSystemId() + "/" + at.getId();
        Splittable split = appTemplateToSplittable(at);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(PUT, address, split.getPayload());
        deServiceFacade.getServiceData(wrapper, new AppTemplateCallbackConverter(factory, callback));
    }

    @Override
    public void createAppTemplate(AppTemplate at, DECallback<AppTemplate> callback) {
        String address = APPS + "/" + deClientConstants.deSystemId();
        Splittable split = appTemplateToSplittable(at);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(POST, address, split.getPayload());
        deServiceFacade.getServiceData(wrapper, new AppTemplateCallbackConverter(factory, callback));
    }

    @Override
    public void updateAppLabels(AppTemplate at, DECallback<AppTemplate> callback) {
        String address = APPS +  "/" + at.getSystemId() + "/" + at.getId();
        Splittable split = appTemplateToSplittable(at);
        ServiceCallWrapper wrapper = new ServiceCallWrapper(PATCH, address, split.getPayload());
        deServiceFacade.getServiceData(wrapper, new AppTemplateCallbackConverter(factory, callback));
    }

    Splittable appTemplateToSplittable(AppTemplate at) {
        AutoBean<AppTemplate> autoBean = AutoBeanUtils.getAutoBean(at);
        Splittable ret = AutoBeanCodex.encode(autoBean);
        if (at.getTools() != null && at.getTools().size() > 0) {
            Splittable tools = StringQuoter.createIndexed();
            for (Tool t : at.getTools()) {
                AutoBean<Tool> toolBean = AutoBeanUtils.getAutoBean(t);
                if (toolBean != null) {
                    Splittable sp = AutoBeanCodex.encode(toolBean);
                    sp.assign(tools, tools.size());
                }
            }
            tools.assign(ret, "tools");

        }
        // JDS Convert Argument.getValue() which contain any selected/checked *Selection types to only
        // contain their value.
        for (ArgumentGroup ag : at.getArgumentGroups()) {
            for (Argument arg : ag.getArguments()) {
                if (arg.getType().equals(ArgumentType.TreeSelection)) {
                    if ((arg.getSelectionItems() != null) && (arg.getSelectionItems().size() == 1)) {
                        SelectionItemGroup sig = appTemplateUtils.selectionItemToSelectionItemGroup(arg.getSelectionItems().get(0));
                        Splittable split = appTemplateUtils.getSelectedTreeItemsAsSplittable(sig);
                        arg.setValue(split);
                    }
                }

            }
        }
        LOG.fine("template from bean-->" + ret.getPayload() + "");
        return ret;
    }


    AppTemplate doCmdLinePreviewCleanup(AppTemplate templateToClean) {
        AppTemplate copy = appTemplateUtils.copyAppTemplate(templateToClean);
        // JDS Transform any Argument's value which contains a full SelectionItem obj to the
        // SelectionItem's value
        for (ArgumentGroup ag : copy.getArgumentGroups()) {
            for (Argument arg : ag.getArguments()) {
                if (appTemplateUtils.isSimpleSelectionArgumentType(arg.getType())) {

                    if ((arg.getValue() != null) && arg.getValue().isKeyed() && !arg.getValue().isUndefined("value")) {
                        arg.setValue(arg.getValue().get("value"));
                    } else {
                        arg.setValue(null);
                    }
                } else if (arg.getType().equals(ArgumentType.TreeSelection)) {
                    if ((arg.getSelectionItems() != null) && (arg.getSelectionItems().size() == 1)) {
                        SelectionItemGroup sig = appTemplateUtils.selectionItemToSelectionItemGroup(arg.getSelectionItems().get(0));
                        List<SelectionItem> siList = appTemplateUtils.getSelectedTreeItems(sig);
                        String retVal = "";
                        for (SelectionItem si : siList) {
                            if (si.getValue() != null) {
                                retVal += si.getValue() + " ";
                            }
                        }
                        arg.setValue(StringQuoter.create(retVal.trim()));
                    }
                } else if (arg.getType().equals(ArgumentType.EnvironmentVariable)) {
                    // Exclude environment variables from the command line
                    arg.setValue(null);
                    arg.setName("");
                } else if (appTemplateUtils.isDiskResourceOutputType(arg.getType())) {
                    if (arg.getFileParameters().isImplicit()) {
                        arg.setValue(null);
                        arg.setName("");
                    }
                } else if (appTemplateUtils.isDiskResourceArgumentType(arg.getType())) {
                    Splittable valSplit = StringQuoter.createSplittable();
                    StringQuoter.create("fakeId").assign(valSplit, "id");
                    StringQuoter.create("/iplant/fake/file").assign(valSplit, "path");
                    arg.setValue(valSplit);
                }
            }
        }

        return copy;
    }

    private void enqueueDataSourceCallback(final DECallback<List<DataSource>> callback) {
        if (dataSourceQueue.isEmpty()) {
            String address = DATA_SOURCES;
            ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
            deServiceFacade.getServiceData(wrapper, new AppsCallback<String>() {
                @Override
                public void onFailure(Integer statusCode, Throwable caught) {
                    callback.onFailure(statusCode, caught);
                }

                @Override
                public void onSuccess(String result) {
                    DataSourceList dsList = AutoBeanCodex.decode(factory, DataSourceList.class, result).as();
                    dataSourceList.clear();
                    dataSourceList.addAll(dsList.getDataSources());

                    while (!dataSourceQueue.isEmpty()) {
                        dataSourceQueue.remove().onSuccess(dataSourceList);
                    }
                }
            });

        }
        dataSourceQueue.add(callback);
    }

    private void enqueueFileInfoTypeCallback(final DECallback<List<FileInfoType>> callback) {
        if (fileInfoTypeQueue.isEmpty()) {
            String address = FILE_INFO_TYPES;
            ServiceCallWrapper wrapper = new ServiceCallWrapper(address);

            deServiceFacade.getServiceData(wrapper, new AppsCallback<String>() {

                @Override
                public void onFailure(Integer statusCode, Throwable caught) {
                    callback.onFailure(statusCode, caught);
                }

                @Override
                public void onSuccess(String result) {
                    FileInfoTypeList fitListWrapper = AutoBeanCodex.decode(factory, FileInfoTypeList.class, result).as();

                    fileInfoTypeList.clear();
                    fileInfoTypeList.addAll(fitListWrapper.getFileInfoTypes());

                    while (!fileInfoTypeQueue.isEmpty()) {
                        fileInfoTypeQueue.remove().onSuccess(fileInfoTypeList);
                    }
                }
            });
        }
        fileInfoTypeQueue.add(callback);

    }

    private void enqueueRefGenomeCallback(final DECallback<List<ReferenceGenome>> callback) {
        if (refGenQueue.isEmpty()) {
            String address = REFERENCE_GENOMES;
            ServiceCallWrapper wrapper = new ServiceCallWrapper(address);
            deServiceFacade.getServiceData(wrapper, new AppsCallback<String>() {
                @Override
                public void onFailure(Integer statusCode, Throwable caught) {
                    callback.onFailure(statusCode, caught);
                }

                @Override
                public void onSuccess(String result) {
                    ReferenceGenomeList rgList = AutoBeanCodex.decode(factory, ReferenceGenomeList.class, result).as();
                    refGenList.clear();
                    refGenList.addAll(rgList.getReferenceGenomes());

                    while (!refGenQueue.isEmpty()) {
                        refGenQueue.remove().onSuccess(refGenList);
                    }
                }
            });

        }
        refGenQueue.add(callback);
    }

}
