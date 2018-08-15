/**
 * @author sriram
 */
import React, { Component } from "react";
import ReactDOM from 'react-dom';
import MenuItem from "@material-ui/core/MenuItem";
import DEHyperlink from "../../../src/util/hyperlink/DEHyperLink";
import withI18N, { getMessage } from "../../util/I18NWrapper";
import ids from "../ids";
import build from "../../util/DebugIDUtil";
import RefreshIcon from "@material-ui/icons/Refresh";
import Button from "@material-ui/core/Button";
import CircularProgress from "@material-ui/core/CircularProgress";
import { withStyles } from "@material-ui/core/styles";
import styles from "../style";
import Menu from "@material-ui/core/Menu";
import Divider from "@material-ui/core/Divider";
import intlData from "../messages";
import classnames from "classnames";
import NotificationIcon from "@material-ui/icons/Notifications";
import tour from "../NewUserTourSteps";


function ErrorComponent(props) {
    return (
        <div style={{outline: 'none'}}>
            <div className={props.classes.notificationError}>
                {getMessage("notificationError")}
            </div>
            <div id={build(ids.DESKTOP,ids.RETRY_BTN)}>
                <Button variant="fab"
                        mini="true"
                        className={props.classes.errorRetryButton}
                        onClick={props.onClick}>
                    <RefreshIcon />
                </Button>
            </div>
        </div>
    )
}

function NotificationFooter(props) {
    return (
        <MenuItem onClick={props.onClick}>
            {(props.unSeenCount > 10) ?
                <div>
                     <span id={build(ids.DESKTOP, ids.NEW_NOTIFICATIONS)}>
                         <DEHyperlink
                             onClick={props.viewNewNotification}
                             text={getMessage("newNotifications", {values: {count: props.unSeenCount}})}/>
                     </span>
                    <span style={{margin: '20px'}}> </span>
                    <span id={build(ids.DESKTOP, ids.MARK_ALL_SEEN)}>
                            <DEHyperlink
                                onClick={() => {
                                    props.markAllAsSeen(true)
                                }}
                                text={getMessage("markAllRead")}/>
                    </span>
                </div>
                :
                <span id={build(ids.DESKTOP, ids.SEE_ALL_NOTIFICATIONS)}>
                    <DEHyperlink
                        onClick={props.viewAllNotification}
                        text={getMessage("viewAllNotifi")}/>
                </span>
            }
        </MenuItem>
    );
}

function Notification(props) {
    const {notification, onClick, classes} = props;
    let className = classes.notification;
    if (!notification.seen) {
        className = classnames(classes.notification, classes.unSeenNotificationBackground);
    }

    return (
        <span style={{outline: 'none'}}>
                    <MenuItem
                        id={notification.message.id}
                        onClick={onClick}
                        className={className}>
                        {notification.message.text}
                        {notification.payload.access_url &&
                        <InteractiveAnalysisUrl notification={notification}/>
                        }
                    </MenuItem>
                    <Divider/>
        </span>
    );

}

function InteractiveAnalysisUrl(props) {
    return (
        <span>
            {getMessage("dot")}
            <a href={props.notification.payload.access_url}
               target="_blank">
                {getMessage("interactiveAnalysisUrl")}
            </a>
        </span>
    );

}

class Notifications extends Component {
    constructor(props) {
        super(props);
        this.state = {
            anchorEl: null,
        };
        this.handleNotificationsClick = this.handleNotificationsClick.bind(this);
        this.onMenuItemSelect = this.onMenuItemSelect.bind(this);
        this.notificationBtn = React.createRef();
    }

    handleNotificationsClick() {
        this.setState({anchorEl: document.getElementById(this.props.anchor)});
        const {unSeenCount, markAllAsSeen} = this.props;
        //if unseencount < 10, mark them as read
        if (unSeenCount > 0 && unSeenCount < 10) {
            markAllAsSeen(false);
        }

    }

    handleClose = () => {
        this.setState({anchorEl: null});
    };

    onMenuItemSelect(event) {
        this.props.notificationClicked(event.currentTarget.id);
        this.handleClose();
    }

    componentDidMount() {
        let ele = ReactDOM.findDOMNode(this.notificationBtn.current);
        ele.setAttribute("data-intro", tour.NotificationWindow.message);
        ele.setAttribute("data-position", tour.NotificationWindow.position);
        ele.setAttribute("data-step", tour.NotificationWindow.step);
    }

    render() {
        const {
            anchorEl,
        } = this.state;

        const {notifications, unSeenCount, classes, notificationLoading, error} = this.props;
        const messages = (notifications && notifications.messages && notifications.messages.length > 0) ? notifications.messages : [];


        return (
            <React.Fragment>
                <NotificationIcon
                    id={ids.NOTIFICATION_ICON}
                    className={classes.menuIcon}
                    onClick={this.handleNotificationsClick}
                    ref={this.notificationBtn}
                />
                {unSeenCount !== 0 &&
                <span id='notifyCount'
                      className={classes.unSeenCount}>
                        {unSeenCount}
                    </span>
                }
                <Menu id={build(ids.DESKTOP, ids.NOTIFICATIONS_MENU)}
                      anchorEl={anchorEl}
                      open={Boolean(anchorEl)}
                      onClose={this.handleClose}
                      className={classes.notificationMenu}
                >
                        {notificationLoading ? (
                                <CircularProgress size={30}
                                                  className={classes.loadingStyle}
                                                  thickness={7}/>
                            )
                            : (error ? (
                                    <ErrorComponent classes={classes}
                                                    onClick={this.props.fetchNotifications}/>
                                ) : (
                                    (messages.length > 0) ?
                                        messages.map(n => {
                                            return (
                                                <Notification key={n.message.id}
                                                              notification={n}
                                                              onClick={this.onMenuItemSelect}
                                                              classes={classes}/>
                                            )
                                        }).reverse() : (
                                            <MenuItem id={build(ids.DESKTOP, ids.EMPTY_NOTIFICATION)}
                                                      onClick={this.onMenuItemSelect}>
                                                <div className={classes.notificationError}>
                                                    {getMessage("noNotifications")}
                                                </div>
                                            </MenuItem>
                                        )))}
                    <NotificationFooter unSeenCount={unSeenCount}
                                        viewAllNotification={this.props.viewAllNotification}
                                        markAllAsSeen={this.props.markAllAsSeen}
                                        viewNewNotification={this.props.viewNewNotification}
                                        onClick={this.handleClose}
                                        />
                    </Menu>
            </React.Fragment>
        );
    }
}

export default withStyles(styles)(withI18N(Notifications, intlData));


