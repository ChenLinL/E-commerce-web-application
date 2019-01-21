/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function createTheHtml(l1,l2,n){
	result ='';
	result += "<th><ul>";
	num = Number(n);
	for(i = 0; i<num; i++)
	{
		result += "<li><a href=single-star.html?id=" + l1[i] + '>'+ l2[i] + '</a></li>'
	}
	result += "</ul></th>";
	return result;
}

function handleMovieResult(resultData) {
	
	// Populate the movie table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");
    
    //Iterate through resultData, no more than 20 entries
    for (let i = 0; i < Math.min(20, resultData.length); i++) {
    	
    	// Concatenate the html tags with resultData jsonObject
    	let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" 
        	+ '<a href="single-movie.html?id=' + resultData[i]['movie_id'] + '">'
        	+ resultData[i]["movie_title"] + 
        	'</a>' + 
        	"</th>";

        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_genre"] + "</th>";
        rowHTML += createTheHtml(resultData[i]["star_id"],resultData[i]["movie_star"],resultData[i]["stars_num"]);
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        rowHTML += "</tr>";
        
        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movies", // Setting request url, which is mapped by StarsServlet in Stars.java
    success:handleMovieResult
    //success: (resultData) => handleMovieResult(resultData)// Setting callback function to handle data returned successfully by the StarsServlet
});