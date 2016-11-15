<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<div class="row">
	<p class="col-sm-4">
	  <strong><bean:message key="label.product.aig.no"/></strong><sup id="fn6-rf"><a class="fn-lnk" href="#fn6"><span class="wb-inv"><bean:message key="label.results.see.footnote"/> </span>6</a></sup>
	</p>
	<p class="col-sm-8">
  		
  		<bean:write name="dpd.selected.product"  property="drugProduct.aiGroupNo" scope="session"/>
	</p>
</div>
<div class="table-responsive mrgn-tp-lg">
	<table class='table table-striped dataTables_info table-bordered data-wb-tables={"ordering" : false}'>
		<caption class="text-left"><bean:message key="label.active.ingredients.caption"/></caption>
		<tr>
	 		<th id="ingredient" scope="col" class="alignCenter"><bean:message key="label.product.ingredient"/></th>
	 		<th id="strength" scope="col" class="alignCenter"><bean:message key="label.product.strength"/></th>
	 	</tr>
		 <logic:present name="dpd.selected.product" property="activeIngredientList" scope="session">
		  	<logic:iterate id="element" name="dpd.selected.product" property="activeIngredientList" indexId="index">
			<tr>
		 		<td>
					<bean:define id="ingrLangOfPart" name="dpd.selected.product" property='<%= "ingredientLangOfPart[" + index + "]" %>'></bean:define>
					<logic:notEmpty name="ingrLangOfPart">
						<span xml:lang='<%= ingrLangOfPart %>' lang='<%= ingrLangOfPart %>'><bean:write name="element" property="ingredient"/></span>
					</logic:notEmpty>
		 			<logic:empty name="ingrLangOfPart"><bean:write name="element" property="ingredient"/></logic:empty>
	 			</td>
		 		<td>
		 			<logic:empty name="dpd.selected.product" property='<%= "aiStrengthAndDosageLangOfPart[" + index + "]" %>'>
		 				<bean:write name="element" property="aiStrengthAndDosageText"/>
		 			</logic:empty>
		 			<%-- If aiStrengthAndDosageLangOfPart is not empty, both the strength unit and the dosage unit must be tested individually, 
		 			     and tagged individually for language of part if necessary --%>
		 			<logic:notEmpty name="dpd.selected.product" property='<%= "aiStrengthAndDosageLangOfPart[" + index + "]" %>'>
		 				<bean:define id="strengthLangOfPart" name="dpd.selected.product" property='<%= "aiStrengthLangOfPart[" + index + "]" %>'></bean:define>
						<logic:notEmpty name="strengthLangOfPart">
		 					<bean:write name="element" property="strength"/>
							<span xml:lang='<%= strengthLangOfPart %>' lang='<%= strengthLangOfPart %>'><bean:write name="element" property="strengthUnit"/></span>
		 				</logic:notEmpty>
		 				<logic:empty name="strengthLangOfPart">
		 					<bean:write name="element" property="aiStrengthText"/>
		 				</logic:empty>
		 				
		 				
		 				<bean:define id="dosageLangOfPart" name="dpd.selected.product" property='<%= "aiDosageLangOfPart[" + index + "]" %>'></bean:define>
		 				<logic:notEmpty name="dosageLangOfPart">
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
		 				<logic:empty name="dosageLangOfPart">
		 					<bean:write name="element" property="aiDosageText"/>
		 				</logic:empty>
		 			</logic:notEmpty>
		 		</td>
		 	</tr>	   
		  </logic:iterate> 
	 </logic:present>
	</table>
</div>	

<logic:notPresent name="dpd.selected.product" property="activeIngredientList" scope="session">
	<bean:message bundle="messageRes" key="message.not.provided"/>
</logic:notPresent>

<div class="row mrgn-tp-lg">
	<div class="col-xs-6">
	  <html:link forward ="SearchPage" name="paramsLang">
	   <bean:message key="label.results.new.search"/>
	  </html:link>
	  <bean:define id="lang" >
		<bean:message bundle="clfRes" key="label.app.lang" />
		</bean:define>
	  <bean:define id="no" name="dpd.selected.product" property="drugProduct.aiGroupNo" />
		<% 
		   java.util.HashMap params= new java.util.HashMap();
		   params.put("no", no);
		   params.put("lang", lang);
		   pageContext.setAttribute("paramsName2", params);
		   
		 %>
	</div>
	<div class="col-xs-6"> 
	  <html:link action="/search-fast-recherche-rapide" name="paramsName2" >
	    <bean:message key="label.product.similar.prod"/>
	  </html:link>
	</div>
</div>
<!-- #EndEditable -->