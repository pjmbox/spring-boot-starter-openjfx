package name.pjmbox.jfx.starter.splash;

import javafx.animation.FadeTransition;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SplashPreloader extends Preloader {

	private static String imagePath = "/images/splash.png";
	private static String text = "please wait...";

	public static void setImagePath(String v) {
		imagePath = v;
	}

	public static void setText(String v) {
		text = v;
	}

	private Stage preloaderStage;
	private ProgressBar progressBar;

	protected Parent initRootNode(ProgressBar progressBar, Image image) {
		var bimg = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		var default_borderstroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				BorderWidths.DEFAULT);

		var loading = new VBox(20);
		loading.setMaxWidth(Region.USE_PREF_SIZE);
		loading.setMaxHeight(Region.USE_PREF_SIZE);
		loading.getChildren().add(progressBar);
		loading.getChildren().add(new Label(text));

		var root = new BorderPane(loading);
		root.setBackground(new Background(bimg));
		root.setBorder(new Border(default_borderstroke));

		return root;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		progressBar = new ProgressBar(0);
		var img = new Image(getClass().getResourceAsStream(imagePath));
		var root = initRootNode(progressBar, img);
		var scene = new Scene(root, Color.TRANSPARENT);

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setWidth(img.getWidth());
		primaryStage.setHeight(img.getHeight());
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
		primaryStage.show();

		preloaderStage = primaryStage;
	}

	protected void fadeScene(double delay, double start, double end, boolean hide) {
		var fade = new FadeTransition(Duration.millis(0.3 * 1000), preloaderStage.getScene().getRoot());
		fade.setFromValue(start);
		fade.setToValue(end);
		fade.setCycleCount(1);
		fade.setDelay(Duration.millis(delay * 1000));
		if (hide) {
			fade.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					preloaderStage.hide();
				}
			});
		}
		fade.play();
	}

	@Override
	public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
		switch (stateChangeNotification.getType()) {
		case BEFORE_INIT:
			progressBar.setProgress(0.1);
			fadeScene(0, 0, 1, false);
			break;
		case BEFORE_LOAD:
			break;
		case BEFORE_START:
			fadeScene(0.3, 1, 0, true);
			progressBar.setProgress(1);
			break;
		default:
			break;
		}
	}

	@Override
	public void handleProgressNotification(ProgressNotification info) {
		progressBar.setProgress(info.getProgress());
	}

	@Override
	public void handleApplicationNotification(PreloaderNotification info) {
		if (info instanceof ProgressNotification) {
			handleProgressNotification((ProgressNotification) info);
		}
	}
}