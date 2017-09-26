package com.csc.csa.loader;



/**
 * Interface for creating specific JobReport classes for use with the 
 * EmailingJobExecutionListener.
 *
 */
public interface JobReport {	
	
	public boolean isEmailReportRequired();	
	
	public void setEmailReportRequired(boolean emailReportRequired) ;
	
	public boolean isIgnoreAuditFile();
	
	public void setIgnoreAuditFile(boolean ignoreAuditFile) ;
	
}
