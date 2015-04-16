package au.edu.unimelb.comp90020.server;

import java.net.*;
import java.io.*;
import java.util.*;

import au.edu.unimelb.comp90020.util.Symbols;

/*
 * The name server maintains a table of (name, hostName, portNumber) to give a mapping from a process name 
 * to the host and the port number. For simplicity, we assume that the maximum size of the table is 100 
 * and that, there are only two operations on the table: insert and search. This table is kept by the object NameTable
 *  
 * We call getInputstream and getOutputStream to get input and output, streams associated with the socket. 
 * Now we can simply use all methods associated for reading and writing input streams to read and write data from the socket.
 * 
 * At most one client is handled at a time. Once a request is handled, the main loop of the name server accepts another connection.
 */
public class NameServer {
	
	NameTable table;

	public NameServer() {
		this.table = new NameTable();
	}

	void handleclient(Socket theClient) {
		try {
			BufferedReader din = new BufferedReader(new InputStreamReader(theClient.getInputStream()));
			PrintWriter pout = new PrintWriter(theClient.getOutputStream());
			String getline = din.readLine();
			StringTokenizer st = new StringTokenizer(getline);
			String tag = st.nextToken();
			
			if (tag.equals("search")) {
				int index = table.search(st.nextToken());
				if (index == -1) // not found
					pout.println(-1 + " " + "nullhost");
				else
					pout.println(table.getPort(index) + " "+ table.getHostName(index));
			} else if (tag.equals("insert")) {
				String name = st.nextToken();
				String hostName = st.nextToken();
				int port = Integer.parseInt(st.nextToken());
				int retValue = table.insert(name, hostName, port);
				pout.println(retValue);
			}
			pout.flush();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/*
	 * The name server creates a server socket with the specified port. It then listens to any incoming connections by the 
	 * method accept. The accept method returns the socket whenever a connection is made. It then handles the request that 
	 * arrives on that socket by the method handleClient.
	 */
	public static void main(String[] args) {
		NameServer ns = new NameServer();
		System.out.println("NameServer started:");
		try {
			ServerSocket listener = new ServerSocket(Symbols.SERVER_PORT);
			while (true) {
				Socket aClient = listener.accept();
				ns.handleclient(aClient);
				aClient.close();
			}
		} catch (IOException e) {
			System.err.println("Server aborted:" + e);
		}
	}
}
