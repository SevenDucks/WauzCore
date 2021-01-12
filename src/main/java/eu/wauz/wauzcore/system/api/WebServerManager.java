package eu.wauz.wauzcore.system.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.google.common.io.Files;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import eu.wauz.wauzcore.skills.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.SystemAnalytics;
import eu.wauz.wauzcore.system.WauzDebugger;
import eu.wauz.wauzcore.system.achievements.WauzAchievement;
import eu.wauz.wauzcore.system.quests.WauzQuest;

/**
 * Starts a HTTP server, that can deliver game information over an api.
 * Suppresses restriction warnings from sun httpserver classes.
 * List of valid requests:</br>
 * /get/stats <b>General gameplay statistics</b>
 * 
 * @author Wauzmons
 */
@SuppressWarnings("restriction")
public class WebServerManager implements HttpHandler {

	/**
	 * The HTTP server.
	 */
	private HttpServer server;
	
	/**
	 * Creates and starts the web server on given port.
	 * 
	 * @param port The port for web requests.
	 */
	public WebServerManager(int port) {
		try {
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext("/get/resourcepack", this);
			server.createContext("/get/system", this);
			server.createContext("/get/stats", this);
			server.createContext("/get/staff", this);
			server.createContext("/", this);
			server.setExecutor(null);
			server.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the web server.
	 */
	public void stop() {
		server.stop(0);
	}

	/**
	 * Handles incoming requests.
	 * 
	 * @param httpExchange The encapsulated HTTP request.
	 */
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		
		if(path.equals("/get/resourcepack")) {
			sendResourcepack(httpExchange);
		}
		else if(path.equals("/get/system")) {
			sendSystemAnalytics(httpExchange);
		}
		else if(path.equals("/get/stats")) {
			sendStats(httpExchange);
		}
		else if(path.equals("/get/staff")) {
			sendStaff(httpExchange);
		}
		else {
			sendOverview(httpExchange);
		}
	}
	
	/**
	 * Sends a response to the request, containing the resourcepack from the data folder.
	 * 
	 * @param httpExchange The encapsulated HTTP request.
	 */
	private static void sendResourcepack(HttpExchange httpExchange) {
		try {
			File resourcepack = new File(WauzCore.getInstance().getDataFolder(), "Resourcepack.zip");
			sendFileResponse(httpExchange, resourcepack);
		}
		catch (Exception e) {
			WauzDebugger.catchException(WebServerManager.class, e);
		}
	}
	
	/**
	 * Sends a response to the request, containing system info and usage. 
	 * 
	 * @param httpExchange The encapsulated HTTP request.
	 */
	private static void sendSystemAnalytics(HttpExchange httpExchange) {
		try {
			String response = "";
			SystemAnalytics systemAnalytics = new SystemAnalytics();
			response += systemAnalytics.getServerTime() + "\r\n";
			response += systemAnalytics.getSystemArchitecture() + "\r\n";
			response += systemAnalytics.getPlayersOnline() + "\r\n";
			response += systemAnalytics.getCpuUsage() + "\r\n";
			response += systemAnalytics.getRamUsage() + "\r\n";
			response += systemAnalytics.getSsdUsage() + "\r\n";
			sendTextResponse(httpExchange, response, false);
		}
		catch(Exception e) {
			WauzDebugger.catchException(WebServerManager.class, e);
		}
	}

	/**
	 * Sends a response to the request, containing general gameplay statistics.
	 * 
	 * @param httpExchange The encapsulated HTTP request.
	 */
	private static void sendStats(HttpExchange httpExchange) {
		try {
			String response = "";
			response += "[" + 229 + " Square km Map to Explore]\r\n";
			response += "[" + StatisticsFetcher.getTotalCustomEntitiesString() + " Unique Custom Entities]\r\n";
			response += "[" + StatisticsFetcher.getTotalPlayersString() + " Registered Players]\r\n";
			response += "[" + StatisticsFetcher.getTotalPlaytimeDaysString() + " Days of Total Playtime]\r\n";
			response += "[" + WauzQuest.getQuestCount() + " Quests to Complete]\r\n";
			response += "[" + WauzAchievement.getAchievementCount() + " Achievements to Collect]\r\n";
			response += "[" + WauzEquipmentIdentifier.getEquipmentTypeCount() + " Types of Equipment]\r\n";
			response += "[" + WauzPlayerSkillExecutor.getSkillTypesCount() + " Types of Combat Skills]\r\n";
			sendTextResponse(httpExchange, response, false);
		}
		catch(Exception e) {
			WauzDebugger.catchException(WebServerManager.class, e);
		}
	}
	
	/**
	 * Sends a response to the request, containing a list of all staff members.
	 * 
	 * @param httpExchange The encapsulated HTTP request.
	 */
	private static void sendStaff(HttpExchange httpExchange) {
		try {
			String response = "";
			for(String staffString : StatisticsFetcher.getStaffMemberList()) {
				response += "[" + staffString + "]\r\n";
			}
			sendTextResponse(httpExchange, response, false);
		}
		catch(Exception e) {
			WauzDebugger.catchException(WebServerManager.class, e);
		}
	}
	
	/**
	 * Sends a response to the request, containing links to the other requests.
	 * 
	 * @param httpExchange The encapsulated HTTP request.
	 */
	private static void sendOverview(HttpExchange httpExchange) {
		try {
			String response = "";
			response += "<html><head></head><body>";
			response += "<a href=\"/get/resourcepack\">/get/resourcepack<a/></br>";
			response += "<a href=\"/get/system\">/get/system<a/></br>";
			response += "<a href=\"/get/stats\">/get/stats<a/></br>";
			response += "<a href=\"/get/staff\">/get/staff<a/></br>";
			response += "</body></html>";
			sendTextResponse(httpExchange, response, true);
		}
		catch (IOException e) {
			WauzDebugger.catchException(WebServerManager.class, e);
		}
	}

	/**
	 * Sends the given text response to the request.
	 * 
	 * @param httpExchange The encapsulated HTTP request.
	 * @param response The response to send.
	 * @param isHtml If the content type is a HTML.
	 * 
	 * @throws IOException Error while writing response.
	 */
	private static void sendTextResponse(HttpExchange httpExchange, String response, boolean isHtml) throws IOException {
		Headers headers = httpExchange.getResponseHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Content-Type", isHtml ? "text/html" : "text/plain");
		
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(response.getBytes());
		outputStream.close();
	}
	
	/**
	 * Sends the given zip file as response to the request.
	 * 
	 * @param httpExchange The encapsulated HTTP request.
	 * @param response The response to send.
	 * 
	 * @throws IOException Error while writing response.
	 */
	private static void sendFileResponse(HttpExchange httpExchange, File response) throws IOException {
		Headers headers = httpExchange.getResponseHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Content-Type", "application/zip");
		headers.add("Content-Disposition", "attachment; filename=" + response.getName());
		
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(Files.toByteArray(response));
		outputStream.close();
	}
	
}
