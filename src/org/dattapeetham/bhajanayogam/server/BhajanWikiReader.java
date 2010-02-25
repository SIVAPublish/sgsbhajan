package org.dattapeetham.bhajanayogam.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class BhajanWikiReader {
	static int bufferLength = 4000;

	public static String retrieveBhajanText(String urlString) throws Exception {
		boolean isCat = false;
		if (urlString.contains("Category"))
			isCat = true;
		URL pageRef = new URL(urlString);
		URLConnection yc = pageRef.openConnection();
		StringBuffer buffer = new StringBuffer(bufferLength);
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream(), "UTF8"));
		String inputLine;
		boolean contentStarted = false;
		// extract lines from the line containing the words "start content" to
		// "printfooter".
		// wiki inserts these comments when your actual content starts on a
		// page.
		while ((inputLine = in.readLine()) != null) {
			if (inputLine.contains("start content")) {
				contentStarted = true;
				continue;
			} else if (inputLine.contains("printfooter")) {
				break;
			} else if (contentStarted) {
				if (!isCat) {
					buffer.append(inputLine);
				} else if (isCat && inputLine.contains("category")) { 
					continue;
				} else {
					buffer.append(inputLine);
				}

			}
		}

			in.close();
			String string = buffer.toString();
			return string;

		
	}
}

