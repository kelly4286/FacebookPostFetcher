<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="init.jsp" %>
<html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>貼文</title>
</head>
<body>
	<h1>貼文</h1>
	<a class="toHome" href="./index.jsp">回到首頁</a>
	<hr />
	<c:if test="${storeMethod=='toFile'}">
		<form action="./download" method="get">
			<input type="submit" class="btn btn-default" name="get" value="下載檔案"/>
		</form> 
	</c:if>
	<ul>
		<c:forEach var="post" items="${postList}">
		<li>${post.getFieldValue("id")}</li>
		<ul>
			<c:forEach var="field" items="${displayFields}">
				<c:if test="${field!='id'}">
				<li>${field} = 
					<c:if test="${post.getFieldAttribute(field)=='TEXT'}">
						${post.getFieldValue(field)}
					</c:if>
					<c:if test="${post.getFieldAttribute(field)=='DATE'}">
						${post.getFieldValue(field).toString()}
					</c:if>
					<c:if test="${post.getFieldAttribute(field)=='PICTURE'}">
						<img src="${post.getFieldValue(field)}"/>
					</c:if>
					<c:if test="${post.getFieldAttribute(field)=='LINK'}">
						<a href="${post.getFieldValue(field)}">${post.getFieldValue(field)}</a>
					</c:if>
				</li>
				</c:if>
			</c:forEach>
		</ul>
		</c:forEach>
	</ul>
	
	<form action="./getPosts" method="post" class="form-inline">
		<input type="hidden" name="previousPageUrl" value="${previousPageUrl}" >
		<input type="hidden" name="nextPageUrl" value="${nextPageUrl}" >
		<input type="hidden" name="paramString" value="${paramString}" >
		<input type="hidden" name="pageNumber" value="${pageNumber}" >
		<input type="hidden" name="method" value="batch">
		<c:if test="${hasPreviousPage and pageNumber != 1}">
			<input type="submit" class="btn btn-primary" name="post" value="上一頁" />
		</c:if>
		<c:if test="${hasNextPage}">
			<input type="submit" class="btn btn-primary" name="post" value="下一頁" />
		</c:if>
	</form>	
</body>
</html>