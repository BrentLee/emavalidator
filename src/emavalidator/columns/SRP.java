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

package emavalidator.columns;

import emavalidator.AbstractColumnDefinition;
import emavalidator.AbstractErrorEntry.ErrorLevel;
import emavalidator.errors.CellErrorSRP;
import emavalidator.errors.CellErrorSpecificValueFormat;
import emavalidator.validators.CellValidatorRegexFormat;
import emavalidator.validators.ValidatorUtils;

public class SRP extends AbstractColumnDefinition
{
    @Override
    public void buildValidators()
    {
        this.validators.add(new CellValidatorRegexFormat(new String[] {
                            ValidatorUtils.FLOAT_FORMAT_REGEX,
                            ValidatorUtils.EMPTY_STRING_REGEX},
                            false,
                            ErrorLevel.ERROR,
                            CellErrorSpecificValueFormat.FLOAT_FORMAT_ERROR,
                            CellErrorSRP.EXPECTED_SRP_VALUES));
    }
}
