$.urlParam = function(name){
    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
    if (results==null){
       return null;
    }
    else {
        return results[1] || 0;
    }
}

var gameId = $.urlParam('id');
var hangmanimg = document.getElementById("hangmanimg");
var wordSoFarContent = document.getElementById("wordSoFarContent") ;

$( document ).ready(function() {
    $.get( "/hangman/api/game/status/"+gameId, function( data ) {
        hangmanimg.setAttribute("src", "img/" + data.wrongGuesses + ".png");
        wordSoFarContent.innerHTML = data.wordSoFar;
    });
});

(function poll(){
    setTimeout(function() {
        $.ajax({ url: "/hangman/api/game/status/"+gameId, success: function(data){
            //console.debug("%o", data.wrongGuesses);
            hangmanimg.setAttribute("src", "img/" + data.wrongGuesses + ".png");
            wordSoFarContent.innerHTML = data.wordSoFar;
        }, dataType: "json", complete: poll, timeout: 30000 });
    }, 2000);
})();

$('#guessButton').click(function(){
    var guessCharVal = $('#guessChar').val();
    $.post("/hangman/api/game/guess", {game: gameId, character:guessCharVal},
    function(data, status){
        console.log("%o", data);
    });
});