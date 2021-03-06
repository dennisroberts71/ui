<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Pragma", "no-cache");
response.setDateHeader("Expires", 0);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>

<head>
<!--                                                               -->
<!-- Consider inlining CSS to reduce the number of requested files -->
<!--                                                               -->
<link type="image/x-icon" rel="shortcut icon" href="images/favicon.ico">
<link type ="text/css" rel="stylesheet" href="./introjs.min.css">
<link type="text/css" rel="stylesheet" href="./codemirror.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500">

<!-- set by i18n code -->
<title></title>

<!--                                           -->
<!-- This script loads your compiled module.   -->
<!-- If you add any GWT meta tags, they must   -->
<!-- be added before this line.                -->
<!--                                           -->


<script type="text/javascript" language="javascript"
	src="discoveryenvironment/discoveryenvironment.nocache.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/intro.min.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/codemirror.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/javascript.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/shell.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/nexus.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/perl.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/python.js"></script>
<script type="text/javascript" language="javascript"
    src="scripts/r.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/markdown.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/matchbrackets.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/closebrackets.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/Markdown.Converter.js"></script>
<script type="text/javascript" language="javascript"
	src="scripts/Markdown.Sanitizer.js"></script>
<script type="text/javascript" language="javascript"
    src="scripts/handlebars.js"></script>

<%@ include file="react_include.jsp" %>

<%
	out.println("<p style='position:absolute;top:45%; left:48%  margin-top: 45%; margin-left: 48%;'>Loading...Please wait!</p><img style='position:absolute;top:50%; left:50%  margin-top: 50%; margin-left: 50%;' src='./images/loading_spinner.gif'/>");
%>
<c:if test="${isProduction}">
	<!-- Google analytics -->
    <script type="text/javascript">
        var _gaq = _gaq || [];

        _gaq.push(['_setAccount', 'UA-57745299-1']);

        _gaq.push(['_trackPageview']);
        (function() {

            var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;

            ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';

            var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);

        })();

    </script>
</c:if>
<script type="text/javascript">
    var APP_ID = '<%= session.getAttribute("intercomAppId") %>';
    var enabled = <%= session.getAttribute("intercomEnabled")%>;
    if(enabled) {
        (function () {
            var w = window;
            var ic = w.Intercom;
            if (typeof ic === "function") {
                ic('reattach_activator');
                ic('update', intercomSettings);
            } else {
                var d = document;
                var i = function () {
                    i.c(arguments)
                };
                i.q = [];
                i.c = function (args) {
                    i.q.push(args)
                };
                w.Intercom = i;
                function l() {
                    var s = d.createElement('script');
                    s.type = 'text/javascript';
                    s.async = true;
                    s.src = 'https://widget.intercom.io/widget/' + APP_ID;
                    var x = d.getElementsByTagName('script')[0];
                    x.parentNode.insertBefore(s, x);
                }

                if (w.attachEvent) {
                    w.attachEvent('onload', l);
                } else {
                    w.addEventListener('load', l, false);
                }
                w.intercomSettings = {
                    app_id: APP_ID,
                    alignment: 'right',
                    horizontal_padding: 20,
                    vertical_padding: 45,
                    custom_launcher_selector: '#help_menu_intercom_link',
            	};
            }
        })
    ()}
</script>
</head>

<!--                                           -->
<!-- The body can have arbitrary html, or      -->
<!-- you can leave the body empty if you want  -->
<!-- to create a completely dynamic UI.        -->
<!--                                           -->
<body>
	<!-- include for history support -->
	<iframe src="javascript:''" id="__gwt_historyFrame" 
		style="position: absolute; width: 0; height: 0; border: 0">
	</iframe>
</body>
</html>
