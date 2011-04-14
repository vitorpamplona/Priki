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
package org.priki.prevalence;

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.priki.bo.Wiki;

/** The prevalence layer */
public class Prevalence {
    private static Prevalence singleton;
    
    private Prevayler layer;
    private Wiki wiki;
    
    public static synchronized Prevalence getInstance() {
        if (singleton == null) {
            singleton = new Prevalence();
        }
        return singleton;
    }
    
    public static synchronized Prevalence getNewInstance() {
    	singleton = new Prevalence();
        return singleton;
    }    
    
    public static Wiki wiki() {
        return getInstance().getWiki();
    }
    
    private Prevalence() {
        try {
        	PrevaylerFactory factory = new PrevaylerFactory();
        	factory.configurePrevalenceDirectory("BASE");
        	factory.configurePrevalentSystem(new Wiki());
        	layer = factory.create();
            wiki = (Wiki)layer.prevalentSystem();
        } catch (IOException e) {
            new RuntimeException("Prevayler load database IO error. ", e);
        } catch (ClassNotFoundException e) {
       		new RuntimeException("Prevayler load database error: ClassNotFoundException. ", e);
		}
    }
    
    public Prevalence(String base) {
        try {
        	PrevaylerFactory factory = new PrevaylerFactory();
        	factory.configurePrevalenceDirectory(base);
        	factory.configurePrevalentSystem(new Wiki());
        	
        	layer = factory.create();
            wiki = (Wiki)layer.prevalentSystem();
        } catch (IOException e) {
        	e.printStackTrace();
            new RuntimeException("Prevayler load database IO error. ", e);
        } catch (ClassNotFoundException e) {
        	e.printStackTrace();
        	new RuntimeException("Prevayler load database error: ClassNotFoundException. ", e);
		}
    }
    
    public Wiki getWiki() {
        return wiki;
    }
    
    public void close() throws IOException {
    	layer.close();
    }
    
    public void takeSnapshot() throws IOException {
    	layer.takeSnapshot();
    }

    public void execute(PrikiTransaction trans) {
        layer.execute(trans);
    }

}
