package com.csc.csa.loader.listener;

import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.springframework.aop.ThrowsAdvice;

import com.csc.csa.loader.JobReport;

public class LoggingInterceptor implements ThrowsAdvice {
	private JobReport report;
	private static Log log = null;
	public LoggingInterceptor(){
	}
	
	public void before(Method arg0, Object[] arg1, Object arg2) throws Throwable {
		log = LogFactory.getLog(arg2.getClass());
		log.debug("Beginning method: "+arg0.getName());
	}
	
	public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {		
		log = LogFactory.getLog(arg3.getClass());
		log.debug("Ending method: "+arg1.getName());		
	}
	 
	public void afterThrowing(JoinPoint thisJoinPoint, Throwable ex) {
		if(!report.isEmailReportRequired()){
			report.setEmailReportRequired(true);
		}
		Object[] args = thisJoinPoint.getArgs();
		Object target = thisJoinPoint.getTarget();
		log = LogFactory.getLog(target.getClass());
		log.error(target,ex);
		for(int i=0;i<args.length;i++){
			if(args[i] instanceof Collection){
				Collection<?> inputList=(Collection<?>)args[i];
				for(Object input:inputList)
					log.error(input);
			}
		}
	}

	public JobReport getReport() {
		return report;
	}

	public void setReport(JobReport report) {
		this.report = report;
	}

}
