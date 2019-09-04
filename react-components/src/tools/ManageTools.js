/**
 * @author aramsey
 */
import React, { Fragment, useEffect, useState } from "react";

import ids from "./ids";
import messages from "./messages";
import PERMISSION from "../models/Permission";
import styles from "./styles";

import {
    build,
    EmptyTable,
    EnhancedTableHead,
    formatMessage,
    getMessage,
    LoadingMask,
    SearchField,
    TablePaginationActions,
    withI18N,
} from "@cyverse-de/ui-lib";
import {
    Button,
    Checkbox,
    IconButton,
    Menu,
    MenuItem,
    Select,
    Table,
    TableBody,
    TableCell,
    TablePagination,
    TableRow,
    Toolbar,
    withStyles,
} from "@material-ui/core";
import {
    InfoOutlined,
    Menu as MenuIcon,
    Refresh,
    Share,
} from "@material-ui/icons";
import PropTypes from "prop-types";
import { injectIntl } from "react-intl";

const TOOL_FILTER_VALUES = {
    ALL: "ALL",
    MY_TOOLS: "MY_TOOLS",
    PUBLIC: "PUBLIC",
};

const PAGING_OPTIONS = [25, 50, 100, 200, 500, 1000];

function ManageTools(props) {
    const {
        parentId,
        toolList,
        loading,
        presenter,
        searchTerm,
        order,
        orderBy,
        rowsPerPage,
        page,
        intl,
    } = props;

    const [state, setHookState] = useState({
        selectedTool: null,
        toolFilterValue: TOOL_FILTER_VALUES.ALL,
        toolMenuEl: null,
        shareMenuEl: null,
        numToolsSelected: 0,
    });

    const setState = (stateObj) => {
        setHookState({ ...state, ...stateObj });
    };

    useEffect(() => {
        presenter.onToolSelectionChanged(state.selectedTool);
    }, [state.selectedTool]);

    const updateListingConfig = (listingObj) => {
        const currentListingConfig = {
            toolFilterValue: state.toolFilterValue,
            searchTerm: searchTerm,
            order: order,
            orderBy: orderBy,
            rowsPerPage: rowsPerPage,
            page: page,
        };
        let updatedListingConfig = { ...currentListingConfig, ...listingObj };
        setState({
            toolFilterValue: updatedListingConfig.toolFilterValue,
        });
        let isPublic;
        if (
            updatedListingConfig.toolFilterValue === TOOL_FILTER_VALUES.MY_TOOLS
        ) {
            isPublic = false;
        }
        if (
            updatedListingConfig.toolFilterValue === TOOL_FILTER_VALUES.PUBLIC
        ) {
            isPublic = true;
        }

        presenter.loadTools(
            isPublic,
            updatedListingConfig.searchTerm,
            updatedListingConfig.order,
            updatedListingConfig.orderBy,
            updatedListingConfig.rowsPerPage,
            updatedListingConfig.page
        );
    };

    const baseId = build(parentId, ids.MANAGE_TOOLS.VIEW);

    return (
        <Fragment>
            <StyledToolbar
                parentId={baseId}
                presenter={presenter}
                intl={intl}
                searchTerm={searchTerm}
                setState={setState}
                updateListingConfig={updateListingConfig}
                {...state}
            />
            <StyledToolListing
                loading={loading}
                parentId={baseId}
                toolList={toolList}
                presenter={presenter}
                searchTerm={searchTerm}
                order={order}
                orderBy={orderBy}
                rowsPerPage={rowsPerPage}
                page={page}
                setState={setState}
                updateListingConfig={updateListingConfig}
                {...state}
            />
        </Fragment>
    );
}

const StyledToolbar = withStyles(styles)(ToolsToolbar);

function ToolsToolbar(props) {
    const {
        parentId,
        presenter,
        selectedTool,
        toolFilterValue,
        toolMenuEl,
        shareMenuEl,
        searchTerm,
        setState,
        updateListingConfig,
        intl,
        classes,
    } = props;

    const hasWritePermission =
        selectedTool && selectedTool.permission === PERMISSION.WRITE;
    const isOwner = selectedTool && selectedTool.permission === PERMISSION.OWN;
    const isEditable =
        selectedTool &&
        !selectedTool.is_public &&
        (isOwner || hasWritePermission);

    const toolFilterId = build(parentId, ids.MANAGE_TOOLS.TOOL_FILTER);

    return (
        <Toolbar
            id={build(parentId, ids.MANAGE_TOOLS.TOOLBAR)}
            classes={{ root: classes.toolbar }}
        >
            <Button
                onClick={(event) =>
                    setState({ toolMenuEl: event.currentTarget })
                }
                id={build(parentId, ids.MANAGE_TOOLS.TOOLS_MENU)}
                variant="contained"
            >
                <MenuIcon />
                {getMessage("tools")}
            </Button>
            <Menu
                anchorEl={toolMenuEl}
                open={Boolean(toolMenuEl)}
                onClose={() => setState({ toolMenuEl: null })}
            >
                <MenuItem
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.onNewToolSelected();
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.ADD_TOOL_MI)}
                >
                    {getMessage("addTool")}
                </MenuItem>
                <MenuItem
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.onRequestToolSelected();
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.REQUEST_TOOL_MI)}
                >
                    {getMessage("requestToolMI")}
                </MenuItem>
                <MenuItem
                    disabled={!isEditable}
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.onEditToolSelected(selectedTool.id);
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.EDIT_TOOL_MI)}
                >
                    {getMessage("edit")}
                </MenuItem>
                <MenuItem
                    disabled={!isOwner}
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.onDeleteToolsSelected(
                            selectedTool.id,
                            selectedTool.name
                        );
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.DELETE_TOOL_MI)}
                >
                    {getMessage("delete")}
                </MenuItem>
                <MenuItem
                    disabled={!selectedTool}
                    onClick={() => {
                        setState({ toolMenuEl: null });
                        presenter.useToolInNewApp(selectedTool);
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.USE_IN_APP_MI)}
                >
                    {getMessage("useToolInApp")}
                </MenuItem>
            </Menu>
            <Button
                disabled={!isOwner}
                onClick={(event) =>
                    setState({ shareMenuEl: event.currentTarget })
                }
                id={build(parentId, ids.MANAGE_TOOLS.SHARE_MENU)}
                variant="contained"
            >
                <Share />
                {getMessage("share")}
            </Button>
            <Menu
                anchorEl={shareMenuEl}
                open={Boolean(shareMenuEl)}
                onClose={() => setState({ shareMenuEl: null })}
            >
                <MenuItem
                    disabled={!isOwner}
                    onClick={() => {
                        setState({ shareMenuEl: null });
                        presenter.onShareToolsSelected(selectedTool);
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.SHARE_MI)}
                >
                    {getMessage("shareWithCollaborators")}
                </MenuItem>
                <MenuItem
                    disabled={!isOwner}
                    onClick={() => {
                        setState({ shareMenuEl: null });
                        presenter.onRequestToMakeToolPublicSelected(
                            selectedTool
                        );
                    }}
                    id={build(parentId, ids.MANAGE_TOOLS.MAKE_PUBLIC_MI)}
                >
                    {getMessage("makePublic")}
                </MenuItem>
            </Menu>
            <Button
                onClick={() => updateListingConfig({})}
                id={build(parentId, ids.MANAGE_TOOLS.REFRESH)}
                variant="contained"
            >
                <Refresh />
                {getMessage("refresh")}
            </Button>
            <Select
                value={toolFilterValue}
                className={classes.toolTypeSelector}
                onChange={(event) => {
                    let updatedFilter = event.target.value;
                    updateListingConfig({
                        toolFilterValue: updatedFilter,
                        page: 0,
                        searchTerm: "",
                    });
                }}
                id={toolFilterId}
            >
                <MenuItem
                    value={TOOL_FILTER_VALUES.ALL}
                    id={build(toolFilterId, ids.MANAGE_TOOLS.ALL_MI)}
                >
                    {getMessage("allTools")}
                </MenuItem>
                <MenuItem
                    value={TOOL_FILTER_VALUES.MY_TOOLS}
                    id={build(toolFilterId, ids.MANAGE_TOOLS.MY_TOOLS_MI)}
                >
                    {getMessage("myTools")}
                </MenuItem>
                <MenuItem
                    value={TOOL_FILTER_VALUES.PUBLIC}
                    id={build(toolFilterId, ids.MANAGE_TOOLS.PUBLIC_MI)}
                >
                    {getMessage("publicTools")}
                </MenuItem>
            </Select>
            <SearchField
                handleSearch={(searchTerm) =>
                    updateListingConfig({
                        toolFilterValue: "",
                        searchTerm: searchTerm,
                        page: 0,
                    })
                }
                value={searchTerm}
                id={build(parentId, ids.MANAGE_TOOLS.SEARCH)}
                placeholder={formatMessage(intl, "searchTools")}
            />
        </Toolbar>
    );
}

const TABLE_COLUMNS = [
    { name: "", align: "left", enableSorting: false, key: "info", id: "info" },
    { name: "Name", align: "left", enableSorting: true, id: "name" },
    {
        name: "Image Name",
        align: "left",
        enableSorting: false,
        id: "image_name",
    },
    { name: "Tag", align: "left", enableSorting: false, id: "tag" },
    { name: "Status", align: "left", enableSorting: false, id: "status" },
];

const StyledToolListing = withStyles(styles)(ToolListing);

function ToolListing(props) {
    const {
        parentId,
        toolList,
        loading,
        presenter,
        selectedTool,
        numToolsSelected,
        order,
        orderBy,
        page,
        rowsPerPage,
        setState,
        updateListingConfig,
        classes,
    } = props;

    const onRequestSort = (event, property) => {
        const isDesc = orderBy === property && order === "desc";
        updateListingConfig({
            order: isDesc ? "asc" : "desc",
            orderBy: property,
        });
    };

    const clearSelectedTool = () => {
        setState({
            selectedTool: null,
            numToolsSelected: 0,
        });
    };

    return (
        <div className={classes.container}>
            <LoadingMask loading={loading}>
                <Table size="small">
                    <TableBody>
                        {(!toolList || toolList.tools.length === 0) && (
                            <EmptyTable
                                message={getMessage("noTools")}
                                numColumns={TABLE_COLUMNS.length}
                            />
                        )}
                        {toolList &&
                            toolList.tools.length > 0 &&
                            toolList.tools.map((tool) => {
                                const isSelected = selectedTool
                                    ? selectedTool.id === tool.id
                                    : false;
                                return (
                                    <TableRow
                                        tabIndex={-1}
                                        hover
                                        key={tool.id}
                                        selected={isSelected}
                                        onClick={() => {
                                            selectedTool === tool
                                                ? clearSelectedTool()
                                                : setState({
                                                      selectedTool: tool,
                                                      numToolsSelected: 1,
                                                  });
                                        }}
                                    >
                                        <TableCell padding="checkbox">
                                            <Checkbox
                                                id={build(
                                                    parentId,
                                                    tool.id,
                                                    ids.MANAGE_TOOLS
                                                        .TOOL_CHECKBOX
                                                )}
                                                checked={isSelected}
                                            />
                                        </TableCell>
                                        <TableCell padding="none">
                                            <IconButton
                                                onClick={() =>
                                                    presenter.onShowToolInfo(
                                                        tool.id
                                                    )
                                                }
                                                id={build(
                                                    parentId,
                                                    tool.id,
                                                    ids.MANAGE_TOOLS
                                                        .TOOL_INFO_BTN
                                                )}
                                            >
                                                <InfoOutlined />
                                            </IconButton>
                                        </TableCell>
                                        <TableCell>{tool.name}</TableCell>
                                        <TableCell>
                                            {tool.container.image.name}
                                        </TableCell>
                                        <TableCell>
                                            {tool.container.image.tag}
                                        </TableCell>
                                        <TableCell>
                                            {tool.is_public
                                                ? getMessage("public")
                                                : tool.permission}
                                        </TableCell>
                                    </TableRow>
                                );
                            })}
                    </TableBody>
                    <EnhancedTableHead
                        selectable={true}
                        numSelected={numToolsSelected}
                        rowCount={toolList ? toolList.tools.length : 0}
                        baseId={parentId}
                        columnData={TABLE_COLUMNS}
                        order={order}
                        orderBy={orderBy}
                        onSelectAllClick={clearSelectedTool}
                        onRequestSort={onRequestSort}
                    />
                </Table>
                <TablePagination
                    className={classes.tablePagination}
                    colSpan={6}
                    component="div"
                    count={toolList ? toolList.total : 0}
                    rowsPerPage={rowsPerPage}
                    page={page}
                    onChangePage={(event, page) => {
                        updateListingConfig({ page: page });
                    }}
                    onChangeRowsPerPage={(event) => {
                        updateListingConfig({
                            rowsPerPage: event.target.value,
                            page: 0,
                        });
                    }}
                    ActionsComponent={TablePaginationActions}
                    rowsPerPageOptions={PAGING_OPTIONS}
                />
            </LoadingMask>
        </div>
    );
}

ManageTools.propTypes = {
    presenter: PropTypes.shape({
        onShowToolInfo: PropTypes.func.isRequired,
        onNewToolSelected: PropTypes.func.isRequired,
        onRequestToolSelected: PropTypes.func.isRequired,
        onEditToolSelected: PropTypes.func.isRequired,
        onDeleteToolsSelected: PropTypes.func.isRequired,
        useToolInNewApp: PropTypes.func.isRequired,
        onShareToolsSelected: PropTypes.func.isRequired,
        onRequestToMakeToolPublicSelected: PropTypes.func.isRequired,
        loadTools: PropTypes.func.isRequired,
        onToolSelectionChanged: PropTypes.func.isRequired,
    }),
    loading: PropTypes.bool.isRequired,
    parentId: PropTypes.string.isRequired,
    toolList: PropTypes.object,
    searchTerm: PropTypes.string.isRequired,
    order: PropTypes.string.isRequired,
    orderBy: PropTypes.string.isRequired,
    rowsPerPage: PropTypes.number.isRequired,
    page: PropTypes.number.isRequired,
};

export default withI18N(injectIntl(ManageTools), messages);
