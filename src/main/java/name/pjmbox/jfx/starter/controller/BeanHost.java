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

	protected String prefix;
	protected T beanInst;
	protected ConcurrentHashMap<String, Property<?>> hostMap;

	public BeanHost(T bean, String prefix) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		this.prefix = prefix == null ? "" : prefix + "_";
		beanInst = bean;
		hostMap = new ConcurrentHashMap<String, Property<?>>();
		createBindProperties();
		updateBindProperties();
	}

	public BeanHost(T bean) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		prefix = "";
		beanInst = bean;
		hostMap = new ConcurrentHashMap<String, Property<?>>();
		createBindProperties();
		updateBindProperties();
	}

	public T getBean() {
		return beanInst;
	}

	public void setBean(T t) {
		beanInst = t;
	}

	protected void createBindProperties() throws NoSuchFieldException, SecurityException {
		for (Field field : beanInst.getClass().getDeclaredFields()) {
			hostMap.put(prefix + field.getName(), createBindProperty(field.getName()));
		}
	}

	protected Property<?> createBindProperty(String name) throws NoSuchFieldException, SecurityException {
		Field f = beanInst.getClass().getDeclaredField(name);
		if (f.getType() == String.class) {
			return new SimpleStringProperty();
		} else if (f.getType() == Integer.class) {
			return new SimpleIntegerProperty();
		} else if (f.getType() == Float.class) {
			return new SimpleFloatProperty();
		}
		throw new InvalidParameterException(String.format("the class %s is not supported.", f.getType().getName()));
	}

	public void updateBindProperties() {
		for (Field field : beanInst.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				updateBindProperty(beanInst, field);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	protected void updateBindProperty(T bean, Field field) throws IllegalArgumentException, IllegalAccessException {
		Property<?> bindProperty = hostMap.get(prefix + field.getName());
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

	public void updateBeanMembers() {
		for (Field field : beanInst.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			Property<?> bindProperty = hostMap.get(prefix + field.getName());
			try {
				field.set(beanInst, bindProperty.getValue());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <S> Property<S> getProperty(S s, String fieldName) {
		return (Property<S>) hostMap.get(fieldName);
	}

	public void bindBean(String fieldName, Property<?> controlProperty)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			SecurityException, NoSuchMethodException, NoSuchFieldException {
		Property<?> bindProperty = hostMap.get(fieldName);
		var field = bindProperty.getClass().getMethod("bindBidirectional", Property.class);
		field.invoke(bindProperty, controlProperty);
	}
}
