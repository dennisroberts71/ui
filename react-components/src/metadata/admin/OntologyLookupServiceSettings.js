/**
 * @author psarando
 */
import React, { Component } from "react";
import { Field, FieldArray } from "redux-form";

import build from "../../util/DebugIDUtil";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import intlData from "../messages";
import styles from "../style";
import ids from "./ids";

import { FormSelectField } from "../../util/FormField";
import StringListEditor from "./StringListEditor";

import Grid from '@material-ui/core/Grid';
import MenuItem from "@material-ui/core/MenuItem";
import { withStyles } from "@material-ui/core/styles";


const OLSEntityTypes = [
    "CLASS",
    "PROPERTY",
    "INDIVIDUAL",
    "ONTOLOGY",
];

const OLSEntityTypeMenuItems = OLSEntityTypes.map((type, index) => (<MenuItem key={index} value={type}>{type.toLowerCase()}</MenuItem>));

class OntologyLookupServiceSettings extends Component {
    render() {
        const { parentID } = this.props;
        const formID = build(parentID, ids.OLS_PARAMS_EDIT_DIALOG);

        return (
            <Grid container
                  spacing={16}
                  direction="column"
                  justify="flex-start"
                  alignItems="stretch"
            >
                <Grid item>
                    <fieldset>
                        <legend>{getMessage("olsSettingTypeTitle")}</legend>

                        <Field name="type"
                               id={build(formID, ids.ONTOLOGY_ENTITY_TYPE)}
                               label={getMessage("olsSettingTypeLabel")}
                               component={FormSelectField}
                        >
                            {OLSEntityTypeMenuItems}
                        </Field>
                    </fieldset>
                </Grid>

                <Grid item>
                    <FieldArray name="ontology"
                                component={StringListEditor}
                                parentID={build(formID, ids.ONTOLOGIES)}
                                title={getMessage("olsSettingOntologyTitle")}
                                helpLabel={getMessage("olsSettingOntologyHelpLabel")}
                                columnLabel={getMessage("olsSettingOntologyColumnLabel")}
                    />
                </Grid>

                <Grid item>
                    <FieldArray name="childrenOf"
                                component={StringListEditor}
                                parentID={build(formID, ids.ONTOLOGY_CHILDREN)}
                                title={getMessage("olsSettingChildrenOfTitle")}
                                helpLabel={getMessage("olsSettingChildrenOfHelpLabel")}
                                columnLabel={getMessage("olsSettingIRIColumnLabel")}
                    />
                </Grid>

                <Grid item>
                    <FieldArray name="allChildrenOf"
                                component={StringListEditor}
                                parentID={build(formID, ids.ONTOLOGY_ALL_CHILDREN)}
                                title={getMessage("olsSettingAllChildrenOfTitle")}
                                helpLabel={getMessage("olsSettingAllChildrenOfHelpLabel")}
                                columnLabel={getMessage("olsSettingIRIColumnLabel")}
                    />
                </Grid>
            </Grid>
        );
    }
}

export default withStyles(styles)(withI18N(OntologyLookupServiceSettings, intlData));