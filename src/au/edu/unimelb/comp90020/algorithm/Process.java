package au.edu.unimelb.comp90020.algorithm;

import java.io.IOException;

import au.edu.unimelb.comp90020.net.Linker;
import au.edu.unimelb.comp90020.net.Msg;
import au.edu.unimelb.comp90020.net.MsgHandler;
import au.edu.unimelb.comp90020.util.Util;
/*
 * This will allow processes to have access to its identifier myId, the total number 
 * of processes N,and simple send and receive routines. 
 * The method handleMsg is empty, and any class that extends Process is expected to override this method.
 */
public class Process implements MsgHandler {
	
	public enum ProcessState {RELEASED,WANTED,HELD};
	
	private int N, myId;

	public Linker comm;
	private ProcessState state;

	public Process(Linker initComm) {
		this.comm = initComm;
		this.myId = comm.getMyId();
		this.N = comm.getNumProc();
		this.state = ProcessState.RELEASED; //On initialization my state is RELEASED by default
	}

	public synchronized void handleMsg(Msg m, int src, String tag) {
	}

	public void sendMsg(int destId, String tag, String msg) {
		Util.println("Sending msg to " + destId + ":" + tag + " " + msg);
		comm.sendMsg(destId, tag, msg);
	}

	public void sendMsg(int destId, String tag, int msg) {
		sendMsg(destId, tag, String.valueOf(msg) + " ");
	}


	public void broadcastMsg(String tag, int msg) {
		for (int i = 0; i < N; i++)
			if (i != myId)
				sendMsg(i, tag, msg);
	}

	public Msg receiveMsg(int fromId) {
		try {
			return comm.receiveMsg(fromId);
		} catch (IOException e) {
			System.out.println(e);
			comm.close();
			return null;
		}
	}

	public synchronized void myWait() {
		try {
			wait();
		} catch (InterruptedException e) {
			System.err.println(e);
		}
	}
	
	public int getMyId() {
		return myId;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public ProcessState getMyState() {
		return state;
	}

	public void setState(ProcessState state) {
		this.state = state;
	}
	
	public int getNumProcesses() {
		return N;
	}

	public void setNumProcesses(int n) {
		N = n;
	}

}
