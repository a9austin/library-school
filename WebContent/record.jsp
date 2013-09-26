<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js" ></script>
<script>
function load(){
	toggleButton();
}

function checkin(id,value){
	$.get('Checkin', {'id':id, 'row':value}, display_checkin);
}

function toggleButton(){
	if (name != "" && '${rec_id}' == '${id}'){
		$('button[name="checkin_button"]').prop('disabled', false);
	} else{
		$('button[name="checkin_button"]').prop('disabled', true);
	} 
}

function display_checkin(request){
	document.getElementById("mybook_table").deleteRow(request.row);
}

</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Record</title>
</head>
<body onload="load()">
<table id="mybook_table">
<tr><td>Title</td><td>Author</td><td>Check In</td></tr>
 <c:forEach var="i" begin="0" end="${fn:length(ids)-1}">
 <tr>
	<td>${titles[i]}</td><td>${authors[i]}</td>
	<td><button onclick="checkin(this.id,this.value)" type="button" id="${ids[i]}" name="checkin_button" value="${i+1}">Check In</button></td>
 </tr>
 </c:forEach>
</table>

<a href="index.jsp">Home</a>
<a href="patrons.html">Patrons</a>
</body>
</html>