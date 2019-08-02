module spring.boot.starter.openjfx {
	requires transitive javafx.graphics;
	requires transitive javafx.fxml;
	requires javafx.controls;
	requires spring.core;
	requires spring.beans;
	requires transitive spring.context;
	requires spring.boot;
	requires java.sql;
//	requires javafx.base;
//	requires spring.boot.autoconfigure;
//	requires java.desktop;

	opens name.pjmbox.jfx.starter to javafx.graphics, spring.core;
	opens name.pjmbox.jfx.starter.splash to javafx.graphics;

    exports name.pjmbox.jfx.starter;
	exports name.pjmbox.jfx.starter.splash;
}
