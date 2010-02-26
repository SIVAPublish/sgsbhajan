package org.dattapeetham.bhajanayogam.client;

import org.dattapeetham.bhajanayogam.client.content.i18n.CategoryConstants;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.shared.GwtLocale;
//import com.google.gwt.i18n.rebind.LocaleUtils;
//import com.google.gwt.i18n.server.GwtLocaleFactoryImpl;
//import com.google.gwt.i18n.shared.GwtLocale;
//import com.google.gwt.i18n.shared.GwtLocaleFactory;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Bhajana_Yogam implements EntryPoint, ValueChangeHandler,EventPreview  {

	// Category menu
	MenuBar categoryMenu = new MenuBar(true);
	CategoryConstants categoryConstants = GWT.create(CategoryConstants.class);

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

	private TextBox nameField;

	final Button sendButton = new Button("Send");

	final Label textToServerLabel = new Label();
	final Label titleLabel = new Label();

	final HTML bhajanTextLabel = new HTML();
//	final ListBox lb = new ListBox();
	// Create the popup dialog box

	final DialogBox dialogBox = new DialogBox();
	final Button closeButton = new Button("Close");
	// Add a handler to transliterate new URL
	SubmitHandler handler = new SubmitHandler();

	/**
	 * This is the entry point method.
	 */
	@SuppressWarnings("unchecked")
	public void onModuleLoad() {
		DOM.addEventPreview(this);
//		handleLocale();
		nameField = new TextBox();
		nameField.setText("Wiki URL");
		titleLabel.setText(categoryConstants.bhajanayogam());
		RootPanel.get("titleContainer").add(titleLabel);

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
//		RootPanel.get("nameFieldContainer").add(nameField);
		nameField.setEnabled(false);
//		RootPanel.get("sendButtonContainer").add(sendButton);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		dialogBox.setText("Bhajan");
		dialogBox.setAnimationEnabled(true);

		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");

		HorizontalPanel dialogVPanel = new HorizontalPanel();
		dialogVPanel.addStyleName("dialogVPanel");

		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		

		// dialogVPanel.add(serverResponseLabel);
		bhajanTextLabel.setStylePrimaryName("bigger");
		RootPanel.get("bhajanTextContainer").add(bhajanTextLabel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Add a handler to send the name to the server
		SubmitHandler handler = new SubmitHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);

		createMenu();
		// Add it to the root panel.
		RootPanel.get("menuContainer").add(categoryMenu);

	    String initToken = History.getToken();
	    if (initToken.length() == 0) {
	      History.newItem("Datta");
	    }

	    // Add history listener
	    // TODO Fix the history handling
	    History.addValueChangeHandler(this);
	    // Now that we've setup our listener, fire the initial history state.
//	    History.fireCurrentHistoryState();

	}


	private String getLocale() {
		return LocaleInfo.getCurrentLocale().getLocaleName();
//	     GWT.log(GwtLocale.DEFAULT_LOCALE.toString());
	}


	private void createMenu() {
		addmenuItem(categoryConstants.ganapati(), Ganesha);
		addmenuItem(categoryConstants.datta(), Datta);
		addmenuItem(categoryConstants.guru(),Guru);
		addmenuItem(categoryConstants.siva(),Shiva);
		addmenuItem(categoryConstants.vishnu(),Vishnu);
		addmenuItem(categoryConstants.devi(),Devi);
		addmenuItem(categoryConstants.hanuman(),Hanuman);
		addmenuItem(categoryConstants.skanda(),Skanda);
		addmenuItem(categoryConstants.other(),Other);
		addmenuItem(categoryConstants.tatvam(),Tatvam);
		addmenuItem(categoryConstants.ugadi(),Ugadi);
		addmenuItem(categoryConstants.mangalam(), Mangalam);
		
	}


	private void addmenuItem(String string, String cmd) {
		categoryMenu.addItem("<font size=3><b>"+string+"</b></font>",true,getNewCommand(cmd));
	}

	private final static String Ganesha = "%E0%B0%97%E0%B0%A3%E0%B0%AA%E0%B0%A4%E0%B0%BF_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Datta = "%E0%B0%A6%E0%B0%A4%E0%B1%8D%E0%B0%A4_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Devi = "%E0%B0%A6%E0%B1%87%E0%B0%B5%E0%B1%80_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Guru = "%E0%B0%B8%E0%B0%A6%E0%B1%8D%E0%B0%97%E0%B1%81%E0%B0%B0%E0%B1%81_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Vishnu = "%E0%B0%B5%E0%B0%BF%E0%B0%B7%E0%B1%8D%E0%B0%A3%E0%B1%81_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Hanuman = "%E0%B0%B9%E0%B0%A8%E0%B1%81%E0%B0%AE%E0%B0%BE%E0%B0%A8%E0%B1%8D_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Shiva = "%E0%B0%B6%E0%B0%BF%E0%B0%B5_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Skanda = "%E0%B0%B8%E0%B1%8D%E0%B0%95%E0%B0%82%E0%B0%A6_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Other = "%E0%B0%B5%E0%B0%BF%E0%B0%B5%E0%B0%BF%E0%B0%A7_%E0%B0%A6%E0%B1%87%E0%B0%B5%E0%B0%A4%E0%B0%BE_%E0%B0%AD%E0%B0%9C%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Mangalam = "%E0%B0%AE%E0%B0%82%E0%B0%97%E0%B0%B3%E0%B0%82";
	private final static String Tatvam = "%E0%B0%A4%E0%B0%A4%E0%B1%8D%E0%B0%A4%E0%B1%8D%E0%B0%B5_%E0%B0%95%E0%B1%80%E0%B0%B0%E0%B1%8D%E0%B0%A4%E0%B0%A8%E0%B0%B2%E0%B1%81";
	private final static String Ugadi = "%E0%B0%AF%E0%B1%81%E0%B0%97%E0%B0%BE%E0%B0%A6%E0%B0%BF_%E0%B0%AA%E0%B0%BE%E0%B0%9F%E0%B0%B2%E0%B1%81";

	private final static String baseURL = "http://data.sgsdatta.org/bhajan/sahityam/wiki/index.php?title=Category:";
	
	private String curCategory = null;
	private String curBhajan = null;
	private int  curLanguage = 0;
	private boolean valChangeHandled;
	
	private Command getNewCommand(final String string) {
		return new Command() {

			public void execute() {
				gotoCategory(string);
			}

	

		};
	}
	
	private void gotoCategory(final String category) {
		
		nameField.setText(baseURL + category);
		handler.sendNameToServer();
		curCategory = category;
		
			
	}

	// Create a handler for the sendButton and nameField
	class SubmitHandler implements ClickHandler, KeyUpHandler, ChangeHandler {
	
		/**
		 * Fired when the user clicks on the sendButton.
		 */
		public void onClick(ClickEvent event) {
			sendNameToServer();
		}

		/**
		 * Fired when the user types in the nameField.
		 */
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				sendNameToServer();
			}
		}

		/**
		 * Send the name from the nameField to the server and wait for a
		 * response.
		 */
		private void sendNameToServer() {
			sendButton.setEnabled(false);
			String textToServer = nameField.getText();
//			String language = lb.getItemText(lb.getSelectedIndex());
			String language = getLocale();
			textToServerLabel.setText(textToServer);
			bhajanTextLabel.setHTML("<div style=float:bottom><h3>Retrieving page...</h3></div>");
			
			greetingService.transliterateURL(textToServer, language, new AsyncCallback<String>() {
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					dialogBox.setText("Remote Procedure Call - Failure");
					bhajanTextLabel.addStyleName("serverResponseLabelError");
					bhajanTextLabel.setHTML(SERVER_ERROR);
					dialogBox.center();
					closeButton.setFocus(true);
				}

				public void onSuccess(String result) {

					bhajanTextLabel.removeStyleName("serverResponseLabelError");
				
					bhajanTextLabel.setHTML(result);
					createNewHistoryEntry();
				  
//					sendButton.setEnabled(true);

				}
			});
		}

		public void onChange(ChangeEvent event) {
			  curLanguage = ((ListBox)event.getSource()).getSelectedIndex();
				 valChangeHandled = true;
              
//			  changeMenus();
			  sendNameToServer();
			  createNewHistoryEntry();
				
		}

	
	}

	private void createNewHistoryEntry() {
		  History.newItem(HistToken.LANGUAGE_HIST_TOKEN+":"+ curLanguage+"&"+HistToken.CATEGORY_HIST_TOKEN+":"+curCategory);
	}

	/**
	 * @Override This method tracks the URL clicks from the generated page and
	 *           resubmits after changing them
	 * */
	public boolean onEventPreview(Event event) {
		if (DOM.eventGetType(event) == Event.ONCLICK) {
			Element target = DOM.eventGetTarget(event);
			if ("a".equalsIgnoreCase(getTagName(target))) {
				String href = DOM.getElementAttribute((com.google.gwt.user.client.Element) target, "href");
				// now test if href is: // - #anchor link // - "doThat.html" -
				// relative in-page link // - external link (path is different
				// the e.g. // GWT.getModuleBaseURL()
				if (href.startsWith("/bhajan")) {
					nameField.setText("http://data.sgsdatta.org" + href);
					handler.sendNameToServer();
					return false;
				}
			}
		}
		return true;

	}

	public void changeMenus() {
//		GwtLocaleFactory factory = LocaleUtils.getLocaleFactory();
//		GwtLocale l=factory.fromString("te");
//	
		categoryConstants=GWT.create(CategoryConstants.class);
		titleLabel.setText(categoryConstants.bhajanayogam());
		
		
	}


	native String getTagName(Element element)
	/*-{
		return element.tagName;
	}-*/;



	public void onValueChange(ValueChangeEvent event) {
//		History.getToken();
		if(valChangeHandled) { 
			valChangeHandled=false;
		    return;
		}
	    
		String histVal = (String)event.getValue();
		System.out.println("The current history token is: " + histVal);
		HistToken token = parseParamString(histVal);
		if(!token.category.equals("null")) {
			gotoCategory(token.category);
		}

	}
	

	public static HistToken parseParamString(String string) {

		  String[] ray = string.split("&");

		 
		  HistToken token = new HistToken();	
		  for (int i = 0; i < ray.length; i++) {

		    String[] substrRay = ray[i].split(":");
		    
		    if(substrRay[0].equals(HistToken.CATEGORY_HIST_TOKEN)) {
		    	token.category=substrRay[1];
		    } else if (substrRay[0].equals(HistToken.LANGUAGE_HIST_TOKEN)) {
		    	token.language= Integer.parseInt(substrRay[1]);

		  }
		  }

		  return token;

		}
	
}
