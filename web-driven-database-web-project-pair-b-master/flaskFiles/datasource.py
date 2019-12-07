import psycopg2
import getpass

#datasource.py
#@authors Eva Grench and Madeline Prins
#A functional interface and its implementations for crime database queries

'''
class DataSource: constructs variables for filters using input, makes sure they're in the correct type 
to be read by sql queries, and writes and runs sql queries.
'''
class DataSource:
    
    def __init__(self, crimeDate, crimeType, crimeSetting, arrestResult, domesticCrime, communityAreaNumber):
        # Different aspect of each crime that a user may be interested in.
        self.crimeDate = crimeDate
        self.crimeType = crimeType
        self.crimeSetting = crimeSetting
        self.arrestResult = arrestResult
        self.domesticCrime = domesticCrime
        self.communityAreaNumber = communityAreaNumber
        # The beginning of SQL queries for the exploreData page
        self.startQuery = 'SELECT * FROM crimes'
        # Connection to login to the database
        self.connection = None
    
    # Setter methods that change the filter to what was selected in a SQL friendly format
    def setCrimeDate(self, selectedCrimeDate):
        self.crimeDate = "'" + selectedCrimeDate + "'"
    
    def setArrest(self):
        self.arrestResult = "'TRUE'"
        
    def setDomestic(self):
        self.domesticCrime = "'TRUE'"
        
    def setCrimeType(self, selectedCrime):
        self.crimeType = "'" + selectedCrime + "'"
        
    def setCrimeSetting(self, selectedCrimeSetting):
        self.crimeSetting = selectedCrimeSetting
        
    def setCommunityArea(self, selectedCommunityArea):
        self.communityAreaNumber = selectedCommunityArea
        
    # Resetter methods that allow users to do multiple queries in one session
    def resetCrimeDate(self):
        self.crimeDate = ''
    
    def resetArrest(self):
        self.arrestResult = ''
        
    def resetDomestic(self):
        self.domesticCrime = ''
        
    def resetCrimeType(self):
        self.crimeType = ''
        
    def resetCrimeSetting(self):
        self.crimeSetting = ''
        
    def resetCommunityArea(self):
        self.communityAreaNumber = ''
        
    # Login to the database
    def loginToDataBase(self):
        database = 'grenche'
        user = 'grenche'
        password = 'paper267towel'
        try:
            self.connection = psycopg2.connect(database=database, user=user, password=password, host="localhost")
        except Exception as e:
            print('Connection error: ', e)
            exit()
            
    #########################################################################################################
    # Use the instance variables to make strings that eventually become part of a SQL query
    # Convert some variables to strings if they are not already
    
    def filterByCrimeDate(self):
        # If nothing specified, return blank string, so it is not included in the query
        if self.crimeDate == '':
            return ''
        # Otherwise, use the name of the variable in the table and instance variables to make the string
        else:
            stringCrimeDate = ' crimeDate = ' + self.crimeDate + ' AND'
            return stringCrimeDate
        
    def filterByCrimeType(self):
        if self.crimeType == '':
            return ''
        else:
            stringCrimeType = ' crimeType = ' + self.crimeType + ' AND'
            return stringCrimeType

    def filterByCrimeSetting(self):
        if self.crimeSetting == '':
            return ''
        else:
            stringCrimeSetting = ' crimeSetting LIKE ' + "'%" + self.crimeSetting + "%'" + ' AND'
            return stringCrimeSetting
        
    def filterByArrestResult(self):
        if self.arrestResult == '':
            return '' 
        else:
            stringArrestResult = ' arrestResult = ' + self.arrestResult + ' AND'
            return stringArrestResult

    def filterByDomesticCrime(self):
        if self.domesticCrime == '':
            return ''
        else:
            stringDomesticCrime = ' domesticCrime = ' + self.domesticCrime + ' AND'
            return stringDomesticCrime

    def filterByCommunityAreaNumber(self):
        if self.communityAreaNumber == '':
            return ''
        else:
            stringCommunityAreaNumber = ' communityAreaNumber = ' + str(self.communityAreaNumber)
            return stringCommunityAreaNumber
        
    #end converting variables to strings
    #########################################################################################################
        
    # Removes the AND from the end of the query string if necessary (i.e. no community area was selected)
    def removeANDFromQuery(self, stringQueryWithAND):
        # String of last four characters of an exploreData query
        endOfQuery = stringQueryWithAND[-4:]
        
        # If there is an unnecessary AND, remove it
        if endOfQuery == ' AND':
            #removes the last four characters if AND
            stringQuery = stringQueryWithAND[:-4]
        # Otherwise, do nothing
        else: 
            stringQuery = stringQueryWithAND
            
        return stringQuery
    
    # Add 'WHERE' to the exploreData query if at least one filter is selected
    def addWHEREToQuery (self):
        if self.filterByCrimeType() == self.filterByCrimeSetting() == self.filterByArrestResult() == self.filterByDomesticCrime() == self.filterByCrimeDate() == self.filterByCommunityAreaNumber() == '':
            self.startQuery = 'SELECT * FROM crimes'
        else:
            self.startQuery = self.startQuery + ' WHERE'
    
    # Clear the query string and reset all filters in the event of a new search
    def clearQueryString(self):
        self.startQuery = 'SELECT * FROM crimes'
        self.resetCrimeDate()
        self.resetArrest()
        self.resetDomestic()
        self.resetCrimeType()
        self.resetCrimeSetting()
        self.resetCommunityArea()
    
    #########################################################################################################
    
    # Query for Searching Explore Data Page that returns a string version of a query with all relevant filters
    def makeQueryExploreData(self):
        # Add 'WHERE' if necessary
        self.addWHEREToQuery()
        
        # Add all the possible filters
        query = self.startQuery + self.filterByCrimeDate() + self.filterByCrimeType() + self.filterByCrimeSetting() + self.filterByArrestResult() + self.filterByDomesticCrime() + self.filterByCommunityAreaNumber()
        
        # Convert the query to a string
        stringQueryWithAND = str(query)
        
        # Remove the last AND if necessary
        stringQuery = self.removeANDFromQuery(stringQueryWithAND)
        
        # Add a limit of 300 crimes per search
        stringQuery += ' LIMIT 300'
        
        # Return the correct format for a SQL query
        return stringQuery

    # Query for seeing how crime rate has changed over time in a given area
    def makeQueryLocationStatsRate(self):
        # Finds the total number of crimes in a given community area and groups them by year
        query = 'SELECT crimeDate, COUNT(*) FROM crimes WHERE communityAreaNumber = '+ self.communityAreaNumber +' GROUP BY crimeDate ORDER BY crimeDate'
        stringQuery = str(query)
        return stringQuery
    
    # Query for seeing the total number of each crime type in a given area
    def makeQueryLocationStatsCrime(self):
        # Finds the total number of each crime in a given community area and orders them from greatest to smallest
        query = 'SELECT crimeType, COUNT(*) FROM crimes WHERE communityAreaNumber = '+ self.communityAreaNumber +' GROUP BY crimeType ORDER BY COUNT DESC'
        stringQuery = str(query)
        return stringQuery
        
    # Runs any of the above three queries by cursor execution
    def runQuery(self, typeOfQuery):
        try: 
            cursor = self.connection.cursor()
            cursor.execute(typeOfQuery)
            results = cursor.fetchall()
            return results
        except Exception as e:
            print('Cursor error', e)
            self.connection.close()
            exit()
            
#########################################################################################################          
'''
Main method member: creates an instance of the DataSource class with various filter parameters, calls the login method, 
and calls the runQuery method on each page 
'''
def main():
    #The different filters in order:
    #crimeDate, crimeType, crimeSetting, arrestResult, domesticCrime, communityAreaNumber
    dataQuery = DataSource('', '', '', '', '', '')
    dataQuery.loginToDataBase()
    dataQuery.runQuery(dataQuery.makeQueryExploreData())

if __name__ == '__main__':
    main()
