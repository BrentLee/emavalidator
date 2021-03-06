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

public class CellErrorUnsupportedColumn extends AbstractErrorEntry
{
    public static final String CELL_ERROR_UNSUPPORTED_COLUMN = "Invalid column header.";
    public static final String EXPECTED_VALUES_UNSUPPORTED_COLUMN = "Please refer to the parent EMA spec for the precise column names, as these are not flexible.";

    public CellErrorUnsupportedColumn(int rowNumber, int columnNumber, String value)
    {
        super(rowNumber, columnNumber, CellErrorUnsupportedColumn.CELL_ERROR_UNSUPPORTED_COLUMN, ErrorLevel.CRITICAL, value, CellErrorUnsupportedColumn.EXPECTED_VALUES_UNSUPPORTED_COLUMN);
    }
}
