<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.*" %>

<!-- #BeginEditable "body" -->
<%@ page import="org.apache.struts.Globals" %>

<bean:define id="myAction" >
		/dispatch-repartition.do
</bean:define>

<!--Display application error messages-->
<div class="mrgn-tp-md mrgn-bttm-lg">
<logic:present name="org.apache.struts.action.ACTION_MESSAGE" scope="request" >
	<div class="alert alert-danger">
		<html:messages id="message"	message="true" bundle="messageRes">
			<h2><bean:write name="message" /></h2>
		</html:messages>
	</div>
</logic:present>

<!--Display validation error messages-->
<logic:present name="org.apache.struts.action.ERROR" scope="request">
	<div class="alert alert-danger">
		<h2><bean:message bundle="messageRes" key="error.message" /></h2>
     	<html:errors bundle="messageRes" />
	</div>
</logic:present>
</div>

<p class="well">
		<bean:message key="label.disclaimer.language" />
</p>
<bean:define id="defaultStatus" value="2"/>

<h2><bean:message key="label.search.criteria" /></h2>

<p class="help-block"><bean:message key="prompt.search.drug.instruction"/></p>

<html:form action="<%=myAction%>" method="post" styleClass="form-horizontal">
<input type="hidden" name="lang" value='<bean:message bundle="clfRes" key="label.app.lang" />'/> 

<fieldset class="mrgn-tp-md"><legend><bean:message key="label.search.by.din" /></legend>
	<div class="form-group">
		<label for="din" class="col-lg-3 control-label"><bean:message key="label.search.drug.din"/></label> 
		<div class="col-lg-9">
			<logic:messagesPresent name="org.apache.struts.action.ERROR" property="DIN">
				<bean:message bundle="messageRes" key="error.invalide.din.without.anchor" />
			</logic:messagesPresent>
			<html:text property="din" size="8"  styleId="din" maxlength="8" styleClass="form-control" />
		</div>
	</div>
</fieldset>	

<fieldset class="mrgn-tp-md"><legend><bean:message key="label.search.by.atc" /></legend>
	<div class="form-group">
		<label for="atc" class="col-lg-3 control-label"><bean:message key="label.search.drug.atc"/></label>
		<div class="col-lg-9">
			<html:text property="atc" size="7" styleId="atc" maxlength="7" styleClass="form-control" />
			</div>
	</div>
</fieldset>

<fieldset class="mrgn-tp-md"><legend><bean:message key="label.search.by.other" /></legend>
	<div class="form-group">
		<label for="status" class="col-lg-3 control-label"><bean:message key="label.search.drug.status"/></label>
		<div class="col-lg-9 colmrgn-bttm-sm">
			<logic:messagesPresent name="org.apache.struts.action.ERROR" property="status">
				<bean:message bundle="messageRes" key="error.invalide.status.without.anchor" />
			</logic:messagesPresent>		
			<html:select name="search" styleId="status" property="status" styleClass="form-control" >
				<html:optionsCollection name="appGlobals" property="status" label="label" value="value"/>
			</html:select>
		</div>
	</div>
	<div class="form-group">
			<label for="company" class="col-lg-3 control-label"><bean:message key="label.search.drug.company"/></label>
	 	<div class="col-lg-9 mrgn-bttm-sm">
			<logic:messagesPresent name="org.apache.struts.action.ERROR" property="companyName">
				<bean:message bundle="messageRes" key="error.invalide.company.without.anchor" />
			</logic:messagesPresent>
			<html:text property="companyName" styleId="company" maxlength="80" styleClass="form-control" />
		</div>
	</div>
	<div class="form-group">
	  		<label for="product" class="col-lg-3 control-label"><bean:message key="label.search.drug.product"/></label>
	 	<div class="col-lg-9 mrgn-bttm-sm">
			<logic:messagesPresent name="org.apache.struts.action.ERROR" property="brandName">
				<bean:message bundle="messageRes" key="error.invalide.product.without.anchor" />
			</logic:messagesPresent>
	 		<html:text property="brandName" styleId="product" maxlength="80" styleClass="form-control" />
		</div>
	</div>
	<div class="form-group">
			<label for="activeIngredient" class="col-lg-3 control-label"><bean:message key="label.search.drug.active.ingredient"/></label>
		<div class="col-lg-9 mrgn-bttm-sm">
			<logic:messagesPresent name="org.apache.struts.action.ERROR" property="activeIngredient">
				<bean:message bundle="messageRes" key="error.invalide.ai.without.anchor" />
			</logic:messagesPresent>
	 		<html:text property="activeIngredient" styleId="activeIngredient" maxlength="240" styleClass="form-control" />
		</div>
	</div>
	<div class="form-group">
	 		<label for="aigNumber" class="col-lg-3 control-label"><bean:message key="label.search.drug.active.ingredient.group"/></label>
		<div class="col-lg-9 mrgn-bttm-sm">
			<logic:messagesPresent name="org.apache.struts.action.ERROR" property="aigNumber">
				<bean:message bundle="messageRes" key="error.invalide.aig.without.anchor" />
			</logic:messagesPresent>
			<html:text property="aigNumber" size="10" styleId="aigNumber" maxlength="10" styleClass="form-control" />
		</div>
	</div>
	
	<div class="form-group">
	 		<label for="drugClass" class="col-lg-3 control-label"><bean:message key="label.search.drug.class"/></label>
	 	
		<div class="col-lg-9 mrgn-bttm-sm">
			<html:select name="search" property="drugClass" styleId="drugClass" multiple="true" size="5" styleClass="form-control" >
				<html:optionsCollection name="appGlobals" property="uniqueDrugClasses" label="label" value="value" />
			</html:select>
		</div>
	</div>
	<div class="form-group">
	 		<label for="route" class="col-lg-3 control-label"><bean:message key="label.search.drug.route"/></label>
	 	
		<div class="col-lg-9 mrgn-bttm-sm">
			<html:select name="search" property="route" styleId="route" multiple="true" size="5" styleClass="form-control" >
				<html:optionsCollection name="appGlobals" property="uniqueRoutes" label="label" value="value" />
			</html:select>
		</div>
	</div>
	<div class="form-group">
	 		<label for="dosage" class="col-lg-3 control-label"><bean:message key="label.search.drug.dosage.form"/></label>
	 	
		<div class="col-lg-9 mrgn-bttm-sm">
			<html:select name="search" property="dosage" styleId="dosage" multiple="true" size="5" styleClass="form-control" >			
				<html:optionsCollection name="appGlobals" property="uniqueForms" label="label" value="value" />
			</html:select>
		</div>
	</div>
	<div class="form-group">
	 		<label for="schedule" class="col-lg-3 control-label"><bean:message key="label.search.drug.schedule"/></label>
	 	
		<div class="col-lg-9">
			<html:select name="search" property="schedule" styleId="schedule" multiple="true" size="5" styleClass="form-control" >			
				<html:optionsCollection name="appGlobals" property="uniqueSchedules" label="label" value="value" />
			</html:select>
		</div>
	</div>
</fieldset>

<div class="form-group">	
	<div class="col-lg-offset-3">
		<html:submit property="method" styleClass="btn btn-primary mrgn-rght-sm mrgn-lft-md"><bean:message key="button.search" /></html:submit>
		<html:reset property="method" styleClass="btn btn-default"><bean:message key="button.reset" />
		</html:reset>
 	</div>
</div>
</html:form>
<!-- #EndEditable -->