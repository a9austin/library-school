<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" ></script>

<script>
var search_by_title = true;
var i = 0;
var books_display = 10;
var pages_length = 0;
var titles = null;
var authors = null; 
var libraryID = null;
var name = "";
var userid = "";
var error = "";

function login_function(){
	var login_id = $('#login_id').val();
	$.get('Login', {"login_id":login_id}, display_login);
}

function display_login(request){
	name = request.name;
	userid = request.userid;
	error = request.error;
	$('#login_id').val(' ');
	if (name != "" || name != "undefined") 
	{
		document.getElementById('login_display').innerHTML = name;
	}
	document.getElementById('error_display').innerHTML = error;
	enableFunctions();
}


function enableFunctions(){
	highlight();
	enableCheckOut();
	enableRecord();
	
	// Display Login 
	document.getElementById('login_display').innerHTML = name;
	document.getElementById('error_display').innerHTML = error;
}

function enableCheckOut(){
	// Check if a user is logged in 
	if (name != ""){
		$('button[name="checkout_button"]').prop('disabled', false);
		$('button[value="Checked out"]').prop('disabled', true);
	} else{
		$('button[name="checkout_button"]').prop('disabled', true);
	}
}

function enableRecord(){
	if (name != ""){
		document.getElementById('display_record').innerHTML = '<a href=Record?hidden_id='+ userid +'>My Record</a>';		
	}else{
		document.getElementById('display_record').innerHTML = '';
	}
}

function highlight(){
	if (search_by_title){
		$('#title_td').css("background-color","yellow");
		$('#author_td').css("background-color","white");
	} else{
		$('#title_td').css("background-color","white");
		$('#author_td').css("background-color","yellow");
	}
}

function load_table(){
	$.get('LoadBooks', display_ten);
}

function checkout(val){
	var id = '';
	// Parse ID String 
	for (var i=0; i < val.length; i++) { 
	     //console.log(texto.charAt(i));
	     if (val.charAt(i) == '_'){
	    	 break;
	     }
	     id += val.charAt(i);
	}
	$.get('Checkout', {"book_id":id, 'login_id':userid}, display_checkout);
}

function display_checkout(request){
	// Refresh the table depending on what has been searched, which will disable the checked out. 
	var search = $('#search_textbox_id').val();
	refresh(search);
	
}

function display_ten(request){
	titles = request.titles;
	authors = request.authors;
	checked = request.checked;
	ids = request.ids;
	pages_length = titles.length;
	name = request.name;
	userid = request.userid;

	 
	if (pages_length < 10){
		i = 0;
	}else{
		i = books_display-10;	
	}
	
	
	var table = '<table> <tr><td id="title_td"><button onclick="order(this.value)" type="button" value="title" id="title_button">Title</button></td>'
	+ '<td id="author_td"><button onclick="order(this.value)" type="button" value="author" id="author_button">Author</button></td><td>Checked Out</td></tr>';
	while(i < books_display){
		if (i == pages_length){
			break;
		}
		table += '<tr><td>'+ titles[i] +'</td><td>' + authors[i] + '</td><td>' 
		+ '<button onclick="checkout(this.id)" type="button" id="' + ids[i] + '_checkout_id" name="checkout_button" value="'+checked[i]+'"  enabled>'+checked[i]+'</button>'  
		+ '</td><td></tr>';
		i++;
	}
	table += '</table>';
	document.getElementById('book_table').innerHTML = table;
	enableFunctions();
	

}

function display_next(){
	if (books_display < pages_length){
		i = books_display;
		books_display = books_display + 10;
		
		var table = '<table> <tr><td id="title_td"><button onclick="order(this.value)" type="button" value="title" id="title_button">Title</button></td>'
		+ '<td id="author_td"><button onclick="order(this.value)" type="button" value="author" id="author_button">Author</button></td><td>Checked Out</td></tr>';
		while(i < books_display){
			if (i == pages_length){
				break;
			}
			table += '<tr><td>'+ titles[i] +'</td><td>' + authors[i] + '</td><td>' 
			+ '<button onclick="checkout(this.id)" type="button" id="' + ids[i] + '_checkout_id" name="checkout_button" value="'+checked[i]+'"  enabled>'+checked[i]+'</button>'  
			+ '</td><td></tr>';
			i++;
		}
		table += '</table>';
		document.getElementById('book_table').innerHTML = table;
		enableFunctions();
	}
}

function display_previous(){
	if (books_display > 10){
		books_display = books_display - 10;
		i = books_display - 10;
		
		var table = '<table> <tr><td id="title_td"><button onclick="order(this.value)" type="button" value="title" id="title_button">Title</button></td>'
		+ '<td id="author_td"><button onclick="order(this.value)" type="button" value="author" id="author_button">Author</button></td><td>Checked Out</td></tr>';
		while(i < books_display){
			if (i == pages_length){
				break;
			}
			table += '<tr><td>'+ titles[i] +'</td><td>' + authors[i] + '</td><td>' 
			+ '<button onclick="checkout(this.id)" type="button" id="' + ids[i] + '_checkout_id" name="checkout_button" value="'+checked[i]+'"  enabled>'+checked[i]+'</button>'  
			+ '</td><td></tr>';
			i++;
		}
		table += '</table>';
		document.getElementById('book_table').innerHTML = table;
		enableFunctions();
	}
}

function refresh(val){
	var selected = $('input:radio[name=search_type]:checked').val();
	$.get('Search',{type:selected, prefix:val, sort_title:search_by_title}, display_ten);
	pages_display = 10;
}

function order(val){
	var search = $('#search_textbox_id').val();
	if (val == 'title'){
		search_by_title = true;
		refresh(search);
		$('#title_td').css("background-color","yellow");
		$('#author_td').css("background-color","white");
	}
	else if (val == 'author'){
		search_by_title = false;
		refresh(search);
		$('#title_td').css("background-color","white");
		$('#author_td').css("background-color","yellow");
	}
}

</script>
 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Welcome</title>
</head>
<body onload="load_table()">
<!-- <span id="login_name"></span> -->
<font color="green"><span id="login_display"></span></font>
<font color="red"><span id="error_display"></span></font>
<!-- <form action="Login" method="post"> -->
<form>
Login: <input type="text" id="login_id" name="login_id"> <br>
<input onclick="login_function()" type="button" id="login_button" value="Login"><br><br>
Search: <input type="text" id="search_textbox_id" onkeyup="refresh(this.value)"> <br>
Title <input type="radio" name="search_type" value="title" checked="checked">
Author <input type="radio" name="search_type" value="author"><br>
<span id="book_table"></span>
<button onclick="display_previous()" type="button" id="prev_button">Previous</button>
<button onclick="display_next()" type="button" id="next_button">Next</button>
<button type="button" id="reset_button">Reset</button>
</form>

<a href="patrons.html">Patrons</a>
<span id="display_record"></span>
<br>
<font color="red"><span id="error"></span></font>
</body>
</html>