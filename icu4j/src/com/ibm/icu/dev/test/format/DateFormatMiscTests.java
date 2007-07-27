/*
 *******************************************************************************
 * Copyright (C) 2001-2007, International Business Machines Corporation and    *
 * others. All Rights Reserved.                                                *
 *******************************************************************************
 */

/** 
 * Port From:   ICU4C v1.8.1 : format : DateFormatMiscTests
 * Source File: $ICU4CRoot/source/test/intltest/miscdtfm.cpp
 **/

package com.ibm.icu.dev.test.format;

import com.ibm.icu.text.*;

import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Locale;
import java.util.Date;

/** 
 * Performs miscellaneous tests for DateFormat, SimpleDateFormat, DateFormatSymbols
 **/
public class DateFormatMiscTests extends com.ibm.icu.dev.test.TestFmwk {

    public static void main(String[] args) throws Exception{
        new DateFormatMiscTests().run(args);
    }
    
    /*
     * @bug 4097450
     */
    public void Test4097450() {
        //
        // Date parse requiring 4 digit year.
        //
        String dstring[] = {
            "97", "1997", "97", "1997", "01", "2001", "01", "2001",
             "1", "1", "11", "11", "111", "111"}; 
    
        String dformat[] = 
            {
                "yy", "yy", "yyyy", "yyyy", "yy", "yy", "yyyy", "yyyy", 
                "yy", "yyyy", "yy", "yyyy", "yy", "yyyy"};         
    
        SimpleDateFormat formatter;
        SimpleDateFormat resultFormatter = new SimpleDateFormat("yyyy");
        logln("Format\tSource\tResult");
        logln("-------\t-------\t-------");
        for (int i = 0; i < dstring.length ; i++) {
            log(dformat[i] + "\t" + dstring[i] + "\t");
            formatter = new SimpleDateFormat(dformat[i]);
            try {
                StringBuffer str = new StringBuffer("");
                FieldPosition pos = new FieldPosition(0);
                logln(resultFormatter.format(formatter.parse(dstring[i]), str, pos).toString()); 
            }
            catch (ParseException exception) {
                errln("exception --> " + exception);
            }
            logln("");
        }
    }
    
    /* @Bug 4099975
     * SimpleDateFormat constructor SimpleDateFormat(String, DateFormatSymbols)
     * should clone the DateFormatSymbols parameter
     */
    public void Test4099975new() {
        Date d = new Date();
        //test SimpleDateFormat Constructor
        {
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.US);
            SimpleDateFormat df = new SimpleDateFormat("E hh:mm", symbols);
            SimpleDateFormat dfClone = (SimpleDateFormat) df.clone();
            
            logln(df.toLocalizedPattern());
            String s0 = df.format(d);
            String s_dfClone = dfClone.format(d);
            
            symbols.setLocalPatternChars("abcdefghijklmonpqr"); // change value of field
            logln(df.toLocalizedPattern());
            String s1 = df.format(d);
            
            if (!s1.equals(s0) || !s1.equals(s_dfClone)) {
                errln("Constructor: the formats are not equal");
            }
            if (!df.equals(dfClone)) {
                errln("The Clone Object does not equal with the orignal source");
            }
        }
        //test SimpleDateFormat.setDateFormatSymbols()
        {
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.US);
            SimpleDateFormat df = new SimpleDateFormat("E hh:mm");
            df.setDateFormatSymbols(symbols);
            SimpleDateFormat dfClone = (SimpleDateFormat) df.clone();
            
            logln(df.toLocalizedPattern());
            String s0 = df.format(d);
            String s_dfClone = dfClone.format(d);
            
            symbols.setLocalPatternChars("abcdefghijklmonpqr"); // change value of field
            logln(df.toLocalizedPattern());
            String s1 = df.format(d);
            
            if (!s1.equals(s0) || !s1.equals(s_dfClone)) {
                errln("setDateFormatSymbols: the formats are not equal");
            }
            if (!df.equals(dfClone)) {
                errln("The Clone Object does not equal with the orignal source");
            }
        }
    }
    
    /*
     * @bug 4117335
     */
    public void Test4117335() {
        final String bc = "\u7D00\u5143\u524D";
        final String ad = "\u897f\u66a6";
        final String jstLong = "\u65e5\u672c\u6a19\u6e96\u6642";
        final String jdtLong = "\u65e5\u672c\u590f\u6642\u9593";
        final String jstShort = "JST";
        final String jdtShort = "JDT";
        final String tzID = "meta/Japan";

        DateFormatSymbols symbols = new DateFormatSymbols(Locale.JAPAN);
        final String[] eras = symbols.getEras();

        assertEquals("BC =", bc, eras[0]);
        assertEquals("AD =", ad, eras[1]);

        // don't use hard-coded index!
        final String zones[][] = symbols.getZoneStrings();
        int index = -1;
        for (int i = 0; i < zones.length; ++i) {
            if (tzID.equals(zones[i][0])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            errln("could not find " + tzID);
        } else {
            assertEquals("Long zone name = ", jstLong, zones[index][1]);
            assertEquals("Short zone name = ", jstShort, zones[index][2]);
            assertEquals("Long zone name (3) = ", jdtLong, zones[index][3]);
            assertEquals("Short zone name (4) = ", jdtShort, zones[index][4]);
        }
    }
}
