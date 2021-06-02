package eu.wauz.wauzcore.system.api;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;

/**
 * Manages an embedded FTP server to transfer game files.
 * 
 * @author Wauzmons
 */
public class FtpServerManager {
	
	/**
	 * The FTP server.
	 */
	private FtpServer server;
	
	/**
	 * Creates and starts the file server on the given port.
	 * 
	 * @param port The port for connections.
	 */
	public FtpServerManager(int port) {
		try {
			FtpServerFactory serverFactory = new FtpServerFactory();
			ListenerFactory listenerFactory = new ListenerFactory();
			listenerFactory.setPort(port);
			serverFactory.addListener("default", listenerFactory.createListener());
			serverFactory.setUserManager(new FtpUserManager());
			server = serverFactory.createServer();
			server.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the file server.
	 */
	public void stop() {
		server.stop();
	}

}
