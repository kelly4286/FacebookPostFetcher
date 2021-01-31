package com.kelly.service;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.kelly.model.ReturnPostData;
import com.kelly.util.Configuration;
import com.kelly.util.Constants;
import com.kelly.util.DbConnector;
import com.kelly.util.FileProcessor;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Post;

import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.auth.AccessToken;
import facebook4j.internal.logging.Logger;

public class GetPostsServlet extends HttpServlet {
	private static final long serialVersionUID = 4179545353414298791L;
	final static Logger logger = Logger.getLogger(GetPostsServlet.class);
	private FacebookClient facebookClient = null;		// restfb的Facebook Client
	private Configuration configuration = null;			// config.properties中的config資料
	private DbConnector dbConnection = null;			// DB connection
	private FileProcessor fileProcessor = null;			// File processor
	private String storeMethod = "";					// user所選的儲存方法
	private AccessToken info = null;					// facebook access token
	String[] displayFields = null;						// user欲顯示的欄位
	private String paramString = "";					// 欄位query字串
	private Connection<Post> myPosts = null;			// query回來之post資料
	

	/**
	 * 處理前端Form所傳回之資料的相關動作
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		List<ReturnPostData> outputList = null;	// ReturnPostData物件清單
		int pageNumber = 1;						// 目前的分頁號碼
		int limit = 25;							// 最多幾篇貼文
		
		// 得到facebook access token
		getFacebookUserToken(request);

		if (request.getParameter("method").equalsIgnoreCase("overall")) {
			
			// 如果是一次抓取所有資料
			limit = Integer.valueOf(request.getParameter("limit"));

			initialize(request);

			outputList = getOutputDataList(myPosts, displayFields, limit);
			setAttributes(request, displayFields, paramString, outputList, pageNumber, false, false, "", "");

		} else {
			
			// 以批次方式抓取資料
			if (request.getParameter("post").equalsIgnoreCase("下一頁")) {
				
				// 處理下一頁的資料
				paramString = request.getParameter("paramString");
				displayFields = paramString.split(",");
				String nextPageUrl = request.getParameter("nextPageUrl");
				nextPageUrl += "&access_token=" + info.getToken();
				myPosts = facebookClient.fetchConnectionPage(nextPageUrl, Post.class);
				
				// 頁數加一
				pageNumber = Integer.valueOf(request.getParameter("pageNumber"));
				pageNumber += 1;
				
			} else if (request.getParameter("post").equalsIgnoreCase("上一頁")) {
				
				// 處理上一頁的資料
				paramString = request.getParameter("paramString");
				displayFields = paramString.split(",");
				String previousPageUrl = request.getParameter("previousPageUrl");
				previousPageUrl += "&access_token=" + info.getToken();
				myPosts = facebookClient.fetchConnectionPage(previousPageUrl, Post.class);
				
				// 頁數減一
				pageNumber = Integer.valueOf(request.getParameter("pageNumber"));
				pageNumber -= 1;
				
			} else {
				
				// 處理分頁第一頁的資料
				initialize(request);
				
			}
			outputList = getOutputDataList(myPosts, displayFields, limit);

			// 設定傳回前端之attribute
			setAttributes(request, displayFields, paramString, outputList, pageNumber, myPosts.hasPrevious(),
					myPosts.hasNext(), myPosts.getPreviousPageUrl(), myPosts.getNextPageUrl());
		}

		// 存入DB或檔案
		if (storeMethod != null && storeMethod.equalsIgnoreCase("toDb")) {
			storeDataToDB(outputList);
		} else if (storeMethod != null && storeMethod.equalsIgnoreCase("toFile")) {
			storeDataToFile(outputList);
		}

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/posts.jsp");
		dispatcher.forward(request, response);
	}
	
	/**
	 * 初始化config、DB連線及檔案資訊，並且讀取前端傳回之parammeter，並傳至FB抓取貼文
	 * 
	 * @param request HTTP request
	 */
	private void initialize(HttpServletRequest request) {
		configuration = new Configuration();
		dbConnection = new DbConnector(configuration.getProp());
		fileProcessor = new FileProcessor(configuration.getProp());
		
		String target = request.getParameter("target"); // 抓取個人塗鴉牆或公開粉絲頁
		String pageId = request.getParameter("pageId");	// 公開粉絲頁ID
		storeMethod = request.getParameter("storeMethod");
		displayFields = request.getParameterValues("displayFields");
		
		// 組出欄位query字串
		paramString = composeQueryParameters(displayFields);

		facebookClient = new DefaultFacebookClient(info.getToken());
		
		// 抓取貼文
		myPosts = facebookClient.fetchConnection(getTargetUrl(target, pageId), Post.class,
				Parameter.with("fields", paramString));
	}
	
	/**
	 * 將貼文資料存入檔案
	 * 
	 * @param outputList 貼文資料
	 */
	private void storeDataToFile(List<ReturnPostData> outputList) {
		fileProcessor.writeDataToFile(outputList);
	}
	
	/**
	 * 將貼文資料存入DB
	 * 
	 * @param outputList 貼文資料
	 */
	private void storeDataToDB(List<ReturnPostData> outputList) {
		dbConnection.getConnection();
		for (ReturnPostData data : outputList) {
			// 檢查DB中是否已存在此貼文ID
			boolean hasExist = dbConnection.checkPostExist(data.getFieldValue(Constants.FIELD_NAME_ID));
			if (hasExist) {
				// 若存在更新貼文資料
				dbConnection.updatePost(data);
			}
			else {
				// 否則insert貼文資料
				dbConnection.insertPost(data);
			}
		}
		dbConnection.closeConnection();
	}
	
	/**
	 * 得到user的access token
	 * 
	 * @param request HTTP request
	 */
	private void getFacebookUserToken(HttpServletRequest request) throws ServletException {
		Facebook facebook = (Facebook) request.getSession().getAttribute("facebook");
		try {
			info = facebook.getOAuthAccessTokenInfo();
		} catch (FacebookException e) {
			throw new ServletException(e);
		}
	}
	
	/**
	 * 利用前端user選取的欄位資料，組出欄位query字串，以逗點分隔
	 * 
	 * @param displayFields 欄位清單
	 * @return query字串
	 */
	private String composeQueryParameters(String[] displayFields) {
		StringBuilder paramString = new StringBuilder("");

		for (int i = 0; i < displayFields.length; i++) {
			if (i != 0)
				paramString.append(",");
			paramString.append(displayFields[i]);
		}
		return paramString.toString();
	}
	
	/**
	 * 抓取個人塗鴉牆或公開粉絲頁貼文，依設定組出query URL
	 * 
	 * @param target 個人塗鴉牆或公開粉絲頁
	 * @param pageId 公開粉絲頁ID
	 * @return query URL
	 */
	private String getTargetUrl(String target, String pageId) {
		String targetUrl = "";
		if (target.equalsIgnoreCase("myPage"))
			targetUrl = "me/feed";
		else if (target.equalsIgnoreCase("publicPage") && !pageId.isEmpty())
			targetUrl = pageId + "/feed";
		return targetUrl;
	}
	
	/**
	 * 利用傳回之Post資料建立ReturnPostData物件清單
	 * 
	 * @param myPosts query回來之post資料
	 * @param displayFields 欲抓取的欄位
	 * @param limit 最多幾篇貼文
	 * @return ReturnPostData物件清單
	 */
	private List<ReturnPostData> getOutputDataList(Connection<Post> myPosts, String[] displayFields, int limit) {
		List<ReturnPostData> outputList = new ArrayList<ReturnPostData>();
		for (List<Post> connection : myPosts) {
			for (Post post : connection) {
				ReturnPostData postData = new ReturnPostData();
				for (String field : displayFields) {
					String value = "";
					Date date = null;
					try {
						switch (field) {
						case Constants.FIELD_NAME_MESSAGE:
							value = post.getMessage();
							break;
						case Constants.FIELD_NAME_CREATE_TIME:
							date = new java.sql.Date(post.getCreatedTime().getTime());
							break;
						case Constants.FIELD_NAME_DESCRIPTION:
							value = post.getDescription();
							break;
						case Constants.FIELD_NAME_FROM:
							value = post.getFrom().getName();
							break;
						case Constants.FIELD_NAME_FULL_PICTURE:
							value = post.getFullPicture();
							break;
						case Constants.FIELD_NAME_ID:
							value = post.getId();
							break;
						case Constants.FIELD_NAME_ICON:
							value = post.getIcon();
							break;
						case Constants.FIELD_NAME_LINK:
							value = post.getLink();
							break;
						case Constants.FIELD_NAME_NAME:
							value = post.getName();
							break;
						case Constants.FIELD_NAME_PICTURE:
							value = post.getPicture();
							break;
						case Constants.FIELD_NAME_PLACE:
							value = post.getPlace().getName();
							break;
						case Constants.FIELD_NAME_SOURCE:
							value = post.getSource();
							break;
						case Constants.FIELD_NAME_STORY:
							value = post.getStory();
							break;
						case Constants.FIELD_NAME_PRIVACY:
							value = "Value: " + post.getPrivacy().getValue() + ", Description: "
									+ post.getPrivacy().getDescription();
							break;
						case Constants.FIELD_NAME_TYPE:
							value = post.getType();
							break;
						case Constants.FIELD_NAME_UPDATED_TIME:
							date = new java.sql.Date(post.getCreatedTime().getTime());
							break;
						default:
							break;
						}
					} catch (Exception e) {
						value = "NO DATA";
					} finally {
						if (field.equalsIgnoreCase(Constants.FIELD_NAME_UPDATED_TIME)
								|| field.equalsIgnoreCase(Constants.FIELD_NAME_CREATE_TIME))
							postData.setFieldValue(field, date);
						else
							postData.setFieldValue(field, value);

					}
				}
				outputList.add(postData);
				if (outputList.size() >= limit)
					break;
			}
			if (outputList.size() >= limit)
				break;
		}
		return outputList;
	}
	
	/**
	 * 設定request attribute
	 * 
	 * @param request HTTP request
	 * @param displayFields 欲顯示的欄位
	 * @param paramString 欄位query字串
	 * @param outputList ReturnPostData物件清單
	 * @param pageNumber 目前的分頁號碼
	 * @param hasPreviousPage 有前一頁
	 * @param hasNextPage 有下一頁
	 * @param previousPageUrl 前一頁URL
	 * @param nextPageUrl 下一頁URL
	 */
	private void setAttributes(HttpServletRequest request, String[] displayFields, String paramString,
			List<ReturnPostData> outputList, int pageNumber, boolean hasPreviousPage, boolean hasNextPage,
			String previousPageUrl, String nextPageUrl) {
		request.setAttribute("displayFields", displayFields);
		request.setAttribute("paramString", paramString);
		request.setAttribute("postList", outputList);
		request.setAttribute("pageNumber", pageNumber);
		request.setAttribute("hasPreviousPage", hasPreviousPage);
		request.setAttribute("hasNextPage", hasNextPage);
		request.setAttribute("previousPageUrl", previousPageUrl);
		request.setAttribute("nextPageUrl", nextPageUrl);
		request.setAttribute("storeMethod", storeMethod);
	}

}
