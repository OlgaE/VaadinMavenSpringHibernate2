package com.demo.VaadinMavenSpringHibernate2;

import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class TopView extends CustomComponent {

	private VerticalLayout vLayout;
	private Label welcomeLabel;
	private Label commentLabel;
	private TextArea textArea;
	private Button submitButton;
	
	public VerticalLayout getLayout(){
		return vLayout;
	}
	
	public TextArea getTextArea(){
		return textArea;
	}
	
	public Button getSubmitButton(){
		return submitButton;
	}
	
	public TopView(){
		
		// Main layout for the top part of the page:
		vLayout = new VerticalLayout();

		// Getting a bean from the Spring bean factory:
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet
				.getCurrent().getServletContext());
		BeanInterface bean = (BeanInterface) helper.getBean("myBean");
		String welcome = bean.sayHello();
		
		// Welcome message:
		welcomeLabel = new Label(welcome);
		welcomeLabel.setStyleName("welcomestyle");
		vLayout.addComponent(welcomeLabel);
		
		// "Leave a comment" label:
		commentLabel = new Label("Please leave a comment:");
		commentLabel.setStyleName("textstyle");
		vLayout.addComponent(commentLabel);
		
		// Text area:
		textArea = new TextArea("");
		textArea.setHeight("60");
		textArea.setWidth("350");
		vLayout.addComponent(textArea);
		
		// Submit button:
		submitButton = new Button("Submit");
		vLayout.addComponent(submitButton);
		
		setSizeFull();
	}
}
