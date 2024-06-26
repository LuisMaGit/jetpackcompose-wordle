## Palabri
Wordle native android app in spanish 🇪🇸

[![image](https://github.com/LuisMaGit/jetpackcompose-wordle/assets/70621340/d118f44b-5f27-406f-8436-35b9c33d0ad9)](https://play.google.com/store/apps/details?id=com.luisma.palabri&hl=en&gl=US)

This app uses the same approach as the [NY Times Wordle](https://www.nytimes.com/games/wordle/index.html) but in spanish: a new 5 letter word is published every 24h and the user has 6 tries
to guess it, also, in this version, the user can play incomplete old matchs. The app is fully off-line, uses an internal db.

<img width="170" height="340" alt="Screenshot 2024-03-17 at 15 22 10" src="https://github.com/LuisMaGit/jetpackcompose-wordle/assets/70621340/5b285cf1-1e8b-43b0-acee-91288567acd7">
<img width="170" height="340" alt="Screenshot 2024-03-17 at 15 20 25" src="https://github.com/LuisMaGit/jetpackcompose-wordle/assets/70621340/98bd5148-fdc6-481e-bcad-e8d11aa3ed5a">
<img width="170" height="340" alt="Screenshot 2024-03-17 at 15 22 06" src="https://github.com/LuisMaGit/jetpackcompose-wordle/assets/70621340/3eed44cf-c7af-44ee-a645-5b5a40815621">
<img width="170" height="340" alt="Screenshot 2024-03-17 at 15 21 53" src="https://github.com/LuisMaGit/jetpackcompose-wordle/assets/70621340/333c7e3e-c83c-42a2-8223-832d8f7f7f4c">
<img width="170" height="340" alt="Screenshot 2024-03-17 at 15 21 43" src="https://github.com/LuisMaGit/jetpackcompose-wordle/assets/70621340/9ebc6136-f128-4dd1-adcb-7622c8596b9b">
<img width="170" height="340" alt="Screenshot 2024-03-17 at 15 21 27" src="https://github.com/LuisMaGit/jetpackcompose-wordle/assets/70621340/6784ff89-fdd2-479a-9d16-15d6a14551d6">

## Project structure

<code>core</code>: high level logic and db operations

<code>core_ui</code>: components library

<code>game</code>: views, viewmodels and game main logic

<code>router</code>: navigation controller

## Stack
* Jetpack Compose
* SqlDelight
* Kotlinx Collections Immutable
* JUnit 4
* Mockk
