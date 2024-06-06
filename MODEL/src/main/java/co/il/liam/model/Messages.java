package co.il.liam.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Messages extends BaseList<Message, Messages> {
    public void sortByTime() {
        // Use a custom comparator for sorting
        Collections.sort(this, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                // Compare dates (oldest to newest)
                int dateComparison = compareDates(message1.getDate(), message2.getDate());
                if (dateComparison != 0) {
                    return dateComparison;
                }

                // If dates are equal, compare times (ascending order)
                int timeComparison = compareTimes(message1.getTime(), message2.getTime());
                if (timeComparison != 0) {
                    return timeComparison;
                }

                // If dates and times are equal, compare seconds (ascending order)
                return message1.getSeconds() - message2.getSeconds();
            }
        });
    }



    // Helper method to compare dates in DD/MM/YYYY format
    private int compareDates(String date1, String date2) {
        // Use a date formatter to parse strings as dates
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dateObject1 = format.parse(date1);
            Date dateObject2 = format.parse(date2);
            return dateObject1.compareTo(dateObject2);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parsing exception (optional)
            return 0; // Or throw an exception
        }
    }

    // Helper method to compare times in 24-hour format (HH:mm)
    private int compareTimes(String time1, String time2) {
        // Parse strings to integers for comparison
        int time1Value = Integer.parseInt(time1.replace(":", ""));
        int time2Value = Integer.parseInt(time2.replace(":", ""));
        return time1Value - time2Value;
    }
}
