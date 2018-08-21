import React, { Component } from 'react';
import Button from '@material-ui/core/Button';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Dialog from '@material-ui/core/Dialog';
import withI18N, { getMessage } from "../../../util/I18NWrapper";
import Table from "@material-ui/core/Table";
import EnhancedTableHead from "../../../util/table/EnhancedTableHead";
import ids from "../../ids";
import TableRow from "@material-ui/core/TableRow";
import intlData from "../../messages"
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import IconButton from "@material-ui/core/IconButton";
import CloseIcon from "@material-ui/icons/Close";
import Color from "../../../util/CyVersePalette";

const columnData = [
    {name: "Job Id", numeric: false, enableSorting: false},
    {name: "Type", numeric: false, enableSorting: false},
];

class AnalysisInfoDialog extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dialogOpen: props.dialogOpen,
        }
    }

    render() {
        const {info} = this.props;
        return (
            <Dialog open={this.state.dialogOpen}>
                <DialogTitle style={{backgroundColor: Color.blue}}>
                    {getMessage("analysisInfoTitle")}
                    <IconButton
                        aria-label="More"
                        aria-haspopup="true"
                        onClick={this.handleDotMenuClick}
                        style={{position: 'absolute', top: 10, left: 375}}
                    >
                        <CloseIcon/>
                    </IconButton>
                </DialogTitle>
                <DialogContent>
                    <Table>
                        <EnhancedTableHead
                            columnData={columnData}
                            ids={ids}
                            baseId="analysis"
                        />
                        <TableBody>
                            {info.steps.map(n => {
                                return (
                                    <TableRow key={n.step_number}>
                                        <TableCell>{n.external_id}</TableCell>
                                        <TableCell>{n.step_type}</TableCell>
                                    </TableRow>
                                );
                            })}
                        </TableBody>
                    </Table>
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={() => {
                            this.setState({dialogOpen: false})
                        }}
                        color="primary">
                        {getMessage("okBtnText")}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    }
}

export default withI18N(AnalysisInfoDialog, intlData);