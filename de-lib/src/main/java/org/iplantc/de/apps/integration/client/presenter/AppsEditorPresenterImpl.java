package org.iplantc.de.apps.integration.client.presenter;

import org.iplantc.de.apps.client.events.AppSavedEvent;
import org.iplantc.de.apps.integration.client.events.ArgumentOrderSelected;
import org.iplantc.de.apps.integration.client.events.DeleteArgumentEvent;
import org.iplantc.de.apps.integration.client.events.DeleteArgumentEvent.DeleteArgumentEventHandler;
import org.iplantc.de.apps.integration.client.events.DeleteArgumentGroupEvent;
import org.iplantc.de.apps.integration.client.events.PreviewAppSelected;
import org.iplantc.de.apps.integration.client.events.PreviewJsonSelected;
import org.iplantc.de.apps.integration.client.events.SaveAppSelected;
import org.iplantc.de.apps.integration.client.events.UpdateCommandLinePreviewEvent;
import org.iplantc.de.apps.integration.client.presenter.visitors.DeleteArgumentGroup;
import org.iplantc.de.apps.integration.client.presenter.visitors.GatherAllEventProviders;
import org.iplantc.de.apps.integration.client.presenter.visitors.InitLabelOnlyEditMode;
import org.iplantc.de.apps.integration.client.presenter.visitors.InitializeArgumentEventManagement;
import org.iplantc.de.apps.integration.client.presenter.visitors.InitializeArgumentGroupEventManagement;
import org.iplantc.de.apps.integration.client.presenter.visitors.InitializeDragAndDrop;
import org.iplantc.de.apps.integration.client.presenter.visitors.RegisterEventHandlers;
import org.iplantc.de.apps.integration.client.view.AppEditorToolbar;
import org.iplantc.de.apps.integration.client.view.AppsEditorView;
import org.iplantc.de.apps.integration.client.view.dialogs.CommandLineOrderingDialog;
import org.iplantc.de.apps.integration.shared.AppIntegrationModule;
import org.iplantc.de.apps.widgets.client.events.ArgumentAddedEvent;
import org.iplantc.de.apps.widgets.client.events.ArgumentAddedEvent.ArgumentAddedEventHandler;
import org.iplantc.de.apps.widgets.client.events.ArgumentGroupAddedEvent;
import org.iplantc.de.apps.widgets.client.events.ArgumentGroupAddedEvent.ArgumentGroupAddedEventHandler;
import org.iplantc.de.apps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.de.apps.widgets.client.view.AppLaunchPreviewView;
import org.iplantc.de.apps.widgets.client.view.AppLaunchView.RenameWindowHeaderCommand;
import org.iplantc.de.client.events.EventBus;
import org.iplantc.de.client.models.IsMinimizable;
import org.iplantc.de.client.models.apps.integration.AppTemplate;
import org.iplantc.de.client.models.apps.integration.AppTemplateAutoBeanFactory;
import org.iplantc.de.client.models.apps.integration.Argument;
import org.iplantc.de.client.models.apps.integration.ArgumentGroup;
import org.iplantc.de.client.models.apps.integration.ArgumentType;
import org.iplantc.de.client.models.apps.integration.FileParameters;
import org.iplantc.de.client.models.errorHandling.SimpleServiceError;
import org.iplantc.de.client.models.tool.Tool;
import org.iplantc.de.client.services.AppTemplateServices;
import org.iplantc.de.client.services.UUIDServiceAsync;
import org.iplantc.de.client.util.AppTemplateUtils;
import org.iplantc.de.client.util.JsonUtil;
import org.iplantc.de.commons.client.ErrorHandler;
import org.iplantc.de.commons.client.info.ErrorAnnouncementConfig;
import org.iplantc.de.commons.client.info.IplantAnnouncer;
import org.iplantc.de.commons.client.info.SuccessAnnouncementConfig;
import org.iplantc.de.commons.client.views.dialogs.IPlantDialog;
import org.iplantc.de.commons.client.views.dialogs.IplantInfoBox;
import org.iplantc.de.shared.AppsCallback;
import org.iplantc.de.shared.AsyncProviderWrapper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author jstroot
 * 
 */
public class AppsEditorPresenterImpl implements AppsEditorView.Presenter,
                                                DeleteArgumentEventHandler,
                                                ArgumentAddedEventHandler,
                                                ArgumentGroupAddedEventHandler {

    class DoSaveCallback extends AppsCallback<AppTemplate> {
        private final AsyncCallback<Void> onSaveCallback;
        private final IplantAnnouncer announcer1;
        private final EventBus eventBus1;
        private final AppTemplate at;
        private final RenameWindowHeaderCommand renameCommand;
        private final AppsEditorPresenterImpl presenterImpl;
        private final String successfulSaveMsg;
        private final String failedSaveMsg;

        DoSaveCallback(AsyncCallback<Void> onSaveCallback,
                       final AppTemplate appTemplate,
                       final IplantAnnouncer announcer,
                       final EventBus eventBus,
                       final RenameWindowHeaderCommand renameCmd,
                       final AppsEditorPresenterImpl presenterImpl,
                       final String successfulSaveMsg,
                       final String failedSaveMsg,
                       final boolean ispublic) {
            this.onSaveCallback = onSaveCallback;
            this.at = appTemplate;
            this.announcer1 = announcer;
            this.eventBus1 = eventBus;
            this.renameCommand = renameCmd;
            this.presenterImpl = presenterImpl;
            this.successfulSaveMsg = successfulSaveMsg;
            this.failedSaveMsg = failedSaveMsg;
            at.setPublic(ispublic);
        }

        @Override
        public void onFailure(Integer statusCode, Throwable caught) {
            AutoBean<SimpleServiceError> error = AutoBeanCodex.decode(appTemplateFactory,
                                                                      SimpleServiceError.class,
                                                                      caught.getMessage());
            announcer.schedule(new ErrorAnnouncementConfig(
                    failedSaveMsg + " " + error.as().getReason()));
            if (onSaveCallback != null) {
                onSaveCallback.onFailure(caught);
            }
        }

        @Override
        public void onSuccess(AppTemplate savedTemplate) {
            String atId = at.getId();
            if (Strings.isNullOrEmpty(atId)) {
                at.setId(savedTemplate.getId());
            } else if (!atId.equalsIgnoreCase(savedTemplate.getId())) {
                // JDS There was an app ID, but now we are changing it. This is undesired.
                LOG.warning("Attempt to change app ID from \"" + atId + "\" to \"" + savedTemplate.getId() + "\"");
            }
            // Update editor with new template from server
            savedTemplate.setPublic(at.isPublic());
            presenterImpl.view.getEditorDriver().edit(savedTemplate);
            presenterImpl.lastSave = copyAppTemplate(presenterImpl.flushViewAndClean());


            if (renameCommand != null) {
                renameCommand.setAppTemplate(presenterImpl.lastSave);
                renameCommand.execute();
            }
            eventBus1.fireEvent(new AppSavedEvent(presenterImpl.lastSave));

            announcer1.schedule(new SuccessAnnouncementConfig(successfulSaveMsg));
            if (onSaveCallback != null) {
                onSaveCallback.onSuccess(null);
            }
        }

        AppTemplate copyAppTemplate(AppTemplate templateToCopy) {
            return appTemplateUtils.copyAppTemplate(templateToCopy);
        }
    }
    /**
     * This dialog is used when the user attempts to close the view when the current AppTemplate contains
     * errors
     * 
     * @author jstroot
     * 
     */
    private final class ContainsErrorsOnHideDialog extends IplantInfoBox {
        private final HandlerRegistration beforeHideHndlrReg;
        private final Component component;

        private ContainsErrorsOnHideDialog(AppsEditorView.AppsEditorViewAppearance appearance,
                                           Component component,
                                           HandlerRegistration beforeHideHndlrReg) {
            super(appearance.warning(), appearance.appContainsErrorsPromptToContinue());
            this.component = component;
            this.beforeHideHndlrReg = beforeHideHndlrReg;
            setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
            setIcon(appearance.errorIcon());
        }

        @Override
        protected void onButtonPressed(TextButton button) {
            if (button == getButtonBar().getItemByItemId(PredefinedButton.YES.name())) {
                // JDS Abort current AppTemplate and hide.
                clearRegisteredHandlers();
                beforeHideHndlrReg.removeHandler();
                component.hide();
            }
            hide();
        }

    }
    /**
     * This dialog is used when the user is attempting to edit a new AppTemplate, but the existing
     * AppTemplate contains errors.
     * 
     * @author jstroot
     * 
     */
    private final class ContainsErrorsOnSwitchDialog extends IplantInfoBox {
        private final AppTemplate appTempl;
        private final HasOneWidget container;
        private final RenameWindowHeaderCommand renameHeaderCmd;

        private ContainsErrorsOnSwitchDialog(AppsEditorView.AppsEditorViewAppearance appearance,
                                             HasOneWidget container,
                                             AppTemplate appTemplate,
                                             RenameWindowHeaderCommand renameCmd) {
            super(appearance.warning(), appearance.appContainsErrorsPromptToContinue());
            this.container = container;
            this.appTempl = appTemplate;
            this.renameHeaderCmd = renameCmd;
            setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
            setIcon(appearance.errorIcon());
        }

        @Override
        protected void onButtonPressed(TextButton button) {
            if (button == getButtonBar().getItemByItemId(PredefinedButton.YES.name())) {
                // JDS Abort current AppTemplate and switch.
                AppsEditorPresenterImpl.this.appTemplate = appTempl;
                AppsEditorPresenterImpl.this.go(container);
                if (renameHeaderCmd != null) {
                    renameHeaderCmd.execute();
                }
            }
            hide();
        }
    }

    /**
     * This dialog is used when the user attempts to close the view or click "Save" when the current
     * AppTemplate contains unsaved changes.
     * 
     * @author jstroot
     * 
     */
    private final class PromptForSaveDialog extends IplantInfoBox {
        private final HandlerRegistration beforeHideHndlrReg;
        private final Component component;

        private PromptForSaveDialog(AppsEditorView.AppsEditorViewAppearance appearance,
                                    Component component,
                                    HandlerRegistration beforeHideHndlrReg) {
            super(appearance.save(), appearance.unsavedChanges());
            setIcon(appearance.questionIcon());
            setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
            this.component = component;
            TextButton gtb = new TextButton();
            gtb.setEnabled(false);
            TextButton xtb = new TextButton();
            xtb.setEnabled(true);
            this.beforeHideHndlrReg = beforeHideHndlrReg;
        }

        @Override
        protected void onButtonPressed(TextButton button) {
            if (button == getButtonBar().getItemByItemId(PredefinedButton.YES.name())) {
                // JDS Do save and let window close
                beforeHideHndlrReg.removeHandler();
                clearRegisteredHandlers();
                doOnSaveClicked(null);
                component.hide();
            } else if (button == getButtonBar().getItemByItemId(PredefinedButton.NO.name())) {
                // JDS Just let window close
                clearRegisteredHandlers();
                beforeHideHndlrReg.removeHandler();
                component.hide();
            }
            hide();
            appTemplate = null;
        }
    }
    /**
     * This dialog is used when the user is attempting to edit a new AppTemplate, but the current
     * AppTemplate has unsaved changes.
     * 
     * @author jstroot
     * 
     */
    private final class PromptForSaveThenSwitchDialog extends IplantInfoBox {
        private final AppTemplate appTempl;
        private final HasOneWidget container;
        private final RenameWindowHeaderCommand renameHeaderCmd;

        private PromptForSaveThenSwitchDialog(AppsEditorView.AppsEditorViewAppearance appearance,
                                              HasOneWidget container,
                                              AppTemplate appTemplate,
                                              RenameWindowHeaderCommand renameCmd) {
            super(appearance.save(), appearance.unsavedChanges());
            setIcon(appearance.questionIcon());
            setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
            this.container = container;
            this.appTempl = appTemplate;
            this.renameHeaderCmd = renameCmd;
        }

        @Override
        protected void onButtonPressed(TextButton button) {
            if (button == getButtonBar().getItemByItemId(PredefinedButton.YES.name())) {
                // Perform save, then switch.
                doOnSaveClicked(new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {/* Do Nothing */}

                    @Override
                    public void onSuccess(Void result) {
                        AppsEditorPresenterImpl.this.appTemplate = appTempl;
                        AppsEditorPresenterImpl.this.go(container);
                        if (renameHeaderCmd != null) {
                            renameHeaderCmd.execute();
                        }
                    }
                });
            } else if (button == getButtonBar().getItemByItemId(PredefinedButton.NO.name())) {
                // JDS Abort current changes to AppTemplate and switch.
                AppsEditorPresenterImpl.this.appTemplate = appTempl;
                AppsEditorPresenterImpl.this.go(container);
                if (renameHeaderCmd != null) {
                    renameHeaderCmd.execute();
                }
            }
            hide();
        }
    }

    private final AppTemplateUtils appTemplateUtils;
    private final List<HandlerRegistration> handlerRegistrations = Lists.newArrayList();
    private String baseID;

    public static native void doJsonFormattting(XElement textArea,String val,int width, int height) /*-{
		var myCodeMirror = $wnd.CodeMirror(textArea, {
			value : val,
			mode : {
				name : "javascript",
				json : true
			}
		});
		myCodeMirror.setOption("lineWrapping", false);
		myCodeMirror.setSize(width, height);
		myCodeMirror.setOption("readOnly", true);
    }-*/;
    private final AppsEditorView.AppsEditorViewAppearance appearance;
    private AppTemplate appTemplate;
    private final AppTemplateServices atService;
    private HandlerRegistration beforeHideHandlerRegistration;
    private final EventBus eventBus;
    private AppTemplate lastSave;

    private boolean onlyLabelEditMode = false;

    private boolean postEdit = false;

    private RenameWindowHeaderCommand renameCmd;

    private final UUIDServiceAsync uuidService;

    private final AppsEditorView view;
    private final IplantAnnouncer announcer;
    private final AppTemplateAutoBeanFactory appTemplateFactory;

    Logger LOG = Logger.getLogger(AppsEditorPresenterImpl.class.getName());

    @Inject Provider<AppLaunchPreviewView> previewViewProvider;
    @Inject AsyncProviderWrapper<CommandLineOrderingDialog> commandLineDialogProvider;

    @Inject
    AppsEditorPresenterImpl(final AppsEditorView view,
                            final EventBus eventBus,
                            final AppTemplateServices atService,
                            final UUIDServiceAsync uuidService,
                            final AppsEditorView.AppsEditorViewAppearance appearance,
                            final IplantAnnouncer announcer,
                            final AppTemplateUtils appTemplateUtils,
                            final AppTemplateAutoBeanFactory appTemplateFactory) {
        this.view = view;
        this.eventBus = eventBus;
        this.atService = atService;
        this.uuidService = uuidService;
        this.appearance = appearance;
        this.announcer = announcer;
        this.appTemplateUtils = appTemplateUtils;
        this.appTemplateFactory = appTemplateFactory;

        setUpHandlers(view);
    }

    void setUpHandlers(AppsEditorView view) {
        AppEditorToolbar toolbar = view.getToolbar();

        view.addDeleteArgumentGroupEventHandler(this);
        view.addUpdateCommandLinePreviewEventHandler(this);
        toolbar.addArgumentOrderSelectedHandler(this);
        toolbar.addPreviewAppSelectedHandler(this);
        toolbar.addPreviewJsonSelectedHandler(this);
        toolbar.addSaveAppSelectedHandler(this);
    }

    @Override
    public void doArgumentDelete(DeleteArgumentEvent event) {
        AutoBean<Argument> autoBean = AutoBeanUtils.getAutoBean(event.getArgumentToBeDeleted());

        // Remove all handlers store in the autobean
        if (autoBean.getTag(AppsEditorView.Presenter.HANDLERS) != null) {
            List<HandlerRegistration> handlerRegs = autoBean.getTag(AppsEditorView.Presenter.HANDLERS);
            for (HandlerRegistration hr : handlerRegs) {
                hr.removeHandler();
            }
        }
    }

    @Override
    public void doArgumentGroupDelete(DeleteArgumentGroupEvent event) {
        AppTemplate flush = view.flush();
        if (flush.getArgumentGroups().size() == 1) {
            announcer.schedule(new ErrorAnnouncementConfig(appearance.cannotDeleteLastArgumentGroup(), true, 3000));
            return;
        }
        AutoBean<ArgumentGroup> autoBean = AutoBeanUtils.getAutoBean(event.getArgumentGroupToBeDeleted());

        // Remove all handlers store in the autobean
        if (autoBean.getTag(AppsEditorView.Presenter.HANDLERS) != null) {
            List<HandlerRegistration> handlerRegs = autoBean.getTag(AppsEditorView.Presenter.HANDLERS);
            for (HandlerRegistration hr : handlerRegs) {
                hr.removeHandler();
            }
        }

        view.getEditorDriver().accept(new DeleteArgumentGroup(event.getArgumentGroupToBeDeleted(), appearance));
    }

    @Override
    public AppTemplate getAppTemplate() {
        return appTemplate != null ? flushViewAndClean() : null;
    }

    @Override
    public void go(HasOneWidget container) {
        clearRegisteredHandlers();

        boolean isPublic = appTemplate.isPublic() != null ? appTemplate.isPublic() : false;

        setLabelOnlyEditMode(isPublic);

        if (!isPublic) {
            checkForDeprecatedTools(appTemplate.getTools());
        }

        view.getEditorDriver().edit(appTemplate);
        view.onArgumentSelected(new ArgumentSelectedEvent(null));

        // Set the view's debug ID after the editor driver has processed edit(appTemplate) and initialized all the subEditors
        view.asWidget().ensureDebugId(baseID);

        /*
         * JDS Set postEdit to true to enable handling of ArgumentGroupAddedEvents and
         * ArgumentAddedEvents
         */
        postEdit = true;
        view.getEditorDriver().accept(new InitLabelOnlyEditMode(isLabelOnlyEditMode()));

        view.getEditorDriver().accept(new InitializeDragAndDrop(this));
        GatherAllEventProviders gatherAllEventProviders = new GatherAllEventProviders(appearance, this, this);
        view.getEditorDriver().accept(gatherAllEventProviders);
        final RegisterEventHandlers registerEventHandlers = new RegisterEventHandlers(this, this, this, gatherAllEventProviders);
        view.getEditorDriver().accept(registerEventHandlers);
        // Grab handler registrations related to this class
        this.handlerRegistrations.addAll(registerEventHandlers.getHandlerRegistrations());

        /*
         * JDS Make a copy so we can check for differences on exit.
         */
        lastSave = appTemplateUtils.copyAppTemplate(flushViewAndClean());

        updateCommandLinePreview(lastSave);
        if (container.getWidget() == null) {
            // JDS Only set widget if container has no widget.
            container.setWidget(view);
        }
    }
    
    @Override
    public void go(final HasOneWidget container, final AppTemplate appTemplate) {
        go(container, appTemplate, null);
    }

    @Override
    public void setViewDebugId(String baseID) {
        this.baseID = baseID;
    }

    @Override
    public void go(final HasOneWidget container, final AppTemplate appTemplate, final RenameWindowHeaderCommand renameCmd) {
        this.renameCmd = renameCmd;
        // If we are editing a new AppTemplate, and the current the current AppTemplate has unsaved changes
        if ((appTemplate != null) && (lastSave != null) && isEditorDirty() && !Strings.nullToEmpty(appTemplate.getId()).equals(Strings.nullToEmpty(lastSave.getId()))) {

            // JDS ScheduleDeferred to ensure that the dialog's show() method is called after any parent container's show() method.
            Scheduler.get().scheduleDeferred(new ScheduledCommand() {

                @Override
                public void execute() {
                    if (!isViewValid()) {
                        // JDS View has changes, but contains errors.
                        new ContainsErrorsOnSwitchDialog(appearance, container, appTemplate, renameCmd).show();
                    } else {
                        // JDS There are differences and form is valid, so prompt user to save.
                        new PromptForSaveThenSwitchDialog(appearance, container, appTemplate, renameCmd).show();
                    }

                }
            });
        } else {
            this.appTemplate = appTemplate;
            go(container);
            if (renameCmd != null) {
                renameCmd.execute();
            }
        }
    }

    @Override
    public boolean isEditorDirty() {
        try {
            // Determine if there are any changes, variables are broken out for readability
            AutoBean<AppTemplate> lastSaveAb = AutoBeanUtils.getAutoBean(lastSave);
            AutoBean<AppTemplate> currentAb = AutoBeanUtils.getAutoBean(appTemplateUtils.copyAppTemplate(flushViewAndClean()));
            String lastSavePayload = AutoBeanCodex.encode(lastSaveAb).getPayload();
            String currentPayload = AutoBeanCodex.encode(currentAb).getPayload();
            return !lastSavePayload.equals(currentPayload);
        } catch (IllegalStateException e) {
            /*
             * JDS This is expected to occur when 'flush()' is called when 'edit()' was not called first.
             */
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean isLabelOnlyEditMode() {
        return onlyLabelEditMode;
    }

    @Override
    public void onArgumentAdded(ArgumentAddedEvent event) {
        if (!postEdit) {
            return;
        }

        view.getEditorDriver().accept(new InitializeArgumentEventManagement(event.getArgumentEditor()));
        view.asWidget().ensureDebugId(baseID);
    }

    @Override
    public void onArgumentGroupAdded(ArgumentGroupAddedEvent event) {
        if (!postEdit) {
            return;
        }

        AutoBean<ArgumentGroup> autoBean = AutoBeanUtils.getAutoBean(event.getArgumentGroup());
        view.getEditorDriver().accept(new InitializeArgumentGroupEventManagement(appearance, autoBean, event.getArgumentGroupEditor(), this, this));
        event.getArgumentGroupEditor().addArgumentAddedEventHandler(this);
        event.getArgumentGroupEditor().addArgumentGroupSelectedHandler(view);
        view.asWidget().ensureDebugId(baseID);
    }

    @Override
    public void onArgumentOrderSelected(ArgumentOrderSelected event) {
        AppTemplate flushRawApp = view.getEditorDriver().flush();
        final List<Argument> allTemplateArguments = getAllTemplateArguments(flushRawApp);
        uuidService.getUUIDs(allTemplateArguments.size(), new AsyncCallback<ArrayList<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                commandLineDialogProvider.get(new AsyncCallback<CommandLineOrderingDialog>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                    }

                    @Override
                    public void onSuccess(CommandLineOrderingDialog dialog) {
                        List<Argument> argumentList =
                                createArgumentList(allTemplateArguments, result);
                        dialog.addOkButtonSelectHandler(handler -> updateCommandLinePreview(view.flush()));
                        dialog.show(argumentList);

                    }
                });

            }
        });
    }

    List<Argument> createArgumentList(List<Argument> arguments, ArrayList<String> uuids) {
        List<Argument> orderedArguments = Lists.newArrayList();
        for (Argument arg : arguments) {
            if (Strings.isNullOrEmpty(arg.getId())) {
                if (!uuids.isEmpty()) {
                    arg.setId(uuids.remove(0));
                }
            }
            if (orderingRequired(arg)) {
                Integer order = arg.getOrder();

                // JDS If the order is null or 0, set it to a number higher than the length of the
                // list to ensure that already numbered arguments are sorted into their appropriate
                // places
                if ((order == null) || (order <= 0)) {
                    arg.setOrder(arguments.size() + 1);
                }

                orderedArguments.add(arg);
            }
        }
        return orderedArguments;
    }



    @Override
    public void onBeforeHide(final BeforeHideEvent event) {
        if ((event.getSource() instanceof IsMinimizable) && ((IsMinimizable)event.getSource()).isMinimized()) {
            return;
        }
        if (isEditorDirty()) {
            event.setCancelled(true);
            final Component component = event.getSource();
            if (isViewValid()) {
                // JDS There are differences and form is valid, so prompt user to save.
                new PromptForSaveDialog(appearance, component, beforeHideHandlerRegistration).show();
            } else {
                new ContainsErrorsOnHideDialog(appearance, component,
                        beforeHideHandlerRegistration).show();
            }
        } else {
            clearRegisteredHandlers();
            appTemplate = null;
        }
    }

    @Override
    public void onPreviewJsonSelected(PreviewJsonSelected event) {
        AppTemplate appTemplate = flushViewAndClean();
        Splittable split = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(appTemplateUtils.removeEmptyGroupArguments(appTemplate)));
        IPlantDialog dlg = new IPlantDialog();
        dlg.setHeading(appearance.previewJSON());
        dlg.setPredefinedButtons(PredefinedButton.OK);
        dlg.setSize("500", "350");
        dlg.setResizable(false);
        dlg.show();
        dlg.ensureDebugId(AppIntegrationModule.Ids.PREVIEW_JSON_DLG);
        dlg.getOkButton().ensureDebugId(AppIntegrationModule.Ids.PREVIEW_JSON_DLG + AppIntegrationModule.Ids.OK);
        doJsonFormattting(dlg.getBody(),
                          JsonUtil.prettyPrint(split.getPayload(), null, 4),
                          dlg.getBody().getOffsetWidth(),
                          dlg.getBody().getOffsetHeight());
        dlg.forceLayout();
    }

    @Override
    public void onPreviewAppSelected(PreviewAppSelected event) {
        AppLaunchPreviewView preview = previewViewProvider.get();
        preview.edit(flushViewAndClean(), null);
        preview.show();
        preview.asWidget().ensureDebugId(AppIntegrationModule.Ids.PREVIEW_APP_DLG);
    }

    @Override
    public void onSaveAppSelected(SaveAppSelected event) {
        if (isViewValid()) {
            doOnSaveClicked(null);
        } else {
            IplantInfoBox errorsInfo = new IplantInfoBox(appearance.warning(),
                    hasTemplateError() ? appearance.appMissingInfo() : appearance.appContainsErrorsUnableToSave());
            errorsInfo.setIcon(MessageBox.ICONS.error());
            errorsInfo.show();
        }
    }

    boolean hasTemplateError() {
        return view.getAppTemplatePropertyEditor().hasErrors();
    }

    @Override
    public void onUpdateCommandLinePreview(UpdateCommandLinePreviewEvent event) {
        updateCommandLinePreview(view.getEditorDriver().flush());
    }

    @Override
    public boolean orderingRequired(Argument arg) {
        if (arg == null) {
            return false;
        }
        ArgumentType type = arg.getType();
        if (type.equals(ArgumentType.Info) || type.equals(ArgumentType.EnvironmentVariable)) {
            return false;
        }
    
        FileParameters dataObject = arg.getFileParameters();
        boolean isOutput = ArgumentType.FileOutput.equals(type)
                               || ArgumentType.FolderOutput.equals(type)
                               || ArgumentType.MultiFileOutput.equals(type);
        return !(isOutput && (dataObject != null) && dataObject.isImplicit());
    }

    @Override
    public void setBeforeHideHandlerRegistration(HandlerRegistration hr) {
        this.beforeHideHandlerRegistration = hr;
    }

    @Override
    public void setLabelOnlyEditMode(boolean onlyLabelEditMode) {
        this.onlyLabelEditMode = onlyLabelEditMode;
        view.setOnlyLabelEditMode(onlyLabelEditMode);
    }

    private void doOnSaveClicked(AsyncCallback<Void> onSaveCallback) {
        AppTemplate toBeSaved = flushViewAndClean();
        doSave(toBeSaved, onSaveCallback);
    }

    private void doSave(AppTemplate toBeSaved, final AsyncCallback<Void> onSaveCallback) {
        // JDS Make a copy so we can check for differences on exit
        lastSave = appTemplateUtils.removeDateFields((appTemplateUtils.copyAppTemplate(toBeSaved)));
        
        DoSaveCallback saveCallback = new DoSaveCallback(onSaveCallback,
                                                         appTemplate,
                                                         announcer,
                                                         eventBus,
                                                         renameCmd,
                                                         this,
                                                         appearance.saveSuccessful(),
                                                         appearance.unableToSave(),
                                                         appTemplate.isPublic());

        // service requires that this key is NOT present in json
        lastSave.setPublic(null);
        if (isLabelOnlyEditMode()) {
            atService.updateAppLabels(lastSave, saveCallback);
        } else {
            if (Strings.isNullOrEmpty(lastSave.getId())) {
                atService.createAppTemplate(lastSave, saveCallback);
            } else {
                atService.saveAndPublishAppTemplate(lastSave, saveCallback);
            }
        }
    
    }

    AppTemplate flushViewAndClean() {
        return appTemplateUtils.removeEmptyGroupArguments(view.flush());
    }

    private List<Argument> getAllTemplateArguments(AppTemplate at) {
        if (at == null) {
            return Collections.emptyList();
        }
        List<Argument> args = Lists.newArrayList();
        for (ArgumentGroup ag : at.getArgumentGroups()) {
             args.addAll(ag.getArguments());
        }
        return args;
    }

    private boolean isViewValid() {
        flushViewAndClean();
        return !view.hasErrors();
    }

    private void updateCommandLinePreview(final AppTemplate at) {
        AppTemplate cleaned = appTemplateUtils.removeEmptyGroupArguments(at);

        // do not send id for new App Template
        if (Strings.isNullOrEmpty(cleaned.getId())) {
            cleaned.setId(null);
        }

        // do not send integration dates
        cleaned.setPublishedDate(null);

        atService.cmdLinePreview(cleaned, new AppsCallback<String>() {

            @Override
            public void onFailure(Integer statusCode, Throwable caught) {
                ErrorHandler.post(caught);
            }

            @Override
            public void onSuccess(String result) {
                Splittable split = StringQuoter.split(result);
                String cmdLinePrev = split.get("params").asString();

                /*
                 * JDS If the given AppTemplate has a valid DeployedComponent, prepend the
                 * DeployedComponent name to the command line preview
                 */
                final List<Tool> tools = at.getTools();
                if ((tools != null)
                        && !tools.isEmpty()
                        && (tools.get(0) != null)) {
                    cmdLinePrev = tools.get(0).getName() + " " + cmdLinePrev;
                }
                view.setCmdLinePreview(cmdLinePrev);
            }
        });
    }

    void clearRegisteredHandlers(){
        for(HandlerRegistration hr : handlerRegistrations){
            hr.removeHandler();
        }
        handlerRegistrations.clear();

        // If 'lastSave' is null, then the view's editorDrive.edit() has not been called yet
        if(lastSave == null){
            return;
        }
        // Clean any handlers from the AppTemplate
        final AutoBean<Object> autoBean = AutoBeanUtils.getAutoBean(view.flush());
        final Object tag;
        if (autoBean != null) {
            tag = autoBean.getTag(AppsEditorView.Presenter.HANDLERS);
            if ((tag != null)
                    && (tag instanceof List)) {
                @SuppressWarnings("unchecked") List<HandlerRegistration> handlerRegistrationList = (List<HandlerRegistration>) tag;
                for(HandlerRegistration hr : handlerRegistrationList){
                    hr.removeHandler();
                }
            }
        }
    }

    private void checkForDeprecatedTools(final List<Tool> tools) {
        if (tools != null && tools.size() > 0) {
            for (Tool t : tools) {
                if (t!= null && t.isDeprecated()) {
                    IplantInfoBox errorsInfo =
                            new IplantInfoBox(appearance.warning(), appearance.appUsesDeprecatedTools());
                    errorsInfo.setIcon(MessageBox.ICONS.warning());
                    errorsInfo.show();
                    return;

                }
            }
        }
    }
}
