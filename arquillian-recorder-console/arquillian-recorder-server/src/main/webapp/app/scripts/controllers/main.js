'use strict';

var reportsPath = '/api/reports.json';

angular.module('arquillianReporterServerUiApp')

    .factory('Report', function($resource) {
        return $resource(reportsPath);
    })

  .controller('MainCtrl', function ($scope, Report) {
        $scope.showReport = false;

    $scope.reports = Report.get();

        $scope.selectReport = function(reportNumber) {
            $scope.showReport = true;
            $scope.activeReportNumber = reportNumber;
            $scope.activeReport = $scope.reports.report[reportNumber];
        }
  });
