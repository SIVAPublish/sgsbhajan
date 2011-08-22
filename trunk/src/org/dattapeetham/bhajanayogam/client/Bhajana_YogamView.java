package org.dattapeetham.bhajanayogam.client;

import org.dattapeetham.bhajanayogam.client.Bhajana_Yogam.SubmitHandler;
import org.dattapeetham.bhajanayogam.client.content.i18n.CategoryConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Bhajana_YogamView implements ClickHandler {
	public MenuBar categoryMenu;
	public TextBox urlField;
	public Label textToServerLabel;
	public Label titleLabel;
	public HTML bhajanTextLabel;
	public HTML catLabel;
	public TabPanel tabPanel;
	public DialogBox dialogBox;
	public Button closeButton;
	public SubmitHandler handler;
	public boolean valChangeHandled;
	public static final int INDEX_TAB = 0;
	public static final int BHAJAN_TAB = 1;

	CategoryConstants categoryConstants = GWT.create(CategoryConstants.class);

	public Bhajana_YogamView(MenuBar categoryMenu, Label textToServerLabel, Label titleLabel, HTML bhajanTextLabel,
			HTML catLabel, TabPanel tabPanel, DialogBox dialogBox, Button closeButton, SubmitHandler handler) {
		urlField = new TextBox();
		urlField.setText("Wiki URL");
		titleLabel.setText(categoryConstants.bhajanayogam());
		bhajanTextLabel.setHTML(categoryConstants.welcome());
		catLabel.setHTML(categoryConstants.welcome());
		RootPanel.get("titleContainer").add(titleLabel);

		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		urlField.setEnabled(false);

		dialogBox.setText("Bhajan");
		dialogBox.setAnimationEnabled(true);

		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");

		HorizontalPanel dialogVPanel = new HorizontalPanel();
		dialogVPanel.addStyleName("dialogVPanel");

		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		

		bhajanTextLabel.setStylePrimaryName("bigger");
	
		tabPanel.add(catLabel,categoryConstants.index());
		tabPanel.add(bhajanTextLabel,categoryConstants.bhajan());
		
		RootPanel.get("bhajanTextContainer").add(tabPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				
			}
		});

		// Add a handler to send the name to the server
		SubmitHandler handler = new SubmitHandler();
		urlField.addKeyUpHandler(handler);

		createMenu();
		// Add it to the root panel.
		RootPanel.get("menuContainer").add(categoryMenu);
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


	public void buildItemView(String[] result) {
		catLabel.removeStyleName("serverResponseLabelError");
		catLabel.removeStyleName("retrievingMessage");
		tabPanel.selectTab(INDEX_TAB);
		
		for(String item : result) {
//			Anchor a = new Anchor();
			Label l = new Label(item);
			l.addClickHandler(this);
				
				
			});
		}

	public void onClick(ClickEvent event) {
		((Label)event.getSource()).getText();
	
	}
	    
		// TODO Auto-generated method stub
		
	}
}