package eu.wauz.wauzcore.system.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.StringUtils;

import eu.wauz.wauzcore.data.ServerConfigurator;

/**
 * An user manager for the FTP server.
 * 
 * @author Wauzmons
 * 
 * @see FtpServerManager
 */
public class FtpUserManager implements UserManager {
	
	/**
	 * The base directory of the server.
	 */
	private static final String BASE_DIR = Bukkit.getWorldContainer().getAbsolutePath();
	
	/**
	 * A map of all users, indexed by username.
	 */
	private Map<String, User> userMap = new HashMap<>();
	
	/**
	 * Creates an user manager to manage the FTP users from the Server.yml file.
	 * 
	 * @param name The name of the user.
	 * 
	 * @see ServerConfigurator#getServerFtpUsers()
	 */
	public FtpUserManager() {
		for(String username : ServerConfigurator.getServerFtpUsers()) {
			BaseUser user = new BaseUser();
			user.setName(username);
			user.setEnabled(true);
			user.setMaxIdleTime(1800);
			user.setAuthorities(Arrays.asList(new WritePermission(), new ConcurrentLoginPermission(3, 3)));
			user.setPassword(ServerConfigurator.getFtpUserPassword(username));
			user.setHomeDirectory(BASE_DIR + ServerConfigurator.getFtpUserHomePath(username));
			userMap.put(username, user);
		}
	}

	@Override
	public User getUserByName(String username) throws FtpException {
		return userMap.get(username);
	}

	@Override
	public String[] getAllUserNames() throws FtpException {
		return (String[]) userMap.keySet().toArray();
	}

	@Override
	public void delete(String username) throws FtpException {
		throw new FtpException("Can't edit virtual users!");
	}

	@Override
	public void save(User user) throws FtpException {
		throw new FtpException("Can't edit virtual users!");
	}

	@Override
	public boolean doesExist(String username) throws FtpException {
		return userMap.containsKey(username);
	}

	@Override
	public User authenticate(Authentication authentication) throws AuthenticationFailedException {
		if(!(authentication instanceof UsernamePasswordAuthentication)) {
			throw new AuthenticationFailedException("Invalid authentication method!");
		}
		UsernamePasswordAuthentication auth = (UsernamePasswordAuthentication) authentication;
		User user = userMap.get(auth.getUsername());
		if(user == null) {
			throw new AuthenticationFailedException("Invalid username!");
		}
		if(!StringUtils.equals(user.getPassword(), auth.getPassword())) {
			throw new AuthenticationFailedException("Invalid password!");
		}
		return user;
	}

	@Override
	public String getAdminName() throws FtpException {
		return null;
	}

	@Override
	public boolean isAdmin(String username) throws FtpException {
		return false;
	}

}
