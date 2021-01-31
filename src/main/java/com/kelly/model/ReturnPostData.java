package com.kelly.model;

import java.sql.Date;

import com.kelly.util.Constants;

public class ReturnPostData {
	private String message = "";
	private Date createTime = null;
	private String actions = "";
	private String description = "";
	private String from = "";
	private String fullPicture = "";
	private String id = "";
	private String icon = "";
	private String link = "";
	private String name = "";
	private String picture = "";
	private String place = "";
	private String privacy = "";
	private String source = "";
	private String story = "";
	private String type = "";
	private Date updatedTime = null;

	/**
	 * 得到某欄位的值
	 * 
	 * @param field 欄位名稱
	 * @return 該欄位的值(文字格式)
	 */
	public String getFieldValue(String field) {
		switch(field) {
		case Constants.FIELD_NAME_MESSAGE: return message;
		case Constants.FIELD_NAME_CREATE_TIME: return createTime != null? createTime.toString(): "";
		case Constants.FIELD_NAME_ACTIONS: return actions;
		case Constants.FIELD_NAME_DESCRIPTION: return description;
		case Constants.FIELD_NAME_FROM: return from;
		case Constants.FIELD_NAME_FULL_PICTURE: return fullPicture;
		case Constants.FIELD_NAME_ID: return id;
		case Constants.FIELD_NAME_ICON: return icon;
		case Constants.FIELD_NAME_LINK: return link;
		case Constants.FIELD_NAME_NAME: return name;
		case Constants.FIELD_NAME_PICTURE: return picture;
		case Constants.FIELD_NAME_PLACE: return place;
		case Constants.FIELD_NAME_SOURCE: return source;
		case Constants.FIELD_NAME_STORY: return story;
		case Constants.FIELD_NAME_PRIVACY: return privacy;
		case Constants.FIELD_NAME_TYPE: return type;
		case Constants.FIELD_NAME_UPDATED_TIME: return updatedTime != null? updatedTime.toString(): "";
		default: return "";
		}
	}
	/**
	 * 得到某日期欄位的值
	 * 
	 * @param field 欄位名稱
	 * @return 該欄位的值(日期格式)
	 */
	public Date getFieldValueOfDate(String field) {
		switch(field) {
		case Constants.FIELD_NAME_CREATE_TIME: return createTime;
		case Constants.FIELD_NAME_UPDATED_TIME: return updatedTime;
		default: return null;
		}
	}
	/**
	 * 設定某欄位的值
	 * 
	 * @param field 欄位名稱
	 * @return 該欄位的值(文字格式)
	 */
	public void setFieldValue(String field, String value) {
		switch(field) {
		case Constants.FIELD_NAME_MESSAGE: message = value; break;
		case Constants.FIELD_NAME_ACTIONS: actions = value; break;
		case Constants.FIELD_NAME_DESCRIPTION: description = value; break;
		case Constants.FIELD_NAME_FROM: from = value; break;
		case Constants.FIELD_NAME_FULL_PICTURE: fullPicture = value; break;
		case Constants.FIELD_NAME_ID: id = value; break;
		case Constants.FIELD_NAME_ICON: icon = value; break;
		case Constants.FIELD_NAME_LINK: link = value; break;
		case Constants.FIELD_NAME_NAME: name = value; break;
		case Constants.FIELD_NAME_PICTURE: picture = value; break;
		case Constants.FIELD_NAME_PLACE: place = value; break;
		case Constants.FIELD_NAME_SOURCE: source = value; break;
		case Constants.FIELD_NAME_STORY: story = value; break;
		case Constants.FIELD_NAME_PRIVACY: privacy = value; break;
		case Constants.FIELD_NAME_TYPE: type = value; break;
		default: return ;
		}
	}
	/**
	 * 設定某日期欄位的值
	 * 
	 * @param field 欄位名稱
	 * @return 該欄位的值(日期格式)
	 */
	public void setFieldValue(String field, Date value) {
		switch(field) {
		case Constants.FIELD_NAME_CREATE_TIME: createTime = value; break;
		case Constants.FIELD_NAME_UPDATED_TIME: updatedTime = value; break;
		default: return ;
		}
	}
	/**
	 * 得到欄位的屬性：文字或日期或圖片或連結
	 * 
	 * @param field 欄位名稱
	 * @return 該欄位的屬性
	 */
	public String getFieldAttribute(String field) {
		switch(field) {
		case Constants.FIELD_NAME_MESSAGE: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_CREATE_TIME: return Constants.FIELD_ATTRIBUTE_DATE;
		case Constants.FIELD_NAME_ACTIONS: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_DESCRIPTION: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_FROM: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_FULL_PICTURE: return Constants.FIELD_ATTRIBUTE_PICTURE;
		case Constants.FIELD_NAME_ID: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_ICON: return Constants.FIELD_ATTRIBUTE_PICTURE;
		case Constants.FIELD_NAME_LINK: return Constants.FIELD_ATTRIBUTE_LINK;
		case Constants.FIELD_NAME_NAME: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_PICTURE: return Constants.FIELD_ATTRIBUTE_PICTURE;
		case Constants.FIELD_NAME_PLACE: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_SOURCE: return Constants.FIELD_ATTRIBUTE_LINK;
		case Constants.FIELD_NAME_STORY: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_PRIVACY: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_TYPE: return Constants.FIELD_ATTRIBUTE_TEXT;
		case Constants.FIELD_NAME_UPDATED_TIME: return Constants.FIELD_ATTRIBUTE_DATE;
		default: return "";
		}
	}
}
