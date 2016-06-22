<%----------------------------------------------------------------------------->
< This is a partial page intended to be used as the sub-header of Health Canada web application.
<-------------------------------------------------------------------------- --%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<!-- SUB-HEADER BEGINS | DEBUT DU SOUS EN-TETE -->
	<div class="wb-eqht">
		<section class="sect-lnks">
			<h2><bean:message key="label.h3.app.info" /></h2>
			<ul class="list-unstyled">
				<li><a href="<bean:message key='link.sidebar.tips' />"> <bean:message
					key="label.sidebar.help" /> </a></li>
				<li><a href="<bean:message key='link.sidebar.terminology' />"> <bean:message
					key="label.sidebar.terminology" /> </a></li>
				<li><a href="<bean:message key='link.sidebar.extract' />"> <bean:message
					key="label.sidebar.extract.data" /> </a>
				</li>
			</ul>
		</section>
		<section class="sect-lnks">
			<h2><bean:message key="label.h3.related.info" /></h2>
			<ul class="list-unstyled">
				<li><a href="<bean:message key='link.sidebar.advisories_warnings' />"> <bean:message
					key="label.sidebar.advisories_warnings" /> </a></li>
				<li><a href="<bean:message key='link.sidebar.parmacovigilance' />"> <bean:message
					key="label.sidebar.pharmacovigilance" /> </a></li>
				<li><a href="<bean:message key='link.sidebar.noc' />" > <bean:message
					key="label.sidebar.noc" /> </a></li>
				<li><a href="<bean:message key='link.sidebar.nhpd'/>" ><bean:message 
					key="label.sidebar.nhpd" /></a></li>	
			</ul>
		</section>		
		<section class="sect-lnks">
			<h2><bean:message key="label.h3.contact.us" /></h2>
			<ul class="list-unstyled">
				<li><a href="<bean:message key='link.sidebar.sipdmail' />"> <bean:message
					key="label.sidebar.sipdmail" /> </a></li>
					<li><a href="<bean:message key='link.sidebar.contactinfo'/>"><bean:message 
						key="label.sidebar.contactinfo" /></a></li>
			</ul>	
		</section>
	</div>


<!-- SUB-HEADER ENDS | FIN DU SOUS EN-TETE-->



