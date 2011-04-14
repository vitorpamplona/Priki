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
package org.priki.format;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.priki.bo.AnonymousUser;
import org.priki.bo.Text;
import org.priki.bo.Wiki;
import org.priki.bo.Wikiword;
import org.priki.utils.FileUtils;

public class StressTest extends TestCase {
    private Text text;
    private HtmlParser parser;
    private Wiki wiki;
    
    private FileUtils file;
    
    private Format format = new SimpleDateFormat("ss:SSS");
    
    public void setUp() {
        wiki = new Wiki();
        file = new FileUtils();
        text = null;
        parser = new HtmlParser(wiki);
    }
   
    
    
    void printMemory(String txt) {
        System.out.println(format.format(new Date()) + ": " + txt + ". " + 
                (wiki.getElementCount() + wiki.getWikiwordCount()) 
                + " elements with " + Runtime.getRuntime().totalMemory() / (1024f * 1024) + " MB");
    }
    
    Text parseText(String text) {
    	return parser.parseText(text, new AnonymousUser("Vitor"));
    }    
    
    public void testParse() throws IOException, InterruptedException {
        printMemory("Starting");
            
        String xp = file.readFile("../format/stressFile.XP");
        String rup = file.readFile("../format/stressFile.RUP");
        String webServices = file.readFile("../format/stressFile.WebServices");
        String power = file.readFile("../format/stressFile.Power");
        
        printMemory("Strings OK");
               
        text = parseText(xp);
        text = parseText(rup);
        text = parseText(webServices);
        
        printMemory("Parsing XP, RUP and WebServices");
        
        text = parseText(power);
        text = parseText(rup);
        text = parseText(webServices);
        
        printMemory("Parsing power, rup and WebServices");
        
        text = parseText(power);
        text = parseText(rup);
        text = parseText(webServices);

        long mem = Runtime.getRuntime().totalMemory();
        
        printMemory("Parsed power, rup and WebServices");
        
        text = parseText(power);
        text = parseText(rup);
        text = parseText(webServices);
        
        printMemory("Parsed power, rup and WebServices");
        
        assertEquals(mem, Runtime.getRuntime().totalMemory());
        
        printMemory("Parsing power");
        
        text = parseText(power);

        assertEquals(mem, Runtime.getRuntime().totalMemory());
        
        printMemory("Defining a text to a Wikiword");        
        
        Wikiword word = new Wikiword("Power");
        word.setDefinition(text);
        
        printMemory("Checking search speed");        
        
        wiki.getWikiword("Vitor");

        printMemory("Checking startWith speed");        
        
        wiki.wordsStartingWith("Vitor");
        
        printMemory("Final"); 
    }
}
