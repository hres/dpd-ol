<!-- BEGINNING RESULT PAGE -->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- #BeginEditable "body" -->
<%@page import="ca.gc.hc.util.ApplicationGlobals"%>

<!-- #BeginBody -->

 
<bean:define id="lang" >
	<bean:message bundle="clfRes" key="label.app.lang" />
</bean:define>
<% 
   java.util.HashMap params= new java.util.HashMap();
   params.put("lang", lang);
   pageContext.setAttribute("paramsName", params);
   
 %>
<div class="row mrgn-bttm-lg">
	<div class="col-md-6">
		<html:link forward ="SearchPage" name="paramsName">
			<bean:message key="label.results.new.search"/>
		</html:link>
	</div>
</div>

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

<jsp:include page="/jsp/search_criteria.jsp"/>

<h2><bean:message key="label.search.results"/></h2>

<c:choose>
	<c:when test="${result_count > 0 }">
		<c:set var="dataTableProcessingToggle" value="<%=session.getAttribute(ApplicationGlobals.DATA_TABLES_PROCESSING_TOGGLE_VALUE)%>"/>
		<c:choose>
			<c:when test='${result_count >= $sessionScope.dataTables.toggle.value}'>
				<c:set var ='dataTableProcessing' value='{ "order" : [3, "asc"]}'/>
			</c:when>
			<c:otherwise>
				<c:set var='dataTableProcessing' value='{ "order" : [3, "asc"],
				"bServerSide": true,
				"bProcessing": true,
				"sAjaxSource": "./search-recherche.do",
				"sServerMethod" : "POST",
				"sPaginationType": "full_numbers",
				"aoColumns": [
		            { "mData": "status", "defaultContent": "" },
		            { "mData": "din", "defaultContent": "" },
		            { "mData": "company", "defaultContent": "" },
		            { "mData": "brand", "defaultContent": "" },
		            { "mData": "drugClass", "defaultContent": "" },
		            { "mData": "pm", "defaultContent": "" },
		            { "mData": "schedule", "defaultContent": "" },
		            { "mData": "aiNum", "defaultContent": "" },
		            { "mData": "majorAI", "defaultContent": "" },
		            { "mData": "AIStrength", "defaultContent": "", "bSearchable": false, "bSortable": false } 
		        	],
		        "iDisplayLength": ${page_length},
		        "iDeferLoading": ${result_count}
		         }'/>
			</c:otherwise>
		</c:choose>
		
		<table id="results" class="table table-striped table-condensed wb-tables" 
			data-wb-tables='${dataTableProcessing}'>
			<caption class="text-left"><bean:message key="table.search.results.caption"/><br><span class="wb-inv"><bean:message key="table.search.results.summary"/></span></caption>
			<thead>
				<tr>
					<th id="status" scope="col"><bean:message key="label.results.status"/></th>
					<th id="din" scope="col"><bean:message key="label.results.din"/></th>
					<th id="company" scope="col"><bean:message key="label.results.company"/></th>
					<th id="brand" scope="col"><bean:message key="label.results.product"/></th>
					<th id="drugClass" scope="col"><bean:message key="label.results.drug.class"/></th>
					<th id="pm" scope="col"><bean:message key="label.results.pm"/>
						<sup id="fn1-rf"><a class="fn-lnk" href="#fn1"><span class="wb-inv"><bean:message key="label.results.see.footnote"/> </span>1</a></sup>
					</th>

					<th id="schedule" scope="col"><bean:message key="label.results.schedule"/></th>
					<th id="aiNum" scope="col"><bean:message key="label.results.ai.count"/> 
						<sup id="fn2-rf"><a class="fn-lnk" href="#fn2"><span class="wb-inv"><bean:message key="label.results.see.footnote"/> </span>2</a></sup>
					</th>
					<th id="majorAI" scope="col"><bean:message key="label.results.ai.major"/>
						<sup id="fn3-rf"><a class="fn-lnk" href="#fn3"><span class="wb-inv"><bean:message key="label.results.see.footnote"/> </span>3</a></sup>
					</th>
					<th id="AIStrength" scope="col"><bean:message key="label.results.ai.major.strength"/></th>
					
				</tr>
			 </thead>
			 <tbody>
			  <logic:iterate id="element" name="dpd.search.results" >
			  <tr>
			    
			    <td>		    	
		 			<bean:write name="element" property="status"/></td>
			    <td>
			    <bean:define id="code" name="element" property="id" />
				<% 
				   java.util.HashMap params2= new java.util.HashMap();
				   params2.put("code", code);
				   params2.put("lang", lang);
				   pageContext.setAttribute("paramsName2", params2);
				   
				 %>
			     <html:link action="info" name="paramsName2">
			     	<bean:write name="element" property="din"/>
			     </html:link>
			    </td>
			    <td><bean:write name="element" property="companyName"/></td>
			    <td>
				    <logic:notEmpty name="element" property="brandNameLangOfPart">
				 		<bean:define id="brandLangOfPart" name="element" property="brandNameLangOfPart"></bean:define>
				 		<span xml:lang='<%= brandLangOfPart %>' lang='<%= brandLangOfPart %>'><bean:write name="element" property="brandName"/></span>
					</logic:notEmpty>
					<logic:empty name="element" property="brandNameLangOfPart">
						<bean:write name="element" property="brandName"/>
					</logic:empty>
				</td>
			    <td>
				    <logic:notEmpty name="element" property="classLangOfPart">
				 		<bean:define id="classLangOfPart" name="element" property="classLangOfPart"></bean:define>
				 		<span xml:lang='<%= classLangOfPart %>' lang='<%= classLangOfPart %>'><bean:write name="element" property="drugClass"/></span>
					</logic:notEmpty>
					<logic:empty name="element" property="classLangOfPart">
						<bean:write name="element" property="drugClass"/>
					</logic:empty>
				</td>
			    <logic:notEqual name="element" property="pm" value="">
			    	<td><bean:message key="label.results.yes"/></td>
				</logic:notEqual>
			    <logic:equal name="element" property="pm" value="">
				    <td><bean:message key="label.results.no"/></td>
			  	</logic:equal>
			    <td>
				    <logic:notEmpty name="element" property="scheduleLangOfPart">
				 		<bean:define id="scheduleLangOfPart" name="element" property="scheduleLangOfPart"></bean:define>
				 		<span xml:lang='<%= scheduleLangOfPart %>' lang='<%= scheduleLangOfPart %>'><bean:write name="element" property="schedule"/></span>
					</logic:notEmpty>
					<logic:empty name="element" property="scheduleLangOfPart">
						<bean:write name="element" property="schedule"/>
					</logic:empty>
				</td>
			    <td><bean:write name="element" property="numberOfAis"/></td>
			    <td>
				 	<bean:define id="firstAILangOfPart" name="element" property="firstAILangOfPart"></bean:define>
				    <logic:notEmpty name="firstAILangOfPart">
				 		<span xml:lang='<%= firstAILangOfPart %>' lang='<%= firstAILangOfPart %>'>
				 		<bean:write name="element" property="firstAIName"/></span>
					</logic:notEmpty>
					<logic:empty name="firstAILangOfPart">
						<bean:write name="element" property="firstAIName"/>
					</logic:empty>
				</td>
				
		 		<td>
		 			<logic:empty name="element" property="aiStrengthAndDosageLangOfPart">
		 				<bean:write name="element" property="aiStrengthAndDosageText"/>
		 			</logic:empty>
		 			<%-- If aiStrengthAndDosageLangOfPart is not empty, both the strength unit and the dosage unit must be tested individually, 
		 			     and tagged individually for language of part if necessary --%>
		 			<logic:notEmpty name="element" property="aiStrengthAndDosageLangOfPart">
						<logic:notEmpty name="element" property="aiStrengthLangOfPart">
		 					<bean:define id="strengthLangOfPart" name="element" property="aiStrengthLangOfPart"></bean:define>
		 					<bean:write name="element" property="strength"/>
							<span xml:lang='<%= strengthLangOfPart %>' lang='<%= strengthLangOfPart %>'><bean:write name="element" property="strengthUnit"/></span>
		 				</logic:notEmpty>
		 				<logic:empty name="element" property="aiStrengthLangOfPart">
		 					<bean:write name="element" property="aiStrengthText"/>
		 				</logic:empty>
		 				
		 				<logic:notEmpty name="element" property="aiDosageLangOfPart">
		 					<bean:define id="dosageLangOfPart" name="element" property="aiDosageLangOfPart"></bean:define>
			 				<logic:notEmpty name="element" property="dosageValue">
						    	<logic:notEmpty name="element" property="dosageUnit">
								    <bean:message key="label.product.per"/>&nbsp;<bean:write name="element" property="dosageValue"/>
									<span xml:lang='<%= dosageLangOfPart %>' lang='<%= dosageLangOfPart %>'><bean:write name="element" property="dosageUnit"/></span> 
						     	</logic:notEmpty>
					 		</logic:notEmpty>
							<logic:empty name="element" property="dosageValue" >
						       <logic:notEmpty name="element" property="dosageUnit">
							       <logic:notEqual name="element" property="dosageUnit" value="%">
							       	  <bean:message key="label.product.per"/>&nbsp;
										<span xml:lang='<%= dosageLangOfPart %>' lang='<%= dosageLangOfPart %>'><bean:write name="element" property="dosageUnit"/></span>
							       </logic:notEqual>
						      </logic:notEmpty>
					     	</logic:empty>	
		 				</logic:notEmpty>
		 				<logic:empty name="element" property="aiDosageLangOfPart">
		 					<bean:write name="element" property="aiDosageText"/>
		 				</logic:empty>
		 			</logic:notEmpty>
		 		</td>
			</tr>
			</logic:iterate>
			 </tbody>
		</table>
		<aside class="wb-fnote wb-fnote-inited" role="note">
			<h3 id="fn"><bean:message key="label.results.table.footnotes"/></h3>
			<dl>
				<dt id="fn1-dt"><bean:message key="label.results.footnote"/> 1</dt>
				<dd aria-labelledby="fn1-dt" tabindex="-1" id="fn1">
					<p><bean:message key="label.legend.pm"/></p>
					<p class="fn-rtn"><a href="#fn1-rf"><span class="wb-inv"><bean:message key="label.results.return.to.footnote"/> </span>1<span class="wb-inv"> <bean:message key="label.results.referrer"/></span></a></p>
				</dd>
				
				<dt id="fn2-dt"><bean:message key="label.results.footnote"/> 2</dt>
				<dd aria-labelledby="fn2-dt" tabindex="-1" id="fn2">
					<p><bean:message key="label.legend.ai"/></p>
					<p class="fn-rtn"><a href="#fn2-rf"><span class="wb-inv"><bean:message key="label.results.return.to.footnote"/> </span>2<span class="wb-inv"> <bean:message key="label.results.referrer"/></span></a></p>
				</dd>
				<dt id="fn3-dt"><bean:message key="label.results.footnote"/> 3</dt>
				<dd aria-labelledby="fn3-dt" tabindex="-1" id="fn3">
					<p><bean:message key="label.legend.ai.name"/></p>
					<p class="fn-rtn"><a href="#fn3-rf"><span class="wb-inv"><bean:message key="label.results.return.to.footnote"/> </span>3<span class="wb-inv"> <bean:message key="label.results.referrer"/></span></a></p>
				</dd>
			</dl>
		</aside>
	</c:when>
	<c:otherwise>
		<div class="alert alert-info"><h3><bean:message bundle="messageRes" key="message.no.match" /></h3><p><bean:message bundle="messageRes" key="message.no.records" /></p></div>	
	</c:otherwise>
</c:choose>

	<div class="row mrgn-tp-md">
		<div class="col-md-6">
			<html:link forward ="SearchPage" name="paramsName">
				<bean:message key="label.results.new.search"/>
			</html:link>
		</div>
	</div>

<!-- #EndEditable -->