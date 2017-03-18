package milandr_ex;

import com.sun.javafx.application.LauncherImpl;
import milandr_ex.model.SplashScreenLoader;

/**
 * Main class for separate loading splash and main app class
 * Created by lizard on 18.03.17 at 13:22.
 */
public class Main {
	public static void main(String[] args) {
		LauncherImpl.launchApplication(MilandrEx.class, SplashScreenLoader.class, args);
	}
}
