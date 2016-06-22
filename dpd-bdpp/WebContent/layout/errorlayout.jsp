<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${ cdn == null }">
   	<fmt:setBundle basename="goc.webtemplate.global.config.cdn" var="cdn" scope="application"/>
</c:if>
<c:set var="wettheme" scope="request">
	<fmt:message key="wettemplate_theme" bundle="${cdn}"/>
</c:set>
<c:set var="wetversion" scope="request">
	<fmt:message key="wettemplate_version" bundle="${cdn}"/>
</c:set>
<jsp:useBean id="applicationscopebean" class="goc.webtemplate.component.jsp.MasterApplicationBean" scope="application"/>
<jsp:useBean id="goctemplateclientbean" class="goc.webtemplate.jsp.beans.ApplicationBaseSettingsBean" scope="application"/>
<c:set var="lang" scope="session">
<bean:write name="lang"/>
</c:set>
<!DOCTYPE html>
<!--[if lt IE 9]><html class="no-js lt-ie9" lang="${lang}" dir="ltr"><![endif]-->
<!--[if gt IE 8]><!-->
<html xmlns="http://www.w3.org/1999/xhtml" class="no-js" lang="${lang}" dir="ltr">
<!--<![endif]-->
<head>
<meta charset="utf-8">
        <!-- Web Experience Toolkit (WET) / Boîte à outils de l'expérience Web (BOEW)
        wet-boew.github.io/wet-boew/License-en.html / wet-boew.github.io/wet-boew/Licence-fr.html -->
        <tiles:useAttribute name="titleKey" />
		<title><bean:message name="titleKey" /></title>
      	<tiles:useAttribute name="formname" scope="session" />
		<% session.setAttribute("URLPath", formname); %>	  
        <meta content="width=device-width,initial-scale=1" name="viewport" />
        <bean:write name="goctemplateclientbean" property="htmlHeaderElements" filter="false"/>
        <!-- Load closure template scripts -->
        <script type="text/javascript" src="<fmt:message key="cdn_url" bundle="${cdn}"/>/<c:out value="${goctemplateclientbean.runOrVersionValue}" />/cls/wet/<fmt:message key="wettemplate_theme" bundle="${cdn}"/>/<c:out value="${goctemplateclientbean.themeVersionValue}" />js/compiled/soyutils.js"></script>
        <script type="text/javascript" src="<fmt:message key="cdn_url" bundle="${cdn}"/>/<c:out value="${goctemplateclientbean.runOrVersionValue}" />/cls/wet/<fmt:message key="wettemplate_theme" bundle="${cdn}"/>/<c:out value="${goctemplateclientbean.themeVersionValue}" />js/compiled/wet-<c:out value="${lang}"/>.js"></script>
        <script type="text/javascript" src="<fmt:message key="cdn_url" bundle="${cdn}"/>/<c:out value="${goctemplateclientbean.runOrVersionValue}" />/cls/wet/<fmt:message key="wettemplate_theme" bundle="${cdn}"/>/<c:out value="${goctemplateclientbean.themeVersionValue}" />js/compiled/plugins-<c:out value="${lang}"/>.js"></script>
        <noscript>
            <!-- Write closure fall-back static file -->
            <bean:write name="refTopStaticfile" filter="false" />
        </noscript>
        <!-- Write closure template -->
        <script type="text/javascript">
            document.write(wet.builder.refTop({
                cdnEnv: "<fmt:message key="cdn_environment" bundle="${cdn}"/>"
            }));
        </script>
        <!--  GoC Web Template Build Version <c:out value="${goctemplateclientbean.webTemplateDistributionVersion}" /> -->
	</head>
	<body vocab="http://schema.org/" typeof="WebPage">		
		<bean:write name="goctemplateclientbean" filter="false" property="sessionTimeoutControl" />
        <div id="def-top">
            <!-- Write closure fall-back static file -->
            <bean:write name="defTopStaticFile" filter="false"  />
        </div>
        <!-- Write closure template -->
	 <script type="text/javascript">
        var defTop = document.getElementById("def-top");
        defTop.outerHTML = wet.builder.top({
            cdnEnv: "<fmt:message key="cdn_environment" bundle="${cdn}" />",
            <bean:write name="goctemplateclientbean" property="showSearch" />
            lngLinks: [{
                lang: "<bean:message bundle="clfRes" key="label.lang" />",
               href: "<bean:write name="languageLinkUrl" /><bean:message bundle="clfRes" key="label.lang" />&url=<bean:write name="formname"/>",
                text: "<bean:message bundle="clfRes" key="header.switchLanguage" />"
            }],
            showPreContent: <bean:write name="goctemplateclientbean" property="showPreContent" />,
            <bean:write name="goctemplateclientbean" property="breadcrumbsList" filter="false" />
           
        });
	</script>

        <main role="main" property="mainContentOfPage" class="container">  
            <!-- the main content -->
            <tiles:insert attribute="body" />
            <!-- end main content -->
            <div id="def-preFooter">
                <!-- Write closure fall-back static file -->
            	<bean:write name="defPreFooter" filter="false"/>
            </div> 
            <!-- Write closure template -->
            <script type="text/javascript">
                var defPreFooter = document.getElementById("def-preFooter");
                defPreFooter.outerHTML = wet.builder.preFooter({
               	    cdnEnv: "<fmt:message key="cdn_environment" bundle="${cdn}"/>",
                    <bean:write name="goctemplateclientbean" property="dateModifiedOrVersionIdentifierValue" filter="false" />
                   showPostContent: <bean:write name="goctemplateclientbean" property="showPostContent" />,
                     <bean:write name="goctemplateclientbean" property="renderFeedbackLink" />
                     <bean:write name="goctemplateclientbean" property="renderSharePageMediaSites" filter="false"/>
	          });
	      </script>   
        </main>	
         <div id="def-footer">
            <!-- Write closure fall-back static file -->
            <bean:write name="defFootStaticFile" filter="false" />
        </div>
        <!-- Write closure template -->
        <script type="text/javascript">
            var defFooter = document.getElementById("def-footer");
            defFooter.outerHTML = wet.builder.footer({
                cdnEnv: "<fmt:message key="cdn_environment" bundle="${cdn}"/>",
                <bean:write name="goctemplateclientbean" property="renderFeatures" />
                <bean:write name="goctemplateclientbean" property="renderLinksList" />
            });
        </script>
        <!-- Write closure template -->
        <script type="text/javascript">
			document.write(wet.builder.refFooter({
			    cdnEnv: "<fmt:message key="cdn_environment" bundle="${cdn}"/>",
			    <bean:write name="goctemplateclientbean" property="renderLeavingSecureSiteWarning" filter="false"  />
			}));
		</script>
        <bean:write name="goctemplateclientbean" property="htmlBodyElements" />
	</body>
</html>