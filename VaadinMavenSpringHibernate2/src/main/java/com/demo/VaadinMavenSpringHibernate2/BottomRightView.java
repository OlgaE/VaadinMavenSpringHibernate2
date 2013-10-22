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
	private Label deleteLabel;
	private Button deleteButton;
	private Label showLabel;
	private Label successLabel;
	
	public Label getSuccessLabel(){
		return successLabel;
	}
	
	public Label getShowLabel() {
		return showLabel;
	}

	public VerticalLayout getLayout() {
		return vLayout;
	}

	public Button getShowResButton() {
		return showResButton;
	}

	public Button getDeleteButton(){
		return deleteButton;
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
		Label emptyLabel = new Label();
		emptyLabel.setHeight("30px");
		
		// "Show" label:
		showLabel = new Label("");
		showLabel.setHeight("13px");
		vLayout.addComponent(showLabel);
		
		// Empty line:
		Label emptyLine = new Label("");
		emptyLine.setHeight("20px");
		vLayout.addComponent(emptyLine);
		
		// "Delete" label:
		deleteLabel = new Label("Delete the last entry:");
		deleteLabel.setStyleName("textstyle");
		vLayout.addComponent(deleteLabel);
		
		// "Delete" button:
		deleteButton = new Button("Delete");
		vLayout.addComponent(deleteButton);
		
		// Success label:
		successLabel = new Label("");
		successLabel.setHeight("13px");
		vLayout.addComponent(successLabel);
	}
}
