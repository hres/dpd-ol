<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<!DOCTYPE html>
<!--[if lt IE 9]><html class="no-js lt-ie9" lang="en" dir="ltr"><![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en" dir="ltr"> <!--<![endif]-->
<head>
	<meta charset="utf-8"/>
	 
	<title><bean:message key="title.failure" /></title>
	<meta content="width=device-width, initial-scale=1" name="viewport"/>
	 
	<meta name="robots" content="noindex, nofollow, noarchive"/>
	 
<!--[if gte IE 9 | !IE ]><!-->
<link href="./distro/4.0.9/assets/favicon.ico" rel="icon"
	type="image/x-icon">
<link rel="stylesheet" href="./distro/4.0.9/css/wet-boew.min.css">
<link rel="stylesheet" href="./distro/4.0.9/css/theme.min.css">
<link rel="stylesheet"
	href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" />
<!--<![endif]-->


<!--[if lt IE 9]>
		<link href="./distro/4.0.9/assets/favicon.ico" rel="shortcut icon" />
		<link rel="stylesheet" href="./distro/4.0.9/css/ie8-wet-boew.min.css" />
		<link rel="stylesheet" href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" />
		<![endif]-->

<noscript>
<link rel="stylesheet" href="./distro/4.0.9/css/noscript.min.css" />
</noscript>
</head>
<body vocab="http://schema.org/" typeof="WebPage">
	<header role="banner" id="wb-bnr" class="container">
		<div class="row">
	   		<div class="brand col-xs-8 col-sm-9 col-md-6">
	      		<a href='<bean:message bundle="clfRes" key="404-500.canada.link" />'><object type="image/svg+xml" tabindex="-1" data="./distro/4.0.9/assets/sig-blk-en.svg"></object><span class="wb-inv"> <bean:message bundle="clfRes" key="404-500.canada.gov" /></span></a>
     		</div>
		</div>
	</header>
	<div role="main" property="mainContentOfPage" class="container">
	<h1><bean:message key="title.failure" /></h1>
		<h2 class="alert alert-warning">
		
      		<html:messages id="message" message="true" bundle="messageRes" >
            <bean:write name="message" />
          	</html:messages>
        	<html:errors bundle="messageRes" />
		</h2>
	</div>
	<!--[if gte IE 9 | !IE ]><!--> <script src="distro/4.0.9/js/jquery-2.1.1.min.js"></script> <script src="distro/4.0.9/js/wet-boew.min.js"></script> <!--<![endif]-->
	<!--[if lt IE 9]><script src="distro/4.0.9/js/ie8-wet-boew2.min.js"></script><![endif]-->
	<footer role="contentinfo" id="wb-info">
		<nav role="navigation" class="container visible-sm visible-md visible-lg wb-navcurr">
		
		<div class="row">
			<div class="col-sm-12 col-lg-12 text-center">
				<ul id="gc-tctr" class="list-inline">
					<li><a href=""><bean:message bundle="clfRes" key="404-500.footer.contact" /> &bull;</a></li>
					<li><a href='<bean:message bundle="clfRes" key="404-500.footer.terms.link" />'><bean:message bundle="clfRes" key="404-500.footer.terms" /> &bull;</a></li>
					<li><a href='<bean:message bundle="clfRes" key="404-500.footer.privacy.link" />'><bean:message bundle="clfRes" key="404-500.footer.privacy" /></a></li>
				</ul>
			</div>
		</div>
		</nav>
	<div class="brand">
	<div class="container">
	<div class="row">
	<div class="col-xs-6 visible-sm visible-xs tofpg">
	<a href="#wb-cont"><bean:message bundle="clfRes" key="404-500.top" /> <span class="glyphicon glyphicon-chevron-up"></span></a>
	</div>
	<div class="col-xs-6 col-md-12 text-right">
	<object type="image/svg+xml" tabindex="-1" role="img" data="./distro/4.0.9/assets/wmms-blk.svg" aria-label="<bean:message bundle="clfRes" key="404-500.symbol.canada" />"></object>
	</div>
	</div>
	</div>
	</div>
	</footer>
</body>
</html>

