package milandr_ex.model.mcu.inn;

import milandr_ex.data.AppScene;
import milandr_ex.data.Device;
import milandr_ex.model.BasicController;

public class MCUTimerController extends BasicController {

	@Override
	protected void postInit(AppScene scene) {
		setDevicePair(Device.EPairNames.TMR);
	}

	@Override
	protected void initLater(AppScene scene) {
		super.initLater(scene);
	}
}
