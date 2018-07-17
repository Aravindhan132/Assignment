# Assignment
[Problem Statement]

Implement a simple 2 screen application. The app should parse the JSON files provided with the test and display the info as described below. The application must use a proper architecture chosen by the developer. The developer must be able to explain his choice.

Model:
The application should pull the JSON from the url provided. The data retrieved should then be cached someway with an expiry date of 1 hour (the cache requirement is just for the gameData.json). The application will then use that data to populate the views.

Screen 1:
It should display a list of items using the value of data#name as the label. On clicking an item it should take you to screen 2 which will display the details of the game.

Screen 2:
It should display the name, jackpot and date of the game, using best practices for locale formatting. Use currency provided in JSON to format #jackpot.

Both the screens must have a header showing an avatar image, player name, balance and last login date which are retrieved by requesting the playerInfo.json. 
In screen 2, last login date must be hidden.
Send us the completed source code.

[API Guide]
Game URL: 
https://api.myjson.com/bins/11j2gr  (gameData.json)
Header info URL: 
https://api.myjson.com/bins/w660r (playerInfo.json)

