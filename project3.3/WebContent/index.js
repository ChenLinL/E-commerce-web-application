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

function replaceSpace(target){
	target = target.replace(/ /g,"%20");
	return target;
}

function handlefuntion()
{
	var userinput = document.getElementById('input').value;
	if (userinput < 0)
		alert("Quantity should be >= 0");
	else
	{
		console.log("GO TO ADD");
		return true;
	}
}


function createTheHtml(l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,n){
	result ='';
	result += "<th><ul>";
	num = Number(n);
	for(i = 0; i<num; i++)
	{
		result += "<li><a href=single-star.html?id="+ l1[i]+"&title="+l3+"&year="+l4+"&director="+l5+"&star="+l6+"&firstRecord="+l7+"&numRecord="+l8+"&sortType="+l9+"&sortOrder="+l10+"&title_i="+l11+"&genre="+replaceSpace(l12)+">"+ l2[i] + "</a></li>";
	}
	result += "</ul></th>";
	return result;
}

function test(){
	console.log("work");
}

function handleMovieResult(resultData) {
	
	console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	console.log(resultData);
	
    let searchInfoElement = jQuery('#Search');
    searchInfoElement.append("<p><a href=search.html> Search</a></p>");
    let mainPageElement = jQuery('#mainPage');
    mainPageElement.append("<p><a href=main-page.html> Back to main page</a></p>");
    let scInfoElement = jQuery('#ShopingCart');
    scInfoElement.append("<input type='button' value='Check Out' onclick=window.location=shopingCart.html>");
    let sortTypeInfoElement = jQuery('#SortType');
    sortTypeInfoElement.append('<div class="dropdown"><button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">sortType</button><div class="dropdown-menu" aria-labelledby="dropdownMenuButton"><a class="dropdown-item" href=index.html?title='+resultData[0]['mtitle']+'&year='+resultData[0]['myear']+"&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+resultData[0]['mfirstRecord']+"&numRecord="+resultData[0]['mnumRecord']+"&sortType=rating"+"&sortOrder="+resultData[0]['msortOrder']+"&title_i="+resultData[0]['mtitle_i']+"&genre="+resultData[0]['mgenre']
+'>Rating</a><a class="dropdown-item" href=index.html?title='+replaceSpace(resultData[0]['mtitle'])+"&num="+resultData[0]['num']+'&year='+resultData[0]['myear']+"&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+resultData[0]['mfirstRecord']+"&numRecord="+resultData[0]['mnumRecord']+"&sortType=title"+"&sortOrder="+resultData[0]['msortOrder']+"&title_i="+resultData[0]['mtitle_i']+"&genre="+resultData[0]['mgenre']
    +'>Title</a></div></div>');
    let sortOrderInfoElement = jQuery('#SortOrder');
    sortOrderInfoElement.append('<div class="dropdown"><button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">sortOrder</button><div class="dropdown-menu" aria-labelledby="dropdownMenuButton"><a class="dropdown-item" href=index.html?title='+resultData[0]['mtitle']+"&year="+resultData[0]['myear']+"&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+resultData[0]['mfirstRecord']+"&numRecord="+resultData[0]['mnumRecord']+"&sortType="+resultData[0]['msortType']+"&sortOrder=DESC"+"&title_i="+resultData[0]['mtitle_i']+"&genre="+resultData[0]['mgenre']
    		  +'>DESC</a><a class="dropdown-item" href=index.html?title='+replaceSpace(resultData[0]['mtitle'])+"&num="+resultData[0]['num']+"&year="+resultData[0]['myear']+"&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+resultData[0]['mfirstRecord']+"&numRecord="+resultData[0]['mnumRecord']+"&sortType="+resultData[0]['msortType']+"&sortOrder=ASC"+"&title_i="+resultData[0]['mtitle_i']+"&genre="+resultData[0]['mgenre']
	+'>ASC</a></div></div>');
    let ipInfoElement = jQuery('#items_per_page');
    
    let jq='<div class="dropdown"><button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">items per page</button><div class="dropdown-menu" aria-labelledby="dropdownMenuButton">';
    for(let i =1; i < 100;++i){
    jq+='<a class="dropdown-item" href=index.html?title='
    		+replaceSpace(resultData[0]['mtitle'])+"&year="+resultData[0]['myear']+"&director="+resultData[0]['mdirector']+
    		"&star="+resultData[0]['mstar']+"&firstRecord="+resultData[0]['mfirstRecord']+"&num="+resultData[0]['num']+"&numRecord="+
    		i+"&sortType="+resultData[0]['msortType']+"&sortOrder=DESC"+"&title_i="+resultData[0]['mtitle_i']+"&genre="+resultData[0]['mgenre']
    		  +">"+i+"</a>";
    }
    jq+='</div></div>';
    
    ipInfoElement.append(jq);
  
    let movieTableBodyElement = jQuery("#movie_table_body");
 
    //Iterate through resultData, no more than 20 entries
    var total_movie = parseInt(resultData[0]["num"]);
    var start =  parseInt(resultData[0]["mfirstRecord"]);
    var range =  parseInt(resultData[0]["mnumRecord"]);
    var total_pages = Math.round(total_movie/range);
    var next = 0;
    var prev = 0;
    if((start + range) >= total_pages){
    	next = total_movie - range;
    }
    else{
    	next = start + range;
    }
    if ((start - range)< 0){
    	prev = 0
    }
    else{
    	prev = start - range;
    }
    
    let pignaElement = jQuery("#pigna");
    let p ='<nav aria-label="Page navigation example">'+
      '<ul class="pagination">'+
      '<li class="page-item"><a class="page-link" href='+
      "index.html"+"?title="+replaceSpace(resultData[0]['mtitle'])+"&year="+resultData[0]['myear']+"&num="+parseInt(resultData[0]["num"])+
      "&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+
      prev+"&numRecord="+range+"&sortType="+resultData[0]['msortType']+
      "&sortOrder="+resultData[0]['msortOrder']+"&title_i="+resultData[0]["mtitle_i"]+"&genre="+resultData[0]["mgenre"]
      +">Previous</a></li>";
//    for(var j =1; j<=total_pages; j++){
//    	var mp = start+j;
//    	console.log(mp);
//    	p += '<li class="page-item"><a class="page-link" href='+
//    	"index.html"+"?title="+resultData[0]['mtitle']+"&year="+resultData[0]['myear']+"&num="+parseInt(resultData[0]["num"])+
//        "&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+
//        mp+"&numRecord="+range+"&sortType="+resultData[0]['msortType']+
//        "&sortOrder="+resultData[0]['msortOrder']+"&title_i="+resultData[0]["mtitle_i"]+"&genre="+resultData[0]["mgenre"]	
//    	+">"+j+"</a></li>";
//    }
   
     p+='<li class="page-item"><a class="page-link" href='+
     "index.html"+"?title="+replaceSpace(resultData[0]['mtitle'])+"&year="+resultData[0]['myear']+"&num="+parseInt(resultData[0]["num"])+
     "&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+
     next+"&numRecord="+range+"&sortType="+resultData[0]['msortType']+
     "&sortOrder="+resultData[0]['msortOrder']+"&title_i="+resultData[0]["mtitle_i"]+"&genre="+resultData[0]["mgenre"]
     +">Next</a></li>"
     +"</ul>"+
     "</nav>";
     
     pignaElement.append(p);
    for (let i = 0; i < resultData.length; i++) {
    	
    	// Concatenate the html tags with resultData jsonObject
    	let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML += "<th>" 
        	+ "<a href=single-movie.html?id=" + resultData[i]['movie_id'] +"&title="+replaceSpace(resultData[0]['mtitle'])+"&year="+resultData[0]['myear']+"&director="+resultData[0]['mdirector']+"&star="+resultData[0]['mstar']+"&firstRecord="+resultData[0]['mfirstRecord']+"&numRecord="+resultData[0]['mnumRecord']+"&sortType="+resultData[0]['msortType']+"&sortOrder="+resultData[0]['msortOrder']+"&title_i="+resultData[0]['mtitle_i']+"&genre="+replaceSpace(resultData[0]['mgenre']) +">"
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
        //rowHTML += "<th>" + "<input type=text name=quantity id=input>" + "</th>";
       //rowHTML += "<th>" + "<button onclick=handlefuntion()>Add to Cart</button>" + "/<th>";
        
       
        	
        	//window.location.replace("shopingCart.html?"+"item="+ replaceSpace(resultData[i]['movie_title'])+"&id="+resultData[i]['movie_id']+"&func=add");
        
        rowHTML += "<th>" 
        	+ "<a href=shopingCart.html?"+"item="+ replaceSpace(resultData[i]['movie_title'])+"&id="+resultData[i]['movie_id']+"&func=add>"      	
        	+ "add to the Shopping cart" + 
        	"</a>" + 
        	"</th>";
 		
        rowHTML += "</tr>";
        console.log(rowHTML);
        // Append the row created to the table body, which will refresh the page
        movieTableBodyElement.append(rowHTML);
        //$("#infor_table").submit((event) => submitSearchForm(event));
    
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
console.log(genre);
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/movies?title="+mTitle+"&year="+mYear+"&director="+mDirector+"&star="+mStar+"&firstRecord="+firstRecord+"&numRecord="+numRecord+"&sortType="+sortType+"&sortOrder="+sortOrder+"&title_i="+title_i+"&genre="+genre, // Setting request url, which is mapped by StarsServlet in Stars.java
    //success:handleMovieResult
    success: (resultData) => handleMovieResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});
//$("#infor_table").submit((event) => submitSearchForm(event));