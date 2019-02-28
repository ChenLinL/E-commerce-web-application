/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

var info_map = new Object();
console.log(info_map);
function handleMovieResult(resultData) { 
	
    var rowHTML = document.getElementById("genre");
    var title_rowHTML = document.getElementById("title");
    var alphabet_rowHTML = document.getElementById("alphabet");
    console.log(resultData);
    for (let i = 0; i < resultData[3]['movie_genre'].length; i++)
    {
    	var link = '<a href= "index.html?title=null&year=null&director=null&star=null&firstRecord=0&numRecord=5&sortType=rating&sortOrder=DESC&title_i=null&genre=' + resultData[3]['movie_genre'][i] + '">' + resultData[3]['movie_genre'][i] + '</a>';
    	rowHTML.innerHTML += link + "\u00A0 \u00A0 ";
    }
    
    for (let i = 0; i < resultData[3]['movie_title'].length; i++)
    {
    	if (isNaN(resultData[3]['movie_title'][i]))
    	{
    		resultData[3]['movie_title'][i] = resultData[3]['movie_title'][i].toUpperCase();
    	}
    	
    	var link = '<a href= "index.html?title=null&year=null&director=null&star=null&firstRecord=0&numRecord=5&sortType=rating&sortOrder=DESC&title_i=' + resultData[3]['movie_title'][i]+"&genre=null" +
		'">' + resultData[3]['movie_title'][i] + '</a>';
    	
    	if (!isNaN(resultData[3]['movie_title'][i]))
    		title_rowHTML.innerHTML += link + "\u00A0 \u00A0 ";
    	else
    		alphabet_rowHTML.innerHTML += link + "\xa0 \xa0 ";
    }
    
    let movieInfoElement = jQuery("#movie_table_by_rating");
    let movieTableBodyElement = jQuery("#movie_rating_table_body");
    
 for (let i = 0; i < resultData.length-1; i++) {
    	
        let rowHTML = "";
        	rowHTML += "<tr>";
        	rowHTML += "<th scope=row>" + "Title: " + resultData[i]["top_movie_title"] + "</th>";
        	rowHTML += "</tr>";
        let yearHTML="";
         	yearHTML += "<tr>";
         	yearHTML += "<th scope=row>" + "Released Year: " + resultData[i]["top_movie_year"] + "</th>";
         	yearHTML += "</tr>";
        let directorHTML="";
          	directorHTML += "<tr>";
          	directorHTML += "<th scope=row>" + "Director: " + resultData[i]["top_movie_director"] + "</th>";
          	directorHTML += "</tr>";
          	
        let genreHTML="";
         	genreHTML += "<tr>";
         	genreHTML += "<th scope=row>" + "Genre: " + resultData[i]["top_movie_genre"] + "</th>";
         	genreHTML += "</tr>";
        
        let starHTML="";
        	starHTML += "<tr>";
        	starHTML += "<th scope=row>" + "Stars: " + resultData[i]["top_movie_star"] + "</th>";
         	starHTML += "</tr>";
         	
        let ratingHTML="";
          	ratingHTML += "<tr>";
          	ratingHTML += "<th scope=row>" + "Rating: " + resultData[i]["top_movie_rating"] + "</th>";
          	ratingHTML += "</tr>";
          	
        let gradient="";
         	gradient += "<div id=grad1>";
         	gradient += "</div>"
        movieTableBodyElement.append(rowHTML, yearHTML, directorHTML, genreHTML, starHTML, ratingHTML, gradient);
    }

    
}
function replaceSpace(target){
	target = target.replace(/ /g,"_");
	return target;
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/main-page", // Setting request url, which is mapped by StarsServlet in Stars.java
    success:handleMovieResult
    //success: (resultData) => handleMovieResult(resultData)// Setting callback function to handle data returned successfully by the StarsServlet
});

$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    minChars: 3
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});

/*
 * CS 122B Project 4. Autocomplete Example.
 * 
 * This Javascript code uses this library: https://github.com/devbridge/jQuery-Autocomplete
 * 
 * This example implements the basic features of the autocomplete search, features that are 
 *   not implemented are mostly marked as "TODO" in the codebase as a suggestion of how to implement them.
 * 
 * To read this code, start from the line "$('#autocomplete').autocomplete" and follow the callback functions.
 * 
 */


/*
 * This function is called by the library when it needs to lookup a query.
 * 
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */
function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	console.log("sending AJAX request to backend Java Servlet")
	
	// TODO: if you want to check past query results first, you can do it here
	
	// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
	// with the query data
	var query_key = replaceSpace(query.toLowerCase());
	console.log(query_key);
	if (info_map[query_key] != null){
		console.log("check from map");
		handleLookupAjaxSuccess(info_map[query_key], query, doneCallback);
	}
	else{
		jQuery.ajax({
			"method": "GET",
			// generate the request url from the query.
			// escape the query string to avoid errors caused by special characters 
			"url": "api/movieAuto?query=" + escape(query),
			"success": function(data) {
				// pass the data, query, and doneCallback function into the success handler
				handleLookupAjaxSuccess(data, query, doneCallback) 
			},
			"error": function(errorData) {
				console.log("lookup ajax error")
				console.log(errorData)
			}
		})
	}
}


/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful")
	console.log(data);
	// parse the string into JSON
	var query_key = replaceSpace(query.toLowerCase());
    info_map[query_key] = data;
	var jsonData = JSON.parse(data);
	console.log(info_map);
	console.log(jsonData);
	
	// TODO: if you want to cache the result into a global variable you can do it here

	// call the callback function provided by the autocomplete library
	// add "{suggestions: jsonData}" to satisfy the library response format according to
	//   the "Response Format" section in documentation
	doneCallback( { suggestions: jsonData } );
}


/*
 * This function is the select suggestion handler function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["heroID"])
	window.location.href="single-movie.html?id=" + suggestion["data"]["movieId"] +"&title="+suggestion["value"]+"&year="+
			"&director=&star=&firstRecord=&numRecord=&sortType=&sortOrder=&title_i=&genre=";
}


/*
 * This statement binds the autocomplete library with the input box element and 
 *   sets necessary parameters of the library.
 * 
 * The library documentation can be find here: 
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 * 
 */


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	// TODO: you should do normal search here
}

// bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the handler function
		handleNormalSearch($('#autocomplete').val())
	}
})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button


