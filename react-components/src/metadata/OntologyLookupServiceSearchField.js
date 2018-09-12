/**
 * @author psarando
 */
import React, { Component } from 'react';

import { getMessage } from "../util/I18NWrapper";
import { FormikSearchField } from "../util/FormField";

import ListItemText from '@material-ui/core/ListItemText';
import MenuItem from "@material-ui/core/MenuItem";

const OLSOption = ({
    innerRef,
    isFocused,
    innerProps,
    data,
}) => (
    <MenuItem
        buttonRef={innerRef}
        selected={isFocused}
        {...innerProps}
    >
        <ListItemText primary={data.label}
                      secondary={
                          data.ontology_prefix && data.iri &&
                          `${data.ontology_prefix}: ${data.iri}`
                      }
        />
    </MenuItem>
);

class OntologyLookupServiceSearchField extends Component {
    constructor(props) {
        super(props);

        this.loadOptions = this.loadOptions.bind(this);
    }

    loadOptions(inputValue, callback) {
        const { attribute, presenter } = this.props;

        presenter.searchOLSTerms(inputValue, attribute.settings, callback);
    }

    formatCreateLabel(inputValue) {
        return getMessage("formatMetadataTermFreeTextOption", {values: { inputValue } });
    }

    render() {
        const { attrTemplate, presenter, ...props } = this.props;

        return (
            <FormikSearchField loadOptions={this.loadOptions}
                               variant="asyncCreatable"
                               labelKey="label"
                               valueKey="label"
                               CustomOption={OLSOption}
                               formatCreateLabel={this.formatCreateLabel}
                               {...props}
            />
        );
    }
}

export default OntologyLookupServiceSearchField;
