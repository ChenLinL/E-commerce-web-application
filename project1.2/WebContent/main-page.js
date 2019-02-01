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
		result += "<li><a href=main-page.html?id=" + l1[i] + '>'+ l2[i] + '</a></li>'
	}
	result += "</ul></th>";
	return result;
}

function handleMovieResult(resultData) { 
    var rowHTML = document.getElementById("genre");
    
    for (let i = 0; i < resultData[0]['movie_genre'].length; i++)
    {
    	var link = '<a href= "single-movie.html' + '">' + resultData[0]['movie_genre'][i] + '</a>';
    	if (i != resultData[0]['movie_genre'].length - 1)
    		rowHTML.innerHTML += link + ",   ";
    	else
    		rowHTML.innerHTML += link;
    }
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/main-page", // Setting request url, which is mapped by StarsServlet in Stars.java
    success:handleMovieResult
    //success: (resultData) => handleMovieResult(resultData)// Setting callback function to handle data returned successfully by the StarsServlet
});