package org.chess;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TSUtils {

    /**
     * Returns the start and end positions of the matching regular expression in the string 'orig'
     * @param orig The string to search
     * @param regex The regex to look for
     * @return a Range containing the start and end indices of the match.
     */
    public static Range getPosition(String orig, String regex){
        String sub = TSUtils.substring(orig,regex);
        int index=orig.indexOf(sub);
        return new Range(index,index+sub.length());
    }

    public static String getMatch(String orig, String regex, int start){
        String sub = orig.substring(start);
        Pattern p = Pattern.compile(".*?("+regex+").*?");
        Matcher m = p.matcher(sub);
        if (m.find()){
            return m.group(1);
        }
        return null;
    }
    
    /**
     * Returns true if orig contains the specified regular expression
     * 
     * @param orig
     * @param string
     * @return
     */
    public static boolean containsRegex(String orig, String regex) {
        Pattern p = Pattern.compile(regex, Pattern.DOTALL);
        Matcher m = p.matcher(orig);
        return m.find();
    }

    /**
     * Returns the substring containing the first matching regex pattern on
     * 'original'
     * <p>
     * 
     * @param original - The string to create a substring from
     * @param regex - The regular expression to match
     * @return - The first resulting regex match, or an empty string if no
     *         matches exist.
     */
    public static String substring(String original, String regex) {
        Pattern p = Pattern.compile(regex, Pattern.DOTALL);
        Matcher m = p.matcher(original);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    /**
     * Returns the substring containing the nth matching regex group on
     * 'original'
     * <p>
     * For example, if you want to match a string inside single quotes, but didn't want to include the quotes, you would
     * use
     * <p>
     * {@code substring("my 'test string'","'(.*?)'",1)}
     * <p>
     * Group 1 is what's inside the (), which in this case is what's inside the single quotes.
     * <p>
     * 
     * @param original - The string to create a substring from
     * @param regex - The regular expression to match
     * @param n - The group index to return
     * @return - The nth resulting regex group, or an empty string if no matches
     *         exist.
     */
    public static String substringByGroup(String original, String regex, int n) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(original);
        if (m.find()) {
            try {
                return m.group(n);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }
        return "";
    }

    /**
     * Returns the substring containing the index'th matching regex pattern on
     * 'original'
     * <p>
     * 
     * @param original - The string to create a substring from
     * @param regex - The regular expression to match
     * @param index - You can guess what this does.
     * @return - The index'th resulting regex match, or an empty string if less than 'index'
     *         matches exist.
     */
    public static String substring(String original, String regex, int index) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(original);
        // seek to the result we want
        for (int i = 0; i < index; i++) {
            if (!m.find()) {
                return "";
            }
        }
        if (m.find()) {
            try {
                return m.group();
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }
        return "";
    }

    /**
     * Returns the substring containing the index'th matching regex pattern
     * containing the nth matching regex group on 'original'
     * <p>
     * 
     * @param original - The string to create a substring from
     * @param regex - The regular expression to match
     * @param index - return the index'th matching
     * @param group - the n'th group we want to match
     * @return - The index'th resulting regex match containing the group'th matching
     *         or an empty string if less than 'index' matches exist.
     */
    public static String substring(String original, String regex, int index, int group) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(original);
        // seek to the result we want
        for (int i = 0; i < index; i++) {
            if (!m.find()) {
                return "";
            }
        }
        if (m.find()) {
            try {
                return m.group(group);
            } catch (IndexOutOfBoundsException e) {
                return "";
            }
        }
        return "";
    }
}
