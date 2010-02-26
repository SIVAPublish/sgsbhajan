package org.dattapeetham.bhajanayogam.server;

import org.datanucleus.sco.backed.Collection;
import org.dattapeetham.bhajanayogam.client.TransliterationService;
import org.dattapeetham.transliteration.ICUHelper;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.shared.GwtLocale;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		TransliterationService {

	public String transliterate(String input, String language) {
		if (language.equalsIgnoreCase("ENGLISH"))
			language = "LATIN"; //ICU4j understands only LATIN, not English as the destination
		else if (language.equalsIgnoreCase("HINDI"))
			language = ICUHelper.DEVANAGARI; //ICU4j needs Devanagari to be specified for hindi/marathi etc.,
		try {
			String s= ICUHelper.transliterate(input, language); //$NON-NLS-1$
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
		// TODO Auto-generated method stub
		try {
			String urlContent = BhajanWikiReader.retrieveBhajanText(url);
		
			HTMLTransliterator trans =  new HTMLTransliterator(urlContent);
			String s= trans.transliterateHTML(language);
			

			return s;
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
	
}
