package at.chrl.vaadin.ui;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Title("Hello Window")
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@PreserveOnRefresh
public class HelloWorldUI extends UI {

	int clickCount = 0;

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout content = new VerticalLayout();
		content.setMargin(true);
		setContent(content);

		final Label clickCountLabel = new Label();

		Button button = new Button("Click Me");
		button.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				clickCount++;
				String msg = "Hello Vaadin - " + clickCount + " clicks!";
				clickCountLabel.setCaption("Click count: " + clickCount);
				content.addComponent(new Label(msg));
			}
		});
		content.addComponent(button);
		content.addComponent(clickCountLabel);
	}

}