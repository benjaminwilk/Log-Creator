import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.util.*;

interface logConnect{
	public boolean getLogWriterStatus();
	public void setLogWriterStatus();
	public void formatLogLine(String[] passedContents);
	public void writeLogContents(String userDefinedLog);
}

public class LogCreator implements logConnect{

  /**** VV Change this to move the log file somewhere else. VV ****/
	private String driveLetter;
	private String frontFileName;
	private String rearFileName;
	private String fullPathName = null;
	private boolean logWriter = false;

	public LogCreator(String userDefinedDriveLetter, String userDefinedFrontFileName, String userDefinedRearFileName){
		this.driveLetter = userDefinedDriveLetter;
		this.frontFileName = userDefinedFrontFileName;
		this.rearFileName = userDefinedRearFileName;
		logWriter = true;
		checkForLogFile();
	}
	
	public LogCreator(String userDefinedFullPath){
		this.fullPathName = userDefinedFullPath;
		logWriter = true;
		checkForLogFile();
	}
	
	public LogCreator(){
		/*** Default log file ***/
		this.driveLetter = "J:\\";
		this.frontFileName = "BWilk\\";
		this.rearFileName = "-log-file.log";
		logWriter = true;
		checkForLogFile();
	}
  
	public boolean getLogWriterStatus(){
		return logWriter;
	}

	public void setLogWriterStatus(){
		if(logWriter == true){
			logWriter = false;
		} else {
			logWriter = true;
		}
	}

	private Date generateDateAndTime(){
		Date today = Calendar.getInstance().getTime();
		return today;
	}

	private String getDateHour(){
		SimpleDateFormat formatter = null;
		formatter = new SimpleDateFormat("HH:mm:ss");
		return formatter.format(generateDateAndTime());
	}

	private String getDateMonth(){
		SimpleDateFormat formatter = null;
		formatter = new SimpleDateFormat("MM-dd-yyyy");
		return formatter.format(generateDateAndTime());
	}

	private String createLogFileName(){
		if(fullPathName == null){
			String logFileName = this.driveLetter + this.frontFileName + getDateMonth() + this.rearFileName;
			return logFileName;
		}else{
			return fullPathName;
		}
	}

	private void checkForLogFile(){
		String logNameTitle = createLogFileName();
		File newLogFile = new File(logNameTitle);
		if(newLogFile.exists() && !newLogFile.isDirectory()){
			fullPathName = logNameTitle;
		} else{
			createNewLogFile(newLogFile);
		}
	}
	
	private void createNewLogFile(File newLogFile){
		try{
			PrintWriter write = new PrintWriter(newLogFile, "UTF-8");
			//fullPathName = newLogFile;
		}catch(Exception ex){
			writeLogContents("Something broke in logFileValidation function " + ex);
			//return "Something broke...: " + ex;
		}
	}

	/*public void writeLogContents(String userDefinedLog){
		logFileWriter(userDefinedLog);
	}*/

	public void formatLogLine(String[] logElements){
		/*** VVV This is currently appending text to the end of the other lines.  Sort of broken, I'll fix it later VVV ***/
		String formattedContents = "";
		formattedContents += getDateHour();
		for(int i = 0 ; i < logElements.length; i++){
			formattedContents += logElements[i] + " -- ";
		}
		formattedContents += "\n";
		writeLogContents(formattedContents);
	}

	public void writeLogContents(String formattedLogContents){
		String logFileName = fullPathName;
		try{
			PrintWriter logWriter = new PrintWriter(new FileOutputStream(new File(logFileName), true));
			logWriter.append(formattedLogContents);
			logWriter.println();
			logWriter.close();
		}catch(Exception ex){
			System.out.println(ex);
		}
	}
}
