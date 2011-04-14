/*
 * Priki - Prevalent Wiki
 * Copyright (c) 2005 Priki 
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
package org.priki.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Admin implements Serializable {
    
    public static final long serialVersionUID= 1L;
    
    private String siteName = "Priki";
    private String slogan = "The Prevalent Wiki";    
    private String basePath = "http://localhost:8080/priki";
    private String defaultI18n = "en_US";
    
    private boolean lastChangesInItems = true;
    private boolean lastChangesOnlyNewPages = true;
    private int lastChangesCount = 10;
    
    private AccessManager accessManager;
    private MailConfiguration mailConfiguration;
    private List<Plugin> plugins;
    private List<I18N> i18nOverride;
    
    public String getBasePath() { return this.basePath; }
    public void setBasePath(String basePath) {this.basePath = basePath;}

    public String getSlogan() { return this.slogan; }
    public void setSlogan(String slogan) {this.slogan = slogan;}
    
    public int getLastChangesCount() { return this.lastChangesCount; }
    public void setLastChangesCount(int lastChangesCount) { this.lastChangesCount = lastChangesCount; }

    public String getSiteName() { return this.siteName; }
    public void setSiteName(String siteName) { this.siteName = siteName; }

    public List<Plugin> plugins() {
    	if (plugins == null) {
    		plugins = new ArrayList<Plugin>();
    	}
    	return plugins;
    }
    public List<I18N> i18nOverrides() {
    	if (i18nOverride == null) {
    		i18nOverride = new ArrayList<I18N>();
    	}
    	return i18nOverride;
    }
    
    public AccessManager getAccessManager() {
        if (accessManager == null) { accessManager = new AccessManager(); }
        return accessManager;
    }

    public MailConfiguration getMailConfiguration() {
        if (mailConfiguration == null) { mailConfiguration = new MailConfiguration(); }
        return mailConfiguration;
    }
    
	public String getDefaultI18n() { return defaultI18n; }
	public void setDefaultI18n(String defaultI18n) { this.defaultI18n = defaultI18n; }
	
	public boolean isLastChangesInItems() {
		return lastChangesInItems;
	}
	public void setLastChangesInItems(boolean lastChangesInItems) {
		this.lastChangesInItems = lastChangesInItems;
	}    
    
    public void addPlugin(Plugin plugin) {
    	plugins().add(plugin);
    }
    
    public List<Plugin> getPlugins() {
    	return plugins();
    }
    
    public Plugin getPlugin(String name) {
    	for (Plugin p : plugins()) {
    		if (p.getName().equals(name)) {
    			return p;
    		}
    	}
    	return null;
    }    
    
    public void removePlugin(String pluginName) {
    	Plugin plg = getPlugin(pluginName);
    	if (plg == null) return;
    	plugins().remove(plg);
    }
    
    public void addI18NOverride(I18N i18n) {
    	i18nOverrides().add(i18n);
    }
    
    public List<I18N> getI18NOverrides() {
    	return i18nOverrides();
    }
    
    public I18N getI18NOverride(String language, String key) {
    	for (I18N p : i18nOverrides()) {
    		if (p.getLanguage().equals(language) 
    		&&  p.getKey().equals(key)) {
    			return p;
    		}
    	}
    	return null;
    }    
    
    public void removeI18NOverride(String language, String key) {
    	I18N i18n = getI18NOverride(language, key);
    	if (i18n == null) return;
    	i18nOverrides().remove(i18n);
    }
    
    @Deprecated
    private String userAdmin = AccessManager.DEFAULT_ADMIN;
    @Deprecated
    private String passAdmin = AccessManager.DEFAULT_PASSWD;
    
    @Deprecated
    private User findUserAdmin() {
    	return getAccessManager().getUser(AccessManager.DEFAULT_ADMIN);
    }
    
    @Deprecated
    public String getPassAdmin() { return this.passAdmin; }
    @Deprecated
    public void setPassAdmin(String passAdmin) {
    	findUserAdmin().setPassword(passAdmin);
    	this.passAdmin = passAdmin; 
    }

    @Deprecated
    public String getUserAdmin() { return this.userAdmin; }
    @Deprecated
    public void setUserAdmin(String userAdmin) {
    	findUserAdmin().setPassword(passAdmin);
    	this.userAdmin = userAdmin; 
    }
	public boolean isLastChangesOnlyNewPages() {
		return lastChangesOnlyNewPages;
	}
	public void setLastChangesOnlyNewPages(boolean lastChangesOnlyNewPages) {
		this.lastChangesOnlyNewPages = lastChangesOnlyNewPages;
	}
}
