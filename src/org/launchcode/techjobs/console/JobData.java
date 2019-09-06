package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;
    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     * <p>
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column Column that should be searched.
     * @param value  Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue.contains(value)) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

    private static String makeLowerCase(String searchTerm){
        return searchTerm.toLowerCase();
    }

    private static String makeMapLowerCase (Object value) {
       String stringValue = value.toString();
       String lowerCaseValue = makeLowerCase(stringValue);
       return lowerCaseValue;
    }

    public static ArrayList<HashMap<String, String>> findByValue(String searchTerm) {
        loadData();
        String lowerCaseTerm = makeLowerCase(searchTerm);
        String lowerCaseValue = null;
        ArrayList<HashMap<String, String>> matchedJobs = new ArrayList<>();

        for (HashMap<String, String> job : allJobs) {
            for (Map.Entry lineItem : job.entrySet()){
               lowerCaseValue = makeMapLowerCase(lineItem.getValue());
               if (lowerCaseValue.contains(lowerCaseTerm)){
                   matchedJobs.add(job);
               }
            }
        }
        if (matchedJobs.size() == 0){
            System.out.println("Sorry, we could not find a job with the search term " + searchTerm);
        }
        return matchedJobs;
    }
}
//Note, if I want show results that include jobs that are similar to the search term but not the exact search term, I would create a method that
//returns a boolean value. That method would recieve a hashmap as a parameter. The hashmap would contain the value from the job and the search term.
// Then it would split the value into an arraylist of strings (after I converted it into a string). Then it would
// do a for loop which would traverse the arraylist and check if each string in that list is contained in the searchTerm. If it is contained,
// it would return true.
//it would get the hashmap because the searchbyvalue method would have an extra hashmap variable which would take the value variable from job
// and then it would take the lower case search term. (the searchterm would stay the same as the for loop iterates in the search by value method).
// private static Boolean isInTerm(Hashmap value, term) {
//for (value : value){
// if (term.contains(value){
// return true -- continue
//}
//} Then in the find by value method do this:
// if (lowerCaseValue.contains(lowerCaseTerm) or isInTerm){



