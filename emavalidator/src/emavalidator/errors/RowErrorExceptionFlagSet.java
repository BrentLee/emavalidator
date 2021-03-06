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

package emavalidator.errors;

import emavalidator.AbstractErrorEntry;

public class RowErrorExceptionFlagSet extends AbstractErrorEntry
{
    // Remove this check
    public static final String EXCEPTION_FLAG_NOT_SET_ERROR = "The exception flag column must be 'Yes' if manual columns are filled in.";
    
    public RowErrorExceptionFlagSet(int rowNumber, String errorMessage, String value, String expected)
    {
        super(rowNumber, errorMessage, ErrorLevel.WARNING, value, expected);
    }
}
