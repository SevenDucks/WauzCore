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
import eu.wauz.wauzcore.system.WauzQuest;

@SuppressWarnings("all")
public class WebServerManager implements HttpHandler {

	private HttpServer server;
	
	public WebServerManager(int port) {
		try {
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext("/get/stats", this);
			server.setExecutor(null);
			server.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void stop() {
		server.stop(0);
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		
		if(path.equals("/get/stats")) {
			sendWebappStats(httpExchange);
		}
	}

	private static void sendWebappStats(HttpExchange httpExchange) {
		try {
			String response = "";
			response += 229 + " Sqaure km Map to Explore;\r\n";
			response += StatisticsFetcher.getTotalCustomEntitiesString() + " Unique Custom Entities;\r\n";
			response += StatisticsFetcher.getTotalPlayersString() + " Registered Players;\r\n";
			response += StatisticsFetcher.getTotalPlaytimeDaysString() + " Days of Total Playtime;\r\n";
			response += WauzQuest.getQuestCount() + " Quests to Complete ;\r\n";
			response += 0 + " Achievements to Collect;\r\n";
			response += WauzEquipmentIdentifier.getEquipmentTypeCount() + " Types of Equipment;\r\n";
			response += WauzPlayerSkillExecutor.getSkillTypesCount() + " Types of Combat Skills;\r\n";
			
			Headers headers = httpExchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Content-Type", "text/plain");
			
			httpExchange.sendResponseHeaders(200, response.length());
			OutputStream outputStream = httpExchange.getResponseBody();
			outputStream.write(response.getBytes());
			outputStream.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
