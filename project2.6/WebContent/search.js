
/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleSearchResult(resultDataJson) {
	//console.log(resultDataString);
    //resultDataJson = JSON.parse(resultDataString);
    console.log("OKOK");
    console.log(resultDataJson);
    console.log(resultDataJson["year"]);
    
    // If login succeeds, it will redirect the user to index.html
    window.location = "index.html?title="+resultDataJson["title"]+"&num="+resultDataJson["m_num"]+"&year="+resultDataJson["year"]+"&director="+resultDataJson["director"]+"&star="+resultDataJson["star"]+"&firstRecord=0"+"&numRecord=5"+"&sortType=rating"+"&sortOrder=DESC"+"&title_i=null"+"&genre=null";
    console.log(window.location);
    //window.location = "index.htm?title="+resultDataJson["title"]
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitSearchForm(formSubmitEvent) {
    console.log("submit search form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();
    $.post(
        "api/search",
        // Serialize the login form to the data sent by POST request
        $("#search_form").serialize(),
        (resultDataString) => handleSearchResult(resultDataString)
    );
}
// Bind the submit action of the form to a handler function
$("#search_form").submit((event) => submitSearchForm(event));
