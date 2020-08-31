package taglet;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

import java.util.Map;
import java.util.regex.Pattern;

public abstract class OOPTaglet implements Taglet {

    static final Pattern SPLIT = Pattern.compile("\n\\s+\\|");

    private final String name;

    private final String header;

    private final boolean split;

    protected OOPTaglet(String name, String header, boolean split) {
        this.name = name;
        this.header = header;
        this.split = split;
    }

    protected static void register(Map<String, Object> tagletMap, Object tag) {
        String name;
        try {
            name = ((Taglet)tag).getName();
        } catch (ClassCastException e) {
            name = ((Taglet)tag).getName();
        }
        if (tagletMap.get(name) != null)
            tagletMap.remove(name);
        tagletMap.put(name, tag);
    }

    public String getName() {
        return this.name;
    }

    public String toString(Tag tag) {
        return toString(tag, tag.text());
    }

    public String toString(Tag[] tags) {
        if (tags.length == 0)
            return null;
        String result = "";
        if (this.split) {
            byte b;
            int i;
            Tag[] arrayOfTag;
            for (i = (arrayOfTag = tags).length, b = 0; b < i; ) {
                Tag tag = arrayOfTag[b];
                result = String.valueOf(result) + toString(tag);
                b++;
            }
        } else {
            if (tags.length == 0)
                return result;
            if (tags.length == 1)
                return toString(tags[0]);
            result = String.valueOf(result) + getPluralHeader() + "<DD>";
            byte b;
            int i;
            Tag[] arrayOfTag;
            for (i = (arrayOfTag = tags).length, b = 0; b < i; ) {
                Tag tag = arrayOfTag[b];
                result = String.valueOf(result) + "<li>" + getSingleTag(tag, tag.text()) + "</li>\n";
                b++;
            }
            result = String.valueOf(result) + "</DD>";
        }
        return result;
    }

    protected abstract String getFormattedInformal(Tag paramTag, String paramString);

    private String getSingleTag(Tag tag, String tagtext) {
        String result = getFormattedInformal(tag, getInformalPart(tagtext));
        String[] formals = getFormalParts(tagtext);
        if (formals.length == 0)
            return result;
        String formal = "";
        byte b;
        int i;
        String[] arrayOfString1;
        for (i = (arrayOfString1 = formals).length, b = 0; b < i; ) {
            String f = arrayOfString1[b];
            f = "<br>    " + f;
            f = f.trim();
            formal = String.valueOf(formal) + f;
            b++;
        }
        result = String.valueOf(result) + "<CODE><B>" + formal.replaceAll(" ", "&nbsp;") + "</B></CODE>";
        return result;
    }

    private String toString(Tag tag, String tagtext) {
        return String.valueOf(getHeader()) + "<DD>" + getSingleTag(tag, tagtext) + "</DD>\n";
    }

    static String getInformalPart(String tagtext) {
        String temp = SPLIT.split(tagtext)[0];
        if (temp.startsWith("|"))
            return "";
        return temp;
    }

    static String[] getFormalParts(String tagtext) {
        String inf = getInformalPart(tagtext);
        tagtext = tagtext.substring(inf.length(), tagtext.length());
        if (tagtext.equals(""))
            return new String[0];
        int index1 = tagtext.indexOf("|");
        tagtext = tagtext.substring(index1 + 1, tagtext.length());
        int nbFirstSpaces = countLeadingSpaces(tagtext);
        String[] result = SPLIT.split(tagtext);
        for (int i = 0; i < result.length; i++)
            result[i] = stripLeadingSpaces(result[i], nbFirstSpaces);
        return result;
    }

    static int countLeadingSpaces(String string) {
        int result = 0;
        while (string.indexOf(" ") == 0) {
            result++;
            string = string.substring(1, string.length());
        }
        return result;
    }

    static String stripLeadingSpaces(String string, int nbSpaces) {
        int count = nbSpaces;
        while (string.indexOf(" ") == 0 && count != 0) {
            string = string.substring(1, string.length());
            count--;
        }
        return string;
    }

    protected String getHeader() {
        return "<DT><B>" + this.header + ":" + "</B>";
    }

    protected String getPluralHeader() {
        return "<DT><B>" + this.header + "s:" + "</B>";
    }
}
