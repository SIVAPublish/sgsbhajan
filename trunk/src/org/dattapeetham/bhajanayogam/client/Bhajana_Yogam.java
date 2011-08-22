package org.dattapeetham.bhajanayogam.client;

import org.dattapeetham.bhajanayogam.WikiCategories;
import org.dattapeetham.bhajanayogam.client.content.i18n.CategoryConstants;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Bhajana_Yogam implements EntryPoint, ValueChangeHandler<String>, Event.NativePreviewHandler, WikiCategories  {

	private static final String COLON = ":";
	private static final String WIKI_BASE_URL = "http://data.sgsdatta.org";
	private static final String BHAJAN_BASE_URL = WIKI_BASE_URL + "/bhajan/sahityam/wiki/index.php?title=";
//TODO Remove the tab references..they already exist in the view.
	private static final int INDEX_TAB = 0;
	private static final int BHAJAN_TAB = 1;


	/**
	 * 
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final TransliterationServiceAsync greetingService = GWT.create(TransliterationService.class);

	/**
	 * This is the entry point method.
	 */
	@SuppressWarnings("unchecked")
	public void onModuleLoad() {
		Event.addNativePreviewHandler(this);

		if(getUserAgent().contains("firefox"))
		{
			
		}

//		handleLocale();
	
	

	    // Add history listener
	    // TODO Fix the history handling
	    History.addValueChangeHandler(this);
	    // Now that we've setup our listener, fire the initial history state.
	    History.fireCurrentHistoryState();

	}


	private String getLocale() {
		return LocaleInfo.getCurrentLocale().getLocaleName();
//	     GWT.log(GwtLocale.DEFAULT_LOCALE.toString());
	}





	private static final String AMPERSAND = "&";
	
	private String curCategory = "";
	private String curBhajan = null;
	protected Bhajana_YogamView data = new Bhajana_YogamView(new MenuBar(true), new Label(), new Label(),
			new HTML(), new HTML(), new TabPanel(), new DialogBox(), new Button("Close"), new SubmitHandler());

	private Command getNewCommand(final String string) {
		return new Command() {

			public void execute() {
				if(curCategory.equals(string)){
					data.tabPanel.selectTab(Bhajana_YogamView.INDEX_TAB);
				} else {
					gotoCategory(string);
				}
			}

	

		};
	}
	
private void gotoCategory(final String category) {
		
//		urlField.setText(baseURL + category);
		
	greetingService.getItemList( category, new AsyncCallback<String[]>() {
		public void onFailure(Throwable caught) {
			// Show the RPC error message to the user
			data.dialogBox.setText("Remote Procedure Call - Failure");
			data.bhajanTextLabel.addStyleName("serverResponseLabelError");
			data.bhajanTextLabel.setHTML(SERVER_ERROR);
			data.dialogBox.center();
			data.closeButton.setFocus(true);
		}

		public void onSuccess(String result) {
//			targetLabel.setVisible(true);
			targetLabel.removeStyleName("serverResponseLabelError");
			targetLabel.removeStyleName("retrievingMessage");
			targetLabel.setHTML(result);
			createNewHistoryEntry();
		  
//			sendButton.setEnabled(true);

		}

		public void onSuccess(String[] result) {
//			targetLabel.setVisible(true);
			data.buildItemView(result);
		
			createNewHistoryEntry();
		  			
		}
	});
		data.tabPanel.selectTab(Bhajana_YogamView.INDEX_TAB);
		data.handler.sendt13nRequestToServer(data.catLabel);
		curCategory = category;
		
			
	}
	private void gotoCategory2(final String category) {
		
		data.urlField.setText(baseURL + category);
		data.tabPanel.selectTab(Bhajana_YogamView.INDEX_TAB);
		data.handler.sendt13nRequestToServer(data.catLabel);
		curCategory = category;
		
			
	}

	// Create a handler for the sendButton and nameField
	class SubmitHandler implements ClickHandler, KeyUpHandler {
	
		protected static final String SELECT_CATEGORY_ERROR = "Please select a deity to view bhajans";

		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event) {
			sendt13nRequestToServer(data.catLabel);
		}

		/**
		 * Fired when the user types in the nameField.
		 */
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				sendt13nRequestToServer(data.catLabel);
			}
		}

		/**
		 * Send the name from the nameField to the server and wait for a
		 * response.
		 */
		private void sendt13nRequestToServer(final HTML targetLabel) {
			String textToServer = data.urlField.getText();
//			String language = lb.getItemText(lb.getSelectedIndex());
			String language = getLocale();
			data.textToServerLabel.setText(textToServer);
			targetLabel.setHTML("<div class=retrievingMessage>"+categoryConstants.retrieving()+"</div>");
			
			greetingService.transliterateURL(textToServer, language, new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					data.dialogBox.setText("Remote Procedure Call - Failure");
					data.bhajanTextLabel.addStyleName("serverResponseLabelError");
					data.bhajanTextLabel.setHTML(SERVER_ERROR);
					data.dialogBox.center();
					data.closeButton.setFocus(true);
				}

				public void onSuccess(String result) {
//					targetLabel.setVisible(true);
					targetLabel.removeStyleName("serverResponseLabelError");
					targetLabel.removeStyleName("retrievingMessage");
					targetLabel.setHTML(result);
					createNewHistoryEntry();
				  
//					sendButton.setEnabled(true);

				}
			});
		}

		
	
	}

	private void createNewHistoryEntry() {
		  History.newItem(HistToken.CATEGORY_TOKEN+COLON+curCategory+AMPERSAND+HistToken.BHAJAN_TOKEN+COLON+curBhajan);
	}

	/**
	 * @Override This method tracks the URL clicks from the generated page and
	 *           resubmits after changing them
	 * */
	public void onPreviewNativeEvent(NativePreviewEvent pevent) {
		NativeEvent event = pevent.getNativeEvent();
//		GWT.log(event.toDebugString()+"," + event.getSource()+"," + event.getTypeInt()+"," + event.getAssociatedType()+","+event.getType());
		if (Event.getTypeInt(event.getType()) == Event.ONCLICK) {
			Element target = Element.as(event.getEventTarget());
			if (AnchorElement.is(target)) {
				   AnchorElement targetElement = target.cast();
				   String href = targetElement.getHref(); 
			
			
//			System.out.println(target.getClass().getName());
//			if ("a".equalsIgnoreCase(getTagName(target))) {
//				String href = DOM.getElementAttribute((com.google.gwt.user.client.Element) target, "href");
				// now test if href is: // - #anchor link // - "doThat.html" -
				// relative in-page link // - external link (path is different
				// the e.g. // GWT.getModuleBaseURL()
				if (href.startsWith("/bhajan")) {
					event.preventDefault();
					curBhajan = href;
					gotoBhajan(href);
					pevent.cancel();
				}
			}
		}
		 pevent.consume();
		
	}

	

	private void gotoBhajan(String href) {
		data.urlField.setText(WIKI_BASE_URL + href);
		data.tabPanel.selectTab(BHAJAN_TAB);
		data.handler.sendt13nRequestToServer(data.bhajanTextLabel);
	}
	
	private void gotoNamedBhajan(String teluguBhajanName) {		
		data.urlField.setText(BHAJAN_BASE_URL + teluguBhajanName);
		data.tabPanel.selectTab(BHAJAN_TAB);
		data.handler.sendt13nRequestToServer(data.bhajanTextLabel);
	}
	
	
	native String getTagName(Element element)
	/*-{
		return element.tagName;
	}-*/;


	public static native String getUserAgent() /*-{
	return navigator.userAgent.toLowerCase();
	}-*/;



	public void onValueChange(ValueChangeEvent<String> event) {
//		History.getToken();
		if(data.valChangeHandled) { 
			data.valChangeHandled=false;
		    return;
		}
		String histVal = (String)event.getValue();
		System.out.println("The current history token is: " + histVal);
		HistToken token = parseParamString(histVal);
		if(!token.category.equals("null")) {
//			gotoCategory(token.category);
		} else if (token.bhajan != null && !token.bhajan.equals("null")) {
			this.gotoBhajan(token.bhajan);
		}

	}
	

	public  HistToken parseParamString(String string) {

		  String[] ray = string.split(AMPERSAND);

		 
		  HistToken token = new HistToken();	
		  for (int i = 0; i < ray.length; i++) {

		    String[] substrRay = ray[i].split(COLON);
		    
		    if(substrRay[0].equals(HistToken.CATEGORY_TOKEN)) {
		    	token.category=substrRay[1];
		    } else if (substrRay[0].equals(HistToken.BHAJAN_TOKEN)) {
		    	token.bhajan= substrRay[1];
		  }
		  }

		  return token;

		}

}
