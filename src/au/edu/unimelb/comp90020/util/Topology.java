package au.edu.unimelb.comp90020.util;

import java.io.*;
import java.util.*;

/*
 * We first read the topology of the underlying network. This is done by the method readNeighbors in the class
 *  Topology. The list of neighbors of Pi are assumed to be enumerated in the file “topologyi” 
 *  If such a file is not found, then it is assumed that all other processes are neighbors.
 */
public class Topology {
	public static void readNeighbors(int myId, int N, IntLinkedList neighbors) {
		Util.println("Reading topology");
		try {
			BufferedReader dIn = new BufferedReader(new FileReader("topology"
					+ myId));
			StringTokenizer st = new StringTokenizer(dIn.readLine());
			while (st.hasMoreTokens()) {
				int neighbor = Integer.parseInt(st.nextToken());
				neighbors.add(neighbor);
			}
		} catch (FileNotFoundException e) {
			for (int j = 0; j < N; j++)
				if (j != myId)
					neighbors.add(j);
		} catch (IOException e) {
			System.err.println(e);
		}
		Util.println(neighbors.toString());
	}
}
