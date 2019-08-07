package name.pjmbox.jfx.starter.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.concurrent.ConcurrentHashMap;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BeanHost<T> {

	protected T beanInst;
	protected ConcurrentHashMap<String, Property<?>> hostMap;

	public BeanHost(T bean) {
		beanInst = bean;
		hostMap = new ConcurrentHashMap<String, Property<?>>();
	}

	protected void updateBindProperty(T bean, Field field) throws IllegalArgumentException, IllegalAccessException {
		Property<?> bindProperty = hostMap.get(field.getName());
		if (field.getType() == String.class) {
			var a = (SimpleStringProperty) bindProperty;
			a.setValue((String) field.get(bean));
		} else if (field.getType() == Integer.class) {
			var a = (SimpleIntegerProperty) bindProperty;
			a.setValue((Integer) field.get(bean));
		} else if (field.getType() == Float.class) {
			var a = (SimpleFloatProperty) bindProperty;
			a.setValue((Float) field.get(bean));
		}
	}

	public void updateBean(T bean) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		for (Field field : beanInst.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			if (hostMap.containsKey(field.getName())) {
				updateBindProperty(bean, field);
			}
		}
	}

	public Property<?> getProperty(String name) {
		return hostMap.get(name);
	}

	protected Property<?> createBindProperty(String name) throws NoSuchFieldException, SecurityException {
		Field f = beanInst.getClass().getDeclaredField(name);
		if (f.getType() == String.class) {
			return new SimpleStringProperty();
		}
		else if (f.getType() == Integer.class) {
			return new SimpleIntegerProperty();
		}
		else if (f.getType() == Float.class) {
			return new SimpleFloatProperty();
		}
		throw new InvalidParameterException(String.format("the class %s is not supported.", f.getType().getName()));
	}

	public void bindBean(String name, Property<?> controlProperty)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			SecurityException, NoSuchMethodException, NoSuchFieldException {
		Property<?> bindProperty;
		if (hostMap.containsKey(name)) {
			bindProperty = hostMap.get(name);
		} else {
			bindProperty = createBindProperty(name);
			hostMap.put(name, bindProperty);
		}
		var field = bindProperty.getClass().getMethod("bindBidirectional", Property.class);
		field.invoke(bindProperty, controlProperty);
	}
}
