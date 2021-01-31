package com.kelly.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 下載已建立之JSON檔案，將檔案傳回至前端給user下載
	 */
	@Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         String fileName = "facebook.json";
         String fileType = "text/plain";

         response.setContentType(fileType);
         response.setHeader("Content-disposition","attachment; filename=" + fileName);

         File my_file = new File(fileName);

         OutputStream out = response.getOutputStream();
         FileInputStream in = new FileInputStream(my_file);
         byte[] buffer = new byte[4096];
         int length;
         while ((length = in.read(buffer)) > 0){
            out.write(buffer, 0, length);
         }
         in.close();
         out.flush();
    }
}