package au.edu.unimelb.comp90020.net;

import java.util.*;
import java.io.*;

import au.edu.unimelb.comp90020.algorithm.Process.ProcessState;
import au.edu.unimelb.comp90020.util.IntLinkedList;
import au.edu.unimelb.comp90020.util.Topology;
import au.edu.unimelb.comp90020.util.Util;
/*
 * Links all the processes in the system.
 * We now show a java class Linker that, allows us to link a given set of processes with each other. 
 * Assume that we want to start n processes PI,P2,. ..,Pn, in a distributed system and establish connections 
 * between them such that any of the process can send and receive messages with any other process. 
 * We would like to support direct naming to send and receive messages; that is, processes are unaware of the 
 * host addresses and port numbers. 
 * They simply use process identifiers (1...n) to send and receive messages.
 * Once all the connections are established, the Linker provides methods to send and receive messages from process Pi to Pj.
 * We will require each message to contain at least four fields: source identifier, destination identifier, 
 * message type (or the message tag), and actual message.
 */
public class Linker {
	
	PrintWriter[] dataOut;
	BufferedReader[] dataIn;
	BufferedReader dIn;
	int myId, N;
	Connector connector;
	public IntLinkedList neighbors = new IntLinkedList();

	public Linker(String basename, int id, int numProc) throws Exception {
		this.myId = id;
		this.N = numProc;
		this.dataIn = new BufferedReader[numProc];
		this.dataOut = new PrintWriter[numProc];
		Topology.readNeighbors(myId, N, neighbors);
		this.connector = new Connector();
		this.connector.Connect(basename, myId, numProc, dataIn, dataOut);
	}

	public void sendMsg(int destId, String tag, String msg) {
		dataOut[destId].println(myId + " " + destId + " " + tag + " " + msg + "#");
		dataOut[destId].flush();
	}

	public void sendMsg(int destId, String tag) {
		sendMsg(destId, tag, " 0 ");
	}

	public void multicast(IntLinkedList destIds, String tag, String msg) {
		for (int i = 0; i < destIds.size(); i++) {
			sendMsg(destIds.getEntry(i), tag, msg);
		}
	}

	public Msg receiveMsg(int fromId) throws IOException {
		String getline = dataIn[fromId].readLine();
		Util.println(" Received message from: " + getline);
		StringTokenizer st = new StringTokenizer(getline);
		int srcId = Integer.parseInt(st.nextToken());
		int destId = Integer.parseInt(st.nextToken());
		String tag = st.nextToken();
		String msg = st.nextToken("#");
		return new Msg(srcId, destId, tag, msg);
	}

	public int getMyId() {
		return myId;
	}

	public int getNumProc() {
		return N;
	}

	public void close() {
		connector.closeSockets();
	}
}
