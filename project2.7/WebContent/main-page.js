/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

<<<<<<< HEAD
=======
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

>>>>>>> f3409eb8b828f8fd936bc16f6e3a5ef950bd3046
function handleMovieResult(resultData) { 
	
    var rowHTML = document.getElementById("genre");
    var title_rowHTML = document.getElementById("title");
    var alphabet_rowHTML = document.getElementById("alphabet");
    
<<<<<<< HEAD
    for (let i = 0; i < resultData[3]['movie_genre'].length; i++)
    {
    	var link = '<a href= "index.html?title=null&year=null&director=null&star=null&firstRecord=0&numRecord=5&sortType=rating&sortOrder=DESC&title_i=null&genre=' + resultData[3]['movie_genre'][i] + '">' + resultData[3]['movie_genre'][i] + '</a>';
    	rowHTML.innerHTML += link + "\u00A0 \u00A0 ";
    }
    
    for (let i = 0; i < resultData[3]['movie_title'].length; i++)
    {
    	if (isNaN(resultData[3]['movie_title'][i]))
    	{
    		resultData[3]['movie_title'][i] = resultData[3]['movie_title'][i].toUpperCase();
    	}
    	
    	var link = '<a href= "index.html?title=null&year=null&director=null&star=null&firstRecord=0&numRecord=5&sortType=rating&sortOrder=DESC&title_i=' + resultData[3]['movie_title'][i]+"&genre=null" +
		'">' + resultData[3]['movie_title'][i] + '</a>';
    	
    	if (!isNaN(resultData[3]['movie_title'][i]))
=======
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
>>>>>>> f3409eb8b828f8fd936bc16f6e3a5ef950bd3046
    		title_rowHTML.innerHTML += link + "\u00A0 \u00A0 ";
    	else
    		alphabet_rowHTML.innerHTML += link + "\xa0 \xa0 ";
    }
    
<<<<<<< HEAD
    let movieInfoElement = jQuery("#movie_table_by_rating");
    let movieTableBodyElement = jQuery("#movie_rating_table_body");
    
 for (let i = 0; i < resultData.length-1; i++) {
    	
        let rowHTML = "";
        	rowHTML += "<tr>";
        	rowHTML += "<th scope=row>" + "Title: " + resultData[i]["top_movie_title"] + "</th>";
        	rowHTML += "</tr>";
        let yearHTML="";
         	yearHTML += "<tr>";
         	yearHTML += "<th scope=row>" + "Released Year: " + resultData[i]["top_movie_year"] + "</th>";
         	yearHTML += "</tr>";
        let directorHTML="";
          	directorHTML += "<tr>";
          	directorHTML += "<th scope=row>" + "Director: " + resultData[i]["top_movie_director"] + "</th>";
          	directorHTML += "</tr>";
          	
        let genreHTML="";
         	genreHTML += "<tr>";
         	genreHTML += "<th scope=row>" + "Genre: " + resultData[i]["top_movie_genre"] + "</th>";
         	genreHTML += "</tr>";
        
        let starHTML="";
        	starHTML += "<tr>";
        	starHTML += "<th scope=row>" + "Stars: " + resultData[i]["top_movie_star"] + "</th>";
         	starHTML += "</tr>";
         	
        let ratingHTML="";
          	ratingHTML += "<tr>";
          	ratingHTML += "<th scope=row>" + "Rating: " + resultData[i]["top_movie_rating"] + "</th>";
          	ratingHTML += "</tr>";
          	
        let gradient="";
         	gradient += "<div id=grad1>";
         	gradient += "</div>"
        movieTableBodyElement.append(rowHTML, yearHTML, directorHTML, genreHTML, starHTML, ratingHTML, gradient);
    }
    
=======
>>>>>>> f3409eb8b828f8fd936bc16f6e3a5ef950bd3046
}

jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/main-page", // Setting request url, which is mapped by StarsServlet in Stars.java
    success:handleMovieResult
    //success: (resultData) => handleMovieResult(resultData)// Setting callback function to handle data returned successfully by the StarsServlet
});