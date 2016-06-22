<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page import="org.apache.struts.Globals" %>

<h2><bean:message key="label.results.criteria"/></h2>
<logic:present name="dpd.search.criteria" property="din" scope="session">
	<p>
<%--		<strong><bean:message key="label.results.criteria"/></strong>&nbsp;  --%>
		<bean:message key="label.criteria.drug.din"/>
		&nbsp;
		<bean:write name="dpd.search.criteria" property="din"/>
	</p>
</logic:present>
<logic:notPresent name="dpd.search.criteria" property="din" scope="session">
	<ul class="noBullet">
		<logic:present name="dpd.search.criteria" property="atc" scope="session">
	   		<li><bean:message key="label.criteria.drug.atc"/>
	   			&nbsp;
 					<bean:write name="dpd.search.criteria" property="atc"/>
			</li>
	 	</logic:present>
		<logic:notPresent name="dpd.search.criteria" property="atc" scope="session">
			<li><div class="indent2">
				<bean:message key="label.criteria.drug.status"/>
				
				<bean:define id="statusId" name="dpd.search.criteria" property="statusCode" />
				<logic:iterate name="appGlobals" property="status" id="statusElement">
					<logic:equal name="statusElement" property="value" value='<%= statusId.toString() %>' >
 						<bean:write name="statusElement" property="label" />
			        </logic:equal>
			    </logic:iterate>
		    	</div>
	    	</li>		
			<logic:present name="dpd.search.criteria" property="companyName" scope="session">
		     	<li><div class="indent2"><bean:message key="label.criteria.drug.company"/>
		     		
				<bean:write name="dpd.search.criteria" property="companyName"/>
	     			</div>
     			</li>
		    </logic:present>
		    <logic:present name="dpd.search.criteria" property="brandName" scope="session">
		     	<li><div class="indent2">
		     		<bean:message key="label.criteria.drug.product"/>		     	
		     		<bean:write name="dpd.search.criteria" property="brandName"/>
		     		</div>
	     		</li>
		    </logic:present>
		    <logic:present name="dpd.search.criteria" property="activeIngredient" scope="session">
		     	<li><div class="indent2"><bean:message key="label.criteria.drug.ai"/>
 					<bean:write name="dpd.search.criteria" property="activeIngredient"/>
					</div>
				</li>
			</logic:present>
		    <logic:present name="dpd.search.criteria" property="aigNumber" scope="session">
		    	<li><div class="indent2">
		    		<bean:message key="label.criteria.drug.aig"/>
		     		<bean:write name="dpd.search.criteria" property="aigNumber"/>
		     		</div>
	     		</li>
		    </logic:present>
		    <logic:present name="dpd.search.criteria" property="drugClass" scope="session">
		    	<li><div class="indent2">
		    		<bean:message key="label.criteria.drug.class"/>
		     		<bean:write name="drugClassNames"/>
		     		</div>
	     		</li>
		    </logic:present>
		    <logic:present name="dpd.search.criteria" property="routeEnumeration" scope="session">
		    	<li><div class="indent2">
		    		<bean:message key="label.criteria.drug.route"/>
 					<bean:write name="dpd.search.criteria" property="routeEnumeration"/>
		     		</div>
				</li>
		    </logic:present>
		    <logic:present name="dpd.search.criteria" property="dosageEnumeration" scope="session">
		    	<li><div class="indent2">
		    		<bean:message key="label.criteria.drug.dosage"/>
	 				<bean:write name="dpd.search.criteria" property="dosageEnumeration"/>
		     		</div>
		     	</li>
		    </logic:present>
		    <logic:present name="dpd.search.criteria" property="scheduleEnumeration" scope="session">
		    	<li><div class="indent2">
		    		<bean:message key="label.criteria.drug.schedule"/>
 					<bean:write name="dpd.search.criteria" property="scheduleEnumeration"/>
		     		</div>
		     	</li>
		    </logic:present>
	 	</logic:notPresent>
	</ul> 
</logic:notPresent>
<!-- #EndEditable -->