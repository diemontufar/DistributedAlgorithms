package au.edu.unimelb.comp90020.client;

import java.io.*;

import au.edu.unimelb.comp90020.net.Msg;
import au.edu.unimelb.comp90020.net.MsgHandler;

public class ListenerThread extends Thread {
	int channel;
	MsgHandler process;

	public ListenerThread(int channel, MsgHandler process) {
		this.channel = channel;
		this.process = process;
	}

	public void run() {
		while (true) {
			try {
				Msg m = process.receiveMsg(channel);
				process.handleMsg(m, m.getSrcId(), m.getTag());
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
}
