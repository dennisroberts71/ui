/**
 *
 */
package org.iplantc.de.collaborators.client.util;

import org.iplantc.de.client.models.collaborators.Subject;
import org.iplantc.de.collaborators.client.events.UserSearchResultSelected;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent.BeforeLoadHandler;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * @author sriram
 *
 */
public class UserSearchField implements IsWidget,
                                        UserSearchResultSelected.HasUserSearchResultSelectedEventHandlers {

    public class UsersLoadConfig extends PagingLoadConfigBean {
        private String query;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }
    }

    public interface UserSearchFieldAppearance {
        void render(Cell.Context context, Subject subject, String searchTerm, SafeHtmlBuilder sb);

        String searchCollab();

        String collaboratorDisplayName(Subject c);
    }

    private final UserSearchRPCProxy searchProxy;
    private ComboBox<Subject> combo;
    private ListView<Subject, Subject> view;
    private UserSearchFieldAppearance appearance;
    private HandlerManager handlerManager;
    private String tag;

    @Inject
    public UserSearchField(UserSearchFieldAppearance appearance,
                           UserSearchRPCProxy searchProxy) {
        this.appearance = appearance;
        this.searchProxy = searchProxy;
        PagingLoader<UsersLoadConfig, PagingLoadResult<Subject>> loader = buildLoader();

        ListStore<Subject> store = buildStore();
        loader.addLoadHandler(new LoadResultListStoreBinding<UsersLoadConfig, Subject, PagingLoadResult<Subject>>(
                store));

        view = buildView(store);

        ComboBoxCell<Subject> cell = buildComboCell(store, view);
        initCombo(loader, cell);
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private void initCombo(PagingLoader<UsersLoadConfig, PagingLoadResult<Subject>> loader,
            ComboBoxCell<Subject> cell) {
        combo = new ComboBox<Subject>(cell);
        combo.setLoader(loader);
        combo.setMinChars(3);
        combo.setWidth(250);
        combo.setHideTrigger(true);
        combo.setEmptyText(appearance.searchCollab());
        combo.addSelectionHandler(new SelectionHandler<Subject>() {

            @Override
            public void onSelection(SelectionEvent<Subject> event) {
                Subject subject = combo.getListView().getSelectionModel().getSelectedItem();
                ensureHandlers().fireEvent(new UserSearchResultSelected(subject, tag));
                combo.clear();
            }
        });
    }

    private ComboBoxCell<Subject> buildComboCell(ListStore<Subject> store,
                                                 ListView<Subject, Subject> view) {
        ComboBoxCell<Subject> cell = new ComboBoxCell<Subject>(store,
                                                               new StringLabelProvider<Subject>() {

                    @Override
                    public String getLabel(Subject c) {
                        return appearance.collaboratorDisplayName(c);
                    }

                }, view) {
            @Override
            protected void onEnterKeyDown(Context context, Element parent, Subject value,
                    NativeEvent event, ValueUpdater<Subject> valueUpdater) {
                if (isExpanded()) {
                    super.onEnterKeyDown(context, parent, value, event, valueUpdater);
                }
            }

        };

        return cell;
    }

    private ListView<Subject, Subject> buildView(ListStore<Subject> store) {
        ListView<Subject, Subject> view = new ListView<Subject, Subject>(store, new IdentityValueProvider<Subject>());

        view.setCell(new AbstractCell<Subject>() {

            @Override
            public void render(com.google.gwt.cell.client.Cell.Context context, Subject value,
                    SafeHtmlBuilder sb) {
                appearance.render(context, value, searchProxy.getLastQueryText(), sb);
            }

        });
        return view;
    }

    private PagingLoader<UsersLoadConfig, PagingLoadResult<Subject>> buildLoader() {
        PagingLoader<UsersLoadConfig, PagingLoadResult<Subject>> loader = new PagingLoader<UsersLoadConfig, PagingLoadResult<Subject>>(
                searchProxy);
        loader.useLoadConfig(new UsersLoadConfig());
        loader.addBeforeLoadHandler(new BeforeLoadHandler<UsersLoadConfig>() {

            @Override
            public void onBeforeLoad(BeforeLoadEvent<UsersLoadConfig> event) {
                String query = combo.getText();
                if (query != null && !query.equals("")) {
                    event.getLoadConfig().setQuery(query);
                }

            }
        });
        return loader;
    }

    private ListStore<Subject> buildStore() {
        ListStore<Subject> store = new ListStore<Subject>(item -> item.getId());
        return store;
    }

    @Override
    public Widget asWidget() {
        return combo;
    }

    public void setViewDebugId(String debugId) {
        combo.setId(debugId);
    }

    @Override
    public HandlerRegistration addUserSearchResultSelectedEventHandler(UserSearchResultSelected.UserSearchResultSelectedEventHandler handler) {
        return ensureHandlers().addHandler(UserSearchResultSelected.TYPE, handler);
    }

    protected HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(this);
        }
        return handlerManager;
    }
}
