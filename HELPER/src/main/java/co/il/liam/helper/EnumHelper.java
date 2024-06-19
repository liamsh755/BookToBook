package co.il.liam.helper;

public class EnumHelper {

    public static String fixEnumText(String s) {
        String capitalize =  s.charAt(0) + s.substring(1).toLowerCase();
        int underscore = capitalize.indexOf('_');
        if (underscore != -1) {
            return capitalize.substring(0, underscore) + " " + capitalize.substring(underscore + 1);
        }
        return capitalize;
    }

    public static String revertFixedEnumText(String s) {
        String upper = s.toUpperCase();
        int space = upper.indexOf(' ');
        if (space != -1) {
            return upper.substring(0, space) + "_" + upper.substring(space + 1);
        }
        return upper;
    }
}
