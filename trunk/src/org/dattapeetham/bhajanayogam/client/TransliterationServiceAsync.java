package org.dattapeetham.bhajanayogam.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TransliterationServiceAsync {

	void transliterateURL(String url, String language,
			AsyncCallback<String> callback);


}
 