
import flask
from flask import render_template, request, jsonify
import json
import sys
import datasource
import random

app = flask.Flask(__name__)

'''
flask_app.py
@authors: Eva Grench and Madeline Prins
The program that connects the SQL queries with the front-end html files
'''

# Renders certain pages given a route
@app.route('/')
@app.route('/index.html')
def callHomePage():
    return render_template('index.html')

@app.route('/exploreData.html')
def callExploreData():
    dataQuery.clearQueryString()
    return render_template('exploreData.html')

@app.route('/locationStats.html')
def callLocationStats():
    dataQuery.clearQueryString()
    return render_template('locationStats.html')

# Renders the results page after a query has been made on the exploreData page
@app.route('/exploreResults.html', methods = ['POST', 'GET'])
def callExploreResults():
    if request.method == 'POST':
        # Get the input from the form
        result = request.form
        
        # If a form has been submitted, set the correct filters in datasource.py according to the form
        setVarsExploreData(result)
        
        # Run an exploreData query once all of the variables have been set
        queryResults = dataQuery.runQuery(dataQuery.makeQueryExploreData())
        
        # Put all of the individual crimes into a list in the correct format (i.e. a list of dictionaries)
        finalResults = []
        for result in queryResults:
            finalResults.append(formatExploreData(result))
            
        return render_template('exploreResults.html', result = finalResults)
    
# Sets the necessary filters in datasource.py depending on the form results
def setVarsExploreData(formResults):
    if "crimeDate" in formResults and formResults["crimeDate"] != '':
        dataQuery.setCrimeDate(formResults["crimeDate"])
    if "crimeType" in formResults:
        dataQuery.setCrimeType(formResults["crimeType"])
    if "crimeSetting" in formResults:
        dataQuery.setCrimeSetting(formResults["crimeSetting"])
    if ("arrest", "TRUE") in formResults.items():
        dataQuery.setArrest()
    if ("domestic", "TRUE") in formResults.items():
        dataQuery.setDomestic()
    if "communityAreaNumber" in formResults and formResults["communityAreaNumber"] != '':
        dataQuery.setCommunityArea(formResults["communityAreaNumber"])
    
# Takes in a list of tuples and converts them to dictionaries where the key is the filter chosen
def formatExploreData(dataAsLists):
    # All possible filters
    keyList = ["crimeDate", "block", "crimeType", "crimeDescription", "crimeSetting", "arrestResult", "domesticCrime", "communityAreaNumber"]
    
    # Add keys and values for each item in the given list
    dataAsDict = {}
    for i in range (0, 8):
        dataAsDict[keyList[i]] = dataAsLists[i]
        
    return dataAsDict

# Renders the results page after a user selected a community area
@app.route('/locationResults.html', methods = ['POST', 'GET'])
def callLocationResults():
    if request.method == 'POST':
        formResult = request.form
        # If the form has been submitted and they inputted a community number, set the instance
        # variable in datasource.py
        if "communityAreaNumber" in formResult and formResult["communityAreaNumber"] != '':
            # Sets the community area based on user input
            dataQuery.setCommunityArea(formResult["communityAreaNumber"])
        else:
            print(random.randint(1, 78))
            # Sets the community area to a random area if there is not user input
            dataQuery.setCommunityArea(str(random.randint(1, 78)))
       
        # Executes the queries that get crime rate and crime type statistics    
        queryRateResults = dataQuery.runQuery(dataQuery.makeQueryLocationStatsRate())
        queryBarResults = dataQuery.runQuery(dataQuery.makeQueryLocationStatsCrime())
        
        return render_template('locationResults.html', result = queryRateResults, barResult = queryBarResults, location = dataQuery.communityAreaNumber)
        
if __name__ == '__main__':
    if len(sys.argv) != 3:
        print('Usage: {0} host port'.format(sys.argv[0]), file=sys.stderr)
        exit()
        
    dataQuery = datasource.DataSource('', '', '', '', '', '')
    dataQuery.loginToDataBase()

    host = sys.argv[1]
    port = sys.argv[2]
    app.run(host=host, port=port)
    
    app.run(debug = True)
