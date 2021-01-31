<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ include file="init.jsp" %>
<html>
<head>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
<title>登入 Facebook</title>
</head>
<body>
	<tag:notloggedin>
		<h2>登入 Facebook</h2>
		<a href="signin"><img src="./assets/login-with-facebook.png" alt="Sign in with Facebook"></a>
		<hr />
	</tag:notloggedin>
	<tag:loggedin>
		<h1>歡迎  ${facebook.name}</h1>
		<a class="toHome" href="./logout">登出</a>
		<hr />
		
		<form action="./getPosts" method="post" class="form-inline" >
		<div class="form-group">
			<label class="control-label">抓取哪種貼文：</label>
			<label><input type="radio" class="form-control" name="target" value="myPage" checked>個人塗鴉牆</label>  
			<label><input type="radio" class="form-control" name="target" value="publicPage">公開粉絲頁，粉絲頁ID：</label> <input type="text" name="pageId">
			<br/>
			<label class="control-label">抓取數量：</label>
			<label><input type="radio" class="form-control" name="method" value="batch" checked>批次，一次抓取25個</label>  
			<label><input type="radio" class="form-control" name="method" value="overall">總數，</label> <input type="text" name="limit">
  			<br/>
  			<label class="control-label">欄位：</label><br/>
  			<label><input type="checkbox" class="form-control" name="displayFields" value="message">Message</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="created_time">Created Time</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="description">Description</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="from">From</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="full_picture">Full Picture</label>  
  			<label><input type="checkbox" class="form-control disabled" name="displayFields" value="id" checked>Id</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="icon">Icon</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="link">Link</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="name">Name</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="picture">Picture</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="place">Place</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="privacy">Privacy</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="source">Source</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="story">Story</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="type">Type</label>  
  			<label><input type="checkbox" class="form-control" name="displayFields" value="updated_time">Updated Time</label>  
  			<br/>
  			<label><input type="radio" class="form-control" name="storeMethod" value="toDb">存入資料庫</label>  
  			<label><input type="radio" class="form-control" name="storeMethod" value="toFile">存成檔案</label>  
  			<br/>
  			<input type="submit" class="btn btn-primary" name="post" value="送出" />
		</div>
		</form>
	</tag:loggedin>
</body>
</html>