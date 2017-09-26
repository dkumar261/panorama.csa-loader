package com.csc.csa.loader.task;


import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.csc.csa.loader.JobReport;
import com.csc.csa.loader.helper.InputResourceFileHelper;
import com.csc.csa.loader.util.Constants;

/**
 * This tasklet looks for the input file and moves it from the source folder to the destination folder for processing.
 *
 * <li>If a file is found, it is moved into the destination folder.</li>
 * <li>If no file is found, the ExitStatus is returned as NO INPUT FILE.</li>
 * <ul>
 * 
 * @see InputResourceFileHelper
 */
public class MoveInputFileToProcessingTasklet implements Tasklet, InitializingBean{
	private static final Log log = LogFactory.getLog(MoveInputFileToProcessingTasklet.class);
	private InputResourceFileHelper fileHelper; 
	private boolean failIfNoFileFound = false;
	private JobReport report;	
	
	@Override
	public RepeatStatus execute(StepContribution contribution,	ChunkContext chunkContext) throws Exception {
		if (fileHelper.isFileAlreadyInDestinationFolder()) {
			log.info("An existing file is in the destination folder.  Will use this file as the source file.  " + fileHelper.getSourceFile());
			return RepeatStatus.FINISHED; 
		}
		
		if (fileHelper.getSourceFile() == null) {
			log.debug("No Input file was found. Finished.  " );
			if (failIfNoFileFound) {
				throw new UnexpectedJobExecutionException("No Input file was found. Fail.  ");				
			} else {
				contribution.setExitStatus(new ExitStatus("NO INPUT FILE", "No input file.  Process should stop here.  "));
				report.setIgnoreAuditFile(true);
				return RepeatStatus.FINISHED; 
				
			}
		}

		File fromFile = fileHelper.getSourceFile();

		
		if (fromFile.length() == 0) {
			log.error("Input file had zero bytes.  Finished.  Deleting source file.  "  + fileHelper.getSourceFile());
			boolean success = fromFile.delete();
			if (!success) {
				String msg;
				msg = "Source File could not be deleted from["+fromFile.getAbsolutePath()+"]";
				throw new UnexpectedJobExecutionException(msg);

			}

			if (failIfNoFileFound) {
				throw new UnexpectedJobExecutionException("Zero byte input file. Fail.  " + fromFile.getAbsolutePath());				
			} else {
				contribution.setExitStatus(new ExitStatus("NO INPUT FILE", "Zero byte input file.  Process should stop here." + fromFile.getAbsolutePath()));
				return RepeatStatus.FINISHED; 
			}			
		}
		
		if(Constants.LIFECOMM.equals(System.getProperty(Constants.SYSTEM))){
			contribution.setExitStatus(new ExitStatus("LILECOMM INPUT FILE", "Lifecomm input extract received" + fromFile.getAbsolutePath()));
		}
		else if(Constants.PANO.equals(System.getProperty(Constants.SYSTEM))){
			System.out.println("Constants.PANO="+Constants.PANO+"=Constants.SYSTEM="+Constants.SYSTEM+"=System.getProperty(Constants.SYSTEM)="+System.getProperty(Constants.SYSTEM));
			contribution.setExitStatus(new ExitStatus("PANO INPUT FILE", "Lifecomm input extract received" + fromFile.getAbsolutePath()));
		}
		else if(Constants.ZLIFE.equals(System.getProperty(Constants.SYSTEM))){
			contribution.setExitStatus(new ExitStatus("ZLIFE INPUT FILE", "Lifecomm input extract received" + fromFile.getAbsolutePath()));
		}else{
			String msg;
			msg = "Unknown system value present system = "+System.getProperty(Constants.SYSTEM);
			throw new UnexpectedJobExecutionException(msg);
		}
		File toFile = fileHelper.getDestinationFile();

		boolean success = false;
		for (int tryNumber = 0; tryNumber < 10; tryNumber++) {
				success = fromFile.renameTo(toFile);
			if (success) break;
			Thread.sleep(60000);
		}

		if (!success) {
			String msg;
				msg = "File could not be moved from["+fromFile.getAbsolutePath()+"] to["+toFile.getAbsolutePath()+"]";
			throw new UnexpectedJobExecutionException(msg);
		}
		log.info("Moved input file ["+fromFile.getName()+"] from["+fromFile.getParent()+"] to["+toFile.getParent()+"]");	
		
		return RepeatStatus.FINISHED;
	}
	

	 
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	public void setFileHelper(InputResourceFileHelper fileHelper) {
		this.fileHelper = fileHelper;
	}



	public void setFailIfNoFileFound(boolean failIfNoFileFound) {
		this.failIfNoFileFound = failIfNoFileFound;
	}



	public JobReport getReport() {
		return report;
	}



	public void setReport(JobReport report) {
		this.report = report;
	}
}