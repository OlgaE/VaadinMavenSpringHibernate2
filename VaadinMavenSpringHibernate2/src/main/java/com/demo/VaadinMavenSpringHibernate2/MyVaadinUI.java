package com.demo.VaadinMavenSpringHibernate2;

import java.util.Date;

import javax.servlet.annotation.WebServlet;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI {

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;

	UserComment userCommentObj2 = new UserComment();

	String userComment;
	Label textDisplayLabel;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class)
	public static class Servlet extends VaadinServlet {
	}

	static {
		// Getting a SessionFactory object:
		// Session factory is created once per application.
		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	}

	@Override
	protected void init(VaadinRequest request) {

		final UserComment userCommentObj = new UserComment();

		// Getting a bean from the Spring bean factory:
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet
				.getCurrent().getServletContext());
		final MyBeanInterface bean = (MyBeanInterface) helper.getBean("myBean");

		VerticalSplitPanel vPanel = new VerticalSplitPanel();
		vPanel.setSplitPosition(50);
		setContent(vPanel);

		HorizontalSplitPanel hPanel = new HorizontalSplitPanel();
		vPanel.setSecondComponent(hPanel);

		VerticalLayout vLayout1 = new VerticalLayout();
		vPanel.setFirstComponent(vLayout1);

		final VerticalLayout vLayout2 = new VerticalLayout();
		hPanel.setFirstComponent(vLayout2);

		final VerticalLayout vLayout3 = new VerticalLayout();
		hPanel.setSecondComponent(vLayout3);

		// TextArea:
		Label commentLabel = new Label("Please leave a comment:");
		commentLabel.setStyleName("textstyle");
		vLayout1.addComponent(commentLabel);
		final TextArea textArea = new TextArea("");
		textArea.setStyleName("textstyle");
		textArea.setHeight("60");
		textArea.setWidth("350");
		textArea.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				userComment = (String) textArea.getValue();
			}
		});
		textArea.setImmediate(true);
		vLayout1.addComponent(textArea);

		final Label messageLabel = new Label("Message:");
		messageLabel.setStyleName("textstyle");
		final Label dataEnteredLabel = new Label("Date entered:");
		dataEnteredLabel.setStyleName("textstyle");

		// SubmitButton:
		Button button = new Button("Submit");
		button.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {

				// saving data in a database:
				userCommentObj.setUserText(userComment);
				userCommentObj.setDate(new Date());

				Session session = sessionFactory.openSession();
				session.beginTransaction();
				session.save(userCommentObj);
				session.getTransaction().commit();
				session.close();
				
				vLayout2.addComponent(new Label("***"));
				vLayout2.addComponent(dataEnteredLabel);
				vLayout2.addComponent(new Label(new Date().toString()));
				vLayout2.addComponent(messageLabel);
				vLayout2.addComponent(new Label(userComment));

				textArea.setValue("");
			}
		});
		vLayout1.addComponent(button);

		Label message = new Label("Your comment will appear here..");
		vLayout2.addComponent(message);

		// Getting data back from the database:
		Button showDataButton = new Button("Show Data");
		showDataButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				Session session = sessionFactory.openSession();
				session.beginTransaction();

				// Getting the object back from the database:
				userCommentObj2 = (UserComment) session.get(UserComment.class,1); // 1 - userId
				session.close();
				
				vLayout3.addComponent(new Label(userCommentObj2.getUserText()));
			}
		});
		vLayout3.addComponent(showDataButton);
	}
}
