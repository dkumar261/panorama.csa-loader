package com.csc.csa.loader.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.SkipListener;

import com.csc.csa.loader.JobReport;
import com.csc.csa.loader.domain.Party;

public class PolicyRecordReadListener implements SkipListener<Party, Party> {
	private static final Log logger = LogFactory.getLog(PolicyRecordReadListener.class);
	private JobReport report;
	public void onSkipInRead(Throwable t) {
		logger.error("Exception occured while reading record", t);
		if(!report.isEmailReportRequired()){
			report.setEmailReportRequired(true);
		}
	}

	public void onSkipInProcess(Party item, Throwable t) {

	}

	public void onSkipInWrite(Party item, Throwable t){
		
	}

	public JobReport getReport() {
		return report;
	}

	public void setReport(JobReport report) {
		this.report = report;
	}	
}
