package com.csc.csa.loader.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 * Chunk Listener that logs processing time per chunk (at the DEBUG level).<br/>  
 * Shows Actual/Average/Total
 *
 */
public class LoggingChunkListener implements ChunkListener {
	private static final Log log = LogFactory.getLog(LoggingChunkListener.class);
	long totalTime;
	long chunkStartTime;
	int chunkCount=0;

	@Override
	public void afterChunk(ChunkContext ctx) {
		chunkCount++;
		long chunkTime = System.currentTimeMillis()-chunkStartTime;
		totalTime+=chunkTime;
		
		long seconds = chunkTime /1000;
		long avgSeconds = (totalTime/chunkCount)/1000;
		long totalSeconds = totalTime/1000;
		log.debug("Chunk end ["+seconds+"s] avg["+avgSeconds+"s] total["+totalSeconds+"s]");

	}

	@Override
	public void beforeChunk(ChunkContext ctx) {
		log.debug("chunk start");
		chunkStartTime = System.currentTimeMillis();
	}
	public void afterChunkError(ChunkContext ctx){
		
	}

}
