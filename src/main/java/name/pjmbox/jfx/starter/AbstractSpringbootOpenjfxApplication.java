package name.pjmbox.jfx.starter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import javafx.application.Application;
import javafx.application.Preloader.ProgressNotification;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import name.pjmbox.jfx.starter.splash.SplashPreloader;

public class AbstractSpringbootOpenjfxApplication extends Application {

	private static final String JVM_PARAMETER_PRELOADER = "javafx.preloader";
	private static boolean isSplashEnabled = false;
	private static String splashClassString = SplashPreloader.class.getName();

	protected static void enableSplash(boolean enabled) {
		isSplashEnabled = enabled;
	}

	protected static void setSplashClass(Class<?> c) {
		splashClassString = c.getName();
	}

	public static void main(String[] args) {
		if (isSplashEnabled) {
			System.setProperty(JVM_PARAMETER_PRELOADER, splashClassString);
		}
		launch(args);
	}

	@Autowired
	protected FxmlLoaderFactory fxmlLoaderFactory;
	protected String fxmlPath = "";
	protected String styleString = Application.STYLESHEET_CASPIAN;
	private Scene scene;

	private AbstractSpringbootOpenjfxApplication(String fxmlPath) {
		this.fxmlPath = fxmlPath;
	}

	protected void setApplicationStyle(String style) {
		this.styleString = style;
	}

	protected void setProgress(double p) {
		if (isSplashEnabled) {
			notifyPreloader(new ProgressNotification(p));
		}
	}

	@Override
	public void init() throws Exception {
		setProgress(0.2);
		SpringApplication.run(getClass());

		setProgress(0.3);
		Application.setUserAgentStylesheet(styleString);

		setProgress(0.4);
		var fxmlLoader = fxmlLoaderFactory.getFXMLLoader();

		setProgress(0.5);
		var root = (Parent) fxmlLoader.load(getClass().getResourceAsStream(fxmlPath));

		setProgress(0.6);
		scene = new Scene(root);

		setProgress(0.9);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(scene);
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());
		primaryStage.show();
	}
}
