<%--
 Copyright (C) 2009  HungryHobo@mail.i2p
 
 The GPG fingerprint for HungryHobo@mail.i2p is:
 6DD3 EAA2 9990 29BC 4AD2 7486 1E2C 7B61 76DC DC12
 
 This file is part of I2P-Bote.
 I2P-Bote is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.
 
 I2P-Bote is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 
 You should have received a copy of the GNU General Public License
 along with I2P-Bote.  If not, see <http://www.gnu.org/licenses/>.
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ib" uri="I2pBoteTags" %>

<%
    pageContext.setAttribute("NOT_STARTED", i2p.bote.network.NetworkStatus.NOT_STARTED);
    pageContext.setAttribute("DELAY", i2p.bote.network.NetworkStatus.DELAY);
    pageContext.setAttribute("CONNECTING", i2p.bote.network.NetworkStatus.CONNECTING);
    pageContext.setAttribute("CONNECTED", i2p.bote.network.NetworkStatus.CONNECTED);
    pageContext.setAttribute("ERROR", i2p.bote.network.NetworkStatus.ERROR);
%> 

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="i2pbote.css" />
    <meta http-equiv="refresh" content="20" />
</head>

<body style="background-color: transparent; margin: 0px;">

<div class="statusbox">
    <c:set var="connStatus" value="${ib:getNetworkStatus()}"/>
    <c:choose>
        <c:when test="${connStatus == NOT_STARTED}"><img src="images/redsquare.png"/> Not Started</c:when>
        <c:when test="${connStatus == DELAY}"><img src="images/yellowsquare.png"/> Waiting 3 Minutes...<br/>
            <div style="text-align: center">
                <form action="connect.jsp" target="_top" method="GET">
                    <button type="submit">Connect Now</button>
                </form>
            </div>
        </c:when>
        <c:when test="${connStatus == CONNECTING}"><img src="images/yellowsquare.png"/> Connecting...</c:when>
        <c:when test="${connStatus == CONNECTED}"><img src="images/greensquare.png"/> Connected</c:when>
        <c:when test="${connStatus == ERROR}"><img src="images/redsquare.png"/> Error</c:when>
    </c:choose>
</div>

</body>
</html>