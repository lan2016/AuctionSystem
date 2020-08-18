package com.auction.jetty;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;

import com.auction.cachecontroller.AuctionCacheController;
import com.auction.constants.AuctionConstants;
import com.jio.handlers.AuctionHandler;

public class AuctionJettyServer {
	// private static Logger logger =
	// LogManager.getLogger(AuctionJettyServer.class);

	public static void startJetty() {
		//logger.info("*******Going to start jetty server*************");
		try {
			Server server = new Server();
			ServerConnector connector = new ServerConnector(server);

			connector.setHost(AuctionCacheController.getIp());

			connector.setPort(Integer.parseInt(AuctionCacheController.getPort()));
			server.setConnectors(new Connector[] { connector });

			ContextHandlerCollection contexts = new ContextHandlerCollection();
			HandlerCollection handler = new HandlerCollection();

			ServletContextHandler contexthandler = new ServletContextHandler();
			contexthandler.setContextPath(AuctionConstants.CONTEXT);

			contexthandler.addServlet(AuctionHandler.class, "");
			contexthandler.setAllowNullPathInfo(true);

			contexts.addHandler(contexthandler);

			handler.setHandlers(new Handler[] { contexts, new DefaultHandler() });
			server.setHandler(handler);

			try {
				server.start();
				// logger.info("-----------server started----------Es Handler
				// Up---------------------");
				System.out.println(" Jetty Server Started");
			} catch (Exception e) {
				// logger.error("exception",e);
				System.out.println(e);
			}

		} catch (Exception e) {
			// logger.error("exception",e);
			e.printStackTrace();
			System.out.println("Server not started");

		}

	}

}
