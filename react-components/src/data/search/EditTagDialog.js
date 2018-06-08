import ids from './ids';
import messages from './messages';
import withI18N, { getMessage } from "../../util/I18NWrapper";

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import PropTypes from 'prop-types';
import React, {Component} from 'react';
import TextField from '@material-ui/core/TextField';

/**
 * @author aramsey
 * A dialog for editing the description on a Tag
 */
class EditTagDialog extends Component {
    constructor(props) {
        super(props);

        this.state = {
            description: ''
        };

        this.handleClose = this.handleClose.bind(this);
        this.handleSave = this.handleSave.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        let value = event.target.value;
        this.setState({description: value})
    }

    handleSave() {
        let tag = this.props.tag;
        tag.description = this.state.description;
        this.props.handleSave(tag);
    }

    handleClose() {
        this.props.handleClose();
    }

    componentWillReceiveProps(nextProps) {
        let tag = nextProps.tag;

        this.setState({description: tag ? tag.description : ''})
    }

    render() {
        let { open } = this.props;

        return (
            <Dialog open={open}
                    onClose={this.handleClose}>
                <DialogTitle id={ids.saveSearchDlg}>
                    {getMessage('editTagDescription')}
                </DialogTitle>
                <DialogContent>
                    <TextField id={ids.editTagInput}
                               multiline
                               rowsMax='4'
                               label={getMessage('description')}
                               value={this.state.description}
                               onChange={this.handleChange}/>
                </DialogContent>
                <DialogActions>
                    <Button variant="flat"
                            id={ids.editTagSave}
                            color="primary"
                            onClick={this.handleSave}>
                        {getMessage('saveBtn')}
                    </Button>
                    <Button variant="flat"
                            id={ids.editTagCancel}
                            color="primary"
                            onClick={this.handleClose}>
                        {getMessage('cancelBtn')}
                    </Button>
                </DialogActions>
            </Dialog>
        )
    }
}

EditTagDialog.propTypes = {
    open: PropTypes.bool.isRequired,
    tag: PropTypes.shape({
        id: PropTypes.string.isRequired,
    }),
    handleSave: PropTypes.func.isRequired,
    handleClose: PropTypes.func.isRequired
};

export default withI18N(EditTagDialog, messages);