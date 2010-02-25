package org.dattapeetham.bhajanayogam.server;

import java.io.IOException;
import java.io.StringReader;

import org.dattapeetham.transliteration.ICUHelper;

public class HTMLTransliterator {
	
	
	
	private String html;
	private StringReader reader;

	public HTMLTransliterator(String html) {
		// TODO Auto-generated constructor stub
		this.html = html;
	}

	public String transliterateHTML(String language) {
		if(language.equals("Telugu"))
			return html;
		reader = new StringReader(html);
		boolean intag = false;
		boolean inamp = false;
		StringBuffer textBuffer = new StringBuffer(100);
		StringBuffer outBuffer = new StringBuffer(html.length());
		
		try {
			for (char c = ' ';c!=65535;c=(char) reader.read()) {
                
				if(c=='>') { //HTML tag ends
					intag = false;
					outBuffer.append(c);
				} else if(c==';') { //&nbsp; etc., end
					if(inamp) {
						inamp = false;
					}
					outBuffer.append(c);
				} else if(c=='<') { //HTML tag starts
					intag = true;
					outBuffer.append(transliterate(textBuffer.toString(),language)); //transliterate accumulated text 
					outBuffer.append(c);
					textBuffer = new StringBuffer(100);
				} else	if(c=='&') { //& notation character starts
						inamp=true;
						outBuffer.append(transliterate(textBuffer.toString(),language));
						outBuffer.append(c);
						textBuffer = new StringBuffer(100);					
				} else 
					if(intag || inamp) {
						outBuffer.append(c);
					}
				else textBuffer.append(c);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outBuffer.toString();
	}


	public String transliterateHTML1(String language) {
		if(language.equals("Telugu"))
			return html;
		reader = new StringReader(html);
		boolean intag = false;
		StringBuffer textBuffer = new StringBuffer(100);
		StringBuffer outBuffer = new StringBuffer(html.length());
		
		try {
			for (char c = ' ';c!=65535;c=(char) reader.read()) {
                
				if(c=='>' || c==';') {
					intag = false;
					outBuffer.append(c);
				} else if(c=='<' || c=='&') {
					intag = true;
					outBuffer.append(transliterate(textBuffer.toString(),language));
					outBuffer.append(c);
					textBuffer = new StringBuffer(100);
				} else 
					if(intag) {
						outBuffer.append(c);
					}
				else textBuffer.append(c);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return outBuffer.toString();
	}

	public String transliterate(String input, String language) {
		if (language.equalsIgnoreCase("ENGLISH"))
			language = "LATIN"; //ICU4j understands only LATIN, not English as the destination
		else if (language.equalsIgnoreCase("HINDI"))
			language = ICUHelper.DEVANAGARI; //ICU4j needs Devanagari to be specified for hindi/marathi etc.,
		try {
			return ICUHelper.transliterate(input, language); 
		} catch (Exception e) {
			e.printStackTrace();
			return input;
		}

	}
	
	public static void main(String[] args) {
		HTMLTransliterator trans = new HTMLTransliterator("<H1>datta</H1>&nbsp;");
		trans.transliterateHTML("HINDI");
	}

}
