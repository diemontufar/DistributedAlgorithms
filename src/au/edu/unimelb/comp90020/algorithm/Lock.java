package au.edu.unimelb.comp90020.algorithm;

import au.edu.unimelb.comp90020.net.MsgHandler;

public interface Lock extends MsgHandler {
	public void requestCS(); // may block

	public void releaseCS();
}
