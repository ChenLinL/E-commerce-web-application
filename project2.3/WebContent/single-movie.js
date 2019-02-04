/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */


//$(window).on('load', $.get("api/movies", {"movie_name": "something"}, function(resultData){handleResult(resultData)} ));

function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}


function createTheHtml(l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,n){
	result ='';
	result += "<th><ul>";
	num = Number(n);
	for(i = 0; i<num; i++)
	{
		result += "<li><a href=single-star.html?id=" + l1[i] +"&title="+l3+"&year="+l4+"&director="+l5+"&star="+l6+"&firstRecord="+l7+"&numRecord="+l8+"&sortType="+l9+"&sortOrder="+l10+"&title_i="+l11+"&genre="+l12+ '>'+ l2[i] + '</a></li>';
		//result += "<li><a href=single-star.html?id=" + l1[i]+ '>'+ l2[i] + '</a></li>';
	}
	result += "</ul></th>";
	console.log(result);
	return result;

}
/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function handleResult(resultData) {
    // populate the movie info h3
    // find the empty h3 body by id "movie_info"
    let movieInfoElement = jQuery("#movie_info");

    console.log("handleResult: populating movie table from resultData");
    
    let backInfoElement = jQuery('#Back');
    backInfoElement.append("<p><a href=index.html"+"?title="+resultData[0]['mtitle']+"&year="+resultData[0]['myear']+"&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+resultData[0]['mfirstRecord']+"&numRecord="+resultData[0]['mnumRecord']+"&sortType="+resultData[0]['msortType']+"&sortOrder="+resultData[0]['msortOrder']+"&title_i="+resultData[0]["mtitle_i"]+"&genre="+resultData[0]["mgenre"]+">Back to Search Result</p>");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#single_movie_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < resultData.length; i++) {
        let rowHTML = "";
        	rowHTML += "<tr>";
        	rowHTML += "<th>" + resultData[i]["movie_id"] + "</th>";
        	rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        	rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        	rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        	rowHTML += "<th>" + resultData[i]["movie_genre"] + "</th>";
        	rowHTML += createTheHtml(resultData[i]["star_id"],resultData[i]["movie_star"],resultData[0]['mtitle'],resultData[0]['myear'],resultData[0]['mdirector'],resultData[0]['mstar'],resultData[0]['mfirstRecord'],resultData[0]['mnumRecord'],resultData[0]['msortType'],resultData[0]['msortOrder'],resultData[0]["mtitle_i"],resultData[0]["mgenre"],resultData[i]["stars_num"]);
        	rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>"; 
        	rowHTML += "</tr>";
        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Makes the HTTP GET request and registers on success callback function handleResult
//Get id from URL
let movieId = getParameterByName('id');
let mTitle = getParameterByName('title');
let mYear = getParameterByName('year');
let mDirector = getParameterByName('director');
let mStar = getParameterByName('star');
let firstRecord = getParameterByName('firstRecord');
let numRecord = getParameterByName('numRecord');
let sortType = getParameterByName('sortType');
let sortOrder = getParameterByName('sortOrder');
let title_i = getParameterByName('title_i');
let genre = getParameterByName('genre');
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-movie?id=" + movieId+"&title="+mTitle+"&year="+mYear+"&director="+mDirector+"&star="+mStar+"&firstRecord="+firstRecord+"&numRecord="+numRecord+"&sortType="+sortType+"&sortOrder="+sortOrder+"&title_i="+title_i+"&genre="+genre, // Setting request url, which is mapped by StarsServlet in Stars.java
    //success: handleResult
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
