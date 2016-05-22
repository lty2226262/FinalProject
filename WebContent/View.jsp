<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import = "java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

    <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            Make your voice heard.
        </title>
        <link rel="stylesheet" media="screen" href="assets/bootstrap/css/bootstrap.min.css" />
        <link rel="stylesheet" media="screen" href="assets/layout_wide.css" />
        <link rel="stylesheet" media="screen" href="https://cdn.codementor.io/assets/css_new/home/index-9712a1604a197e9030636297c0bbdb53.css" />

        
    </head>
    <body class="minibar" ng-controller="MainCtrl">
        <div class="navbar menu_new_v2" id="main-header" style="margin-bottom:0px;">
            <div class="navbar-inner notFixed">
                <div class="container">
                    <ul class="nav pull-left">
                        <li>
                            <a href="#">Hello World</a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        
        <div class="innerBlock blockDecoration">
            <div class = "customizeContainer">
                <div class="row-fluid">
                    <div class ="span12" align="left">
                        <% for(ArrayList<Long> i : (ArrayList<ArrayList<Long>>)request.getAttribute("result")) {%>
						      <p><%= i.toString() %>:)</p><br>
						<% } %>
                    </div>
                </div>
            </div>
        </div>
        
    </body>
</html>