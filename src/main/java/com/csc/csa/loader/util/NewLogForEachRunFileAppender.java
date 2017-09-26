package com.csc.csa.loader.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorCode;

public class NewLogForEachRunFileAppender extends FileAppender {

//	public NewLogForEachRunFileAppender() {
//	}
//
//	public NewLogForEachRunFileAppender(Layout layout, String filename,
//			boolean append, boolean bufferedIO, int bufferSize)
//			throws IOException {
//		super(layout, filename, append, bufferedIO, bufferSize);
//	}
//
//	public NewLogForEachRunFileAppender(Layout layout, String filename,
//			boolean append) throws IOException {
//		super(layout, filename, append);
//	}
//
//	public NewLogForEachRunFileAppender(Layout layout, String filename)
//			throws IOException {
//		super(layout, filename);
//	}
//
//	public void activateOptions() {
//		if (fileName != null) {
//			try {
//				fileName = getNewLogFileName();
//				setFile(fileName, fileAppend, bufferedIO, bufferSize);
//			} catch (Exception e) {
//				errorHandler.error("Error while activating log options", e,
//						ErrorCode.FILE_OPEN_FAILURE);
//			}
//		}
//	}
//
//	private String getNewLogFileName() {
//		String dateStr=
//				new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(new Date());
//		StringBuffer newFileName=new StringBuffer(fileName);
//		newFileName.append("_"+dateStr+".txt");
//		return newFileName.toString();
//	}
}