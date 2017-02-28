package milandr_ex.model.mcu.ext;

import milandr_ex.model.BasicController;

/**
 * Created by lizard2k1 on 28.02.2017.
 */
public abstract class MCUExtPairController extends BasicController {

	@Override
	protected boolean isExtPair() { return true; }
}
