module spring.boot.starter.openjfx {
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;
	requires transitive spring.context;
	requires java.sql;
	requires javafx.controls;
	requires spring.core;
	requires spring.beans;
	requires spring.boot;
	requires javafx.base;

	opens name.pjmbox.jfx.starter to javafx.graphics, spring.core;
	opens name.pjmbox.jfx.starter.controller to spring.core, javafx.fxml, spring.aop;
	opens name.pjmbox.jfx.starter.splash to javafx.graphics;

    exports name.pjmbox.jfx.starter;
	exports name.pjmbox.jfx.starter.splash;
	exports name.pjmbox.jfx.starter.controller;
}
