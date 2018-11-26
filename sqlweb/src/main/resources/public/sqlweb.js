var app = angular.module('myApp', []);
app.controller('formCtrl', function($scope, $http) {
	$scope.output_msg = null;
	$scope.queryString = '';
	$scope.dsMetaData = null;
	$scope.rsData = null;
	$scope.history = [];
	$scope.maxHistory = 50;

	var header_config = {headers: {'Content-Type': 'application/json'}}
	
	$http.get("dataSources")
    	.then(function(response) {
    		$scope.dsData = response.data; 
    });
	
	$scope.getMetaData = function() {
		if ($scope.dataSource == undefined || $scope.dataSource == '') {
			return false;
		}
		$http({
			method: 'POST',
			url: 'dataSourceInfo',
			data:  {dataSource: $scope.dataSource},
		 	config: header_config
		}).then(
			function(response) {
				alert(response.data);
				$scope.dsMetaData = response.data;
				$scope.output_msg = null;
			},
			function(response) {
				$scope.output_msg = "Service unavailable. Please try again.";
				$scope.dsMetaData = null;
			}
		);
	};
	
	$scope.getResult = function() {
		if ($scope.dataSource == undefined || $scope.dataSource == '') {
			return false;
		}
		if ($scope.queryString == undefined || $scope.queryString == '') {
			return false;
		}
		var histRec = {
				'dataSource': $scope.dataSource,
				'queryString': $scope.queryString,
				'status': 'error',
				'elapsedTime':0,
				'rowsEffected': 0
		};
		var startTime = Date.now();
		$http({
				method: 'POST',
				url: 'query',
				data:  {dataSource: $scope.dataSource, queryString:$scope.queryString},
			 	config: header_config
		}).then(
			function(response) {
				$scope.rsData = response.data;
				$scope.output_msg = null;
				if ($scope.rsData.statusCode == 100) {
					histRec.status = 'success';
					histRec.rowsEffected = response.data.resultCount;
				}
			},
			function(response) {
				$scope.output_msg = "Service unavailable. Please try again.";
				$scope.rsData = null;
			}
		);
		var elapsedTime = Date.now()-startTime;
		histRec.elapsedTime = elapsedTime;
		if ($scope.history.length >= $scope.maxHistory) {
			$scope.history.pop();
		}
		$scope.history.unshift(histRec);
	};
	
	$scope.getHistResult = function(ind) {
		var histRec = $scope.history[ind];
		var newhistRec = {
				'dataSource': histRec.dataSource,
				'queryString': histRec.queryString,
				'status': 'error',
				'elapsedTime':0,
				'rowsEffected': 0
		};
		var startTime = Date.now();
		$http({
				method: 'POST',
				url: 'query',
				data:  {dataSource: histRec.dataSource, queryString:histRec.queryString},
			 	config: header_config
		}).then(
			function(response) {
				$scope.rsData = response.data;
				$scope.output_msg = null;
				if ($scope.rsData.statusCode == 100) {
					newhistRec.status = 'success';
					newhistRec.rowsEffected = response.data.resultCount;
				}
			},
			function(response) {
				$scope.output_msg = "Service unavailable. Please try again.";
				$scope.rsData = null;
			}
		);
		var elapsedTime = Date.now()-startTime;
		newhistRec.elapsedTime = elapsedTime;
		if ($scope.history.length >= $scope.maxHistory) {
			$scope.history.pop();
		}
		$scope.history.unshift(newhistRec);
	};
});
app.filter('myFormat', function() {
    return function(x) {
        var txt = "";
        if (x == null) {
            return txt;
        } else {
        	return x;	
        }
    };
});

function openTab(evt, tabName) {
    var i;
    var x = document.getElementsByClassName("tabPanel");
    for (i = 0; i < x.length; i++) {
       x[i].style.display = "none";  
    }
    var tablinks = document.getElementsByClassName("tablink");
    for (i = 0; i < x.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" w3-light-grey", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " w3-light-grey";
}

function openResultTab(evt, tabName) {
    var i;
    var x = document.getElementsByClassName("rstabPanel");
    for (i = 0; i < x.length; i++) {
       x[i].style.display = "none";  
    }
    var tablinks = document.getElementsByClassName("rstablink");
    for (i = 0; i < x.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" w3-light-grey", "");
    }
    document.getElementById(tabName).style.display = "block";
    evt.currentTarget.className += " w3-light-grey";
}

function myFunction(obj) {
	var x = obj.nextSibling;
    if (x.className.indexOf("w3-show") == -1) {
        x.className += " w3-show";
    } else {
        x.className = x.className.replace(" w3-show", "");
    }
}


function resetContainers(){
	var w = window.innerWidth
	|| document.documentElement.clientWidth
	|| document.body.clientWidth;

	var h = window.innerHeight
	|| document.documentElement.clientHeight
	|| document.body.clientHeight;
	
}