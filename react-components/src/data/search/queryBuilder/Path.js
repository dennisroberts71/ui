import build from "../../../util/DebugIDUtil";
import ids from "../ids";
import { options } from "./Operators";
import ReduxTextField from "./ReduxTextField";
import SelectOperator from "./SelectOperator";

import { Field } from "redux-form";
import React, { Fragment } from "react";

/**
 * A component which allows users to specify a path prefix in QueryBuilder
 */
function Path(props) {
    let operators = [
        options.Begins,
        options.BeginsNot,
    ];

    let {parentId} = props;

    return (
        <Fragment>
            <SelectOperator operators={operators}
                            parentId={parentId}/>
            <Field name='prefix'
                   operators={operators}
                   id={build(parentId, ids.path)}
                   component={ReduxTextField}/>
        </Fragment>
    )
}

export default Path;