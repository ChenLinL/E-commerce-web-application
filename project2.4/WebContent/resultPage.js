
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

//function replaceSpace(target){
//	target = target.replace(/ /g,"_");
//	return target;
//}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
//function handleResult(resultData) {
//    // populate the movie info h3
//    // find the empty h3 body by id "movie_info"
//
//    console.log("handleResult: populating movie table from resultData");
//    
//    let backInfoElement = jQuery('#Back');
//    backInfoElement.append("<p><a href=main-page.html"+">Back to Search Result</a></p>");
//
//}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Makes the HTTP GET request and registers on success callback function handleResult
//Get id from URL
let c_id = getParameterByName('c_id');
let backInfoElement = jQuery('#Back');
backInfoElement.append("<p><a href=main-page.html"+">Back to Main Page</a></p>");
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/resultPage?c_id="+c_id
    //success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});