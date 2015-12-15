package com.example.labs.mongo;

import java.net.UnknownHostException;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDBConnection {

	private static MongoDBConnection instance = null;
	private static final Logger LOG = LogManager.getLogger(MongoDBWebListener.class);
	private MongoClient cli;
	private DB usingDatabase;

	
	public MongoDBConnection(ServletContext ctx) {
		String mongoHost = ctx.getInitParameter("MONGODB_HOST"),
			   mongoPort = ctx.getInitParameter("MONGODB_PORT");
		
		usingDatabase = null;
		
		try {
			cli = new MongoClient(mongoHost, Integer.valueOf(mongoPort));
		} catch (NumberFormatException e) {
			LOG.error("Cannot establish connection, MONGODB_PORT is not a number");
			throw new RuntimeException("MongoClient init failed", e);
		} catch (UnknownHostException e) {
			LOG.error("Unknown host");
			throw new RuntimeException("MongoClient init failed", e);
		}
	}
	
	public static MongoDBConnection getInstance() {
		if (instance == null) {
			instance = new MongoDBConnection(ServletActionContext.getServletContext());
		}
		return instance;
	}
	
	public void connect(String databaseName) {
		usingDatabase = cli.getDB(databaseName);
	}
	
	public DBCollection getMongoCollection(String collectionName) {
		if (usingDatabase == null) {
			LOG.error("You are not connected to a database yet");
			return null;
		}	
		return usingDatabase.getCollection(collectionName);
	}
	
}
