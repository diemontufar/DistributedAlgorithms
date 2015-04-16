package au.edu.unimelb.comp90020.net;

import java.io.*;
/*
 * Any lock implementation in a distributed environment will 
 * also have to handle messages that are used by the algorithm for locking. 
 * For this we use the interface MsgHandler 
 */
public interface MsgHandler {
	
	public void handleMsg(Msg m, int srcId, String tag);

	public Msg receiveMsg(int fromId) throws IOException;
}
