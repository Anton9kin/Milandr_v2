package milandr_ex;

import com.aquafx_project.AquaFx;
import com.guigarage.flatterfx.FlatterConfiguration;
import com.guigarage.flatterfx.FlatterFX;
import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import milandr_ex.data.AppScene;
import milandr_ex.model.RootLayoutController;
import milandr_ex.utils.LoaderUtils;
import org.aerofx.AeroFX;

import static milandr_ex.data.Constants.*;


public class MilandrEx extends Application {
	private AppScene scene;

	public MilandrEx() {
	}

	public void setScene(AppScene scene) {
		this.scene = scene;
	}

	public AppScene getScene() {
		return scene;
	}

	@Override
	public void start(Stage primStage) {
//		this.primaryStage.getIcons().add(new Image("file:resourses/images/recept1.png"));
//		setUserAgentStylesheet(STYLESHEET_CASPIAN);
//		setUserAgentStylesheet(STYLESHEET_MODENA);
		if (USE_AERO_STYLE) AeroFX.style();
		if (USE_AQUA_STYLE) AquaFx.style();
		if (USE_FLAT_STYLE) FlatterFX.style();
		initRootLayout(primStage);
		showMain();
	}

	/**
	 * Initializes the root layout
	 * 
	 * @param stage
	 */
	
	public BorderPane initRootLayout(Stage stage){
		LoaderUtils.initRootLayout(stage);
		setScene((AppScene) stage.getScene());
		return getScene().getRootLayout();
	}

	/**
	 * Show recept overview inside root layout
	 * 
	 */
	public void showMain(){
		((RootLayoutController) getScene().getRootController()).CloseProject();
	}

	/**
	 * 
	 * Returns the main stage
	 */
	public Stage getPrimaryStage(){
		return scene.getAppStage();
	}

	@SuppressWarnings("SameParameterValue")
	private static void preInit(boolean useAqua, boolean useHover, boolean newCombo) {
		USE_FLAT_STYLE = false;
		USE_AERO_STYLE = false;
		USE_AQUA_STYLE = useAqua;
		USE_HOVERED_STYLE = !useAqua && useHover;
		NEW_PAIRS_COMBO_STYLE = !useAqua && newCombo;
	}

	public static void main(String[] args) {
//		preInit(true, false, false);
//		preInit(false, false, false);
//		preInit(false, false, true);
		preInit(false, true, false);
//		preInit(false, true, true);
		launch(args);
	}
}
