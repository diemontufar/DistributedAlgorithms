package au.edu.unimelb.comp90020.server;

/*
 * For simplicity, we assume that the maximum size of the table is 100 
 * and that there are only two operations on the table: insert and search.
 */
public class NameTable {
	
	final int maxSize = 100;
	private String[] names = new String[maxSize];
	private String[] hosts = new String[maxSize];
	private int[] ports = new int[maxSize];
	private int dirsize = 0;

	public int search(String name) {
		for (int i = 0; i < dirsize; i++)
			if (names[i].equals(name))
				return i;
		return -1;
	}

	public int insert(String name, String hostName, int portNumber) {
		int oldIndex = search(name); // is it already there
		if ((oldIndex == -1) && (dirsize < maxSize)) {
			names[dirsize] = name;
			hosts[dirsize] = hostName;
			ports[dirsize] = portNumber;
			dirsize++;
			return 1;
		} else
			// already there, or table full
			return 0;
	}

	public int getPort(int index) {
		return ports[index];
	}

	public String getHostName(int index) {
		return hosts[index];
	}
}
