package com.csc.csa.loader.listener;

import java.io.File;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import com.csc.csa.loader.JobReport;
import com.csc.csa.loader.util.Constants;

/**
 * This class is used to send a job notification email at the end of a job execution.
 * <p/>
 * When a job ends with an exception, an email is sent.  If the Job Report property is set,
 * an email can be sent after a successful job.
 *
 */
public class EmailingJobExecutionListener implements JobExecutionListener {
	private static final Log auditLogger = LogFactory.getLog(Constants.AUDIT_LOGGER);
	private static final Log logger = LogFactory.getLog(EmailingJobExecutionListener.class);
	private MailSender mailSender;
    private SimpleMailMessage templateMessage;	
	private String failureTo;
	private JobReport report;
	private JobOperator jobOperator;
	public void setTemplateMessage(SimpleMailMessage templateMessage) {
		this.templateMessage = templateMessage;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		StringBuilder sb = new StringBuilder();	
		if(!report.isIgnoreAuditFile())
			writeAuditFile(jobExecution);
		if ("FAILED".equals(jobExecution.getExitStatus().getExitCode()) || report.isEmailReportRequired()) {
			String logPath=getLogFilePath();//CSCOPT-1053
			sb.append("The Errors can be located in the logs folder "+logPath+"\r\n");
			List<Throwable> exceptionList = jobExecution.getAllFailureExceptions();
			for (Throwable throwable : exceptionList) {
				logger.error(throwable);
				sb.append("Failure Reason: " + throwable.toString()+"\r\n");
				Throwable t = throwable.getCause();
				while (t != null) {
					sb.append("Failure Cause: " + t.toString()+"\r\n");
					t=t.getCause();
				}
			}
			sendEmail(failureTo,"Error with " ,sb.toString());
		}				
	}
	public void writeAuditFile(JobExecution jobExecution){
		try{
			auditLogger.info(jobOperator.getSummary(jobExecution.getId()));
			auditLogger.info(jobOperator.getStepExecutionSummaries(jobExecution.getId()));
		}catch(Exception e){
			logger.error("Unable to write audit data", e);
		}
	}
	@Override
	public void beforeJob(JobExecution jobExecution) {
	}
	public void setFailureTo(String failureTo) {
		this.failureTo = failureTo;
	}
	
	private void sendEmail(String messageTo, String subjectPrefix, String messageText) {
		SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
		 msg.setSubject(subjectPrefix+msg.getSubject());
		if (messageTo != null) msg.setTo(messageTo); //otherwise use template to:
        msg.setText(messageText);
        try{
            this.mailSender.send(msg);
        }
        catch(MailException ex) {
           logger.error("Could not mail job failure.", ex);
        }		
	}
	public JobReport getReport() {
		return report;
	}

	public void setReport(JobReport report) {
		this.report = report;
	}

	public JobOperator getJobOperator() {
		return jobOperator;
	}

	public void setJobOperator(JobOperator jobOperator) {
		this.jobOperator = jobOperator;
	}
	//CSCOPT-1053
	private String getLogFilePath(){
		String path="";
		try{
			Enumeration<DailyRollingFileAppender> e = Logger.getRootLogger().getAllAppenders();
		    while ( e.hasMoreElements() ){
		    	DailyRollingFileAppender app = e.nextElement();
		    	String fileName=app.getFile();
		    	File logFolder=new File(fileName);
		    	path=logFolder.getAbsolutePath().substring(0, logFolder.getAbsolutePath().lastIndexOf("\\"));
		    	break;
		    }
		}catch(Exception e){
			logger.error("Unable to retrieve log file location", e);
		}
	    return path;
	}
}
