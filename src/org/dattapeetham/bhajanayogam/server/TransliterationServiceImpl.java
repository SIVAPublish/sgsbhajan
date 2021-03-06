package org.dattapeetham.bhajanayogam.server;

import java.util.HashMap;

import org.datanucleus.sco.backed.Collection;
import org.dattapeetham.bhajanayogam.WikiCategories;
import org.dattapeetham.bhajanayogam.client.TransliterationService;
import org.dattapeetham.transliteration.ICUHelper;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TransliterationServiceImpl extends RemoteServiceServlet implements
		TransliterationService, WikiCategories {
	
	static HashMap<String,  HashMap<String,String>> categoryMap = new HashMap<String, HashMap<String,String>>();


	public String transliterate(String input, String language) {
		if (language.equalsIgnoreCase("ENGLISH"))
			language = "LATIN"; //ICU4j understands only LATIN, not English as the destination
		else if (language.equalsIgnoreCase("HINDI"))
			language = ICUHelper.DEVANAGARI; //ICU4j needs Devanagari to be specified for hindi/marathi etc.,
		try {
			String s= ICUHelper.transliterateFromTelugu(input, language); //$NON-NLS-1$
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return input;
		}

	}

	/**
	 * Retrieves content from specified URL and transliterates content it to specified language.
	 */
	public String transliterateURL(String url, String language) {
	
		try {
			String urlContent = BhajanWikiReader.retrieveBhajanText(url);
			if(language.equals("te"))
				return urlContent;
			return transliterate(urlContent, language);
			
		} catch (Exception e) {
			e.printStackTrace();

			return Messages.getString("URL_READ_EXCEPTION_MSG");
		}
		
	
		
	}

	public Collection getCategories() {
		Collection c = new Collection(null, null);

		for (String s : Messages.getBhajanCategories()){
			c.add(s);
		}
		return c;
	}

	public String[] getItemList(String category) {
		// TODO Auto-generated method stub
		HashMap<String,String> catMap = categoryMap.get(category);
		if(catMap.get(category)== null) {
			try {
				catMap=HTMLAnchorParser.getItems(baseURL+category);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			categoryMap.put(category, catMap);
		}
	
//		String categoryContent = transliterateURL(baseURL + category, LocaleInfo.getCurrentLocale().getLocaleName());
		
		return (String[])catMap.keySet().toArray();
	}
	
}
