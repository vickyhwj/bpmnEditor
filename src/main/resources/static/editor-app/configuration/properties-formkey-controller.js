/*
 * Activiti Modeler component part of the Activiti project
 * Copyright 2005-2014 Alfresco Software, Ltd. All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

/*
 * Assignment
 */
var KisBpmFormkeyCtrl = [ '$scope', '$modal', '$http', '$rootScope',function($scope, $modal, $http,$rootScope) {
	
	console.log("KisBpmFormkeyCtrl",$rootScope);


	var opts = {
	    template:  'editor-app/configuration/properties/formkey-popup.html?version=' + Date.now(),
	    scope: $scope
	};
	
	// Open the dialog
	$modal(opts);
	
	 $scope.$on('versionidChange', function (event, versionid) {
	    	console.log('versionidChange');
	    	alert('versionidChange');
	    	$scope.property.value = versionid;
	    });


	
	
}];

var KisBpmFormkeyPopupCtrl = [ '$scope', '$modal', function($scope, $modal) {
	
 
    // Close button handler
    $scope.close = function() {
    	alert('close');
    	console.log('close')
    	$scope.property.mode = 'read';
    	$scope.$hide();
    };
    $scope.formkey=$scope.property.value;
    
    $scope.say=function(){
    	console.log($scope.$parent.property);
    	alert(123);
    	$scope.property.value = {};
	    
	    $scope.property.value = $scope.formkey;
	        
	    $scope.updatePropertyInModel($scope.property);
	    $scope.close();

    }
    
    $scope.$on('versionidChange', function (event, versionid) {
    	console.log('fkversionidChange');
    	alert('fkversionidChange');
    	$scope.property.value = versionid;
    });

    
   
}];

