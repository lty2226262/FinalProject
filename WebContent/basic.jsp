<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Director | Dashboard</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
    <meta name="description" content="Developed By M Abdur Rokib Promy">
    <meta name="keywords" content="Admin, Bootstrap 3, Template, Theme, Responsive">
    <!-- bootstrap 3.0.2 -->
    <link href="css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <!-- font Awesome -->
    <link href="css/font-awesome.min.css" rel="stylesheet" type="text/css" />
    <!-- Ionicons -->
    <link href="css/ionicons.min.css" rel="stylesheet" type="text/css" />
    <!-- Morris chart -->
    <link href="css/morris/morris.css" rel="stylesheet" type="text/css" />
    <!-- jvectormap -->
    <link href="css/jvectormap/jquery-jvectormap-1.2.2.css" rel="stylesheet" type="text/css" />
    <!-- Date Picker -->
    <link href="css/datepicker/datepicker3.css" rel="stylesheet" type="text/css" />
    <!-- fullCalendar -->
    <!-- <link href="css/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" /> -->
    <!-- Daterange picker -->
    <link href="css/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css" />
    <!-- iCheck for checkboxes and radio inputs -->
    <link href="css/iCheck/all.css" rel="stylesheet" type="text/css" />
    <!-- bootstrap wysihtml5 - text editor -->
    <!-- <link href="css/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" rel="stylesheet" type="text/css" /> -->
    <link href='http://fonts.googleapis.com/css?family=Lato' rel='stylesheet' type='text/css'>
    <!-- Theme style -->
    <link href="css/style.css" rel="stylesheet" type="text/css" />
    <script src="js/echarts.min.js"></script>



    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
          <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
          <![endif]-->

          <style type="text/css">

          </style>
      </head>
      <body class="skin-black">
        <!-- header logo: style can be found in header.less -->
        <header class="header">
            <a href="index.html" class="logo">
                Lighthouse
            </a>
            <!-- Header Navbar: style can be found in header.less -->
            <nav class="navbar navbar-static-top" role="navigation">
                <!-- Sidebar toggle button-->
                <a href="#" class="navbar-btn sidebar-toggle" data-toggle="offcanvas" role="button">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </a>
                <div class="navbar-right">
                    <ul class="nav navbar-nav">
                        <!-- Messages: style can be found in dropdown.less-->
                        <li class="dropdown tasks-menu">
                            <ul class="dropdown-menu">
                                <li>
                                    <!-- inner menu: contains the actual data -->
                                    <ul class="menu">
                                        <li><!-- Task item -->
                                            <a href="#">
                                                <h3>
                                                    Design some buttons
                                                    <small class="pull-right">20%</small>
                                                </h3>
                                                <div class="progress progress-striped xs">
                                                    <div class="progress-bar progress-bar-success" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">20% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li><!-- end task item -->
                                        <li><!-- Task item -->
                                            <a href="#">
                                                <h3>
                                                    Create a nice theme
                                                    <small class="pull-right">40%</small>
                                                </h3>
                                                <div class="progress progress-striped xs">
                                                    <div class="progress-bar progress-bar-danger" style="width: 40%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">40% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li><!-- end task item -->
                                        <li><!-- Task item -->
                                            <a href="#">
                                                <h3>
                                                    Some task I need to do
                                                    <small class="pull-right">60%</small>
                                                </h3>
                                                <div class="progress progress-striped xs">
                                                    <div class="progress-bar progress-bar-info" style="width: 60%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">60% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li><!-- end task item -->
                                        <li><!-- Task item -->
                                            <a href="#">
                                                <h3>
                                                    Make beautiful transitions
                                                    <small class="pull-right">80%</small>
                                                </h3>
                                                <div class="progress progress-striped xs">
                                                    <div class="progress-bar progress-bar-warning" style="width: 80%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">80% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li><!-- end task item -->
                                    </ul>
                                </li>
                                <li class="footer">
                                    <a href="#">View all tasks</a>
                                </li>
                            </ul>
                        </li>
                        <!-- User Account: style can be found in dropdown.less -->
                        <li class="dropdown user user-menu">
                            <ul class="dropdown-menu dropdown-custom dropdown-menu-right">
                                <li class="dropdown-header text-center">Account</li>

                                <li>
                                    <a href="#">
                                    <i class="fa fa-clock-o fa-fw pull-right"></i>
                                        <span class="badge badge-success pull-right">10</span> Updates</a>
                                    <a href="#">
                                    <i class="fa fa-envelope-o fa-fw pull-right"></i>
                                        <span class="badge badge-danger pull-right">5</span> Messages</a>
                                    <a href="#"><i class="fa fa-magnet fa-fw pull-right"></i>
                                        <span class="badge badge-info pull-right">3</span> Subscriptions</a>
                                    <a href="#"><i class="fa fa-question fa-fw pull-right"></i> <span class=
                                        "badge pull-right">11</span> FAQ</a>
                                </li>

                                <li class="divider"></li>

                                    <li>
                                        <a href="#">
                                        <i class="fa fa-user fa-fw pull-right"></i>
                                            Profile
                                        </a>
                                        <a data-toggle="modal" href="#modal-user-settings">
                                        <i class="fa fa-cog fa-fw pull-right"></i>
                                            Settings
                                        </a>
                                        </li>

                                        <li class="divider"></li>

                                        <li>
                                            <a href="#"><i class="fa fa-ban fa-fw pull-right"></i> Logout</a>
                                        </li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </nav>
                </header>
                <div class="wrapper row-offcanvas row-offcanvas-left">
                    <!-- Left side column. contains the logo and sidebar -->
                    <aside class="left-side sidebar-offcanvas">
                        <!-- sidebar: style can be found in sidebar.less -->
                        <section class="sidebar">
                            <!-- sidebar menu: : style can be found in sidebar.less -->
                            <ul class="sidebar-menu">
                                <li class="active">
                                    <a href="View.jsp">
                                        <i class="fa fa-globe"></i> <span>Research Field</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="general.jsp">
                                        <i class="fa fa-globe"></i> <span>Famous Author</span>
                                    </a>
                                </li>

                                <li>
                                    <a href="basic.jsp">
                                        <i class="fa fa-globe"></i> <span>Famous Affiliation</span>
                                    </a>
                                </li>

                                <li>
                                    <a href="simple.jsp">
                                        <i class="fa fa-globe"></i> <span>Famous Article</span>
                                    </a>
                                </li>
                                <li>
                                    <a href="hard.jsp">
                                        <i class="fa fa-globe"></i> <span>Popular Words</span>
                                    </a>
                                </li>

                            </ul>
                        </section>
                        <!-- /.sidebar -->
                    </aside>

                    <aside class="right-side">

                <!-- Main content -->
                <section class="content">

                    <div class="row" style="margin-bottom:5px;">


                        <div class="col-md-3" style="width: 100%; position: relative; padding-bottom: 66%;">
                            <div class="sm-st clearfix">
                                <div class="sm-st-info">
                                    <div id="main" style="width: 900px; height: 600px;"></div>
										<jsp:useBean id="user" class="handsome.is.joey.servlet.UserBean"></jsp:useBean>
										<p id="option" hidden><jsp:getProperty property="option3" name="user"/> </p>
                                    <script type="text/javascript">
                                        // åºäºåå¤å¥½çdomï¼åå§åechartså®ä¾
                                        var myChart = echarts.init(document.getElementById('main'));


                                        //
										var tempoption = document.getElementById('option').innerHTML;
										function stringToJson(stringValue) 
										{ 
										eval("var theJsonValue = "+stringValue); 
										return theJsonValue; 
										} 
                                        option = stringToJson(tempoption);
                                        /* option = {
                                            tooltip : {
                                                trigger : 'axis'
                                            },
                                            legend : {
                                                data : [ 'é®ä»¶è¥é', 'èçå¹¿å', 'è§é¢å¹¿å', 'ç´æ¥è®¿é®', 'æç´¢å¼æ' ,'é®ä»¶è¥é1', 'èçå¹¿å1', 'è§é¢å¹¿å1', 'ç´æ¥è®¿é®1', 'æç´¢å¼æ1']
                                            },
                                            grid : {
                                                left : '3%',
                                                right : '4%',
                                                bottom : '3%',
                                                containLabel : true
                                            },
                                            xAxis : {
                                                type : 'category',
                                                boundaryGap : false,
                                                data : [ 'å¨ä¸', 'å¨äº', 'å¨ä¸', 'å¨å', 'å¨äº', 'å¨å­', 'å¨æ¥' ]
                                            },
                                            yAxis : {
                                                type : 'value'
                                            },
                                            series : [ {
                                                name : 'é®ä»¶è¥é',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 120, 132, 101, 134, 90, 230, 210 ]
                                            }, {
                                                name : 'èçå¹¿å',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 220, 182, 191, 234, 290, 330, 310 ]
                                            }, {
                                                name : 'è§é¢å¹¿å',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 150, 232, 201, 154, 190, 330, 410 ]
                                            }, {
                                                name : 'ç´æ¥è®¿é®',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 320, 332, 301, 334, 390, 330, 320 ]
                                            }, {
                                                name : 'æç´¢å¼æ',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 820, 932, 901, 934, 1290, 1330, 1320 ]
                                            }, 
                                                     {
                                                name : 'é®ä»¶è¥é1',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 120, 132, 101, 134, 90, 230, 210 ]
                                            }, {
                                                name : 'èçå¹¿å1',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 220, 182, 191, 234, 290, 330, 310 ]
                                            }, {
                                                name : 'è§é¢å¹¿å1',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 150, 232, 201, 154, 190, 330, 410 ]
                                            }, {
                                                name : 'ç´æ¥è®¿é®1',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 320, 332, 301, 334, 390, 330, 320 ]
                                            }, {
                                                name : 'æç´¢å¼æ1',
                                                type : 'line',
                                                stack : 'æ»é',
                                                data : [ 820, 932, 901, 934, 1290, 1330, 1320 ]
                                            }]
                                        }; */
                                //
                                        // ä½¿ç¨åæå®çéç½®é¡¹åæ°æ®æ¾ç¤ºå¾è¡¨ã
                                        myChart.setOption(option);
                                    </script>

                                </div>
                            </div>
                        </div>
                    </div>
              </section>
            </aside><!-- /.right-side -->

        </div><!-- ./wrapper -->
</body>
</html>