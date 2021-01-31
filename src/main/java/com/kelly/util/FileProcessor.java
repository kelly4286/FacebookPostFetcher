package com.kelly.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import com.kelly.model.ReturnPostData;

import facebook4j.internal.logging.Logger;

public class FileProcessor {
	final static Logger logger = Logger.getLogger(FileProcessor.class);
	private File file = null;			// 檔案
	private BufferedWriter bw = null;	// buffer
	
	/**
	 * 初始化檔案讀取相關設定
	 * 
	 * @param prop properties檔案
	 */
	public FileProcessor(Properties prop) {
		try {
			file = new File(prop.getProperty("file.storePath"));

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 將貼文資料以JSON形式寫進File
	 * 
	 * @param postList 貼文資料
	 */
	public void writeDataToFile(List<ReturnPostData> postList) {
		JSONArray ja = new JSONArray();
		
		for(ReturnPostData p : postList) {
			JSONObject jo = new JSONObject();
			jo.put(Constants.FIELD_NAME_ID, p.getFieldValue(Constants.FIELD_NAME_ID));
			jo.put(Constants.FIELD_NAME_CREATE_TIME, p.getFieldValue(Constants.FIELD_NAME_CREATE_TIME));
			jo.put(Constants.FIELD_NAME_UPDATED_TIME, p.getFieldValue(Constants.FIELD_NAME_UPDATED_TIME));
			jo.put(Constants.FIELD_NAME_MESSAGE, p.getFieldValue(Constants.FIELD_NAME_MESSAGE));
			jo.put(Constants.FIELD_NAME_DESCRIPTION, p.getFieldValue(Constants.FIELD_NAME_DESCRIPTION));
			jo.put(Constants.FIELD_NAME_FROM, p.getFieldValue(Constants.FIELD_NAME_FROM));
			jo.put(Constants.FIELD_NAME_FULL_PICTURE, p.getFieldValue(Constants.FIELD_NAME_FULL_PICTURE));
			jo.put(Constants.FIELD_NAME_ICON, p.getFieldValue(Constants.FIELD_NAME_ICON));
			jo.put(Constants.FIELD_NAME_LINK, p.getFieldValue(Constants.FIELD_NAME_LINK));
			jo.put(Constants.FIELD_NAME_NAME, p.getFieldValue(Constants.FIELD_NAME_NAME));
			jo.put(Constants.FIELD_NAME_PICTURE, p.getFieldValue(Constants.FIELD_NAME_PICTURE));
			jo.put(Constants.FIELD_NAME_PLACE, p.getFieldValue(Constants.FIELD_NAME_PLACE));
			jo.put(Constants.FIELD_NAME_PRIVACY, p.getFieldValue(Constants.FIELD_NAME_PRIVACY));
			jo.put(Constants.FIELD_NAME_SOURCE, p.getFieldValue(Constants.FIELD_NAME_SOURCE));
			jo.put(Constants.FIELD_NAME_STORY, p.getFieldValue(Constants.FIELD_NAME_STORY));
			jo.put(Constants.FIELD_NAME_TYPE, p.getFieldValue(Constants.FIELD_NAME_TYPE));
			
			ja.put(jo);
		}
		
		JSONObject mainObj = new JSONObject();
		mainObj.put("posts", ja);
		
		try {
			bw.write(mainObj.toString());
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
