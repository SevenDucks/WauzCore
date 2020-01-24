package eu.wauz.wauzcore.system.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import eu.wauz.wauzcore.items.identifiers.WauzEquipmentIdentifier;
import eu.wauz.wauzcore.skills.execution.WauzPlayerSkillExecutor;
import eu.wauz.wauzcore.system.SystemAnalytics;
import eu.wauz.wauzcore.system.WauzQuest;
import eu.wauz.wauzcore.system.achievements.WauzAchievement;

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
			server.createContext("/get/system", this);
			server.createContext("/get/stats", this);
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
		
		if(path.equals("/get/system")) {
			sendSystemAnalytics(httpExchange);
		}
		if(path.equals("/get/stats")) {
			sendStats(httpExchange);
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
			response += systemAnalytics.getSystemArchitecture() + "\r\n";
			response += systemAnalytics.getPlayersOnline() + "\r\n";
			response += systemAnalytics.getCpuUsage() + "\r\n";
			response += systemAnalytics.getRamUsage() + "\r\n";
			response += systemAnalytics.getSsdUsage() + "\r\n";
			sendTextResponse(httpExchange, response);
		}
		catch(Exception e) {
			e.printStackTrace();
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
			response += "[" + 229 + " Sqaure km Map to Explore]\r\n";
			response += "[" + StatisticsFetcher.getTotalCustomEntitiesString() + " Unique Custom Entities]\r\n";
			response += "[" + StatisticsFetcher.getTotalPlayersString() + " Registered Players]\r\n";
			response += "[" + StatisticsFetcher.getTotalPlaytimeDaysString() + " Days of Total Playtime]\r\n";
			response += "[" + WauzQuest.getQuestCount() + " Quests to Complete]\r\n";
			response += "[" + WauzAchievement.getAchievementCount() + " Achievements to Collect]\r\n";
			response += "[" + WauzEquipmentIdentifier.getEquipmentTypeCount() + " Types of Equipment]\r\n";
			response += "[" + WauzPlayerSkillExecutor.getSkillTypesCount() + " Types of Combat Skills]\r\n";
			sendTextResponse(httpExchange, response);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends the given text response to the request.
	 * 
	 * @param httpExchange The encapsulated HTTP request.
	 * @param response The response to send.
	 * 
	 * @throws IOException
	 */
	private static void sendTextResponse(HttpExchange httpExchange, String response) throws IOException {
		Headers headers = httpExchange.getResponseHeaders();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Content-Type", "text/plain");
		
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream outputStream = httpExchange.getResponseBody();
		outputStream.write(response.getBytes());
		outputStream.close();
	}
	
}
