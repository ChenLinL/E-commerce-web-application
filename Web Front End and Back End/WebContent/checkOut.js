/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleResult(resultDataString) {
    resultDataJson = JSON.parse(resultDataString);
    
    console.log("status");

    // If login succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        window.location.replace("resultPage.html?c_id="+resultDataJson["customerId"]);
    } else {
        // If login fails, the web page will display 
        // error messages on <div> with id "login_error_message"
        console.log("Your Credit card Information is incorrect. Please try again");
        $("#checkOut_error_message").text("Your Credit card Information is incorrect. Please try again");
        $("#checkOut_error_message").css({'color': 'gold', 'text-align': 'center',
        	'margin-top': '20px',
        	'font-family' : '"Comic Sans MS", cursive, sans-serif'});
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitForm(formSubmitEvent) {
    console.log("submit checkout form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();
    
    $.post(
        "api/checkOut",
        // Serialize the login form to the data sent by POST request
        $("#checkOut_form").serialize(),
        (resultDataString) => handleResult(resultDataString)
    );
}

// Bind the submit action of the form to a handler function
$("#checkOut_form").submit((event) => submitForm(event));