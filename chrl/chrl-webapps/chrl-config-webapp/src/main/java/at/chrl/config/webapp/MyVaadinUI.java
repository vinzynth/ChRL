package at.chrl.config.webapp;

import java.util.Date;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.BasicEventProvider;

@Theme("mytheme")
@SuppressWarnings("serial")
public class MyVaadinUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = MyVaadinUI.class, widgetset = "at.chrl.config.webapp.AppWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        
        Label lbl = new Label("Browser Size: " + getPage().getBrowserWindowWidth() + "x" +getPage().getBrowserWindowHeight());
        
        layout.addComponent(lbl);
        
        getPage().addBrowserWindowResizeListener(event -> lbl.setDescription("Browser Size: " + event.getWidth() + "x" + event.getHeight()));
        
        Button button = new Button("Click Me Twice!");
        button.addClickListener(event -> {
			layout.addComponent(new Label("Thank you for clicking"));
			Notification.show("nice tray?", "Tray :D", Type.TRAY_NOTIFICATION);
		});
        layout.addComponent(button);
        
        BasicEventProvider bep = new BasicEventProvider();
        
        
        bep.addEvent(new BasicEvent("basic event1", "basiv event1 desc", new Date(), new Date(new Date().getTime() + 60*60*1000)));
        layout.addComponent(new Calendar(bep));
        bep.addEvent(new BasicEvent("basic event2", "basiv event2 desc", new Date(new Date().getTime() + 2*60*60*1000), new Date(new Date().getTime() + 4*60*60*1000)));
        
        bep.addEvent(new BasicEvent("test", "all day long", new Date()));
        
        
        Notification.show("notify!", "notyify desc", Type.WARNING_MESSAGE);
        Notification.show("notify! err", "notyify err desc", Type.ERROR_MESSAGE);
        
        Notification.show("nice tray?", "Tray :D", Type.TRAY_NOTIFICATION);
        Notification.show("nice tray?... again?", "Another Tray =)", Type.TRAY_NOTIFICATION);
        
        
    }
}
