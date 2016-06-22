<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<!-- the main content -->
<div class="row mrgn-tp-lg"> 
	 <div class="col-xs-3 col-sm-2 col-md-1 text-center mrgn-tp-md"> 
	  <span class="glyphicon glyphicon-warning-sign glyphicon-error"></span> 
	 </div> 
	  <div class="col-xs-9 col-sm-10 col-md-11"> 
	  <h1 class="mrgn-tp-md"><bean:message bundle="clfRes" key="404.title"/></h1> 
	  <p class="pagetag"><b><bean:message bundle="clfRes" key="404.error"/></b></p> 
	 </div>
</div> 
<p><bean:message bundle="clfRes" key="404.sorry" /></p>
<ul>
	<li><bean:message bundle="clfRes" key="return.to" /> <a href='<bean:message bundle="clfRes" key="home.page.link" />'><bean:message bundle="clfRes" key="home.page" /></a></li>
	<li><a href='<bean:message bundle="clfRes" key="href.contactUs" />'><bean:message bundle="clfRes" key="contact.us" /></a>&nbsp;<bean:message bundle="clfRes" key="contact.us.help.you" /> </li>
	
</ul>

           