package au.edu.unimelb.comp90020.client;

import au.edu.unimelb.comp90020.algorithm.Lock;
import au.edu.unimelb.comp90020.algorithm.RAMutex;
import au.edu.unimelb.comp90020.net.Linker;
import au.edu.unimelb.comp90020.net.MsgHandler;
import au.edu.unimelb.comp90020.util.Util;

/*
 * This is the main client 123
 */
public class LockTester {
	
	public static void main(String[] args) throws Exception {
		Linker comm = null;
		try {
			String baseName = args[0];
			int myId = Integer.parseInt(args[1]);
			int numProc = Integer.parseInt(args[2]);
			comm = new Linker(baseName, myId, numProc);
			Lock lock = null;
			if (args[3].equals("RicartAgrawala"))
				lock = new RAMutex(comm);
			for (int i = 0; i < numProc; i++)
				if (i != myId)
					(new ListenerThread(i, (MsgHandler) lock)).start();
			while (true) {
				System.out.println(myId + " is not in CS");
				Util.mySleep(2000);
				lock.requestCS();
				Util.mySleep(2000);
				System.out.println(myId + " is in CS *****");
				lock.releaseCS();
			}
		} catch (InterruptedException e) {
			if (comm != null)
				comm.close();
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
