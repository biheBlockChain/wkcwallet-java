package com.wrbug.wkcwallet.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wrbug on 2017/5/4.
 */
public class TextUtils {
    public static boolean isEmpty(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    public static boolean isEmpty(CharSequence... charSequences) {
        if (charSequences != null) {
            for (CharSequence charSequence : charSequences) {
                if (isEmpty(charSequence)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isEqual(CharSequence charSequence1, CharSequence charSequence2) {
        if (charSequence1 != null && charSequence2 != null) {
            return charSequence1.toString().equals(charSequence2.toString());
        }
        return false;
    }

    public static boolean isEqualsIgnoreCase(CharSequence charSequence1, CharSequence charSequence2) {
        if (charSequence1 != null && charSequence2 != null) {
            return charSequence1.toString().equalsIgnoreCase(charSequence2.toString());
        }
        return false;
    }

    public static boolean isDigitsOnly(CharSequence str) {
        if (isEmpty(str)) {
            return false;
        }
        final int len = str.length();
        for (int i = 0; i < len; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static boolean hasEmoji(String content) {
        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return true;
        }
        return false;
    }
}
