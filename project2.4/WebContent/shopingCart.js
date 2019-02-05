/**
 * Handle the data returned by IndexServlet
 * @param resultDataString jsonObject, consists of session info
 */
//function handleSessionData(resultDataString) {
//    resultDataJson = JSON.parse(resultDataString);
//
//    console.log("handle session response");
//    console.log(resultDataJson);
//    console.log(resultDataJson["sessionID"]);
//
//    // show the session information 
//    $("#sessionID").text("Session ID: " + resultDataJson["sessionID"]);
//    $("#lastAccessTime").text("Last access time: " + resultDataJson["lastAccessTime"]);
//}

/**
 * Handle the items in item list 
 * @param resultDataString jsonObject, needs to be parsed to html 
 */
//function handleCartArray(resultDataString) {
//	
//}
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

function handleCartArray(resultDataJson) {
    //const resultArray = resultDataString.split(",");
	//resultDataJson = JSON.parse(resultDataString)
    console.log(resultDataJson["key"]);
	console.log(resultDataJson["value"]);
    
    // change it to html list
	 let backInfoElement = jQuery('#Back');
	 backInfoElement.append("<p><a href=main-page.html>Back to mainPage</a></p>");
	 
	 let checkOutInfoElement = jQuery('#CheckOut');
	 checkOutInfoElement.append("<p><a href=checkOut.html>Check Out</a></p>");
	let shoppingTableBodyElement = jQuery("#shopping_table_body");
//    let res = "<ul>";
//    for(let i = 0; i < resultDataJson["key"].length; i++) {
//        // each item will be in a bullet point
//        res += "<li>" + resultDataJson["key"][i] +"num:"+resultDataJson["value"][i]+ "</li>";   
//    }
//    res += "</ul>";
	for (let i = 0; i < resultDataJson["key"].length; i++) {
    	
        //rowHTML += "<th>" + resultData[i]["movie_title"] + "</th>";
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>"+ resultDataJson["key"][i]+"</th>";
        rowHTML += "<th>"+"</th>";
        rowHTML += "<th>" + resultDataJson["value"][i] + "</th>";
        rowHTML += "<th>" 
        	+ "<a href=shopingCart.html?item="+ resultDataJson["key"][i]+"&id="+resultDataJson["id"][i]+"&func=add>"+"add one more"+
        	"</a>" + 
        	"</th>";
        rowHTML += "<th>" 
        	+ "<a href=shopingCart.html?item="+resultDataJson["key"][i]+"&id="+resultDataJson["id"][i]+"&func=sub>"+ "sub one more" + 
        	"</a>" + 
        	"</th>"; 
        rowHTML += "</tr>";
        console.log(rowHTML);
        // Append the row created to the table body, which will refresh the page
        shoppingTableBodyElement.append(rowHTML);
	}
    // clear the old array and show the new array in the frontend
//    $("#item_list").html("");
//    $("#item_list").append(res);
}

/**
 * Submit form content with POST method
 * @param cartEvent
 */
//function handleCartInfo(cartEvent) {
//    console.log("submit cart form");
//    /**
//     * When users click the submit button, the browser will not direct
//     * users to the url defined in HTML form. Instead, it will call this
//     * event handler when the event is triggered.
//     */
//    cartEvent.preventDefault();
//
//    $.get(
//        "api/shopingCart?item=itt&func=add",
//        // Serialize the cart form to the data sent by POST request
//        (resultDataString) => handleCartArray(resultDataString)
//    );
//}

//$.ajax({
//    type: "POST",
//    url: "api/shopingCart",
//    success: (resultDataString) => handleSessionData(resultDataString)
//});

// Bind the submit action of the form to a event handler function
//$("#cart").submit((event) => handleCartInfo(event));
let func = getParameterByName('func');
let item = getParameterByName('item');
let Id = getParameterByName('id');
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    //url: "api/single-movie?id=" + movieId+"&title="+mTitle+"&year="+mYear+"&director="+mDirector+"&star="+mStar+"&firstRecord="+firstRecord+"&numRecord="+numRecord+"&sortType="+sortType+"&sortOrder="+sortOrder+"&title_i="+title_i+"&genre="+genre, // Setting request url, which is mapped by StarsServlet in Stars.java
    //success: handleResult
    url: "api/shopingCart?item="+item+"&func="+func+"&id="+Id,
    success: (resultDataString) => handleCartArray(resultDataString)// Setting callback function to handle data returned successfully by the SingleStarServlet
});