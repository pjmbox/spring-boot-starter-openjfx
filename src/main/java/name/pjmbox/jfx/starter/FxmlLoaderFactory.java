package name.pjmbox.jfx.starter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;

@Service
public class FxmlLoaderFactory implements Callback<Class<?>, Object>, ApplicationContextAware {

	protected ApplicationContext applicationContext;
	@Override
	public Object call(Class<?> param) {
		return applicationContext.getBean(param);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public FXMLLoader getFXMLLoader() {
		var fxmlLoader = new FXMLLoader();
		fxmlLoader.setControllerFactory(this);
		return fxmlLoader;
	}

}
