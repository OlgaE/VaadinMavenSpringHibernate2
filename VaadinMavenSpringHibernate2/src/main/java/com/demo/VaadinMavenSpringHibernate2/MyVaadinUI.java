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

		// Setting up the panels:
		VerticalSplitPanel vPanel = new VerticalSplitPanel();
		vPanel.setSplitPosition(45);
		setContent(vPanel);

		HorizontalSplitPanel hPanel = new HorizontalSplitPanel();
		vPanel.setSecondComponent(hPanel);

		// Top view layout:
		TopView topViewObj = new TopView();
		VerticalLayout topViewLayout = topViewObj.getLayout();
		vPanel.setFirstComponent(topViewLayout);

		// Bottom left layout:
		BottomLeftView bottomLeftViewObj = new BottomLeftView();
		final VerticalLayout vLayoutLeft = bottomLeftViewObj.getLayout();
		hPanel.setFirstComponent(vLayoutLeft);

		// Bottom right layout:
		BottomRightView bottomRightViewObj = new BottomRightView();
		final VerticalLayout vLayoutRight = bottomRightViewObj.getLayout();
		hPanel.setSecondComponent(vLayoutRight);
	
		// Action handler for the textarea:
		final TextArea textArea = topViewObj.getTextArea();
		textArea.addValueChangeListener(new Property.ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				userComment = (String) textArea.getValue();
			}
		});
		textArea.setImmediate(true);

		// Action handler for SubmitButton:
		Button button = topViewObj.getSubmitButton();
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
				vLayoutLeft.addComponent(dataEnteredLabel);		
				
				vLayoutLeft.addComponent(new Label(new Date().toString()));
				
				Label messageLabel = new Label("Message:");
				messageLabel.setStyleName("textstyle");
				vLayoutLeft.addComponent(messageLabel);
				
				vLayoutLeft.addComponent(new Label(userComment));

				textArea.setValue("");
			}
		});
		
		// Action handler for "Show" button:
		Button showDataButton = bottomRightViewObj.getShowResButton();
		final Label showEntryLabel = bottomRightViewObj.getShowLabel();
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
					showEntryLabel.setCaption(comment.getUserText());
				} else{
					showEntryLabel.setCaption("Data base is empty.");
				}
				session.close();
			}
		});
		
		// Action handler for "Delete" button:
		Button deleteButton = bottomRightViewObj.getDeleteButton();
		final Label successLabel = bottomRightViewObj.getSuccessLabel();
		deleteButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				
				Session session = sessionFactory.openSession();
				session.beginTransaction();

				// Getting the last entry:
				Criteria criteria = session.createCriteria(UserComment.class);
				criteria.addOrder(Order.desc("id"));
				criteria.setMaxResults(1);
				UserComment lastComment = (UserComment)criteria.uniqueResult();
				
				if(lastComment != null){
					session.delete(lastComment);
					session.getTransaction().commit();
					successLabel.setCaption("Comment was deleted.");
				}else{
					successLabel.setCaption("No comments to delete.");
				}
				session.close();
			}
		});
		
	}
}
