package name.pjmbox.jfx.starter;

import java.io.InputStream;

import javafx.scene.image.Image;
import name.pjmbox.jfx.starter.splash.SplashPreloader;

public abstract class AbstractOpenJfxStarterApplication {

	protected static void setRootFxmlInputStream(InputStream inputStream) {
		OpenjfxApplicationRunner.setFxmlInputStream(inputStream);
	}

	protected static void setSplashImageInputStream(InputStream inputStream) {
		SplashPreloader.setImage(new Image(inputStream));
	}

	protected static void setSplashText(String text) {
		SplashPreloader.setText(text);
	}

	protected static void setSplashAlwaysOnTop(boolean v) {
		SplashPreloader.setAlwaysOnTop(v);
	}

	protected static void enableSplash(boolean v) {
		OpenjfxApplicationRunner.enableSplash(v);
	}

	protected static void setSplashClass(Class<?> c) {
		OpenjfxApplicationRunner.setSplashClass(c);
	}

	protected static void setApplicationStyle(String v) {
		OpenjfxApplicationRunner.setApplicationStyle(v);
	}

	protected static void setApplicationName(String n) {
		OpenjfxApplicationRunner.setApplicationName(n);
	}

	public static void run(Class<?> baseClass, String[] args) {
		OpenjfxApplicationRunner.setBaseClass(baseClass);
		OpenjfxApplicationRunner.run(args);
	}
}
