module spring.boot.starter.openjfx {
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;
	requires transitive spring.context;
	requires java.sql;
	requires javafx.base;
	requires javafx.controls;
	requires spring.core;
	requires spring.beans;
	requires spring.boot;
	requires spring.jcl;

	opens name.pjmbox.jfx.starter to javafx.graphics, spring.core;
	opens name.pjmbox.jfx.starter.controller to spring.core, javafx.fxml;
	opens name.pjmbox.jfx.starter.splash to javafx.graphics;

    exports name.pjmbox.jfx.starter;
	exports name.pjmbox.jfx.starter.controller;
}
