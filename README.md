# RemindMe

## About the App

RemindMe is a native Android app developed for Dr. Lehr's CS3398 Software Engineering class. 
The app is designed to be used every day to help the user keep track of and organize their daily
tasks. You can create tasks, set due dates, and keep them organized into lists.

### Technologies Used
The app is built on the Android framework, and uses an implementation of SQLite to store the user's
lists and tasks on the phone. 

## How to Build the App

RemindMe is a self contained project that is ready to build and deploy. All that's needed to
build the app is to check it out from the Github repository as a new project using the 
Android Studio VCS tools. When the project is loaded, click Tools -> Build to build the project. 
After Android Studio is finished building the project, it can be installed on a phone or run in
an Android Emulator.

## Third Party Libraries Used
We would like to give credit to the developers of the following libraries that were critical in the
development of the app.


* [Parceler](https://github.com/johncarl81/parceler), a serialization library for sending data between activities
* [Stetho](http://facebook.github.io/stetho/), a debugging bridge developed by Facebook that makes it easy to inspect your app with Google Chrome
* [DBFlow](https://github.com/Raizlabs/DBFlow), an Object Relational Management library that uses Java annotations to make creating and managing databases in Android simple
* [Advanced Recycler View](https://github.com/h6ah4i/android-advancedrecyclerview), a library for Android RecyclerView that adds features such as swiping and Drag and Drop
* [Material Dialogs](https://github.com/afollestad/material-dialogs), a library that condenses the creation of pop-up dialogs into a single function call

## The Development Process 
This app was created using the Agile Development process with two week sprints. This process aided in the rapid development of this app. 
Biweekly standups were held to ensure progress was kept at a steady pace. Weekly team meetings helped team members in understanding
each other's code and ensure the team had a common goal each sprint. 


## The Project Contributors of RemindMe
* Taurino Tostado Jr - Splash Screen, helped with loading lists from the database and came up with creative ideas to design the app.
* Taylor Helton - Calendar Views, allowing users to view Month/Day/Week views.
* Brandon Claggett - Created the add task activity, added the add task FAB, implemented task sorting algorithms. 
* Chris Foytek - Created the navigation slideout drawer, data provider for abstraction between database and UI code, and code for UI representation of tasks/lists.


## Want to Contribute to the App?
 Here are some ideas that the team wanted to integrate in to the app:
 * Switch from a local database to a web based database such as AWS.
 * View lists within each calendar view using parcels 
 * Enable push notifications for task dates and times
 * Extend agile concepts of the project by implementing automated unit testing with a continuous integration service to further increase collaboration and lower deployment risk
 * Enable rapid release of changes with continous deployment
 
