<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<!-- the main content -->
	<div class="row">
		<div class="col-lg-12 col-md-12 col-sm-12 text-center">
			<h1><bean:message key="label.timeout.title" /></h1>
		</div>
	</div><br>

	<div class="row">
		<div class="col-lg-12 col-md-12 col-sm-12 text-center">
			<p><bean:message key="label.timeout.message" /></p>
			<p><bean:message key="label.timeout.amount" /></p>
			<p><bean:message key="label.timeout.action" /></p><br>
			<bean:message bundle="clfRes" key="return.to" /> <a href='<bean:message bundle="clfRes" key="home.page.link" />'><bean:message bundle="clfRes" key="home.page" /></a>
		</div>
		
	</div>
	
<!-- the main content end-->		