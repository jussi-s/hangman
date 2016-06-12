# Hangman
This repository contains an implementation of the [Hangman game] (https://en.wikipedia.org/wiki/Hangman_(game)) using Java, Jersey, MongoDB and Spring.

## Architecture
The game is implemented as a traditional web application, in which the client (Web Browser) interacts with the backend using its REST API.

![Hangman Game Architecture](https://cloud.githubusercontent.com/assets/7324176/15991723/fb2d8ebc-30c3-11e6-8579-46ffdf235816.png)

## Data Model
The game data is stored in a MongoDB database. The document format is the following (using the word CAT as an example):

```json
{
  "word": "CAT",
  "wordSoFar": "CAT",
  "wrongGuesses": 0,
  "wordCharMap": {
    "A": [
      1
    ],
    "C": [
      0
    ],
    "T": [
      2
    ]
  },
  "guessedChars": [
    "A",
    "C",
    "T"
  ]
}
```

Where:

- **word** - the word for the game
- **wordSoFar** - the word that includes the characters guessed so far
- **wrongGuesses** - amount of wrong guesses (max. 10)
- **wordCharMap** - map of characters and their indices in the word, used when guessing a character
- **guessedChars** - characters guessed so far

## REST API Endpoints
| Method        | Endpoint      | Payload                     | Usage                                | Returns                     |
|:------------- |:------------- |:--------------------------- |:-------------------------------------|:----------------------------|
| POST          | /game/start   | wordForGame: word           | Start a new game with the given word | JSON containing the game ID |
| POST          | /game/guess   | game: gameId, character: ch | Guess a character for the given game | Guess Status (correct/incorrect/game complete/already guessed)|
| GET           | /game/status  | N/A                         | Get status of all games              | JSON containing status of each game|
| GET          | /game/status/{id} | N/A                       | Get status for a given game          | JSON containing status of the game |
