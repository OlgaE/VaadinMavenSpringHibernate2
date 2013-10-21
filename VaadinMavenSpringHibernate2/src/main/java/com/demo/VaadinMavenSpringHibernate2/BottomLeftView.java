package com.demo.VaadinMavenSpringHibernate2;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class BottomLeftView extends CustomComponent {

	private VerticalLayout vLayout;
	private Label messageLabel;
	
	public VerticalLayout getLayout() {
		return vLayout;
	}

	public Label getMessageLabel() {
		return messageLabel;
	}

	public BottomLeftView(){
		
		vLayout = new VerticalLayout();
		
		// Message:
		messageLabel = new Label("Your message will appear here..");
		messageLabel.setStyleName("textstyle");
		vLayout.addComponent(messageLabel);
	}
}
