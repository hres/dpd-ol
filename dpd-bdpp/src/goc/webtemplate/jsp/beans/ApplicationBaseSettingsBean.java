package goc.webtemplate.jsp.beans;

import goc.webtemplate.Breadcrumb;
import goc.webtemplate.Constants;
import goc.webtemplate.SessionTimeout;
import goc.webtemplate.component.jsp.BaseBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.struts.util.MessageResources;

import ca.gc.hc.util.ApplicationGlobals;

public class ApplicationBaseSettingsBean extends BaseBean {
	Locale locale = (Locale) ApplicationGlobals.instance().getUserLocale();
	 
	MessageResources resource = MessageResources.getMessageResources("resources.ApplicationResources");
	MessageResources resourceClf = MessageResources.getMessageResources("resources.CommonLookFeelResources");
	@Override
	public void setHeaderTitle() {
		
	}

	@Override
	public void setShowSearch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLanguageLinkUrl() {
		String strLinkUrl = this.getRequest().getContextPath() + "/switchlocale.do?lang=";
		this.langLinkUrl = strLinkUrl;  
	}

	@Override
	public void setShowPreContent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBreadcrumbsList() {
		locale = (Locale) ApplicationGlobals.instance().getUserLocale();
		 
		String strLink1 = resource.getMessage(locale, "href.home"); 
		String strLink2 = resource.getMessage(locale, "href.drugHealthProducts");
		String strLink3 = resource.getMessage(locale, "href.drugProducts");
		String strLink4 = resource.getMessage(locale, "href.drugProductDatabase");
		String strText1 = resource.getMessage(locale, "breadCrumbs.home");
		String strText2 = resource.getMessage(locale, "breadCrumbs.drugHealthProducts");
		String strText3 = resource.getMessage(locale, "breadCrumbs.drugProducts");
		String strText4 = resource.getMessage(locale, "breadCrumbs.drugProductDatabase");
		String strText5 = resource.getMessage(locale, "breadCrumbs.application");
		this.breadCrumbsList.clear();
		this.breadCrumbsList.add(new Breadcrumb(strLink1, strText1, ""));
		this.breadCrumbsList.add(new Breadcrumb(strLink2, strText2, ""));
		this.breadCrumbsList.add(new Breadcrumb(strLink3, strText3, ""));
		this.breadCrumbsList.add(new Breadcrumb(strLink4, strText4, ""));
		this.breadCrumbsList.add(new Breadcrumb("", strText5, ""));

	}

	@Override
	public void setLeavingSecureSiteWarningEnabled() {
		this.leavingSecureSiteWarningEnabled = false;
	}

	@Override
	public void setLeavingSecureSiteWarningMessage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLeavingSecureSiteRedirectUrl() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLeavingSecureSiteExcludedDomains() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDateModified() {

		String strDate = resourceClf.getMessage(locale, "dcterms_modified");
		Date dtDate = null;
		try {
			dtDate = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		this.dateModified = dtDate;
	}

	@Override
	public void setVersionIdentifier() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShowPostContent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setShowFeedbackLink() {
		this.showFeedbackLink = true;
	}

	@Override
	public void setShowSharePageLink() {
		this.showSharePageLink = true; 
	}

	@Override
	public void setSharePageMediaSites() {
		this.sharePageMediaSites.clear();
		this.sharePageMediaSites.add(Constants.SocialMediaSites.bitly);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.linkedin);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.blogger);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.myspace);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.delicious);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.pinterest);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.digg);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.reddit);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.diigo);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.stumbleupon);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.email);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.tumblr);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.facebook);
	    	this.sharePageMediaSites.add(Constants.SocialMediaSites.twitter);
		this.sharePageMediaSites.add(Constants.SocialMediaSites.gmail);
	    	this.sharePageMediaSites.add(Constants.SocialMediaSites.yahoomail);
	    	this.sharePageMediaSites.add(Constants.SocialMediaSites.googleplus);
	}

	@Override
	public void setShowFeature() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContactLinks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNewsLinks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAboutLinks() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHtmlHeaderElements() {
		this.htmlHeaderElements.clear();
		locale = (Locale) ApplicationGlobals.instance().getUserLocale();
		 
		String strContent1 = resourceClf.getMessage(locale, "meta_title");
		String strContent2 = resourceClf.getMessage(locale, "meta_description");
		String strContent3 = resourceClf.getMessage(locale, "meta_creator");
		String strContent4 = resourceClf.getMessage(locale, "meta_language");
		String strContent5 = resourceClf.getMessage(locale, "meta_subject");
		String strContent6 = resourceClf.getMessage(locale, "dcterms_issued");
		String strContent7 = resourceClf.getMessage(locale, "dcterms_modified");
		String strContent8 = resourceClf.getMessage(locale, "meta_institution");
		
		String strName1 = "dc.title";
		String strName2 = "gc.description.long";
		String strName3 = "dc.creator";
		String strName4 = "dcterms.language";
		String strName5 = "dc.subject";
		String strName6 = "dc.date.issued";
		String strName7 = "dc.date.modified";
	
		this.htmlHeaderElements.add("<meta name=\"" + strName1 + "\" content=\"" + strContent1 + "\">");
		this.htmlHeaderElements.add("<meta name=\"" + strName2 + "\" content=\"" + strContent2 + "\">");
		this.htmlHeaderElements.add("<meta name=\"" + strName3 + "\" content=\"" + strContent3 + "\">");
		this.htmlHeaderElements.add("<meta name=\"" + strName4 + "\" title=\"ISO639-2\" content=\"" + strContent4 + "\">");
		this.htmlHeaderElements.add("<meta name=\"dc.institution\" content=\"" + strContent8 + "\">");
		this.htmlHeaderElements.add("<meta name=\"" + strName5 + "\" content=\"" + strContent5 + "\">");
		this.htmlHeaderElements.add("<meta name=\"dc.audience\" content=\"public\">");
		this.htmlHeaderElements.add("<meta name=\"" + strName6 + "\" content=\"" + strContent6 + "\">");
		this.htmlHeaderElements.add("<meta name=\"" + strName7 + "\" content=\"" + strContent7 + "\">");
	}

	@Override
	public void setHtmlBodyElements() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStaticFallbackFilePath() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSessionTimeoutConfigurations() {
		SessionTimeout configs = new SessionTimeout();
		
		configs.setInActivity(1800000);
		configs.setReactionTime(300000);
		configs.setSessionAlive(1800000);
		configs.setLogoutUrl("timeout.do");
	
		this.sessionTimeoutConfigurations = configs;
	}
	
	@Override
	public void setSessionTimeoutEnabled() {
		this.sessionTimeoutEnabled = true;
		
	}

	@Override
	public void setLeftMenuSections() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPrivacyLinkUrl() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTermsConditionsLinkUrl() {
		// TODO Auto-generated method stub
		
	}

}
