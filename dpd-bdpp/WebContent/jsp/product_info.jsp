<!-- BEGINNING PRODUCT INFORMATION PAGE -->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<!-- #BeginEditable "body" -->
<%@ page import="org.apache.struts.Globals" %>

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

<logic:equal name="similarAIG" value="Y" scope="request">
	<div class="alert alert-info"><h3><bean:message bundle="messageRes" key="message.no.match" /></h3><p><bean:message bundle="messageRes" key="message.no.similar.aig.product" /></p></div>
</logic:equal>
<logic:equal name="similarProduct" value="Y" scope="request">
	<div class="alert alert-info"><h3><bean:message bundle="messageRes" key="message.no.match" /></h3><p><bean:message bundle="messageRes" key="message.no.similar.company.product" /></p></div>
</logic:equal>

<p class="mrgn-tp-md mrgn-bttm-lg">
		<html:link forward ="SearchPage" name="paramsLang">
		   <bean:message key="label.results.new.search"/>
		</html:link>
</p>

<%-- if the drug class is not veterinary and the pm exist put a disclaimer and the pm link --%>
<logic:notEqual name="dpd.selected.product" property="drugProduct.drugClassE" scope="session" value="Veterinary">
	<logic:present name="dpd.selected.product" property="pmVO" scope="session">
		<logic:notEqual name="dpd.selected.product" property="pmVO.pmName" value="" scope="session">	
			<logic:notEqual name="dpd.selected.product" property="drugProduct.drugIdentificationNumber"  scope="session" 
				value="02337258" >
					<logic:notEqual name="dpd.selected.product" property="drugProduct.drugIdentificationNumber"  scope="session" 
						value="02336650" >	
					<p class="well">
						<bean:message key="label.pm.disclaimer" />
					</p>
				</logic:notEqual>
			</logic:notEqual>
			
			<%-- added condition if DIN = 02336650 put h1n1 disclaimer - Date: Oct. 22, 2009 --%>
			<logic:equal name="dpd.selected.product" property="drugProduct.drugIdentificationNumber"  scope="session" 
				value="02336650" >
				<div class="highlight2">
					<bean:message key="label.pm.h1n1.din02336650.disclaimer" />
				</div>
			</logic:equal>
			<%-- added condition if DIN = 02337258 put h1n1 disclaimer - Date: Nov. 5, 2009 --%>
			<logic:equal name="dpd.selected.product" property="drugProduct.drugIdentificationNumber"  scope="session" 
				value="02337258" >
				<div class="highlight2">
					<bean:message key="label.pm.h1n1.din02337258.disclaimer" />
				</div>
			</logic:equal>
			<bean:define type="java.lang.String" id="productMonograph" name="dpd.selected.product"
				property="pmVO.pmName" scope="session" toScope="page" />
			<div><html:hidden name="viewPmForm" property="pmName" value='<%= productMonograph %>'/> </div>
		</logic:notEqual>
	</logic:present>
</logic:notEqual>
<div class="row">
    <p class="col-sm-4"><strong>Adverse Reaction Report Number:</strong></p>
    <div class="col-sm-8"><p id="ReportNo"></p></div>
</div>
<div class="row">
    <p class="col-sm-4"><strong>
	<bean:message key="label.product.current.status"/></strong></p>
<p class="col-sm-8">	<strong>
	<bean:define id="statusLangOfPart" name="dpd.selected.product" property="statusLangOfPart"></bean:define>
	<logic:notEmpty name="statusLangOfPart">
		<span class="strong" xml:lang='<%= statusLangOfPart %>' lang='<%= statusLangOfPart %>'><bean:write name="dpd.selected.product" property="statusVO.status"/></span>
		</logic:notEmpty>
	<logic:empty name="statusLangOfPart">
		<bean:write name="dpd.selected.product" property="statusVO.status"/>
		</logic:empty>
</strong></p>
</div>
<logic:equal name="dpd.selected.product" property="isRadioPharmaceutical" scope="session" value="false">
<div class="row">
    <p class="col-sm-4">
	<strong><bean:message key="label.product.current.date"/></strong>
	<p class="col-sm-8">	
		<bean:write name="dpd.selected.product" property="statusVO.historyDate" format="yyyy-MM-dd" scope="session"/>
	<br/>
	</p>
	
</div>
<div class="row">
<logic:equal name="dpd.selected.product" property="isApproved" scope="session" value="false">
    <p class="col-sm-4">
		<strong><bean:message key="label.original.market.date"/></strong>
	</p>
	<p class="col-sm-8">	
		<sup id="fn1-rf"><a class="fn-lnk" href="#fn1"><span class="wb-inv"><bean:message key="label.results.see.footnote"/> </span>1</a></sup>
	  	<bean:write name="dpd.selected.product" property="originalMarketDate" format="yyyy-MM-dd" scope="session"/>
	 <br/>
	</p>
	</logic:equal>
</logic:equal>
</div>
<div class="row">
	<p class="col-sm-4">	
	 <strong><bean:message key="label.product.brand.name"/></strong>
	</p>
	<p class="col-sm-8">
	 <bean:define id="brandLangOfPart" name="dpd.selected.product" property="brandNameLangOfPart"></bean:define>
	 <logic:notEmpty name="brandLangOfPart">
	 	<span xml:lang='<%= brandLangOfPart %>' lang='<%= brandLangOfPart %>'><bean:write name="dpd.selected.product" property="drugProduct.brandName"  scope="session"/></span>
	 </logic:notEmpty>
	 <logic:empty name="brandLangOfPart">
	 	<bean:write name="dpd.selected.product" property="drugProduct.brandName"  scope="session"/>
	 </logic:empty>
	</p>
</div>
<div class="row">
	
 <logic:notEmpty name="dpd.selected.product" property="drugProduct.descriptor">
 	<p class="col-sm-4">
 	<strong><bean:message key="label.product.description"/></strong>
 	</p>
 	<p class="col-sm-8">
	 <bean:define id="descriptorLangOfPart" name="dpd.selected.product" property="descriptorLangOfPart"></bean:define>
 	 <logic:notEmpty name="descriptorLangOfPart">
	 	<span xml:lang='<%= descriptorLangOfPart %>' lang='<%= descriptorLangOfPart %>'><bean:write name="dpd.selected.product" property="drugProduct.descriptor"  scope="session"/></span>
	 </logic:notEmpty>
	 <logic:empty name="descriptorLangOfPart">
	 	<bean:write name="dpd.selected.product" property="drugProduct.descriptor"  scope="session"/>
	 </logic:empty>
	<br/>
	</p>
 </logic:notEmpty>

</div>
<%-- if the drug class is not veterinary and the pm does not exist put a label saying the PM is not available --%>
<logic:notEqual name="dpd.selected.product" property="drugProduct.drugClassE" scope="session" value="Veterinary">
	<logic:present name="dpd.selected.product" property="pmVO" scope="session">
	   	<logic:notEqual name="dpd.selected.product" property="pmVO.pmName" value="" scope="session">
			
			<span class="help-block">
				<bean:message key="label.blurb.alternate.format"/>
				<a href="<bean:message key='link.blurb.alternate.format'/>" >
					<bean:message key="label.link.blurb.alternate.format"/>
				</a>.
				</span>
			
			<div class="row">
			<p class="col-sm-4">
			<strong><bean:message key="label.product.din"/></strong>
			</p>
			<p class="col-sm-8">
		 	<bean:write name="dpd.selected.product" property="drugProduct.drugIdentificationNumber"  scope="session"/>
		  	
		  
			<span class="glyphicon glyphicon-paperclip mrgn-lft-xl">
			 	<html:link action="item-iteme" paramId="pm-mp" paramName="dpd.selected.product" paramProperty="pmVO.pmName">
			 		<bean:message key="button.viewPM"/>
			 	</html:link><br/>
			 	</span>
			 </p>
			 </div>
	 		</logic:notEqual>
	
		<logic:equal name="dpd.selected.product" property="pmVO.pmName" value="" scope="session">
			<div class="row">
			<p class="col-sm-4">
			<strong><bean:message key="label.product.din"/></strong>
			</p>
			<p class="col-sm-8">
				<bean:write name="dpd.selected.product" property="drugProduct.drugIdentificationNumber"  scope="session"/>
			 	<div class="indent2 lightgreyBG">
		 			<bean:message key="button.disabledPM"/>
		 		</div>
		 	</p>
		 	</div>
		 	</logic:equal>
		</logic:present>
	</logic:notEqual>
<logic:equal name="dpd.selected.product" property="drugProduct.drugClassE" scope="session" value="Veterinary" >
	<div class="row">
		<p class="col-sm-4">
			<strong><bean:message key="label.product.din"/></strong>
		</p>
		<p class="col-sm-8">
	 		<bean:write name="dpd.selected.product" property="drugProduct.drugIdentificationNumber"  scope="session"/>
	 	</p>
	 	</div>
	</logic:equal>

	<%-- if the drug class is not veterinary and the pm does not exist only put the din no message --%>
	<logic:notEqual name="dpd.selected.product" property="drugProduct.drugClassE" scope="session" value="Veterinary">
		<logic:notPresent name="dpd.selected.product" property="pmVO" scope="session">
			<div class="row">
				<p class="col-sm-4">
					<strong><bean:message key="label.product.din"/></strong>
					</p>
				<p class="col-sm-8">
			 		<bean:write name="dpd.selected.product" property="drugProduct.drugIdentificationNumber"  scope="session"/>
			
				<span class="indent2 lightgreyBG">
		 		<bean:message key="button.disabledPM"/>
		 		</span>
		 		</p>
		 		</div>
			</logic:notPresent>
		</logic:notEqual>
<%-- Handling of labels is postponed until a later release: Martin Bernard 2010-03-10
<p>	
	<logic:present name="approvedLabel" scope="session">
			<strong><bean:message key="label.approvedLabel"/>:&nbsp;</strong>
		<div class="icon"><img src="<bean:message bundle="clfRes" key="label.hc.url"/>/images/common/file_pdf.gif" width="22" height="21" />
	
	 	<html:link action="view-afficher" titleKey="alt.viewApprovedLabel">
	 		<bean:message key="label.approvedLabel"/>
	 	</html:link></div>
		<br />
	</logic:present>
	
	<logic:present name="marketedLabel" scope="session">
			<strong><bean:message key="label.marketedLabel"/>:&nbsp;</strong>
		<div class="icon"><img src="<bean:message bundle="clfRes" key="label.hc.url"/>/images/common/file_pdf.gif" width="22" height="21" />
	
	 	<html:link action="view-afficher?label=marketed" titleKey="alt.viewMarketedLabel">
	 		<bean:message key="label.marketedLabel"/>
	 	</html:link></div>
 		<br />
	</logic:present>
</p>
 --%>
<div class="row">
	<p class="col-sm-4">
		<strong><bean:message key="label.product.company"/></strong>&nbsp;
  	</p>
  	<p class="col-sm-8">
	  	<bean:define id="lang" >
			<bean:message bundle="clfRes" key="label.app.lang" />
		</bean:define>
	  	<bean:define id="code" name="dpd.selected.product" property="companyId" />
	  	
	  	<% 
		   java.util.HashMap params= new java.util.HashMap();
		   params.put("code", code);
		   params.put("lang", lang);
		   pageContext.setAttribute("params", params);
   
 		%>
   		<html:link action="/search-fast-recherche-rapide" name="params" styleId="company" titleKey="link.title.product.company">
    		<bean:write name="dpd.selected.product" property="companyVO.companyName" scope="session"/>
   		</html:link>
 
	
		<logic:present name="dpd.selected.product" property="companyVO.suiteNumner" scope="session">	
	     		<br/>
	     		<bean:write name="dpd.selected.product" property="companyVO.suiteNumner" scope="session"/>&nbsp;
	  	</logic:present>
	  	<logic:notPresent name="dpd.selected.product" property="companyVO.suiteNumner" scope="session">
	  		<logic:present name="dpd.selected.product" property="companyVO.streetName" scope="session">
	  			<br/>
	  		</logic:present>
	  	</logic:notPresent>
	  	
    	<logic:present name="dpd.selected.product" property="companyVO.streetName" scope="session">
			 <bean:define id="streetNameLangOfPart" name="dpd.selected.product" property="streetNameLangOfPart"></bean:define>
    		 <logic:notEmpty name="streetNameLangOfPart">
			 	<span xml:lang='<%= streetNameLangOfPart %>' lang='<%= streetNameLangOfPart %>'><bean:write name="dpd.selected.product" property="companyVO.streetName" scope="session"/></span>
			 </logic:notEmpty>
			 <logic:empty name="streetNameLangOfPart">
			 	<bean:write name="dpd.selected.product" property="companyVO.streetName" scope="session"/>
			 </logic:empty>
    	</logic:present>
	  	

	   <logic:present name="dpd.selected.product" property="companyVO.cityName" scope="session">
	    <br/><bean:write name="dpd.selected.product" property="companyVO.cityName" scope="session"/>
	   </logic:present>
	
	   <logic:present name="dpd.selected.product" property="companyVO.province" scope="session">
	   <br/>
		 <bean:define id="provinceLangOfPart" name="dpd.selected.product" property="provinceLangOfPart"></bean:define>
	   	 <logic:notEmpty name="provinceLangOfPart">
		 	<span xml:lang='<%= provinceLangOfPart %>' lang='<%= provinceLangOfPart %>'><bean:write name="dpd.selected.product" property="companyVO.province" scope="session"/></span>
		 </logic:notEmpty>
		 <logic:empty name="provinceLangOfPart">
		 	<bean:write name="dpd.selected.product" property="companyVO.province" scope="session"/>
		 </logic:empty>
	   </logic:present>
	
	   <logic:present name="dpd.selected.product" property="companyVO.country" scope="session">
	    <br/>
		 <bean:define id="countryLangOfPart" name="dpd.selected.product" property="countryLangOfPart"></bean:define>
	   	 <logic:notEmpty name="countryLangOfPart">
		 	<span xml:lang='<%= countryLangOfPart %>' lang='<%= countryLangOfPart %>'><bean:write name="dpd.selected.product" property="companyVO.country" scope="session"/></span>
		 </logic:notEmpty>
		 <logic:empty name="countryLangOfPart">
		 	<bean:write name="dpd.selected.product" property="companyVO.country" scope="session"/>
		 </logic:empty>
	   </logic:present>
	   
	   <logic:present name="dpd.selected.product" property="companyVO.postalCode" scope="session">
	    &nbsp;<bean:write name="dpd.selected.product" property="companyVO.postalCode" scope="session"/>
	   </logic:present>
	</p>
</div>
<div class="row">
	<p class="col-sm-4">
	<strong><bean:message key="label.product.class" />&nbsp;</strong>
	</p>
	<p class="col-sm-8">
	 <bean:define id="classLangOfPart" name="dpd.selected.product" property="drugClassLangOfPart"></bean:define>
	<logic:notEmpty name="classLangOfPart">
	 	<span xml:lang='<%= classLangOfPart %>' lang='<%= classLangOfPart %>'><bean:write name="dpd.selected.product" property="drugProduct.drugClass" scope="session"/></span>
	</logic:notEmpty>
	<logic:empty name="classLangOfPart">
	 	<bean:write name="dpd.selected.product" property="drugProduct.drugClass" scope="session"/>
	</logic:empty>
	</p>
</div>
<logic:equal name="dpd.selected.product" property="drugProduct.drugClassE" scope="session" value="Veterinary">
	<div class="row">
		<p class="col-sm-4">
	<strong><bean:message key="label.product.veterinarian.species"/></strong>
		</p>
		<p class="col-sm-8">	
	<bean:define id="speciesCount" name="dpd.selected.product" property='<%= "vetSpeciesCount" %>'></bean:define>
	<logic:iterate id="element" name="dpd.selected.product" property="vetSpecies" indexId="index">
			<bean:define id="speciesLangOfPart" name="dpd.selected.product" property='<%= "vetSpeciesLangOfPart[" + index + "]" %>'></bean:define>
			<logic:notEmpty name="speciesLangOfPart">
				<span xml:lang='<%= speciesLangOfPart %>' lang='<%= speciesLangOfPart %>'><bean:write name="element"  property="vetSpecies"/></span><logic:greaterThan name="dpd.selected.product" property="vetSpeciesCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan>
			</logic:notEmpty>
			<logic:empty name="speciesLangOfPart">
				<bean:write name="element"  property="vetSpecies"/><logic:greaterThan name="dpd.selected.product" property="vetSpeciesCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan>
			</logic:empty>
	</logic:iterate>
	</p>
	</div>
</logic:equal> 

<div class="row">
		<p class="col-sm-4">
	<strong><bean:message key="label.product.dosage.form"/></strong>
	</p>
		<p class="col-sm-8">
    <logic:present name="dpd.selected.product" property="formList" scope="session">
   		<logic:iterate id="element" name="dpd.selected.product" property="formList" indexId="index">
			<bean:define id="formLangOfPart" name="dpd.selected.product" property='<%= "formLangOfPart[" + index + "]" %>'></bean:define>
			<logic:notEmpty name="formLangOfPart">
				<span xml:lang='<%= formLangOfPart %>' lang='<%= formLangOfPart %>'><bean:write name="element"  property="pharmaceuticalForm"/></span><logic:greaterThan name="dpd.selected.product" property="pharmaceuticalFormCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan>
			</logic:notEmpty>
			<logic:empty name="formLangOfPart"><bean:write name="element"  property="pharmaceuticalForm"/><logic:greaterThan name="dpd.selected.product" property="pharmaceuticalFormCount" value='<%= new Integer(index + 1).toString() %>'>,&nbsp;</logic:greaterThan></logic:empty>
	</logic:iterate>
   </logic:present>
   <logic:notPresent name="dpd.selected.product" property="formList">
    	<bean:message bundle="messageRes" key="message.not.provided"/>
   </logic:notPresent> 
   <logic:notPresent name="dpd.selected.product" property="formList">
    	<bean:message bundle="messageRes" key="message.not.provided"/>
   </logic:notPresent>
</p>
</div>

<jsp:include page="/jsp/product_info_a.jsp"/>
<jsp:include page="/jsp/product_info_b.jsp"/>

<aside class="wb-fnote wb-fnote-inited" role="note">
	<h3 id="fn"><bean:message key="label.results.page.footnotes"/></h3>
	<dl>
		<dt id="fn1-dt"><bean:message key="label.results.footnote"/> 1</dt>
		<dd aria-labelledby="fn1-dt" tabindex="-1" id="fn1">
			<p><bean:message key="label.product.orig.market.date.help"/></p>
			<p class="fn-rtn"><a href="#fn1-rf"><span class="wb-inv"><bean:message key="label.results.return.to.footnote"/> </span>1<span class="wb-inv"> <bean:message key="label.results.referrer"/></span></a></p>
		</dd>	
		<dt id="fn2-dt"><bean:message key="label.results.footnote"/> 2</dt>
		<dd aria-labelledby="fn2-dt" tabindex="-1" id="fn2">
			<p><bean:message key="label.product.ahfs.help"/></p>
			<p class="fn-rtn"><a href="#fn2-rf"><span class="wb-inv"><bean:message key="label.results.return.to.footnote"/> </span>2<span class="wb-inv"> <bean:message key="label.results.referrer"/></span></a></p>
		</dd>
		<dt id="fn3-dt"><bean:message key="label.results.footnote"/> 3</dt>
		<dd aria-labelledby="fn3-dt" tabindex="-1" id="fn3">
			<p><bean:message key="label.product.atc.help"/></p>
			<p class="fn-rtn"><a href="#fn3-rf"><span class="wb-inv"><bean:message key="label.results.return.to.footnote"/> </span>3<span class="wb-inv"> <bean:message key="label.results.referrer"/></span></a></p>
		</dd>
		<dt id="fn4-dt"><bean:message key="label.results.footnote"/> 4</dt>
		<dd aria-labelledby="fn4-dt" tabindex="-1" id="fn4">
			<p><bean:message key="label.product.aig.no.help0"/>
				<ul>
					<li><bean:message key="label.product.aig.no.help1"/></li>
					<li><bean:message key="label.product.aig.no.help2"/></li>
					<li><bean:message key="label.product.aig.no.help3"/></li>
				</ul>
			<p class="fn-rtn"><a href="#fn4-rf"><span class="wb-inv"><bean:message key="label.results.return.to.footnote"/> </span>4<span class="wb-inv"> <bean:message key="label.results.referrer"/></span></a></p>
		</dd>
		<logic:equal name="showFootnoteFive" value="true" scope="request">
			<dt id="fn5-dt"><bean:message key="label.results.footnote"/> 5</dt>
			<dd aria-labelledby="fn5-dt" tabindex="-1" id="fn5">
				<p><bean:message key="label.product.ahfs.note"/></p>
				<p class="fn-rtn"><a href="#fn5-rf"><span class="wb-inv"><bean:message key="label.results.return.to.footnote"/> </span>5<span class="wb-inv"> <bean:message key="label.results.referrer"/></span></a></p>
			</dd>
		</logic:equal>
		
		
	</dl>
</aside> 