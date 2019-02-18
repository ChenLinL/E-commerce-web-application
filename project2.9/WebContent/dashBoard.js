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

function createTheHtml(l){
	result ='';
	result += "<th><ul>";
	for(i = 0; i<l.length; i++)
	{
		result+= "<li>"+l[i]+"</li>"
	}
	result += "</ul></th>";
	return result;
}

function submitAddForm(Form){
	console.log("hiHi");
	$.get(
	        "api/dashBoard",
	        // Serialize the login form to the data sent by POST request
	        $("#add_form").serialize(),
	        (resultDataString) =>handleAddResult(resultDataString)
	    );
} 


function handleDashResult(resultData) {
	 console.log(window.location.href);
	 let dashBoardBodyElement = jQuery("#dashBoard_body");
	 for (let i = 0; i < resultData["tables"].length; i++) {
	    	
	    	// Concatenate the html tags with resultData jsonObject
	    	let rowHTML = "";
	        rowHTML += "<tr>";
	        //console.log(rowHTML);
	        rowHTML += "<th>" + resultData["tables"][i]["name"] + "</th>";
	        rowHTML += createTheHtml(resultData["tables"][i]["fileds"]);
	        rowHTML += createTheHtml(resultData["tables"][i]["types"]);
	        rowHTML += createTheHtml(resultData["tables"][i]["Null"]);

	        rowHTML += "</tr>";
	        //console.log(rowHTML);
	        // Append the row created to the table body, which will refresh the page
	        dashBoardBodyElement.append(rowHTML);
	    
	    	}
	 if(resultData["status"]=="fail"){
		 alert(resultData["error_message"]);
	 }

	   
}
let movie = getParameterByName('movie');
let year= getParameterByName('year');
let director= getParameterByName('director');
let star= getParameterByName('star');
let bod_star= getParameterByName('bod_star');
let genre= getParameterByName('genre');
console.log(window.location.href);
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/dashBoard?movie="+movie+"&year="+year+"&director="+director+"&star="+star+"&bod_star="+bod_star+"&genre="+genre,
    success: (resultData) => handleDashResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});