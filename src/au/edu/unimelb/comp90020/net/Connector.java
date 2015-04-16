package au.edu.unimelb.comp90020.net;

import java.util.*;
import java.net.*;
import java.io.*;

import au.edu.unimelb.comp90020.util.Name;
import au.edu.unimelb.comp90020.util.Symbols;

/*
 * establishes connections between processes. Since processes may start at different times and at different locations, 
 * we use the Name Server to help processes locate each other. 
 * Any process Pi that starts up first creates a Serversocket for itself. It uses the Serversocket to listen for incoming 
 * requests for communication with all small numbered processes. It then contacts the Nameserver 
 * and inserts its entry in that table. All the smaller numbered processes wait for the entry of Pi to appear in the Nameserver. 
 * When they get the port number from the Nameserver,they use it to connect it to Pi,. Once Pj has established a TCP connection with 
 * all smaller number processes, it tries to connect with higher-number processes.
 */
public class Connector {

	ServerSocket listener;
	Socket[] link;

	public void Connect(String basename, int myId, int numProc,
			BufferedReader[] dataIn, PrintWriter[] dataOut) throws Exception {
		Name myNameclient = new Name();
		link = new Socket[numProc];
		int localport = getLocalPort(myId);
		listener = new ServerSocket(localport);

		/* register in the name server */
		myNameclient.insertName(basename + myId,
				(InetAddress.getLocalHost()).getHostName(), localport);

		/* accept connections from all the smaller processes */
		for (int i = 0; i < myId; i++) {
			Socket s = listener.accept();
			BufferedReader dIn = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			String getline = dIn.readLine();
			StringTokenizer st = new StringTokenizer(getline);
			int hisId = Integer.parseInt(st.nextToken());
			int destId = Integer.parseInt(st.nextToken());
			String tag = st.nextToken();
			if (tag.equals("hello")) {
				link[hisId] = s;
				dataIn[hisId] = dIn;
				dataOut[hisId] = new PrintWriter(s.getOutputStream());
			}
		}
		/* contact all the bigger processes */
		for (int i = myId + 1; i < numProc; i++) {
			PortAddr addr;
			do {
				addr = myNameclient.searchName(basename + i);
				Thread.sleep(100);
			} while (addr.getPort() == -1);
			link[i] = new Socket(addr.getHostName(), addr.getPort());
			dataOut[i] = new PrintWriter(link[i].getOutputStream());
			dataIn[i] = new BufferedReader(new InputStreamReader(
					link[i].getInputStream()));
			/* send a hello message to P_i */
			dataOut[i].println(myId + " " + i + " " + "hello" + " " + "null");
			dataOut[i].flush();
		}
	}

	int getLocalPort(int id) {
		return Symbols.SERVER_PORT + 10 + id;
	}

	public void closeSockets() {
		try {
			listener.close();
			for (int i = 0; i < link.length; i++)
				link[i].close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}
