# web-project
# Madeline Prins and Eva Grench

This repository contains code for our database-driven web project. It consists of the following files:

createtable.sql: A Python script used to create a database and set up the table where our data is stored, including columns and types. It was slightly modified from starter code written by Amy Csizmar Dalal.

Crimes08-18.csv: A comma-space delimited text file containing crime data from Chicago from 2001 to 2018 (despite the name).

datasource.py: A Python script that executes SQL queries using the psycopg2 library. It allows the user to make a variety of queries and runs them, getting results from the database.

flask_app.py: A Python script that uses the flask app to connect the SQL queries and the front-end html pages to display accurate results.

index.html: An html file that structures the homepage of our website. Displays information about the site and the dataset.

exploreData.html: An html file that prompts the user to apply a variety of filters and then submit the form and send them to the results page.

exploreResults.html: An html file that displays a maximum of 300 crimes that match the provided filters (or all crimes if none were selected) in a table format. Allows the user to return to the search page.

locationStats.html: An html file that prompts the user to input a community area and provides information about the community areas of Chicago.

locationResults.html: An html file that displays two graphs showing how crime rates have change and the most common crimes in the selected area. If no area is selected, a random community area is picked.

crimeCSS.css: A css file that formats most of the html files in our website.

exploreCSS.css: A css file that formats the exploreData.html page.

Our project allows users to view data about crimes that have happened in Chicago from 2001 to present. They can search for a list of crimes that have certain criteria (e.g. an arrest occurred, it happened on the street, and it was a theft). If they don't select any filters, they are given 300 crimes from the table and no filters are applied. They also have the option of picking one of the 77 Chicago community areas and viewing graphs that show how the crime rate has changed and which crimes are most common (and how many have been recorded) in that area. If they don't select a community area, they are given data from a random one. Users can search more than once without receiving any errors and can switch between the two search methods as they please.

Search for a list of crimes based on any, all, or none of the following filters:
  - crime date
  - crime type
  - crime setting
  - arrest result
  - domestic crime
  - community area

They will receive a table that lists all (or a limit of 300) crimes fitting the description with more information about each crime, such as the block it occurred on and a brief description.

See how the crime rate has changed over time and which crimes are most common in a selected community area. The results are displayed in two different graphs.

To run the website, get into our directory, cd into flaskFiles and then type python3 flask_app.py perlman.mathcs.carleton.edu 5118. To move around our webpage, navigate to the search page of your choice. Fill out the desired filters using the radio buttons and text inputs with the specified type of input. Click the submit button to see the results. Once on the results page, click the try again button to make another search or switch pages.

Crimes - 2001 to present.csv (name of file from website), Crimes08-18.csv (name of portion of the file we were able to download), Chicago Police Department (owner of the datast), data.cityofchicago.org (publisher of the dataset), https://catalog.data.gov/dataset/crimes-2001-to-present-398a4 (link to the dataset, an explanation of the dataset, and licensing information)


Other Notable Sources:
1. w3schools.com (html and css tutorials)

2. https://gist.github.com/rsutphin/5157757#file-clear_radio-html-L17 (how to reset radio buttons)

3. https://developers.google.com/chart/interactive/docs/gallery/linechart (drawing line graphs from data)

4. https://developers.google.com/chart/interactive/docs/gallery/barchart (drawing bar graphs from data)
