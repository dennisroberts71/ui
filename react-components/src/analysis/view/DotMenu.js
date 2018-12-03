/**
 *  @author sriram
 *
 **/

import React, { Component } from 'react';

import AnalysesMenu from "./AnalysesMenu";

import Menu from "@material-ui/core/Menu";

import IconButton from "@material-ui/core/IconButton";
import MoreVertIcon from "@material-ui/icons/MoreVert";


const ITEM_HEIGHT = 48;

class DotMenu extends Component {

    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
    }

    handleDotMenuClick = event => {
        this.setState({anchorEl: event.currentTarget});
    };

    handleDotMenuClose = () => {
        this.setState({anchorEl: null});
    };

    render() {
        const {anchorEl} = this.state;
        const open = Boolean(anchorEl);

        return (
            <div>
                <IconButton
                    aria-label="More"
                    aria-owns={open ? 'long-menu' : null}
                    aria-haspopup="true"
                    onClick={this.handleDotMenuClick}
                >
                    <MoreVertIcon/>
                </IconButton>
                <Menu
                    anchorEl={anchorEl}
                    open={open}
                    onClose={this.handleDotMenuClose}
                    PaperProps={{
                        style: {
                            maxHeight: ITEM_HEIGHT * 4.5,
                            width: 200,
                        },
                    }}
                >
                    <AnalysesMenu handleClose={this.handleDotMenuClose}
                                  {...this.props} />
                </Menu>
            </div>
        );
    }
}

export default DotMenu;