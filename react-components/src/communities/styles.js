import { palette } from "@cyverse-de/ui-lib";

const styles = (theme) => ({
    column: {
        display: "flex",
        flexDirection: "column",
        width: "100%",
    },

    subjectSearch: {
        flexGrow: 1,
        flexBasis: "200px",
    },

    formItem: {
        margin: "5px",
    },

    toolbar: {
        backgroundColor: palette.lightGray,
    },

    toolbarItem: {
        marginRight: "15px",
    },

    textBlurb: {
        margin: "10px",
    },

    hidden: {
        visibility: "hidden",
    },

    table: {
        height:
            "calc(100% - " +
            theme.mixins.toolbar["@media (min-width:600px)"].minHeight +
            "px)",
        overflow: "auto",
    },

    deleteButton: {
        backgroundColor: theme.palette.error.main,
        "&:hover": {
            backgroundColor: theme.palette.error.dark,
        },
    },

    grow: {
        flexGrow: 1,
    },
});

export default styles;
