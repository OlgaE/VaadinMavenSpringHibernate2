package com.demo.VaadinMavenSpringHibernate2;

import java.util.Date;

import javax.servlet.annotation.WebServlet;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
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

	String userComment;

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class)
	public static class Servlet extends VaadinServlet {
	}

	static {
		
		// Getting a SessionFactory object:
		// Session factory is created once per application.
		try {
			Configuration configuration = new Configuration();
			configuration.configure();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (HibernateException e) {
			System.err.println("Initial SessionFactory creation failed." + e);
		}
	}

	@Override
	protected void init(VaadinRequest request) {

		final UserComment userCommentObj = new UserComment();

		// Getting a bean from the Spring bean factory:
		SpringContextHelper helper = new SpringContextHelper(VaadinServlet
				.getCurrent().getServletContext());
		BeanInterface bean = (BeanInterface) helper.getBean("myBean");

		// Setting up the panels:
		VerticalSplitPanel vPanel = new VerticalSplitPanel();
		vPanel.setSplitPosition(45);
		setContent(vPanel);

		HorizontalSplitPanel hPanel = new HorizontalSplitPanel();
		vPanel.setSecondComponent(hPanel);

		VerticalLayout vLayout1 = new VerticalLayout();
		vPanel.setFirstComponent(vLayout1);

		final VerticalLayout vLayout2 = new VerticalLayout();
		hPanel.setFirstComponent(vLayout2);

		final VerticalLayout vLayout3 = new VerticalLayout();
		hPanel.setSecondComponent(vLayout3);

		// Welcome message:
		String welcome = bean.sayHello();
		Label welcomeLabel = new Label(welcome);
		welcomeLabel.setStyleName("welcomestyle");
		vLayout1.addComponent(welcomeLabel);
		
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
				
				Label dataEnteredLabel = new Label("Date entered:");
				dataEnteredLabel.setStyleName("textstyle");
				vLayout2.addComponent(dataEnteredLabel);		
				
				vLayout2.addComponent(new Label(new Date().toString()));
				
				Label messageLabel = new Label("Message:");
				messageLabel.setStyleName("textstyle");
				vLayout2.addComponent(messageLabel);
				
				vLayout2.addComponent(new Label(userComment));

				textArea.setValue("");
			}
		});
		vLayout1.addComponent(button);

		// Below the split line:
		Label message = new Label("Your comment will appear here..");
		message.setStyleName("textstyle");
		vLayout2.addComponent(message);

		// Getting data back from the database:
		Label showResLabel = new Label("Show the last entry in the database:");
		showResLabel.setStyleName("textstyle");
		vLayout3.addComponent(showResLabel);
		
		Button showDataButton = new Button("Show");
		showDataButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				Session session = sessionFactory.openSession();
				session.beginTransaction();

				// Getting the last entry:
				Criteria criteria = session.createCriteria(UserComment.class);
				criteria.addOrder(Order.desc("id"));
				criteria.setMaxResults(1);
				UserComment comment = (UserComment)criteria.uniqueResult();
				
				if(comment != null){
					vLayout3.addComponent(new Label(comment.getUserText()));
				}
				session.close();
			}
		});
		vLayout3.addComponent(showDataButton);
	}
}
