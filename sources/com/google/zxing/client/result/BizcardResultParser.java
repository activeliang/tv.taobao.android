package com.google.zxing.client.result;

import com.google.zxing.Result;
import java.util.ArrayList;
import java.util.List;

public final class BizcardResultParser extends AbstractDoCoMoResultParser {
    public AddressBookParsedResult parse(Result result) {
        String rawText = getMassagedText(result);
        if (!rawText.startsWith("BIZCARD:")) {
            return null;
        }
        String fullName = buildName(matchSingleDoCoMoPrefixedField("N:", rawText, true), matchSingleDoCoMoPrefixedField("X:", rawText, true));
        String title = matchSingleDoCoMoPrefixedField("T:", rawText, true);
        String org2 = matchSingleDoCoMoPrefixedField("C:", rawText, true);
        return new AddressBookParsedResult(maybeWrap(fullName), (String[]) null, (String) null, buildPhoneNumbers(matchSingleDoCoMoPrefixedField("B:", rawText, true), matchSingleDoCoMoPrefixedField("M:", rawText, true), matchSingleDoCoMoPrefixedField("F:", rawText, true)), (String[]) null, maybeWrap(matchSingleDoCoMoPrefixedField("E:", rawText, true)), (String[]) null, (String) null, (String) null, matchDoCoMoPrefixedField("A:", rawText, true), (String[]) null, org2, (String) null, title, (String[]) null, (String[]) null);
    }

    private static String[] buildPhoneNumbers(String number1, String number2, String number3) {
        List<String> numbers = new ArrayList<>(3);
        if (number1 != null) {
            numbers.add(number1);
        }
        if (number2 != null) {
            numbers.add(number2);
        }
        if (number3 != null) {
            numbers.add(number3);
        }
        int size = numbers.size();
        if (size == 0) {
            return null;
        }
        return (String[]) numbers.toArray(new String[size]);
    }

    private static String buildName(String firstName, String lastName) {
        if (firstName == null) {
            return lastName;
        }
        return lastName == null ? firstName : firstName + ' ' + lastName;
    }
}
