import React from "react";
import getAppsSearchRegex from "../appSearchRegex";
import { Grid, makeStyles, Typography } from "@material-ui/core";
import {
    AppTile,
    getMessage,
    LoadingMask,
    palette,
    withI18N,
} from "@cyverse-de/ui-lib";
import intlData from "../../apps/messages";
import FilterSortToolbar from "./FilterSortToolbar";
import AppListingHeader from "./AppListingHeader";
import appType from "../../appType";
import PropTypes from "prop-types";

/**
 *
 * @author sriram
 *
 * A component that will show a listing of type App with AppTile display
 */

const useStyles = makeStyles((theme) => ({
    container: {
        width: "100%",
        height: "100%",
        marginTop: 0,
        overflow: "auto",
        backgroundColor: palette.white,
    },
}));

function AppTileListing(props) {
    const {
        apps,
        parentId,
        heading,
        typeFilter,
        viewType,
        loading,
        sortField,
        sortDir,
        onTypeFilterChange,
        onSortChange,
        onAppInfoClick,
        onCommentsClick,
        onFavoriteClick,
        disableTypeFilter,
        handleAppSelection,
        isSelected,
        onAppNameClick,
        onRatingDeleteClick,
        onRatingClick,
        searchText,
        onQuickLaunchClick,
    } = props;
    const classes = useStyles();
    const searchRegex = getAppsSearchRegex(searchText);
    return (
        <div className={classes.container}>
            <LoadingMask loading={loading}>
                <AppListingHeader heading={heading} />
                <FilterSortToolbar
                    baseDebugID={parentId}
                    typeFilter={typeFilter}
                    onTypeFilterChange={onTypeFilterChange}
                    onSortChange={onSortChange}
                    viewType={viewType}
                    sortField={sortField}
                    sortDir={sortDir}
                    disableTypeFilter={disableTypeFilter}
                />
                {(!apps || apps.length === 0) && (
                    <div>
                        <Typography component="p">
                            {getMessage("noApps")}
                        </Typography>
                    </div>
                )}
                <Grid container>
                    {apps &&
                        apps.length > 0 &&
                        apps.map((app) => {
                            const external = app.app_type !== appType.de;
                            return (
                                <Grid key={app.id} item>
                                    <AppTile
                                        baseDebugId={parentId}
                                        uuid={app.id}
                                        name={app.name}
                                        creator={app.integrator_name}
                                        rating={app.rating}
                                        type={app.system_id}
                                        isPublic={app.is_public}
                                        isBeta={app.beta}
                                        isDisabled={app.disabled}
                                        isExternal={external}
                                        selected={isSelected(app.id)}
                                        onAppSelected={() => {
                                            handleAppSelection(app);
                                        }}
                                        onAppNameClick={() =>
                                            onAppNameClick(app)
                                        }
                                        onRatingChange={(event, score) => {
                                            onRatingClick(event, app, score);
                                        }}
                                        onDeleteRatingClick={() =>
                                            onRatingDeleteClick(app)
                                        }
                                        onAppInfoClick={() =>
                                            onAppInfoClick(app)
                                        }
                                        onCommentsClick={() =>
                                            onCommentsClick(app)
                                        }
                                        onFavoriteClick={() =>
                                            onFavoriteClick(app)
                                        }
                                        isFavorite={app.is_favorite}
                                        searchText={searchRegex}
                                        onQuickLaunchClick={() =>
                                            onQuickLaunchClick(app)
                                        }
                                    />
                                </Grid>
                            );
                        })}
                </Grid>
            </LoadingMask>
        </div>
    );
}

AppTileListing.propTypes = {
    parentId: PropTypes.string.isRequired,
    apps: PropTypes.array,
    selectedApps: PropTypes.array.isRequired,
    handleAppSelection: PropTypes.func.isRequired,
    onAppNameClick: PropTypes.func,
    onAppInfoClick: PropTypes.func,
    onCommentsClick: PropTypes.func,
    onFavoriteClick: PropTypes.func,
    getAppsSorting: PropTypes.func,
    onRatingDeleteClick: PropTypes.func,
    onRatingClick: PropTypes.func,
    searchText: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.instanceOf(RegExp),
    ]),
    heading: PropTypes.string.isRequired,
    typeFilter: PropTypes.string,
    viewType: PropTypes.string,
    loading: PropTypes.bool.isRequired,
    sortField: PropTypes.string.isRequired,
    onTypeFilterChange: PropTypes.func.isRequired,
};

export default withI18N(AppTileListing, intlData);
