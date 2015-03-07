angular.module('webmonitor').controller('logCtrl',
    [ '$scope', 'gracefulLoader', '$state', 'ngDialog', function($scope, gracefulLoader, $state, ngDialog) {
      $scope.changeLogLevel = function() {
        if ($scope.level) {
          var payload = {
            level : $scope.level
          };
          gracefulLoader.post('../../rest/log/change', payload, function(data) {
            if (data == true) {
              ngDialog.open({
                template : 'templates/log-level-changed.html'
              });
            }
          });
        }
      }
    } ]);