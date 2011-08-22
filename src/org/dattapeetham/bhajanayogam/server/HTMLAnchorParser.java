package org.dattapeetham.bhajanayogam.server;

import net.htmlparser.jericho.*;
import java.util.*;
import java.net.*;

//adapted from http://jericho.htmlparser.net/samples/console/src/FindSpecificTags.java 

public class HTMLAnchorParser {
	
	
	public static HashMap<String, String> getItems(String sourceUrlString) throws Exception {
		
		Source source=new Source(new URL(sourceUrlString));
		source=new Source(source); // have to create a new Source object after changing tag type registrations otherwise cache might contain tags found with previous configuration.

		System.out.println("A Elements:");
		return displaySegments(source.getAllElements(HTMLElementName.A));

  }

	private static  HashMap<String, String> displaySegments(List<? extends Segment> segments) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		for (Segment segment : segments) {
			String link = null, title = null;
			for (Attribute element: segment.getFirstStartTag().getAttributes()) {
				String elementName = element.getName().toLowerCase();
				String elementValue = element.getValue().toLowerCase();
				if(elementName.equals("href")  ){
					link=elementValue;
				}else if (elementName.equals("title")  ){
					title=elementValue;
				}
			}
			if (link!= null && title != null) {
				try {
					String wikiTitle= link.substring(link.indexOf("title=")+6);
					hashMap.put(title, wikiTitle);
					System.out.println("Reference: "+ wikiTitle + " Title: "+ title);
				}catch(Exception e) {}
				
			}
					
		}
		return hashMap;
	}

	
	public static void main(String[] args) throws Exception {
		
		
		getItems("http://data.sgsdatta.org/bhajan/sahityam/wiki/index.php?title=Category:%E0%B0%97%E0%B0%A3%E0%B0%AA%E0%B0%A4%E0%B0%BF_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81");
		
	}
	
}