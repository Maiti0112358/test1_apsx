/***************************************************************************
 * Script Name: bookingcrawl.java
 * Description: Crawls all data about hotels in a city from www.booking.com
 *              and saves in a file in txt format
 * Created By:  Eeran Maiti
 * Date:        1 May 2014
 * Argument:    Output directory (Caution: not any file)
 * Instructions: Edit and customize the following parameters before running
 *                  fDataFile: Name and location of output
 *                  sUrl, sCommentURL, saCommentURL: Selection of cities.
 * *************************************************************************/
package bookingcrawl;

// Import packages for URL operations
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

// Import packages for HTML parsing. Jsoup library is being used
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// Libraties for timestamp
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// Libraries for file operations
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class Bookingcrawl {

    // For New York
    // static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=new&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=New+York+Metropolitan+area%2C+U.S.A.&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=region&dest_id=2331&ac_pageview_id=16839e0e4a4a000e&ac_position=2";
    // For London
    // static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=London&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3D13c965e9e05dccbcd0edebb9f83ab75d%3Bdcid%3D1%3B&dcid=1&sid=13c965e9e05dccbcd0edebb9f83ab75d&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=London%2C+Greater+London%2C+United+Kingdom&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-2601889&ac_pageview_id=181470554f49002f&ac_position=0";
    // For Tokyo
    // static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=tokyo&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Tokyo+Prefecture%2C+Japan&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=region&dest_id=4720&ac_pageview_id=16839e0e4a4a000e&ac_position=1";
    // For Frankfurt
    // static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=Frankfurt&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Frankfurt%2FMain%2C+Hessen%2C+Germany&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-1771148&ac_pageview_id=16839e0e4a4a000e&ac_position=0";
    // For Mumbai
    // static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=Mumbai&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Mumbai+Region+%2C+India&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=region&dest_id=6249&ac_pageview_id=16839e0e4a4a000e&ac_position=1";
    // For Brasilia
    // static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=brasi&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Brasilia%2C+Distrito+Federal%2C+Brazil&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-631243&ac_pageview_id=16839e0e4a4a000e&ac_position=0"
    // For Cape Town
    // Static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=cape+t&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Cape+Town%2C+Western+Cape%2C+South+Africa&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-1217214&ac_pageview_id=16839e0e4a4a000e&ac_position=0";
    // For Sydney
    // Static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=sydney&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Sydney%2C+New+South+Wales%2C+Australia&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-1603135&ac_pageview_id=16839e0e4a4a000e&ac_position=0";
    // For Beijing
    // Static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=bei&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Beijing%2C+Beijing+Area%2C+China&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-1898541&ac_pageview_id=16839e0e4a4a000e&ac_position=0";
    // For San Francisco
    // Static String sUrl = "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=san&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=San+Francisco%2C+California%2C+U.S.A.&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=20015732&ac_pageview_id=16839e0e4a4a000e&ac_position=1";
     
    static String [] sUrl = {
        // For New York
        "http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=new&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=New+York+Metropolitan+area%2C+U.S.A.&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=region&dest_id=2331&ac_pageview_id=16839e0e4a4a000e&ac_position=2"
        // For London
        ,"http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=London&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3D13c965e9e05dccbcd0edebb9f83ab75d%3Bdcid%3D1%3B&dcid=1&sid=13c965e9e05dccbcd0edebb9f83ab75d&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=London%2C+Greater+London%2C+United+Kingdom&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-2601889&ac_pageview_id=181470554f49002f&ac_position=0"
        // For Tokyo
        ,"http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=tokyo&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Tokyo+Prefecture%2C+Japan&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=region&dest_id=4720&ac_pageview_id=16839e0e4a4a000e&ac_position=1"
        // For Frankfurt
        ,"http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=Frankfurt&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Frankfurt%2FMain%2C+Hessen%2C+Germany&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-1771148&ac_pageview_id=16839e0e4a4a000e&ac_position=0"
        // For Mumbai
        ,"http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=Mumbai&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Mumbai+Region+%2C+India&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=region&dest_id=6249&ac_pageview_id=16839e0e4a4a000e&ac_position=1"
        // For Brasilia
        ,"http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=brasi&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Brasilia%2C+Distrito+Federal%2C+Brazil&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-631243&ac_pageview_id=16839e0e4a4a000e&ac_position=0"
        // For Cape Town
        ,"http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=cape+t&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Cape+Town%2C+Western+Cape%2C+South+Africa&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-1217214&ac_pageview_id=16839e0e4a4a000e&ac_position=0"
        // For Sydney
        ,"http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=sydney&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Sydney%2C+New+South+Wales%2C+Australia&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-1603135&ac_pageview_id=16839e0e4a4a000e&ac_position=0"
        // For Beijing
        ,"http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=bei&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=Beijing%2C+Beijing+Area%2C+China&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=-1898541&ac_pageview_id=16839e0e4a4a000e&ac_position=0"
        // For San Francisco
        ,"http://www.booking.com/searchresults.html?src=index&nflt=&ss_raw=san&error_url=http%3A%2F%2Fwww.booking.com%2Findex.en-gb.html%3Fsid%3Dbd07a3cf98f69dbc89a06aab32d887b1%3Bdcid%3D1%3B&dcid=1&sid=bd07a3cf98f69dbc89a06aab32d887b1&si=ai%2Cco%2Cci%2Cre%2Cdi&ss=San+Francisco%2C+California%2C+U.S.A.&checkin_monthday=0&checkin_year_month=0&checkout_monthday=0&checkout_year_month=0&idf=on&interval_of_time=any&sb_predefined_group_options_value=2&no_rooms=1&group_adults=2&group_children=0&dest_type=city&dest_id=20015732&ac_pageview_id=16839e0e4a4a000e&ac_position=1"
     };
     
    // Variables not reccommended to change initialization value
    URL url = null;
    org.jsoup.nodes.Document doc = null;
    // iTotalEntry, iPgCnt and iOffset are used for creating parameters
    int iTotalEntry, iPgCnt, iRevCnt = 0;
    int iOffset = 50;
    int iRoomType = 0;
    static int iCityPointer = 0;
    static int iMaxCity = 10;
    // locLocale denotes numeric data format present in HTML
    Locale locLocale = Locale.GERMANY ;
    // Variables for CouchDB
    public static final String BK_KEY_HOTEL ="HOTEL_NAME";
    public static final String BK_KEY_URL ="URL";
    public static final String BK_KEY_ADDRESS ="ADDRESS";
    public static final String BK_KEY_DESCRIPTION="DESCRIPTION";
    public static final String BK_KEY_ACTIVITY ="ACTIVITIES";
    public static final String BK_KEY_FOOD ="FOOD&DRINKS";
    public static final String BK_KEY_SERVICES ="SERVICES";
    public static final String BK_KEY_GENERAL ="GENERAL";
    public static final String BK_KEY_LANGUAGE ="LANGUAGE";
    public static final String BK_KEY_INTERNET ="INTERNET";
    public static final String BK_KEY_PARKING ="PARKING";
    public static final String BK_KEY_RATING_ALL ="RATING_ALL";
    public static final String BK_KEY_RATING_FAMILY ="RATING_FAMILY";
    public static final String BK_KEY_RATING_COUPLE ="RATING_COUPLE";
    public static final String BK_KEY_RATING_FRIENDS ="RATING_FRIENDS";
    public static final String BK_KEY_RATING_SOLO ="RATING_SOLO";
    public static final String BK_KEY_ROOMTYPE ="ROOMTYPE_";
    public static final String BK_KEY_ROOMFACI ="ROOMFACILITIES_";
    public static final String BK_KEY_ROOMPRICE ="ROOMPRICE_";
    public static final String BK_KEY_DATE = "DATE";
    
    // Variable for file
    static File fDataFile = null;
    static BufferedWriter bwBufferWriter = null;
    static FileWriter fwFileWriter = null;
      
    // Variables for records
    String sHotelName = null, sHotelUrl = null;
    String sName =null, sRevCnt = null;
    static String sDate = null;
    int iFaci = 0, iPrice = 0;
    
   /**
    * Method:      main
    * Description: Entry point of program
    * Input:       Void
    * Output:      Void.
    */
    
    public static void main(String[] args) throws IOException   {
        
        System.out.println("Initiating execution of booking.com crawler");
        // Name output file
        DateFormat df = new SimpleDateFormat("dd_MM_yyyy");
        Date dDate = new Date();
        String sRepDate = df.format(dDate);
        System.out.println("Generating data for "+sRepDate);
        //fDataFile = new File("D:\\booking_data_"+sRepDate+".txt");
        // Check argument and create o/p file
        if (new File(args[0]).isDirectory()) {
            fDataFile = new File(args[0]+"booking_data_"+sRepDate+".txt");
        }
        else {
            System.out.println("The argument is not a valid directory.");
            System.exit(0);
        }
        
        // Create data file
        if (!fDataFile.exists()) {
            if (fDataFile.createNewFile()) {
                System.out.println("Data file is created!");
            } else {
		System.out.println("Failed to create data file!");
            }
	}
        fwFileWriter = new FileWriter(fDataFile,true);
    	bwBufferWriter = new BufferedWriter(fwFileWriter);
        
        // Creating instance of crawler
        Bookingcrawl CR = new Bookingcrawl();
        
        for (int i=0; i<=iMaxCity; i++) {
            iCityPointer = i;
            // Call method to read input URLs iteratively
            CR.parseURL (sUrl [iCityPointer]);
            // Call method to get page count
            CR.getPageCount();
            // Call method to iterate to get data
            CR.fetchData ();
        }
        // Close file
        try {
            if (bwBufferWriter != null)
                bwBufferWriter.close();
            if (fwFileWriter != null)
                fwFileWriter.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // Show ending message
        System.out.println ("Execution over for today");   
    }
    
   /**
    * Method:      parseURL
    * Description: parses the input string into URL
    * Input:       String sURL
    * Output:      Void.
    */
    
    public void parseURL (String sURL) throws IOException {
        
        System.out.println ("Parsing URL");
        try { 
            url = new URL(sURL);
        }
        catch (MalformedURLException e) {
            System.out.println("Invalid starting URL " );
        }
        analyzeURL(url);   
    }
    
   /**
    * Method:      analyzeURL
    * Description: Analyzes input URLs and calls appropriate functions to 
    *              collect required information and to save in database
    * Input:       URL url
    * Output:      Void.
    */
    
    public void analyzeURL (URL url) {
        
        System.out.println("Calling Jsoup library function");
        try {
            doc = Jsoup.parse(url, 5000);
            //System.out.println (doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   
   /**
    * Method:      getPageCount
    * Description: Extracts page count for the data
    * Input:       Void
    * Output:      Void.
    */
    
    public void getPageCount () {
        
        System.out.println ("Extracting required data");
      
        Elements spans = doc.getElementsByTag("h4");
        for (Element span : spans) {
            String sText = span.text();
            if (sText.contains("properties found")) {
                String[] parts = sText.split(" ");
                iTotalEntry = Integer.parseInt(parts[0]);
                System.out.println(iTotalEntry);
            }
        }
        
        iPgCnt = (iTotalEntry /50 );
        if ( (iTotalEntry%50) != 0 ) {
            iPgCnt++;
        } 
    }
    
   /**
    * Method:      fetchData
    * Description: Sets correct parameters and calls method to fetch data
    * Input:       Void
    * Output:      Void.
    */    
    
    public void fetchData () {
        int iTemp =0;
        for (int i=0; i<iPgCnt; i++) {
            getData(iTemp);
            iTemp = iTemp + iOffset;
        }
    }
    
   /**
    * Method:      getData
    * Description: Sets correct parameters and calls method to fetch data
    * Input:       int iVarOffset
    * Output:      Void.
    */        
    
    public void getData (int iVarOffset) {
        
        // For New York
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=2331;dest_type=region;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=New+York+Metropolitan+area%2C+U.S.A.;ss_raw=new;ssb=empty;rows=50;offset="+iVarOffset;
        // For London
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-2601889;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=London%2C%20Greater%20London%2C%20United%20Kingdom;ss_raw=London;ssb=empty;rows=50;offset="+iVarOffset;
        // For Tokyo
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=4720;dest_type=region;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Tokyo+Prefecture%2C+Japan;ss_raw=tokyo;ssb=empty;rows=50;offset="+iVarOffset;
        // For Frankfurt
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-1771148;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Frankfurt%2FMain%2C+Hessen%2C+Germany;ss_raw=Frankfurt;ssb=empty;rows=50;offset="+iVarOffset;
        // For Mumbai
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=6249;dest_type=region;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Mumbai+Region+%2C+India;ss_raw=Mumbai;ssb=empty;rows=50;offset="+iVarOffset;
        // For Brasilia
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-631243;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Brasilia%2C+Distrito+Federal%2C+Brazil;ss_raw=brasi;ssb=empty;rows=50;offset="+iVarOffset;
        // For Cape Town
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-1217214;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Cape+Town%2C+Western+Cape%2C+South+Africa;ss_raw=cape+t;ssb=empty;rows=50;offset="+iVarOffset;
        // For Sydney
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-1603135;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Sydney%2C+New+South+Wales%2C+Australia;ss_raw=sydney;ssb=empty;rows=50;offset="+iVarOffset;
        // For Beijing
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-1898541;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Beijing%2C+Beijing+Area%2C+China;ss_raw=bei;ssb=empty;rows=50;offset="+iVarOffset;
        // For San Francisco
        // String sCommentURL = "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=20015732;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=San+Francisco%2C+California%2C+U.S.A.;ss_raw=san;ssb=empty;rows=50;offset="+iVarOffset;
        String [] sCommentURL = {
            // For New York
            "http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=2331;dest_type=region;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=New+York+Metropolitan+area%2C+U.S.A.;ss_raw=new;ssb=empty;rows=50;offset="+iVarOffset
            // For London
            ,"http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-2601889;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=London%2C%20Greater%20London%2C%20United%20Kingdom;ss_raw=London;ssb=empty;rows=50;offset="+iVarOffset
            // For Tokyo
            ,"http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=4720;dest_type=region;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Tokyo+Prefecture%2C+Japan;ss_raw=tokyo;ssb=empty;rows=50;offset="+iVarOffset
            // For Frankfurt
            ,"http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-1771148;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Frankfurt%2FMain%2C+Hessen%2C+Germany;ss_raw=Frankfurt;ssb=empty;rows=50;offset="+iVarOffset
            // For Mumbai
            ,"http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=6249;dest_type=region;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Mumbai+Region+%2C+India;ss_raw=Mumbai;ssb=empty;rows=50;offset="+iVarOffset
            // For Brasilia
            ,"http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-631243;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Brasilia%2C+Distrito+Federal%2C+Brazil;ss_raw=brasi;ssb=empty;rows=50;offset="+iVarOffset
            // For Cape Town
            ,"http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-1217214;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Cape+Town%2C+Western+Cape%2C+South+Africa;ss_raw=cape+t;ssb=empty;rows=50;offset="+iVarOffset
            // For Sydney
            ,"http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-1603135;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Sydney%2C+New+South+Wales%2C+Australia;ss_raw=sydney;ssb=empty;rows=50;offset="+iVarOffset
            // For Beijing
            ,"http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=-1898541;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=Beijing%2C+Beijing+Area%2C+China;ss_raw=bei;ssb=empty;rows=50;offset="+iVarOffset
            // For San Francisco
            ,"http://www.booking.com/searchresults.en-gb.html?sid=13c965e9e05dccbcd0edebb9f83ab75d;dcid=1;class_interval=1;csflt={};dest_id=20015732;dest_type=city;idf=1;interval_of_time=undef;no_rooms=1;or_radius=25;property_room_info=1;review_score_group=empty;score_min=0;si=ai%2Cco%2Cci%2Cre%2Cdi;src=index;ss=San+Francisco%2C+California%2C+U.S.A.;ss_raw=san;ssb=empty;rows=50;offset="+iVarOffset
        };
  
        URL uCommentURL = null;
        
        org.jsoup.nodes.Document dCommentDoc = null;
        
        try { 
            uCommentURL = new URL(sCommentURL[iCityPointer]);
        }
        catch (MalformedURLException e) {
            System.out.println("Invalid starting URL " );
        }
        
        try {
            dCommentDoc = Jsoup.parse(uCommentURL, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        Elements links = dCommentDoc.select("a"); 

        for (Element link : links) {
            if (link.attr("class").startsWith("hotel_name_link url")) {
                
                sHotelName = link.attr("title");
                sHotelUrl = link.attr("abs:href");
                // Useless Display
                System.out.println(sHotelName);
                //System.out.println(sHotelUrl);
                // Useless Display
                saveInDB(sHotelName, sHotelUrl);
                getHotelData (sHotelUrl);
                getRoomData (sHotelUrl);
                getReviewData (sHotelUrl);
                getComments(); 
            }
        }  
    }
 
  /**
   * Method:      getHotelData
   * Description: parses hotel URL to fetch data
   * Input:       String sHotlUrl
   * Output:      Void.
   */     
    
    public void getHotelData (String sHotlUrl) {
        String sHotelURL = sHotlUrl;
        URL uHotelURL = null;
        String sHotelName, sHotelUrl = null;
        org.jsoup.nodes.Document dHotelDoc = null;
        
        try { 
            uHotelURL = new URL(sHotelURL);
        }
        catch (MalformedURLException e) {
            System.out.println("Invalid starting URL " );
        }
        
        try {
            dHotelDoc = Jsoup.parse(uHotelURL, 5000);
            //System.out.println (dHotelDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }   
        
        getHotelDetails(dHotelDoc);    
    }
 
   /**
   * Method:      getHotelDetails
   * Description: Reads hotel facilities and stores in DB
   * Input:       Document dHotelDoc
   * Output:      Void.
   */   
    
    public void getHotelDetails (org.jsoup.nodes.Document dHotelDoc) {
        //String sActivities, sFood, sInternet, sParking, sServices, sGeneral, sLanguage =null;
        String [] sFacilities = new String[7];
        String sAddress = null, sDescription = null;
        int i = 0;
        try{
        //sActivities = dHotelDoc.select("div.description > p.firstpar").first().text();
        //System.out.println ("Activities: "+sActivities);
            Element content = dHotelDoc.getElementById("summary");
            Elements p= content.getElementsByTag("p");

            String pConcatenated="";
            for (Element x: p) {
              pConcatenated+= x.text();
            }
            sDescription = pConcatenated;
            //System.out.println("Address: "+sAddress);
            
            sAddress = dHotelDoc.select("p.address > span.jq_tooltip").first().text();
            //System.out.println ("Description: "+sDescription);
            /*
            iRoomType = 0;
            Elements tds = dHotelDoc.select("td.ftd");
            
            for (Element td : tds) {
                sRoomType[iRoomType] = td.text();
                System.out.println(td.text());
                iRoomType++;
            }
            */ 
            Elements resultLinks = dHotelDoc.select("div.description > p.firstpar");
                for (Element link : resultLinks) {
                    sFacilities[i] = link.text();
                    //System.out.println ("Facility: "+sFacilities[i]);
                    i++;
                }    

            Elements divs = dHotelDoc.select("div.description");
            for (Element div : divs) {
              String sDivId = div.id();
              if (sDivId.equals("internet_policy") ) {
                  sFacilities[5] = div.text();
                  //System.out.println ("Facility: "+sFacilities[5]);
              }
              if (sDivId.equals("parking_policy") ) {
                  sFacilities[6] = div.text();
                  //System.out.println ("Facility: "+sFacilities[6]);
              }  
            }
        }  catch (Exception e) {
         System.out.println(e.getMessage() );
        }
        // Save hotel details
        saveInDB(sFacilities, sAddress, sDescription);
    }
    
   /**
   * Method:      getRoomData
   * Description: Gets room type, availability and price details
   * Input:       String sRmUrl
   * Output:      Void.
   */    
    
    public void getRoomData (String sRmUrl) {
        
        DateFormat year = new SimpleDateFormat("yyyy");
        DateFormat month = new SimpleDateFormat("MM");
        DateFormat day = new SimpleDateFormat("dd");
        Date date = new Date();
        sDate = date.toString();
        
        // Set date for checkin and checkout
        int iNxtDay = Integer.parseInt(day.format(date).toString())+1;
        int iNxtNxtDay = iNxtDay + 1;
        
        // Create request for room details for next day
        String sRoomUrl = sRmUrl+ "&checkin_monthday="+iNxtDay+"&checkin_year_month="+year.format(date)+
                "-"+month.format(date)+"&checkout_monthday="+iNxtNxtDay+"&checkout_year_month="+year.format(date)+"-"+month.format(date)+"#availability_target";
        URL uRoomUrl = null;
        org.jsoup.nodes.Document dRoomDoc = null;
        
        //System.out.println(sRoomUrl);
        
        try { 
            uRoomUrl = new URL(sRoomUrl);
        }
        catch (MalformedURLException e) {
            System.out.println("Invalid starting URL " );
        }
        
        try {
            dRoomDoc = Jsoup.parse(uRoomUrl, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Gather room details
        getRoomDetails(dRoomDoc);
    }
    
   /**
   * Method:      getRoomDetails
   * Description: Gathers and saves room type, availability and price details
   * Input:       Document
   * Output:      Void.
   */     
    
    public void getRoomDetails (org.jsoup.nodes.Document dRoomDoc) {
        
        String [] sRoomType = new String[100];
        //String [] sRoomKind = new String[50];
        String [] sRoomFaci = new String[200];
        String [] sRoomPrice = new String[200];
        int iCounter = 0;
        iRoomType = 0;
        try {
            // Get room types
            Elements as = dRoomDoc.select("a.jqrt");     

            for (Element a : as) {
                if (!a.text().isEmpty()) {
                    sRoomType[iCounter] = a.text();
                    System.out.println(a.text());
                    iCounter++;
                }
            }

            iRoomType = iCounter;
            iCounter = 0;
            // Get room facilities
            Elements rms = dRoomDoc.select("span[style=display:block;] > span");

            for (Element rm : rms) {
                if (!rm.text().isEmpty()) {
                    sRoomFaci[iCounter] = rm.text();
                    System.out.println(rm.text());
                    iCounter++;
                }
            }
            // Eror check
            if (iRoomType != iCounter) {
                System.out.println("Facilities details missing for some rooms");
            }
            iFaci = iCounter;
            iCounter = 0;
            // Get room price
            Elements rmas = dRoomDoc.select("div.roomDefaultUse ");

            for (Element rma : rmas) {
                if (!rma.text().isEmpty()) {
                    String [] sRoomKind = rma.parent().parent().className().split("counter");
                    int iType = Integer.parseInt(sRoomKind[1].trim())-1;
                    System.out.println(iType);
                    sRoomPrice[iCounter] = "Room_Type_"+iType+":"+rma.text();
                    System.out.println(sRoomPrice[iCounter]);
                    iCounter++;
                }
            }
            // Error check
            if (iRoomType != iCounter) {
                System.out.println("Price details missing for some rooms");
            }
            iPrice = iCounter;
        } catch (Exception e) {
            System.out.println (e.getMessage());
        }
        // Save room details in database
        saveInDB(sHotelName, sRoomType, sRoomFaci, sRoomPrice);
    }
 
   /**
   * Method:      getReviewData
   * Description: Parses data to fetch review details
   * Input:       String sRevUrl
   * Output:      Void.
   */     
    
    public void getReviewData (String sRevUrl) {
        String sReviewURL = sRevUrl+"#tab-reviews";
        URL uReviewURL = null;
        String sHotelName, sHotelUrl = null;
        org.jsoup.nodes.Document dReviewDoc = null;
        
        System.out.println(sRevUrl);
        
        String [] sTemp1 = sRevUrl.split("\\/");
        // The position might change with geographic location. this is for 'gb'
        String sTemp2 = sTemp1[5];
        System.out.println(sName);
        sTemp1 = null;
        
        sTemp1 = sTemp2.split("\\.");
        sName = sTemp1[0];
        System.out.println(sName);
        
        try { 
            uReviewURL = new URL(sReviewURL);
        }
        catch (MalformedURLException e) {
            System.out.println("Invalid starting URL " );
        }
        
        try {
            dReviewDoc = Jsoup.parse(uReviewURL, 5000);
            //System.out.println (dReviewDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }  
        
        getReviewCount(dReviewDoc);
    }
 
   /**
   * Method:      getReviewCount
   * Description: Gets number of reviews
   * Input:       void
   * Output:      Void.
   */    
    
    public void getReviewCount(org.jsoup.nodes.Document dReviewDoc) {
 
        String sRating = null; 
        String [] saRating = new String[10];
        String [] saUserType = new String[10];
        System.out.println ("Extracting required data");
        
        try {
            //Elements divs 
            sRating= dReviewDoc.select("span#rsc_total").first().text();
            System.out.println("Rating: "+sRating);

            String sTemp= dReviewDoc.select("p#rev_out_of > strong").first().text();
            String[] parts = sTemp.split(" ");
            sRevCnt = parts[0];
            System.out.println("Reviews: "+sRevCnt);

            //Element li for ratings
            Elements lis = dReviewDoc.select("li");

            int iCounter = 0;
            for (Element li : lis) {
                if (li.attr("onclick") != null) {
                    try {
                        String [] sTempString1 = li.attr("onclick").split(",") ;
                        String [] sTempString2 = sTempString1[2].split("\\)");

                        saRating[iCounter] = sTempString2[0];
                        System.out.println (saRating[iCounter]);
                        saUserType[iCounter] = li.getElementsByClass("key").text();
                        System.out.println(saUserType[iCounter]);

                        iCounter++;
                    } catch (Exception e) {

                    }
                }
            }         
        } catch (Exception e) {
            System.out.println("Review data not available for "+sHotelName);
        }
        //Save ratings in database
        saveInDB(saRating, saUserType);    
    }
 
   /**
   * Method:      getComments
   * Description: Gets review comments and ratings
   * Input:       void
   * Output:      Void.
   */     
    
    public void getComments () {
        
        // For New York
        // String sCommentURL = "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=us;pagename="
        // For London
        // String sCommentURL = "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=gb;pagename="
        // For Tokyo
        // String sCommentURL = "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=jp;pagename="
        // For Frankfurt
        // String sCommentURL ="http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=de;pagename="        
        // For Mumbai
        // String sCommentURL = "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=in;pagename="
        // For Brasilia
        // String sCommentURL = "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=br;pagename="
        // For Cape Town
        // String sCommentURL = "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=za;pagename="
        // For Sydney
        // String sCommentURL = "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=au;pagename="
        // For Beijing
        // String sCommentURL = "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=cn;pagename="
        // For San Francisco
        // String sCommentURL = "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=us;pagename="
         
        String [] saCommentURL = {
            // For New York
            "http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=us;pagename="
            // For London
            ,"http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=gb;pagename="
            // For Tokyo
            ,"http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=jp;pagename="
            // For Frankfurt
            ,"http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=de;pagename="        
            // For Mumbai
            ,"http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=in;pagename="
            // For Brasilia
            ,"http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=br;pagename="
            // For Cape Town
            ,"http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=za;pagename="
            // For Sydney
            ,"http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=au;pagename="
            // For Beijing
            ,"http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=cn;pagename="
            // For San Francisco
            ,"http://www.booking.com/reviewlist.en-gb.html?sid=9d6ba332a279d42a29cf85a613ff6cda;dcid=1;cc1=us;pagename="  
        };
                                
        String sCommentURL = saCommentURL[iCityPointer] + sName + ";type=total;offset=0;rows=" + sRevCnt;
        URL uCommentURL = null;
        org.jsoup.nodes.Document dCommentsDoc = null;
        try { 
            uCommentURL = new URL(sCommentURL);
        }
        catch (MalformedURLException e) {
            System.out.println("Invalid starting URL " );
        }
        try {
            dCommentsDoc = Jsoup.parse(uCommentURL, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String BKC_KEY_HOTELNAME = "HOTEL_NAME";
        
        Elements comments = dCommentsDoc.select("li.review_tr ");
        int i = 1;
        for (Element comment : comments) {  
            try {
              String sUName = comment.select("div.cell_user_name").first().text();
              String BKC_KEY_USER = "USER_"+i;
              System.out.println(sUName);
              // Save in file
              bwBufferWriter.write(BKC_KEY_USER+":"+sUName);
              bwBufferWriter.newLine();
            }catch (Exception e) {
                e.printStackTrace();
            }
            
            try {
              String sUType = comment.select("div.cell_user_profile").first().text();
              String BKC_KEY_USERTYPE = "USERTYPE_"+i;
              //System.out.println(sUType);
              // Save in file
              bwBufferWriter.write(BKC_KEY_USERTYPE+":"+sUType);
              bwBufferWriter.newLine();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
              String sUDate = comment.select("span.cell_user_date").first().text();
              String BKC_KEY_USERDATE = "USERDATE_"+i;
              //System.out.println(sUDate);
              // Save in file
              bwBufferWriter.write(BKC_KEY_USERDATE+":"+sUDate);
              bwBufferWriter.newLine();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
              String sUPC = comment.select("p.comments_good").first().text();
              String BKC_KEY_POSCOMM = "POSCOMM_"+i;
              //System.out.println(sUPC); 
              bwBufferWriter.write(BKC_KEY_POSCOMM+":"+sUPC);
              bwBufferWriter.newLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
              String sUNC = comment.select("p.comments_bad").first().text();
              String BKC_KEY_NEGCOMM = "NEGCOMM_"+i;
              //System.out.println(sUNC); 
              bwBufferWriter.write(BKC_KEY_NEGCOMM+":"+sUNC);
              bwBufferWriter.newLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
              String sURate= comment.select("span.the_score").first().text();
              String BKC_KEY_USERRATING = "USERRATING_"+i;
              System.out.println(sURate); 
              bwBufferWriter.write(BKC_KEY_USERRATING+":"+sURate);
              bwBufferWriter.newLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
              i++;   
          } 
    }
    
   /**
    * Method:      saveInDB
    * Description: Populates a couchDB JSON document
    * Input:       Various
    * Output:      Void.
    */    
    
    public void saveInDB (String [] saRate, String [] saUType) {
        
        // Save data in file
        try {
            bwBufferWriter.write(BK_KEY_RATING_ALL+":"+saRate[0]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_RATING_FAMILY+":"+saRate[1]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_RATING_COUPLE+":"+saRate[2]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_RATING_FRIENDS+":"+saRate[3]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_RATING_SOLO+":"+saRate[4]); 
            bwBufferWriter.newLine();
        } catch (IOException e ) {
            System.out.println("Error writing in file");
            e.printStackTrace();

        }        
    }
    
    public void saveInDB (String sHN, String sHU) {         
        
        // Save data in file
        try {
            bwBufferWriter.write(BK_KEY_HOTEL+":"+sHN);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_URL+":"+sHU); 
            bwBufferWriter.newLine();
        } catch (IOException e ) {
            System.out.println("Error writing in file");
            e.printStackTrace();
        }
    }
    
    public void saveInDB (String [] sFaci, String sAdd, String sDesc) {
        
        /*
        for (int i= 0; i<iRoomType; i++) {
            properties.put(BK_KEY_ROOMTYPE+i, sRoom[i]);
        }
        */
        // Save data in file
        try {
            bwBufferWriter.write(BK_KEY_ADDRESS+":"+sAdd);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_DESCRIPTION+":"+sDesc);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_ACTIVITY+":"+sFaci[0]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_FOOD+":"+sFaci[1]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_SERVICES+":"+sFaci[2]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_GENERAL+":"+sFaci[3]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_LANGUAGE+":"+sFaci[4]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_INTERNET+":"+sFaci[5]);
            bwBufferWriter.newLine();
            bwBufferWriter.write(BK_KEY_PARKING+":"+sFaci[6]);  
            bwBufferWriter.newLine();
        } catch (IOException e ) {
            System.out.println("Error writing in file");
            e.printStackTrace();
        }
    }
    
    public void saveInDB (String sHN, String [] sType, String [] sFaci, String [] sPrice ) {
        try {
            // Save in file
            bwBufferWriter.write(BK_KEY_DATE+":"+sDate);
            bwBufferWriter.newLine();
            ///////////////
            for (int i= 0; i<iRoomType; i++) {
                // Save in file
                bwBufferWriter.write(BK_KEY_ROOMTYPE+i+":"+sType[i]);
                bwBufferWriter.newLine();
                ///////////////
            }

            for (int i= 0; i<iFaci; i++) {
                // Save in file
                bwBufferWriter.write(BK_KEY_ROOMFACI+i+":"+sFaci[i]);
                bwBufferWriter.newLine();
                ///////////////
            }

            for (int i= 0; i<iPrice; i++) {
                // Save in file
                bwBufferWriter.write(BK_KEY_ROOMPRICE+i+":"+sPrice[i]);
                bwBufferWriter.newLine();
                ///////////////
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    } 
}
/* End of program */
