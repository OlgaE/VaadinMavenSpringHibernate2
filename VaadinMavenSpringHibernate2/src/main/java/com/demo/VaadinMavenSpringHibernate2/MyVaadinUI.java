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
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	UserComment userCommentObj = new UserComment();
	String userComment = "";
	Label textDisplayLabel;
	
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
    	// Getting a bean from the Spring bean factory:
      	SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
    	final MyBeanInterface bean = (MyBeanInterface)helper.getBean("myBean");
    	
        VerticalSplitPanel vPanel = new VerticalSplitPanel();
        vPanel.setSplitPosition(50);
        setContent(vPanel);
        
        VerticalLayout vLayout1 = new VerticalLayout();
        vPanel.setFirstComponent(vLayout1);
        
        final VerticalLayout vLayout2 = new VerticalLayout();
        vPanel.setSecondComponent(vLayout2);

        // TextArea:
        final TextArea textArea = new TextArea("Please leave a comment:");
        textArea.setHeight("60");
        textArea.setWidth("150");
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
                
            	vLayout2.addComponent(new Label(userComment));
            	textArea.setValue("");
            }
        });
        vLayout1.addComponent(button);
        
        Label message = new Label("Your comment will appear here..");
        vLayout2.addComponent(message);
    }
    
    public static void saveUserData(){
    	
		// ********************************************************************************************
		//
		// Using postgresql with these data in pom.xml file:
		//
		/*
		 * <dependency> 
		 * 	<groupId>org.hibernate</groupId>
		 * 	<artifactId>hibernate-core</artifactId>
		 * 	<version>4.2.6.Final</version> 
		 * </dependency> 
		 * <dependency>
		 * 	<groupId>postgresql</groupId> 
		 * 	<artifactId>postgresql</artifactId>
		 * 	<version>9.0-801.jdbc4</version> 
		 * </dependency>
		 */

        // Setting data for the user:
		UserDetails user = new UserDetails();
		user.setUserId(1);
		user.setUserName("The very first user)");
		user.setDateJoined(new Date());
		user.setAddress("First address");
		user.setDescription("Description goes here");;
		user.setVeryLongDescription("Some long text here");
		
		// Getting a Session object:
		// Session factory is created once per application.
		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				configuration.getProperties()).buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		Session session = sessionFactory.openSession();

		// Using session object to save an object.
		// First, begin a transaction:
		session.beginTransaction();

		// Saving:
		session.save(user);

		// Ending the transaction:
		session.getTransaction().commit();
		session.close();
		
		//
		// Fetching a saved object:
		//
		user = null;
		
		// Opening a new session:
		session = sessionFactory.openSession();
		session.beginTransaction();
		
		// Getting the object:
		user = (UserDetails) session.get(UserDetails.class, 1); // 1 - userId (primary key)
		// seesion.get() has a return type of Object, so we need to cast it to out class.
		
		System.out.println("User name retrieved is " + user.getUserName());
		// ************************************************************************************************
    }
}
