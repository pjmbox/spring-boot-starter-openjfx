package name.pjmbox.jfx.starter;

import java.io.InputStream;

import javafx.scene.image.Image;
import name.pjmbox.jfx.starter.splash.SplashPreloader;

public abstract class AbstractOpenJfxStarterApplication {

	protected static void setFxmlInputStream(InputStream inputStream) {
		OpenjfxApplicationRunner.setFxmlInputStream(inputStream);
	}

	protected static void setSplashImageInputStream(InputStream inputStream) {
		SplashPreloader.setImage(new Image(inputStream));
	}

	protected static void setSplashText(String text) {
		SplashPreloader.setText(text);
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

	public static void run(Class<?> baseClass, String[] args) {
		OpenjfxApplicationRunner.setBaseClass(baseClass);
		OpenjfxApplicationRunner.run(args);
	}
}
