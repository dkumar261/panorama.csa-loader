package com.csc.csa.loader;

import com.csc.csa.loader.listener.EmailingJobExecutionListener;
/**
 * Job report that contains a list of errors occurred during database write.
 * If errors are present, the isEmaiReportRequired flag is set to true.
 * Each error will be logged in separate logfile.This logfile location
 * will be sentin error email.
 * 
 * @see EmailingJobExecutionListener
 */

public class ErrorJobReport implements JobReport {	
	boolean emailReportRequired=false;	
	boolean ignoreAuditFile=false;
	public boolean isEmailReportRequired() {
		return emailReportRequired;
	}
	public void setEmailReportRequired(boolean emailReportRequired) {
		this.emailReportRequired = emailReportRequired;
	}	
	public boolean isIgnoreAuditFile() {
		return ignoreAuditFile;
	}
	public void setIgnoreAuditFile(boolean ignoreAuditFile) {
		this.ignoreAuditFile = ignoreAuditFile;
	}
	
}
