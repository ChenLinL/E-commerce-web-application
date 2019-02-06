/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function homeFunction()
{
	
}

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
    var title_rowHTML = document.getElementById("title");
    var alphabet_rowHTML = document.getElementById("alphabet");
    
    for (let i = 0; i < resultData[0]['movie_genre'].length; i++)
    {
    	var link = '<a href= "index.html?title=null&year=null&director=null&star=null&firstRecord=0&numRecord=5&sortType=rating&sortOrder=DESC&title_i=null&genre=' + resultData[0]['movie_genre'][i] + '">' + resultData[0]['movie_genre'][i] + '</a>';
    	//if (i != resultData[0]['movie_genre'].length - 1)
    	rowHTML.innerHTML += link + "\u00A0 \u00A0 ";
    	//else
    	//	rowHTML.innerHTML += link;
    }
    
    for (let i = 0; i < resultData[0]['movie_title'].length; i++)
    {
    	if (isNaN(resultData[0]['movie_title'][i]))
    	{
    		resultData[0]['movie_title'][i] = resultData[0]['movie_title'][i].toUpperCase();
    	}
    	
    	var link = '<a href= "index.html?title=null&year=null&director=null&star=null&firstRecord=0&numRecord=5&sortType=rating&sortOrder=DESC&title_i=' + resultData[0]['movie_title'][i]+"&genre=null" +
		'">' + resultData[0]['movie_title'][i] + '</a>';
    	
    	if (!isNaN(resultData[0]['movie_title'][i]))
    		title_rowHTML.innerHTML += link + "\u00A0 \u00A0 ";
    	else
    		alphabet_rowHTML.innerHTML += link + "\xa0 \xa0 ";
    }
    
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/main-page", // Setting request url, which is mapped by StarsServlet in Stars.java
    success:handleMovieResult
    //success: (resultData) => handleMovieResult(resultData)// Setting callback function to handle data returned successfully by the StarsServlet
});