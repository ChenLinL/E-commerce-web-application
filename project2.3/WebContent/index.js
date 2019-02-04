/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
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
		result += "<li><a href=single-star.html?id="+ l1[i]+"&title="+l3+"&year="+l4+"&director="+l5+"&star="+l6+"&firstRecord="+l7+"&numRecord="+l8+"&sortType="+l9+"&sortOrder="+l10+"&title_i="+l11+"&genre="+l12+">"+ l2[i] + "</a></li>";
	}
	result += "</ul></th>";
	return result;
}

function handleMovieResult(resultData) {
	
	 
    let searchInfoElement = jQuery('#Search');
    searchInfoElement.append("<p><a href=search.html> Search </p>");
	// Populate the movie table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#movie_table_body");
    console.log(resultData[0]["mgenre"]);
    //Iterate through resultData, no more than 20 entries
    for (let i = 0; i < resultData.length; i++) {
    	
    	// Concatenate the html tags with resultData jsonObject
    	let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" 
        	+ "<a href=single-movie.html?id=" + resultData[i]['movie_id'] +"&title="+resultData[0]['mtitle']+"&year="+resultData[0]['myear']+"&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+resultData[0]['mfirstRecord']+"&numRecord="+resultData[0]['mnumRecord']+"&sortType="+resultData[0]['msortType']+"&sortOrder="+resultData[0]['msortOrder']+"&title_i="+resultData[0]['mtitle_i']+"&genre="+resultData[0]['mgenre']+">"
        	+ resultData[i]["movie_title"] + 
        	"</a>" + 
        	"</th>";
        //rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        console.log(rowHTML);
        rowHTML += "<th>" + resultData[i]["movie_year"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_director"] + "</th>";
        rowHTML += "<th>" + resultData[i]["movie_genre"] + "</th>";
        rowHTML += createTheHtml(resultData[i]["star_id"],resultData[i]["movie_star"],resultData[i]["mtitle"],resultData[i]['myear'],resultData[i]['mdirector'],resultData[i]['mstar'],resultData[i]['mfirstRecord'],resultData[i]['mnumRecord'],resultData[i]['msortType'],resultData[i]['msortOrder'],resultData[i]["mtitle_i"],resultData[i]["mgenre"],resultData[i]["stars_num"]);
        rowHTML += "<th>" + resultData[i]["movie_rating"] + "</th>";
        rowHTML += "</tr>";
        
        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
    }
}
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
console.log(mTitle);
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movies?title="+mTitle+"&year="+mYear+"&director="+mDirector+"&star="+mStar+"&firstRecord="+firstRecord+"&numRecord="+numRecord+"&sortType="+sortType+"&sortOrder="+sortOrder+"&title_i="+title_i+"&genre="+genre, // Setting request url, which is mapped by StarsServlet in Stars.java
    //success:handleMovieResult
    success: (resultData) => handleMovieResult(resultData)// Setting callback function to handle data returned successfully by the StarsServlet
});