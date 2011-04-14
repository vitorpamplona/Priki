/*
 * Priki - Prevalent Wiki
 * Copyright (c) 2006 - Priki
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 *
 * @author Vitor Fernando Pamplona - vitor@babaxp.org
 *
 */
package org.priki.prevalence;

import org.priki.bo.AccessManager;
import org.priki.bo.Wiki;
import org.priki.bo.Wikiword;

public class AdminTransaction extends PrikiTransaction {

    public static final long serialVersionUID= 1L;
    
    private String siteName;
    private String slogan;    
    private String adminBasePath;
    private String defaultI18n;  
    private Integer lastChangesCount;
    private boolean lastChangesInItems;
    private boolean lastChangesOnlyNewPages;
    private boolean readonly;
    private boolean acceptAnonymousEditor;
    private boolean acceptAnonymousReader;
    private AccessManager.SignUp signup;
    Wikiword.Visibility defaultVisibility;
    boolean defaultCaseSensitive;
    
	private String smtpHost;
	private String smtpUser;
	private String smtpPassword;
	private String smtpPort;
	private String fromMail;
	private String fromName;    
    
    public AdminTransaction(
            String siteName,
            String slogan,            
            String adminBasePath, 
            String defaultI18n,
            Integer lastChangesCount,
            boolean lastChangesInItems,
            boolean lastChangesOnlyNewPages,
            boolean readonly, 
            boolean acceptAnonymousEditor,
            boolean acceptAnonymousReader,
            AccessManager.SignUp signup,
            Wikiword.Visibility defaultVisibility,
            boolean defaultCaseSensitive) {
        super();
        this.adminBasePath = adminBasePath;
        this.lastChangesCount = lastChangesCount;
        this.readonly = readonly;
        this.defaultI18n = defaultI18n;
        this.siteName = siteName;
        this.slogan = slogan;
        this.signup = signup;
        this.lastChangesInItems = lastChangesInItems;
        this.lastChangesOnlyNewPages = lastChangesOnlyNewPages;
        this.acceptAnonymousReader = acceptAnonymousReader;
        this.acceptAnonymousEditor = acceptAnonymousEditor;
        this.defaultVisibility = defaultVisibility;
        this.defaultCaseSensitive = defaultCaseSensitive;
        this.smtpHost = null;
    	this.smtpUser = null;
    	this.smtpPassword = null;
    	this.smtpPort = null;
    	this.fromMail = null;
    	this.fromName = null; 
    }
    
    public AdminTransaction(
            String siteName,
            String slogan,            
            String adminBasePath, 
            String defaultI18n,
            Integer lastChangesCount,
            boolean lastChangesInItems,
            boolean lastChangesOnlyNewPages,
            boolean readonly, 
            boolean acceptAnonymousEditor,
            boolean acceptAnonymousReader,
            AccessManager.SignUp signup,
            Wikiword.Visibility defaultVisibility,
            boolean defaultCaseSensitive, 
        	String smtpHost,
        	String smtpUser,
        	String smtpPassword,
        	String smtpPort,
        	String fromMail,
        	String fromName             
            ) {
        super();
        this.adminBasePath = adminBasePath;
        this.lastChangesCount = lastChangesCount;
        this.readonly = readonly;
        this.defaultI18n = defaultI18n;
        this.siteName = siteName;
        this.slogan = slogan;
        this.signup = signup;
        this.lastChangesInItems = lastChangesInItems;
        this.lastChangesOnlyNewPages = lastChangesOnlyNewPages;
        this.acceptAnonymousReader = acceptAnonymousReader;
        this.acceptAnonymousEditor = acceptAnonymousEditor;
        this.defaultVisibility = defaultVisibility;
        this.defaultCaseSensitive = defaultCaseSensitive;
        this.smtpHost = smtpHost;
    	this.smtpUser = smtpUser;
    	this.smtpPassword = smtpPassword;
    	this.smtpPort = smtpPort;
    	this.fromMail = fromMail;
    	this.fromName = fromName; 
    }    

    public void executeOn(Wiki wiki) {
        wiki.getAdmin().setSiteName(siteName);
        wiki.getAdmin().setSlogan(slogan);
        wiki.getAdmin().setBasePath(adminBasePath);
        wiki.getAdmin().setLastChangesCount(lastChangesCount);
        wiki.getAdmin().setLastChangesInItems(lastChangesInItems);
        wiki.getAdmin().setDefaultI18n(defaultI18n);
        wiki.getAdmin().setLastChangesOnlyNewPages(lastChangesOnlyNewPages);

        wiki.getAdmin().getAccessManager().setDefaultVisibility(defaultVisibility);
        wiki.getAdmin().getAccessManager().setDefaultCaseSensitive(defaultCaseSensitive);
        wiki.getAdmin().getAccessManager().setSignUp(signup);
        wiki.getAdmin().getAccessManager().setReadonly(readonly);
        wiki.getAdmin().getAccessManager().setAcceptAnonymousEditor(acceptAnonymousEditor);
        wiki.getAdmin().getAccessManager().setAcceptAnonymousReader(acceptAnonymousReader);
        
        if (smtpHost != null) {
        	wiki.getAdmin().getMailConfiguration().setSmtpHost(smtpHost);
        	wiki.getAdmin().getMailConfiguration().setSmtpUser(smtpUser);
        	wiki.getAdmin().getMailConfiguration().setSmtpPassword(smtpPassword);
        	wiki.getAdmin().getMailConfiguration().setSmtpPort(smtpPort);
        	wiki.getAdmin().getMailConfiguration().setFromMail(fromMail);
        	wiki.getAdmin().getMailConfiguration().setFromName(fromName);
        }
    }
}
