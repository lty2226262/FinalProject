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
        <link rel="stylesheet" media="screen" href="https://cdn.codementor.io/assets/css_new/home/get_help_now-b36da489d9936574fa9b718c05d03224.css" />
        <link rel="stylesheet" media="screen" href="https://cdn.codementor.io/assets/css_new/home/directory_new_for_clients-7a271478fbb18bde9958e42c708dd110.css" />
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
    	<div id="main" role="main" style="margin-top:0px;">
        	<div class="directory ng-scope" ng-controller="GetHelpNowCtrl as getHelpNowCtrl">
				<div class="resultBlock" ng-class="{resultOpacity:isSearch}">
		            <div class = "customizeContainer">
		           		<div class="switchBlock">
			                <div class="row-fluid">
			                    <div class ="span12" align="left">
			                        <h2 class="sub-title">
			                            <%=(String)request.getAttribute("beginEnd") %>
			                        </h2>
			                        <hr class="bg-splitter">
			                        <div class="expert-list" ng-hide="loaded" style="margin-top: 10px;">
			                        	 <% for(ArrayList<Long> i : (ArrayList<ArrayList<Long>>)request.getAttribute("result")) {%>
				                        	<div class="mentorModule">
				                        		<div class="row-fluid mentor-item-row">
			                        				 <p><%= i.toString() %>:)</p><br>
				                       				<div class="row-fluid dummy-layer">
														<div class="span1"></div>
														<div class="span7"></div>
														<div class="span2"></div>
														<div class="span2 lastColumn"></div>
													</div>
												</div>
											</div>
										<% } %>
									</div>
			                    </div>
			            	</div>
		                </div>
	                </div>
	            </div>
	        </div>
        </div>
    </body>
</html>