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

function createTheHtml(l1,l2,n){
	result ='';
	result += "<th><ul>";
	num = Number(n);
	for(i = 0; i<num; i++)
	{
		result += "<li><a href=single-movie.html?id=" + l1[i] + '>'+ l2[i] + '</a></li>';
	}
	result += "</ul></th>";
	return result;

}
/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleStarResult(resultData) {
    // populate the movie info h3
    // find the empty h3 body by id "movie_info"
    let movieInfoElement = jQuery("#star_info");

    console.log("handleResult: populating star table from resultData");
    
    let backInfoElement = jQuery('#Back');
    backInfoElement.append("<p><a href=index.html>Back to Movie Page</p>");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#single_star_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < Math.min(20, resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        
        	 rowHTML += "<th>" + resultData[i]["star_name"] + "</th>";
        	 rowHTML += "<th>" + resultData[i]["star_dob"] + "</th>";
             rowHTML += createTheHtml(resultData[i]["moviesId"],resultData[i]["movies"],resultData[i]["movies_num"]);
      
        
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let starId = getParameterByName('id');

// Makes the HTTP GET request and registers on success callback function handleResult

jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-star?id=" + starId, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: handleStarResult
    	//(resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
