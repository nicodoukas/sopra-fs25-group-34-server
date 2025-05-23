# SoPra FS25 Group 34 Server - Hitster

## Introduction
If you love music, then Hitster is the game for you!
Listen to the greatest hits of the past 80 years with your friends, 
and take turns arranging the songs in your timeline in chronological order, 
all while taking a trip down memory lane!
<br>
Hitster combines the joy of listening to (mostly) great music and competing with your friends, to see who is the queen/king of music trivia. 
And even if the music is not great, you can laugh about it with your friends.
<br>
We developed this digital version of Hitster so you can play with your friends, anywhere and anytime you want. 
Nobody needs to own the physical game, you don't need to meet up in person and 
by using a streaming service API, you are never going to run out of new songs to keep the game fresh.
This way, we want to make the game as accessible as possible, while still being able to add new features and improvements.
<br>
<br>
The rules are simple:
1. Every player has his own timeline, initiated with one year.
2. Play a snippet of a song for everyone to hear.
3. Everyone can guess title and artist of the song to earn a coin.
4. Place the song in the correct position in your timeline, based on its release year.
5. Other players can challenge your placement if they think it is incorrect and place the card at another
   position.
6. The player that placed the card at the correct position, wins the card and it is placed in their own timeline.
7. For 3 coins, you can buy a card into your timeline. Therefore, it is advantageous to guess title and
   artist correctly.
8. The game ends when the first player reaches 10 cards in their timeline.

The primary goal of our Hitster application is to be fun, user-friendly 
and to emulate the experience of playing the physical table-top game, while introducing new features and enhancements.
This includes creating an intuitive and responsive user interface, while also being nice to look at. 
Additionally, we need to ensure that the game works on Google Chrome, our target browser.
The biggest upside of our digital version is the unlimited catalogue of songs. 
The table-top version of this game contains a limited number of song-cards and can get repetitive after playing it a few times.
By using Apple Music's huge library, our game stays fresh, even after many rounds.

## Table of Contents
1. Introduction
2. Technologies
3. High-level components
4. Launch & Deployment
5. Roadmap
6. Authors & Acknowledgement
7. License

## Technologies
- Java & Spring Boot Framework: This project uses java as main programming language and uses the Spring Boot Framework
- JPA: used to persist data to database
- Google Cloud: handling server deployment
- Vercel: handling client deployment and hosting client
- REST: used for communication between client and server
- SonarQube: measures Code Quality
- Docker: used to simplify building, testing and deploying application
- Websockets: used for real-time updating of client, using STOMP (Messaging Protocol)
- Apple Music API: used to fetch playlists containing the greatest hits of a decade and corresponding song metadata (Title, Artist, Release Year...)

## High-level components
- [LobbyController](https://github.com/nicodoukas/sopra-fs25-group-34-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/controller/LobbyController.java): Handles the lobby logic, such as inviting a user to a lobby. Also serves as waiting room before the game starts.
- [GameController](https://github.com/nicodoukas/sopra-fs25-group-34-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/controller/GameController.java): Handles the entire game logic, with functions such as checking if a player's placement or guess is correct or updating the player (for example inserting a SongCard into their timeline or adding a coin to their balance).
- [APIService](https://github.com/nicodoukas/sopra-fs25-group-34-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/service/APIService.java): Handles fetching of the playlists and songs directly from Apple Music's Library and directly converting them into a SongCard entity, which is used for every round of a game.
- [WebSocketMessenger](https://github.com/nicodoukas/sopra-fs25-group-34-server/blob/main/src/main/java/ch/uzh/ifi/hase/soprafs24/websocket/WebSocketMessenger.java): Allows for real-time interaction between clients. Used for all WebSocket endpoints & functions, such as playing the same audio simultaneously on different clients.

## Launch & Deployment
After cloning this GitHub repository locally on your own machine, the following steps can be taken to build the application:

### Use the local Gradle Wrapper to build the application:
-   macOS: `./gradlew`
-   Linux: `./gradlew`
-   Windows: `./gradlew.bat`

### Build

```bash
./gradlew build
```

### Run

```bash
./gradlew bootRun
```

You can verify that the server is running by visiting `localhost:8080` in your browser.

### Test

```bash
./gradlew test
```

### Releases

You will work in your own branch, where you can work on your issues locally and freely commit and push to GitHub. 
If you want to merge your contributions to the main branch, open a new merge request for other group members to check out and accept.

## Roadmap
### Our next 4 features to be implemented:
### 1. Global Leaderboard
On the server side, this includes adding statistics for each user and corresponding functions to calculate and save them to the user during/after a game.
These statistics include:
- Nr. of Games played
- Nr. of Games won
- Win rate
- Percentage of correct Title & Artist guesses
- Average timeline length at the end of a game

### 2. Game History
On the server side, this means persisting all games to an external database, as we only save them in memory right now and delete them after finishing.
Additionally, new fields and functions must be added to the player and game, such as their rank in the game, game length and date of the game.

### 3. Individual profile picture
On the server side, this includes adjusting our current ProfilePicture handling to allow the upload and persistance of external non-predefined images to the database.

### 4. Adequate function to check correctness of guesses
Right now, when guessing the Title and Artist of a Song, the spelling is very sensitive when it comes to special characters, which we would like to make more flexible.
For example, for the song title "Don't Look Back in Anger", the guess "Dont Look Back in Anger" is wrong, which can be frustrating.
This task includes writing a function that checks for equivalence of two strings in a more flexible way.

## Authors & Acknowledgement
- [Julia Würsch](https://github.com/monolino)
- [Anja Lindenmann](https://github.com/AnchaXD)
- [Nico Doukas](https://github.com/nicodoukas)
- [Philip Keller](https://github.com/phikell)

We would like to thank our TA, Diyar Taskiran, for his continuous support throughout the development of this project.
Additional thanks go out to the SoPra FS25 organizing staff for making this project possible.

## License
[Apache-2.0 License](https://github.com/nicodoukas/sopra-fs25-group-34-server/blob/main/LICENSE)
