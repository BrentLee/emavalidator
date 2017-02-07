/* Copyright 2014 Google Inc. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package emavalidator;

import java.util.HashMap;

import emavalidator.AbstractErrorEntry.ErrorLevel;

/**
 * A class used to summarize all the errors that occur during validation for a single sheet for a single workbook
 * Also stores all of the critical context under which those errors occurred so those can be referenced later for output
 * @author canavan
 */
public class SheetErrorSummary
{
    /**
     * The 0th based index of the sheet where these errors originate from in the input workbook
     */
    private int sheetIndex;

    /**
     * The name of the sheet itself as input from the workbook
     */
    private String sheetName = "";

    /**
     * The spec itself which was used to validate the input sheet. This will contain the list of columns and their respective indexes as input by the user.
     */
    private AbstractEMASpec validatingSpec;

    /**
     * The specific EMA version which was auto detected by this validator and used to get column validator and row validator definitions from
     */
    private AbstractEMASpec.EMAVersion emaVersion;

    /**
     * A list of ErrorEntrys. Should contain all the errors encountered during validation for this specific input sheet
     */
    private HashMap<AbstractErrorEntry, Integer> errorLog = new HashMap<AbstractErrorEntry, Integer>();
    
    /**
     * A list of NotificationEntrys. Should contain all the notifications encountered during validation for this specific input sheet
     */
    private HashMap<AbstractNotificationEntry, Integer> notificationsLog = new HashMap<AbstractNotificationEntry, Integer>();

    /**
     * A mapping of the number of times a specific error severity level has occurred to the number of times it has occurred
     */
    private HashMap<ErrorLevel, Integer> errorCounts = new HashMap<ErrorLevel, Integer>();

    /**
     * @param sheetName The name of the sheet to summarize
     * @param sheetIndex The index of the sheet to summarize within its containing workbook
     * @param emaVersion The EMA version of the sheet to summarize
     * @param validatingSpec The spec that was used to validate this sheet, not necessarily an exact EMA spec
     */
    public SheetErrorSummary(String sheetName, int sheetIndex, AbstractEMASpec.EMAVersion emaVersion, AbstractEMASpec validatingSpec)
    {
        this.sheetIndex = sheetIndex; this.sheetName = sheetName; this.validatingSpec = validatingSpec; this.emaVersion = emaVersion;
        for(ErrorLevel currentLevel : ErrorLevel.values())
            errorCounts.put(currentLevel, 0);
    }

    /**
     * Reset this instance of sheet error summary back to an empty state. Clears the internal log of errors as well as the error counts
     */
    public void clearErrorSheetSummary()
    {
        this.errorLog.clear();
        this.errorCounts.clear();
    }

    /**
     * Append a new error into the internal error log. Additionally, increment the counter for that error type
     * If the error already exists in the log, this error's location is appended to the existing error rather than appending
     * @param newError The new error to add to the error log if it's new, or append to an existing error entry if it isn't
     */
    public void appendError(AbstractErrorEntry newError)
    {
        repeatedError = this.errorLog.get(newError); // attempt to retrieve the error from the error log
        // if this sheet error summary instance already contains this exact error
        if (repeatedError == null) // if the error doesn't exist, null is returned
        {
            this.errorLog.put(newError, 1); // set the initial counter
        }
        else // else, the error already exists
        {
            repeatedError.assimilateError(newError); // add the new error's data to the existing error
            this.errorLog.put(repeatedError, this.errorLog.get(repeatedError) + 1); // increment the error count
            this.errorCounts.put(newError.getErrorLevel(), errorCounts.get(newError.getErrorLevel()) + 1); // increment the error type counter
        }
    }
    
    public void appendNotification(AbstractNotificationEntry newNotification)
    {
        repeatedNotification = this.notificationsLog.get(newNotification); // attempt to retrieve the notification from the notification log
        // if this sheet error summary instance already contains this exact notification
        if(repeatedNotification == null) // if the notification doesn't exist, null is returned
        {
            this.notificationsLog.put(newNotification, 1); // set the initial counter
        }
        else // else, the notification already exists
        {
            repeatedNotification.assimilateNotification(newNotification); // add the new notification's data to the existing notification
            this.notificationsLog.put(repeatedNotification, this.notificationsLog.get(repeatedNotification) + 1); // increment the notification count
        }
    }

    /**
     * Print out every error in this SheetErrorSummary's internal error log to the standard out
     */
    public void printSheetErrorSummary()
    {
        for(AbstractErrorEntry currentError : this.errorLog)
            System.out.println(currentError.toLogString(this.validatingSpec));
    }

    /**
     * @return The version of the EMA spec that this error log pulls relevant information from when printing the errors that it stores such as column names
     */
    public AbstractEMASpec.EMAVersion getEMAVersion() { return this.validatingSpec.getEMAVersion(); }

    /**
     * @return The index of the sheet that this summary is storing errors from
     */
    public int getSheetIndex() { return sheetIndex; }

    /**
     * @return The index of the sheet that this summary is storing errors from, but 1 based not 0 based for better readability
     */
    public int getExcelCoordinatesSheetIndex() { return sheetIndex+1; }

    /**
     * @return The name of the sheet that this summary is storing errors from
     */
    public String getSheetName() { return sheetName; }

    /**
     * @return The entire spec EMA spec that this summary was graded against. Stores things like custom column configurations, EMA Version, and performs validation
     */
    public AbstractEMASpec getValidatingEMASpec() { return this.validatingSpec; }

    /**
     * @return The mapping of ErrorLevel values to their integer count which is the number of times an error at that level has occurred
     */
    public HashMap<ErrorLevel, Integer> getErrorCounts() { return errorCounts; }

    /**
     * @return The total number of errors in this SheetErrorSummary. Calculated by adding up all the error locations of every single error entry in this SheetErrorSummary's error log
     */
    public int getErrorCount()
    {
        int totalErrorCount = 0
        for (Entry<AbstractErrorEntry, Integer> entry: errorLog.entrySet())
        {
            totalErrorCount += entry.getValue();
        }
        return totalErrorCount;
    }

    /**
     * @return The total number of columns with errors in this SheetErrorSummary. Calculated by adding up all the error locations of every single error entry in this SheetErrorSummary's error log
     */
    public int getErrorColumnsCount() { return this.errorLog.keySet().size(); }

    /**
     * @return The total number of notifications in this SheetErrorSummary. Calculated by adding up all the notification locations of every single notitifcation entry in this SheetErrorSummary's notification log
     */
    public int getNotificationCount()
    {
        int totalNotificationCount = 0;
        for (Entry<AbstractNotificationEntry>, Integer> entry: notificationsLog.entrySet())
        {
            totalNotificationCount += entry.getValue();
        }
        return totalNotificationCount;
    }

    /**
     * @return This SheetErrorSummary's internal error log. Refer to the ErrorLog class for full documentation.
     */
    public HashMap<AbstractErrorEntry, Integer> getErrorLog() { return errorLog; }

    /**
     * @return This SheetErrorSummary's internal error log. Refer to the ErrorLog class for full documentation.
     */
    public HashMap<AbstractNotificationEntry, Integer> getNotificationsLog() { return notificationsLog; }
    
    /**
     * Format this SheetErrorSummary's name, index, EMA version, and error count into a formatted string for error log output
     */
    public String getSheetPrintString()
    {
        StringBuilder returnThis = new StringBuilder(100);
        returnThis.append("Sheet  name: ")
                  .append(this.sheetName)
                  .append(System.lineSeparator())
                  .append("Sheet index: ")
                  .append(this.getExcelCoordinatesSheetIndex())
                  .append(System.lineSeparator())
                  .append("EMA Version: ")
                  .append(this.emaVersion.toString())
                  .append(System.lineSeparator())
                  .append("Error count: ")
                  .append(this.getErrorCount())
                  .append(System.lineSeparator());
        return returnThis.toString();
    }
}
