package com.savvy7.PlantUmlService;

import java.net.URI;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.nio.charset.Charset;

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;

public class Server {
  public static void main(String[] args) throws Exception {
    final int port = parseIntDefault(System.getenv("PORT"), 8080);
    final HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    System.out.println(String.format("Starting server at port %d", port));

    server.createContext("/plantuml", new RequestHandler());
    server.setExecutor(null);

    server.start();
  }

  private static int parseIntDefault(String str, int defaultValue) {
    int result = defaultValue;

    if(str == null) {
      return result;
    }

    try {
      result = Integer.parseInt(str);
    }
    catch (NumberFormatException ex) {
    }

    return result;
  }

  static class RequestHandler implements HttpHandler {
    private final static Logger LOGGER = Logger.getLogger(RequestHandler.class.getName());

    @Override
    public void handle(HttpExchange http) throws IOException {
      final URI uri = http.getRequestURI();
      final Map <String,String>parms = Helpers.queryToMap(uri.getQuery());

      String source = parms.get("source");

      if (source.indexOf("@startuml") < 0 && source.indexOf("@enduml") < 0) {
        source = "@startuml\n" + source + "\n@enduml";
      }

      if (source == null || source.length() == 0) {
        Helpers.sendResponse(http, 400,
          "Use /plantuml/svg?source=@startuml%0A...%0A@enduml\n");

        return;
      }

      FileFormat format = Helpers.formatFromUri(uri);

      if (format == null) {
        Helpers.sendResponse(http, 400, "Need svg, png or txt as format\n");

        return;
      }

      final SourceStringReader reader = new SourceStringReader(source);
      final ByteArrayOutputStream os = new ByteArrayOutputStream();
      final String desc = reader.generateImage(os, new FileFormatOption(format));

      os.close();

      Helpers.sendResponse(http, 400, os.toByteArray());
    }
  }

  static class Helpers {
    public static Map<String, String> queryToMap(String query) {
      final Map<String, String> result = new HashMap<String, String>();

      for (String param : query.split("&")) {
          final String pair[] = param.split("=");

          if (pair.length > 1) {
              result.put(pair[0], pair[1]);
          }
          else {
              result.put(pair[0], "");
          }
      }

      return result;
    }

    public static void sendResponse(HttpExchange http, int result, String data)
      throws IOException
    {
      sendResponse(http, result, data.getBytes());
    }

    public static void sendResponse(HttpExchange http, int result, byte[] data)
      throws IOException
    {
      final OutputStream reply = http.getResponseBody();

      http.sendResponseHeaders(result, data.length);

      reply.write(data);
      reply.close();
    }

    public static FileFormat formatFromUri(URI uri) {
      final String[] pathElements = uri.getPath().split("/");

      if (pathElements.length != 3) {
        return null;
      }
      else {
        final String format = pathElements[2];

        if (format.equals("svg")) {
          return FileFormat.SVG;
        }
        else if (format.equals("png")) {
          return FileFormat.PNG;
        }
        else if (format.equals("txt") || format.equals("atxt") || format.equals("text")) {
          return FileFormat.ATXT;
        }

        return null;
      }
    }
  }
}
