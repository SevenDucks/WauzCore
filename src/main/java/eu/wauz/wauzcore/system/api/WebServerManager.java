package eu.wauz.wauzcore.system.api;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

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
			response += StatisticsFetcher.getTotalCustomEntitiesString() + ";";
			response += StatisticsFetcher.getTotalPlayersString() + ";";
			response += StatisticsFetcher.getTotalPlaytimeDaysString() + ";";
			
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
