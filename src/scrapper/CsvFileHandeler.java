package scrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CsvFileHandeler {

	private LinkedList<Info> list = null;
	private String location = null;
    private final char DEFAULT_SEPARATOR = ',';
    private final char DEFAULT_QUOTE = '"';
	
    
	public CsvFileHandeler() {
		// default location present directory
		location = "";
	}
	
	//mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	public LinkedList<Info> read(String filePath) {
		location = filePath.substring(0, filePath.lastIndexOf("\\")+1);
		
		
		list = new LinkedList<>();
		Info info = null;
		
		try {
		//howtodoinjava.com/core-java/io/how-to-read-data-from-inputstream-into-string-in-java/	
		InputStream in = new FileInputStream(new File(filePath));
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String row;
        
        while ((row = reader.readLine()) != null) {
			List<String> line = parseLine(row);
			info = new Info(line.get(0), line.get(1), line.get(2), line.get(3), line.get(4), line.get(5),
					line.get(6), line.get(7), line.get(8), line.get(9));
			list.add(info);
			System.out.println("[Linkedin_Profile_URL= " + line.get(0) + ", First_Name= " + line.get(1)
					+ " , Last_Name=" + line.get(2) + ", Email_ID= " + line.get(3) + ", Contact_Number= "
					+ line.get(4) + " , Location=" + line.get(5) + ", Industry= " + line.get(6) + ", Designation= "
					+ line.get(7) + " , Company_Name=" + line.get(8) + ", Company_Size= " + line.get(9) + "]");

		}
        System.out.println("location : " + location);
        reader.close();
        	
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	public List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

    public String write(LinkedList<Info> list, int size) {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Calendar cal = Calendar.getInstance();
		String fileName = dateFormat.format(cal.getTime());

		FileWriter writer = null;
		try {
			writer = new FileWriter(location + "Linkedin_" + size + "_list_" + fileName + ".csv");

			writer.append("Linkedin_Profile_URL");
			writer.append(",");
			writer.append("First_Name");
			writer.append(",");
			writer.append("Last_Name");
			writer.append(",");
			writer.append("Email_ID");
			writer.append(",");
			writer.append("Contact_Number");
			writer.append(",");
			writer.append("Location");
			writer.append(",");
			writer.append("Industry");
			writer.append(",");
			writer.append("Designation");
			writer.append(",");
			writer.append("Company_Name");
			writer.append(",");
			writer.append("Company_Size");
			writer.append(",");
			writer.append("\n");

			if (size > 0) {
				Iterator<Info> it = list.iterator();
				while (it.hasNext()) {
					Info info = (Info) it.next();
					writer.append(commaSkiping(info.getLink()));
					writer.append(",");
					writer.append(commaSkiping(info.getFirstName()));
					writer.append(",");
					writer.append(commaSkiping(info.getSecondName()));
					writer.append(",");
					writer.append(commaSkiping(info.getEmail()));
					writer.append(",");
					writer.append(commaSkiping(info.getPhone()));
					writer.append(",");
					writer.append(commaSkiping(info.getLocation()));
					writer.append(",");
					writer.append(commaSkiping(info.getIndustry()));
					writer.append(",");
					writer.append(commaSkiping(info.getCurrentJobTitle()));
					writer.append(",");
					writer.append(commaSkiping(info.getCurrentCompany()));
					writer.append(",");
					writer.append(commaSkiping(info.getCompanySize()));
					writer.append("\n");
				} 
			}
			writer.flush();
			writer.close();
			return "Done";

		} catch (IOException e) {
			// e.printStackTrace();
			return "Error" + e.getMessage();
		}

	}

	protected String commaSkiping(String text) {
		String newText = text;
		if (newText.contains(","))
			if (!newText.startsWith("\"") && !newText.endsWith("\""))
				newText = "\"" + newText + "\"";
		return newText;
	}

	

}
