package com.demo.VaadinMavenSpringHibernate2;

import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class BottomRightView extends CustomComponent {

	private VerticalLayout vLayout;
	private Label showResLabel;
	private Button showResButton;
	
	public VerticalLayout getLayout() {
		return vLayout;
	}

	public Button getShowResButton() {
		return showResButton;
	}

	public BottomRightView(){
		
		vLayout = new VerticalLayout();
		
		// Message:
		showResLabel = new Label("Show the last entry in the database:");
		showResLabel.setStyleName("textstyle");
		vLayout.addComponent(showResLabel);
		
		// "Show" button:
		showResButton = new Button("Show");
		vLayout.addComponent(showResButton);
	}
}
