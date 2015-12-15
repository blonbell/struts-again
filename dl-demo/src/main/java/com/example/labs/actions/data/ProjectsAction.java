/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.example.labs.actions.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.json.annotations.JSON;

import com.example.labs.mongo.MongoDBConnection;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.opensymphony.xwork2.ActionSupport;

/**
 * <code>List Apache projects.</code>
 */
@Result(type = "json")
public class ProjectsAction extends ActionSupport {

    private static final long serialVersionUID = 9037336532369476225L;
    private static final Logger LOG = LogManager.getLogger(ProjectsAction.class);
    private MongoDBConnection connection;
    
    private String fields;
    private Date startDate;
    private Date endDate;
    private String turbine;
    
    private int success;
    private String error;
    private List<Object> data;
    
    public String execute() throws Exception {
    	connection = MongoDBConnection.getInstance();
    	connection.connect("turbines");
    	DBCollection turbineData = connection.getMongoCollection("turbineData");
    	DBObject searchQuery = new BasicDBObject();
    	searchQuery.put("date", BasicDBObjectBuilder.start("$gte", startDate).add("$lte", endDate).get());
    	
    	DBObject keys = new BasicDBObject();
    	keys.put("date", "1");
    	
    	
    	
    	//from query param
    	if (StringUtils.isEmpty(fields)) {
    		success = 0;
    		error = "Please provide valid fields";
    		return ERROR;
    	}
    	
    	
    	
    	String[] fieldKeys = fields.split(",");
    	for (String k : fieldKeys) {
    		keys.put(k, "1");
    	}
    	
    	DBCursor cursor = turbineData.find(searchQuery, keys);
    	DBObject sortTime = new BasicDBObject();
    	
    	sortTime.put("date", 1);
    	cursor.sort(sortTime);
    	List<DBObject> dataPoints = cursor.toArray();
    	
    	LOG.debug("Number of data points: " + dataPoints.size());
    	data = new ArrayList<Object>();
    	for (DBObject dataP : dataPoints) {
    		data.add(dataP);
    	}
    	success = 1;
        return SUCCESS;
    }

    @JSON(serialize = false)
	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	@JSON(serialize = false)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JSON(serialize = false)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@JSON(serialize = false)
	public String getTurbine() {
		return turbine;
	}

	public void setTurbine(String turbine) {
		this.turbine = turbine;
	}


	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}
}
