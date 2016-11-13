# PopularMoviesTwo
Udacity Popular Movies Two

This project is the upgradation to the first Popular movies app.
This includes following changes


#User Interface - Layout

1. UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
2. Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
3. UI contains a screen for displaying the details for a selected movie.
4. Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
5. Movie Details layout contains a section for displaying trailer videos and user reviews.
6. Tablet UI uses a Master-Detail layout implemented using fragments. The left fragment is for discovering movies. The right fragment displays the movie details view for the currently selected movie.



#User Interface - Function

1. When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
2. When a movie poster thumbnail is selected, the movie details screen is launched [Phone] or displayed in a fragment [Tablet].
3. When a trailer is selected, app uses an Intent to launch the trailer.
4. In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.



#Network API Implementation

1. In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
2. App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.
3. App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.



#Data Persistence

1. App saves a "Favorited" movie to SharedPreferences or a database using the movie’s id.
2. When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie IDs stored in SharedPreferences or a database.



General Project Guidelines

1. App conforms to common standards found in the Android Nanodegree General Project Guidelines.



# Suggestions to Make Your Project Stand Out!
1. Implement a content provider to store favorite movie details with a database such as SQLite. Store the title, poster, synopsis, user rating, and release date and display them even when offline.
2. Implement sharing functionality to allow the user to share the first trailer’s YouTube URL from the movie details screen.







License
-------
Copyright 2015 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements.  See the NOTICE file distributed with this work for
additional information regarding copyright ownership.  The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License.  You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
License for the specific language governing permissions and limitations under
the License.
