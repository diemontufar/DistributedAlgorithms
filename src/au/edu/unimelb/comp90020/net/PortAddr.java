package au.edu.unimelb.comp90020.net;

public class PortAddr {
    
	String hostname;
    int portnum;
    
    public PortAddr(String host, int port) {
        hostname = new String(host);
        portnum = port;
    }
    public String getHostName() {
        return hostname;
    }
    public int getPort() {
        return portnum;
    }
}

