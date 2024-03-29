package name.pjmbox.jfx.starter;

import java.io.InputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Preloader.ProgressNotification;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import name.pjmbox.jfx.starter.splash.SplashPreloader;

public class OpenjfxApplicationRunner extends Application {

	private static final String JVM_PARAMETER_PRELOADER = "javafx.preloader";
	private static Class<?> baseClass;
	private static boolean isSplashEnabled = false;
	private static String appName;
	private static String splashClassString = SplashPreloader.class.getName();
	private static String styleString = Application.STYLESHEET_CASPIAN;
	private static InputStream fxmlInputStream;

	public static void setBaseClass(Class<?> c) {
		baseClass = c;
	}
	public static void enableSplash(boolean enabled) {
		isSplashEnabled = enabled;
	}

	public static void setSplashClass(Class<?> c) {
		splashClassString = c.getName();
	}

	public static void setFxmlInputStream(InputStream is) {
		fxmlInputStream = is;
	}

	public static void setApplicationStyle(String style) {
		styleString = style;
	}

	public static void setApplicationName(String n) {
		appName = n;
	}

	public static void run(String[] args) {
		if (isSplashEnabled) {
			System.setProperty(JVM_PARAMETER_PRELOADER, splashClassString);
		}
		if (appName != null) {
			// TODO: 设置Macos菜单的app名字，似乎不成功，留待以后解决。
			System.setProperty("dock:name", appName);
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
		}
		launch(args);
	}

	private Scene scene;

	public OpenjfxApplicationRunner() {
	}

	protected void setProgress(double p) {
		if (isSplashEnabled) {
			notifyPreloader(new ProgressNotification(p));
		}
	}

	@Override
	public void init() throws Exception {
		Application.setUserAgentStylesheet(styleString);
		setProgress(0.2);

		ConfigurableApplicationContext ctx = SpringApplication.run(baseClass);
		setProgress(0.3);

		var fxmlLoaderFactory = new FxmlLoaderFactory();
		setProgress(0.4);

		fxmlLoaderFactory.setApplicationContext(ctx);
		setProgress(0.5);

		var fxmlLoader = fxmlLoaderFactory.getFXMLLoader();
		setProgress(0.6);

		var root = (Parent) fxmlLoader.load(fxmlInputStream);
		setProgress(0.7);

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
