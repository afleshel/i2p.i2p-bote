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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="ib" uri="I2pBoteTags" %>
 
<ib:message key="New Email" var="title" scope="request"/>
<jsp:include page="header.jsp"/>

<div class="main">

<ib:requirePassword>
<jsp:useBean id="jspHelperBean" class="i2p.bote.web.JSPHelper"/>
<c:set var="configuration" value="${jspHelperBean.configuration}"/>
<ib:sendEmail sender="${param.sender}" subject="${param.subject}" message="${param.message}" includeSentTime="${configuration.includeSentTime}">
    <c:forEach var="parameter" items="${ib:getSortedRecipientParams(param)}">
        <c:set var="recipientIndex" value="${fn:substringAfter(parameter.key, 'recipient')}"/>
        <c:set var="recipientTypeAttrName" value="recipientType${recipientIndex}"/>
        <c:if test="${not empty parameter.value}">
            <ib:recipient type="${param[recipientTypeAttrName]}" address="${parameter.value}"/>
        </c:if>
    </c:forEach>
    
    <c:forEach var="parameter" items="${param}">
        <c:if test="${fn:startsWith(parameter.key, 'attachmentNameOrig')}">
            <c:set var="attachmentIndex" value="${fn:substringAfter(parameter.key, 'attachmentNameOrig')}"/>
            <c:set var="tempFileParamName" value="attachmentNameTemp${attachmentIndex}"/>
            <ib:attachment origFilename="${parameter.value}" tempFilename="${param[tempFileParamName]}"/>
        </c:if>
    </c:forEach>
</ib:sendEmail>
</ib:requirePassword>

</div>

<jsp:include page="footer.jsp"/>
