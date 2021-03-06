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
angularStrutsApp.controller('AppController', function ($scope) {  });

angularStrutsApp.controller('HomeController', function ($scope) {
    $scope.name = "Sunshine";
});

angularStrutsApp.controller('ApacheProjectsController', function ($scope, $http, DataService) {
    this.init = function() {
        DataService.getProjects().then(function(data) {
        	console.log(data.data);
            $scope.projects = data.data.projectNames;
        }, function(data) {
            console.log('Could not receive project names.')
        });
    };

    this.init();
});
