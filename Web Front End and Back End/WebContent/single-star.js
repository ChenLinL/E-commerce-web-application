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

function replaceSpace(target){
	target = target.replace(/ /g,"%20");
	return target;
}

function createTheHtml(l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,n){
	result ='';
	result += "<th><ul>";
	num = Number(n);
	for(i = 0; i<num; i++)
	{
		result += "<li><a href=single-movie.html?id=" + l1[i]+"&title="+l3+"&year="+l4+"&director="+l5+"&star="+l6+"&firstRecord="+l7+"&numRecord="+l8+"&sortType="+l9+"&sortOrder="+l10+"&title_i="+l11+"&genre="+l12+ '>'+ l2[i] + '</a></li>';
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
	console.log(resultData);
	
    let movieInfoElement = jQuery("#star_info");

    console.log("handleResult: populating star table from resultData");
    
    let backInfoElement = jQuery('#Back');
    backInfoElement.append("<p><a href=index.html"+"?title="+replaceSpace(resultData[0]['mtitle'])+"&year="+resultData[0]['myear']+"&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+resultData[0]['mfirstRecord']+"&numRecord="+resultData[0]['mnumRecord']+"&sortType="+resultData[0]['msortType']+"&sortOrder="+resultData[0]['msortOrder']+"&title_i="+resultData[0]["mtitle_i"]+"&genre="+replaceSpace(resultData[0]["mgenre"])+">Back to Search Result</p>");
    let scInfoElement = jQuery('#ShopingCart');
    scInfoElement.append("<p><a href=shopingCart.html> Check Out</a></p>");
    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let movieTableBodyElement = jQuery("#single_star_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < Math.min(20, resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        
        	 rowHTML += "<th>" + resultData[i]["star_name"] + "</th>";
        	 rowHTML += "<th>" + resultData[i]["star_dob"] + "</th>";
             rowHTML += createTheHtml(resultData[i]["moviesId"],resultData[i]["movies"],replaceSpace(resultData[i]["mtitle"]),resultData[i]['myear'],resultData[i]['mdirector'],resultData[i]['mstar'],resultData[i]['mfirstRecord'],resultData[i]['mnumRecord'],resultData[i]['msortType'],resultData[i]['msortOrder'],resultData[i]["mtitle_i"],resultData[i]["mgenre"],resultData[i]["movies_num"]);
      
        
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
let starId = getParameterByName('id');
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
console.log(genre);
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-star?id=" + starId+"&title="+mTitle+"&year="+mYear+"&director="+mDirector+"&star="+mStar+"&firstRecord="+firstRecord+"&numRecord="+numRecord+"&sortType="+sortType+"&sortOrder="+sortOrder+"&title_i="+title_i+"&genre="+genre, // Setting request url, which is mapped by StarsServlet in Stars.java
    //success: handleStarResult
    success:(resultData) => handleStarResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
