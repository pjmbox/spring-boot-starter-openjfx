package name.pjmbox.jfx.starter.splash;

import javafx.animation.FadeTransition;
import javafx.application.Preloader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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

	private static final String imagePath = "/starter-images/splash.jpg";
	private static Image image = null;
	private static String text = "please wait...";
	private static boolean isAlwayOnTop = true;

	public static void setImage(Image v) {
		image = v;
	}

	public static void setText(String v) {
		text = v;
	}

	public static void setAlwaysOnTop(boolean v) {
		isAlwayOnTop = v;
	}

	private Stage preloaderStage;
	private ProgressBar progressBar;

	protected Parent initRootNode() {
		var bimg = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
		var default_borderstroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
				BorderWidths.DEFAULT);
		progressBar = new ProgressBar(0);
		progressBar.setMaxWidth(Double.MAX_VALUE);
		progressBar.setMaxHeight(8);

		var loading = new VBox(2);
		loading.setPadding(new Insets(0, 1, 1, 1));
		loading.setMaxWidth(Region.USE_COMPUTED_SIZE);
		loading.setMaxHeight(Region.USE_COMPUTED_SIZE);
		loading.getChildren().add(new Label(text));
		loading.getChildren().add(progressBar);

		var root = new BorderPane();
		root.setBottom(loading);
		root.setBackground(new Background(bimg));
		root.setBorder(new Border(default_borderstroke));

		return root;
	}

	protected void loadImage() {
		if (image == null) {
			image = new Image(getClass().getResourceAsStream(imagePath));
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		loadImage();
		var root = initRootNode();
		var scene = new Scene(root, Color.TRANSPARENT);

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setAlwaysOnTop(isAlwayOnTop);
		primaryStage.setWidth(image.getWidth());
		primaryStage.setHeight(image.getHeight());
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();
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
			preloaderStage.show();
			fadeScene(0, 0, 1, false);
			break;
		case BEFORE_LOAD:
			break;
		case BEFORE_START:
			fadeScene(1.0, 1, 0, true);
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
