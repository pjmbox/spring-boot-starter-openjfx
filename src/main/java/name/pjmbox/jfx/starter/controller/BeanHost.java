package name.pjmbox.jfx.starter.controller;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BeanHost<T> {

	private Log logger = LogFactory.getLog(BeanHost.class);

	protected ConcurrentHashMap<String, Property<?>> hostMap;
	protected String prefix;
	protected T beanInst;

	public BeanHost(T bean, String prefix) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		hostMap = new ConcurrentHashMap<String, Property<?>>();
		this.prefix = prefix == null ? "" : prefix;
		beanInst = bean;
		createProperties();
		updateProperties();
	}

	public BeanHost(T bean) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		hostMap = new ConcurrentHashMap<String, Property<?>>();
		this.prefix = "";
		beanInst = bean;
		createProperties();
		updateProperties();
	}

	public T getBean() {
		updateBean();
		return beanInst;
	}

	public void setBean(T t) {
		beanInst = t;
		updateProperties();
	}

	@SuppressWarnings("unchecked")
	public <S> Property<S> getProperty(Class<S> s, String fieldName) {
		return (Property<S>) hostMap.get(fieldName);
	}

	public void bindProperty(String fieldName, Property<?> property)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			SecurityException, NoSuchMethodException, NoSuchFieldException {
		Property<?> p = hostMap.get(fieldName);
		var f = p.getClass().getMethod("bindBidirectional", Property.class);
		f.invoke(p, property);
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("%s '%s' is bound", property.getClass().getSimpleName(), fieldName));
		}
	}

	protected void createProperties() throws NoSuchFieldException, SecurityException {
		for (Field f : beanInst.getClass().getDeclaredFields()) {
			hostMap.put(prefix + f.getName(), createProperty(f.getName()));
		}
	}

	protected Property<?> createProperty(String n) throws NoSuchFieldException, SecurityException {
		Field f = beanInst.getClass().getDeclaredField(n);
		if (f.getType() == String.class) {
			return new SimpleStringProperty();
		} else if (f.getType() == Integer.class) {
			return new SimpleIntegerProperty();
		} else if (f.getType() == Float.class) {
			return new SimpleFloatProperty();
		}
		throw new InvalidParameterException(String.format("the type %s is not supported.", f.getType().getName()));
	}

	protected void updateBean() {
		for (Field f : beanInst.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			var n = f.getName();
			Property<?> p = hostMap.get(prefix + n);
			var v = p.getValue();
			try {
				f.set(beanInst, v);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("failed to update bean.", e);
			}
			if (logger.isTraceEnabled()) {
				var t = beanInst.getClass().getSimpleName();
				var s = String.format("%s '%s' is updated to '%s'.", t, n, v);
				logger.trace(s);
			}
		}
	}

	protected void updateProperties() {
		for (Field f : beanInst.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
				updateProperty(beanInst, f);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("failed to update properties.", e);
			}
		}
	}

	protected void updateProperty(T bean, Field f) throws IllegalArgumentException, IllegalAccessException {
		var n = prefix + f.getName();
		var v = f.get(bean);
		Property<?> p = hostMap.get(n);
		if (f.getType() == String.class) {
			((SimpleStringProperty) p).setValue((String) v);
		} else if (f.getType() == Integer.class) {
			((SimpleIntegerProperty) p).setValue((Integer) v);
		} else if (f.getType() == Float.class) {
			((SimpleFloatProperty) p).setValue((Float) v);
		} else {
			throw new IllegalArgumentException(String.format("the type %s is not supported.", f.getType().getName()));
		}
		if (logger.isTraceEnabled()) {
			var t = p.getClass().getSimpleName();
			var s = String.format("%s '%s' is updated to '%s'.", t, n, v);
			logger.trace(s);
		}
	}

}
