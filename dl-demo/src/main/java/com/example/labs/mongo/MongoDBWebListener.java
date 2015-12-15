package com.example.labs.mongo;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mongodb.MongoClient;

@WebListener
public class MongoDBWebListener implements ServletContextListener {
	
	private MongoClient mongo;
	private static final Logger LOG = LogManager.getLogger(MongoDBWebListener.class);
	
	public void contextDestroyed(ServletContextEvent sce) {
		mongo.close();
		LOG.debug("Closed mongo connection");
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		String mongoHost = ctx.getInitParameter("MONGODB_HOST"),
			   mongoPort = ctx.getInitParameter("MONGODB_PORT");
		try {
            mongo = new MongoClient(mongoHost, Integer.parseInt(mongoPort));
            
            LOG.debug("Opened mongo connection at " + mongoHost + ":" + mongoPort);
            
            for (String dbName : mongo.getDatabaseNames()) {
            	LOG.debug("DB: " + dbName);
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException("MongoClient init failed");
        }
	}
}
