var app = angular.module('hangmanApp', []);
app.controller('hangmanCtrl', function($scope, $http) {
    $http.get("/hangman/api/game/status").then(function (response) {
        $scope.gameData = response.data;
    });
});

$('#hangmanButton').click(function(){
    var wordForGameVal = $('#wordForGame').val();
    $.post("/hangman/api/game/start", {wordForGame: wordForGameVal},
    function(data, status){
        var jsonData = JSON.parse(data);
        window.location.href = "game.html?id="+jsonData.game;
    });
});