package name.pjmbox.jfx.starter.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import name.pjmbox.jfx.starter.FxmlLoaderFactory;

abstract public class AbstractOpenJfxController implements ApplicationContextAware {

	protected ApplicationContext context;

	@Autowired
	protected FxmlLoaderFactory fxmlLoaderFactory;

	protected AbstractOpenJfxController() {
	}

	abstract protected void controlInitialize() throws IOException;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	protected void useSystemMenu(MenuBar bar) {
		final String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Mac")) {
			bar.useSystemMenuBarProperty().set(true);
		}
	}

	@FXML
	private void initialize() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, IOException {
		bindBean();
		controlInitialize();
	}

	protected Property<?> getCtrlProperty(Node n) {
		if (n.getClass() == TextField.class) {
			return ((TextField) n).textProperty();
		}
		throw new InvalidParameterException(
				String.format("class, %s does not support property binding.", n.getClass().getName()));
	}

	protected void bindBean() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		for (final var f : getClass().getDeclaredFields()) {
			if (f.isAnnotationPresent(BeanHostTag.class)) {
				f.setAccessible(true);
				BeanHostTag bb = f.getAnnotation(BeanHostTag.class);
				BeanHost<?> bbh = (BeanHost<?>) context.getBean(bb.value());
				bbh.bindBean(f.getName(), getCtrlProperty((Node) f.get(this)));
			}
		}
	}
}
