<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<p>
<strong><bean:message key="label.product.route.admin"/></strong>&nbsp;
 
   <logic:present name="dpd.selected.product" property="routeList" scope="session">
   		<logic:iterate id="element" name="dpd.selected.product" property="routeList" indexId="index">
			<bean:define id="routeLangOfPart" name="dpd.selected.product" property='<%= "routeLangOfPart[" + index + "]" %>'></bean:define>
			<logic:notEmpty name="dpd.selected.product" property='<%= "routeLangOfPart[" + index + "]" %>'>
				<span xml:lang='<%= routeLangOfPart %>' lang='<%= routeLangOfPart %>'><bean:write name="element" property="routeOfAdministration"/></span><logic:greaterThan name="dpd.selected.product" property="routeSpeciesCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan>
			</logic:notEmpty>
			<logic:empty name="routeLangOfPart"><bean:write name="element" property="routeOfAdministration"/></logic:empty><logic:greaterThan name="dpd.selected.product" property="routeSpeciesCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan>
	</logic:iterate>
   </logic:present>
   <logic:notPresent name="dpd.selected.product" property="routeList" scope="session">
    	<bean:message bundle="messageRes" key="message.not.provided"/>
   </logic:notPresent>
</p>
<p>
  <strong><bean:message key="label.product.ai.count"/></strong>&nbsp;
  <bean:write name="dpd.selected.product"  property="drugProduct.numberOfAis" scope="session"/>
</p>
<p><strong><bean:message key="label.product.schedule"/></strong>
  &nbsp;
   <logic:present name="dpd.selected.product" property="scheduleList" scope="session">
   		<logic:iterate id="element" name="dpd.selected.product" property="scheduleList" indexId="index">
			<bean:define id="scheduleLangOfPart" name="dpd.selected.product" property='<%= "scheduleLangOfPart[" + index + "]" %>'></bean:define>
			<logic:notEmpty name="dpd.selected.product" property='<%= "scheduleLangOfPart[" + index + "]" %>'>
				<span xml:lang='<%= scheduleLangOfPart %>' lang='<%= scheduleLangOfPart %>'><bean:write name="element" property="schedule"/></span><logic:greaterThan name="dpd.selected.product" property="scheduleSpeciesCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan>
			</logic:notEmpty>
			<logic:empty name="scheduleLangOfPart"><bean:write name="element" property="schedule"/><logic:greaterThan name="dpd.selected.product" property="scheduleSpeciesCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan></logic:empty>
		</logic:iterate>
   </logic:present>
   <logic:notPresent name="dpd.selected.product" property="scheduleList" scope="session">
    	<bean:message bundle="messageRes" key="message.not.provided"/>
   </logic:notPresent>
 </p>
<p>
	<strong><span xml:lang="en" lang="en"><bean:message key="label.product.ahfs"/></span></strong>
		<sup id="fn2-rf"><a class="fn-lnk" href="#fn2"><span class="wb-inv"><bean:message key="label.results.see.footnote"/> </span>2</a></sup>

	<logic:present name="dpd.selected.product" property="ahfsList" scope="session">
   		<logic:iterate id="element" name="dpd.selected.product" property="ahfsList" indexId="index">
    		<logic:match name="element" property="ahfsNumber" value="*">
  				<sup id="fn5-rf"><a class="fn-lnk" href="#fn5"><span class="wb-inv"><bean:message key="label.results.see.footnote"/> </span>5</a></sup>
  				<bean:define id="showFootnoteFive" value="true" toScope="request"></bean:define>
   			</logic:match>
			<bean:define id="ahfsLangOfPart" name="dpd.selected.product" property='<%= "ahfsLangOfPart[" + index + "]" %>'></bean:define>
			<logic:notEmpty name="dpd.selected.product" property='<%= "ahfsLangOfPart[" + index + "]" %>'>
				<bean:write name="element"  property="ahfsNumber"/>&nbsp;
				<span xml:lang='<%= ahfsLangOfPart %>' lang='<%= ahfsLangOfPart %>'><bean:write name="element" property="ahfs"/></span><logic:greaterThan name="dpd.selected.product" property="ahfsSpeciesCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan>
			</logic:notEmpty>
			<logic:empty name="ahfsLangOfPart"><bean:write name="element"  property="ahfsNumber"/>&nbsp;<bean:write name="element" property="ahfs"/><logic:greaterThan name="dpd.selected.product" property="ahfsSpeciesCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan></logic:empty>
  		 </logic:iterate>
  </logic:present>	
		
  <logic:notPresent name="dpd.selected.product" property="ahfsList" scope="session">
   <logic:equal name="dpd.selected.product" property="drugProduct.drugClassE" scope="session" value="Veterinary">
    	<bean:message bundle="messageRes" key="message.not.applicable"/>
   </logic:equal>
   <logic:notEqual name="dpd.selected.product" property="drugProduct.drugClassE" scope="session" value="Veterinary">
    	<bean:message bundle="messageRes" key="message.not.provided"/>
   </logic:notEqual>
  </logic:notPresent>
</p>
<p>
 <strong><bean:message key="label.product.atc"/></strong>
  		<sup id="fn3-rf"><a class="fn-lnk" href="#fn3"><span class="wb-inv"><bean:message key="label.results.see.footnote"/> </span>3</a></sup>
  <logic:present name="dpd.selected.product" property="atcVO.atc" scope="session">
	 <bean:define id="atcLangOfPart" name="dpd.selected.product" property="atcLangOfPart"></bean:define>
  	 <logic:notEmpty name="dpd.selected.product" property="atcLangOfPart">
	 	<bean:write name="dpd.selected.product"  property="atcVO.atcNumber" scope="session"/>&nbsp;
	 	<span xml:lang='<%= atcLangOfPart %>' lang='<%= atcLangOfPart %>'><bean:write name="dpd.selected.product" property="atcVO.atc" scope="session"/></span>
	 </logic:notEmpty>
	 <logic:empty name="atcLangOfPart">
	 	<bean:write name="dpd.selected.product"  property="atcVO.atcNumber" scope="session"/>&nbsp;		    	
 		<bean:write name="dpd.selected.product" property="atcVO.atc" scope="session"/>
	 </logic:empty>
  </logic:present>
  <logic:notPresent name="dpd.selected.product" property="atcVO.atc" scope="session">
	   <logic:equal name="dpd.selected.product" property="drugProduct.drugClassE" scope="session" value="Veterinary">
	    	<bean:message bundle="messageRes" key="message.not.applicable"/>
	   </logic:equal>
	   <logic:notEqual name="dpd.selected.product" property="drugProduct.drugClassE" scope="session" value="Veterinary">
		    <logic:present name="ahfs.92" scope="session">
		     	<bean:message bundle="messageRes" key="message.not.applicable"/>
		    </logic:present>
		    <logic:notPresent name="ahfs.92" scope="session">
		     	<bean:message bundle="messageRes" key="message.not.provided"/>
		    </logic:notPresent>
	   </logic:notEqual>
  </logic:notPresent>
</p>
