package com.csc.csa.loader.helper;

import java.io.File;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.csc.csa.loader.exception.CsaLoaderException;
import com.csc.csa.loader.util.Constants;

/**
 * 
 * @author dkumar261
 * This is a helper class that stores and builds the properties related to an input file.
 * This file is meant to be paired with the InputFileMoveTasklet
 */
@Component(value="inputLoaderFileHelper")
public class InputResourceFileHelper {
	
	private static Log log = LogFactory.getLog(InputResourceFileHelper.class);	

	@Value("${opt.loader.pano.sourceFolder}")
	private File sourceFolderPano;  
	
	@Value("${opt.loader.processingFolder}")
	private File processingFolder; 

	@Value("${opt.loader.archiveFolder}")
	private File archiveFolder;  
	
	private String ourFileName;
	private File sourceFile;
	private File destinationFile;
	private File archiveFile;
	private boolean fileAlreadyInDestinationFolder=false;
	
	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		System.setProperty(Constants.EXTRACT_TYPE,"");
		System.setProperty("db2.jcc.charsetDecoderEncoder", "3");
		Assert.isTrue(sourceFolderPano.exists(),"Source folder must exist ["+sourceFolderPano+"]");
		Assert.isTrue(processingFolder.exists(),"Destination folder must exist ["+processingFolder+"]");
		Assert.isTrue(archiveFolder.exists(),"Archive folder must exist ["+archiveFolder+"]");
		initialize();
	}
	private void initialize() throws Exception{		
		ourFileName = getEarliestMatchingFile(sourceFolderPano);
		if(ourFileName==null || Constants.BLANK.equals(ourFileName)){
			throw new CsaLoaderException("No File(s) present at expected location");
		}else{
			System.setProperty(Constants.SYSTEM, Constants.PANO);
			
			log.debug("System property set to Panorama");
			sourceFile = new File(sourceFolderPano,ourFileName);
			if(ourFileName.contains(Constants.FILE_NAME_POSTFIX)){
				System.setProperty(Constants.EXTRACT_TYPE,Constants.POLICY_EXTRACT);
				log.debug("System property set to Policy extract");
			}else{
				sourceFile=null;
				throw new CsaLoaderException("File name is not correct");				
			}	
		}
		destinationFile = new File(processingFolder,ourFileName);
		archiveFile = new File(archiveFolder,ourFileName);
	}
	
	private String getEarliestMatchingFile(File directory) {		
		File[] files = directory.listFiles();
		if (files.length==0) return null;
		Arrays.sort(files,NameFileComparator.NAME_COMPARATOR);
		log.debug(String.format("found %s",files[0].getName()));
		return files[0].getName();
	}	
	
	public void setDestinationFolder(String destinationFolder) {
		this.processingFolder = new File(destinationFolder);
	}
	public void setArchiveFolder(String archiveFolder) {
		this.archiveFolder = new File(archiveFolder);
	}

	public Resource getSourceFileResource() {
		log.debug(String.format("returning source file %s",sourceFile));
		if (sourceFile==null) return null;
		return new FileSystemResource(sourceFile);
	}

	public Resource getDestinationFileResource() {
		if (destinationFile==null) return null;
		return new FileSystemResource(destinationFile);
	}

	public Resource getArchiveFileResource() {
		if (archiveFile==null) return null;
		FileSystemResource fsr = new FileSystemResource(archiveFile);
		return fsr;
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public File getDestinationFile() {
		return destinationFile;
	}

	public File getArchiveFile() {
		return archiveFile;
	}

	public boolean isFileAlreadyInDestinationFolder() {
		return fileAlreadyInDestinationFolder;
	}

	public String getSourceFileName() {
		return sourceFile.getName();
	}

	@Override
	public String toString() {
		return "InputResourceFileHelper [archiveFile=" + archiveFile
				+ ", archiveFolder=" + archiveFolder + ", destinationFile="
				+ destinationFile + ", destinationFolder=" + processingFolder
				+ ", fileAlreadyInDestinationFolder="
				+ fileAlreadyInDestinationFolder 
				+ ", ourFileName=" + ourFileName + ", sourceFile=" + sourceFile
				+ ", sourceFolder Panorama=" + sourceFolderPano +"]";
	}
	public File getsourceFolderPano() {
		return sourceFolderPano;
	}

	public void setsourceFolderPano(File sourceFolderPano) {
		this.sourceFolderPano = sourceFolderPano;
	}
}
