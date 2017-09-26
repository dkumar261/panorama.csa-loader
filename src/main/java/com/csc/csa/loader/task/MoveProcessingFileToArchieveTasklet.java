package com.csc.csa.loader.task;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;

import com.csc.csa.loader.helper.InputResourceFileHelper;

/**
 * A Job Tasklet used to archive the input file.
 * <p/>
 * This will move the input file from the processing folder to the
 * archive folder as defined in the InputResourceFileHelper.  
 * 
 */
public class MoveProcessingFileToArchieveTasklet implements Tasklet, InitializingBean{
	private static Log log = LogFactory.getLog(MoveProcessingFileToArchieveTasklet.class);
	private InputResourceFileHelper fileHelper;
	
	@Override
	public RepeatStatus execute(StepContribution contribution,	ChunkContext chunkContext) throws Exception {

		
		File fromFile = fileHelper.getDestinationFile();
		if (fromFile==null) {
			String msg = "File could not be moved. InputFileHelper=" +fileHelper;
			throw new IllegalStateException(msg);
		}
		File toFile = fileHelper.getArchiveFile();
		if (log.isDebugEnabled()) log.debug(String.format("renaming [%s] to [%s]",fromFile,toFile));
		boolean success = false;
		for (int tryNumber = 0; tryNumber < 10; tryNumber++) {
				success = fromFile.renameTo(toFile);
			if (success) break;
			log.warn(String.format(String.format("Could not rename. (Does file already exist in destination folder?)  retry [%s]",tryNumber),fromFile,toFile));
			Thread.sleep(60000);
		}

		if (!success) {
			String msg;
				msg = "File could not be moved from["+fromFile.getAbsolutePath()+"] to["+toFile.getAbsolutePath()+"] (Does file already exist in destination folder?)";
			throw new IllegalStateException(msg);
		}
		log.info("Archived input file ["+fromFile.getName()+"] from["+fromFile.getParent()+"] to["+toFile.getParent()+"]");	
			
		
		return RepeatStatus.FINISHED;
	}
	 
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	public void setFileHelper(InputResourceFileHelper fileHelper) {
		this.fileHelper = fileHelper;
	}


}
