package taglet;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Tag;
import com.sun.javadoc.ThrowsTag;
import com.sun.javadoc.Type;
import com.sun.tools.doclets.formats.html.TagletWriterImpl;
import com.sun.tools.doclets.internal.toolkit.taglets.TagletOutput;
import com.sun.tools.doclets.internal.toolkit.taglets.TagletWriter;
import com.sun.tools.doclets.internal.toolkit.taglets.ThrowsTaglet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThrowsTaglet extends ThrowsTaglet{

    public static void register(Map tagletMap) {
        OOPTaglet.register(tagletMap, new ThrowsTaglet());
    }

    protected TagletOutput throwsTagsOutput(ThrowsTag[] throwTags, TagletWriter writer, Set<String> alreadyDocumented, boolean allowDups) {
        TagletOutput result = writer.getOutputInstance();
        if (throwTags.length > 0) {
            byte b;
            int i;
            ThrowsTag[] arrayOfThrowsTag;
            for (i = (arrayOfThrowsTag = throwTags).length, b = 0; b < i; ) {
                ThrowsTag tt = arrayOfThrowsTag[b];
                ClassDoc cd = tt.exception();
                if (allowDups || (!alreadyDocumented.contains(tt.exceptionName()) && (cd == null || !alreadyDocumented.contains(cd.qualifiedName())))) {
                    if (alreadyDocumented.size() == 0)
                        result.appendOutput(((TagletWriterImpl)writer).getThrowsHeader());
                    ThrowsTag tag = new ThrowsTagCopy(tt);
                    result.appendOutput(((TagletWriterImpl)writer).throwsTagOutput(tag));
                    alreadyDocumented.add((cd != null) ? cd.qualifiedName() : tt.exceptionName());
                }
                b++;
            }
        }
        return result;
    }

    private static class TagCopy implements Tag {
        private String name;

        private Doc holder;

        private String kind;

        private String text;

        private Tag[] itags;

        private Tag[] ftags;

        private SourcePosition pos;

        public TagCopy(Tag tag, String text) {
            this(tag);
            this.text = text;
        }

        public TagCopy(Tag tag) {
            this.name = tag.name();
            this.holder = tag.holder();
            this.kind = tag.kind();
            this.text = tag.text();
            this.ftags = tag.firstSentenceTags();
            this.pos = tag.position();
            this.itags = tag.inlineTags();
        }

        public TagCopy(String text) {
            this.text = text;
            this.name = "Text";
            this.holder = null;
            this.kind = "kind";
            this.ftags = null;
            this.pos = null;
            this.itags = null;
        }

        public String name() {
            return this.name;
        }

        public Doc holder() {
            return this.holder;
        }

        public String kind() {
            return this.kind;
        }

        public String text() {
            return this.text;
        }

        public void setInlineTags(Tag[] itags) {
            this.itags = itags;
        }

        public Tag[] inlineTags() {
            return this.itags;
        }

        public Tag[] firstSentenceTags() {
            return this.ftags;
        }

        public SourcePosition position() {
            return this.pos;
        }
    }

    private static class ThrowsTagCopy extends TagCopy implements ThrowsTag {
        private String xname;

        private String comment;

        private ClassDoc exception;

        private Type type;

        public ThrowsTagCopy(ThrowsTag tag) {
            super(tag);
            this.xname = tag.exceptionName();
            this.comment = tag.exceptionComment();
            this.exception = tag.exception();
            this.type = tag.exceptionType();
            setInlineTags(remake(tag.inlineTags()));
        }

        private Tag[] remake(Tag[] given) {
            List<Tag> result = Arrays.asList(given);
            boolean pastFormal = false;
            boolean pastFirst = false;
            System.out.println("NB tags: " + result.size());
            if (result.size() == 0) {
                result = Collections.synchronizedList(new ArrayList<Tag>());
                result.add(new ThrowsTaglet.TagCopy("[CAN]"));
            }
            for (int i = 0; i != result.size(); i++) {
                Tag tag = result.get(i);
                if (!tag.name().equals("@link")) {
                    System.out.println("DOING: " + tag.text());
                    String text = OOPTaglet.getInformalPart(tag.text());
                    text = text.trim();
                    if (!pastFirst) {
                        int indexM = text.toLowerCase().indexOf("[must]");
                        int indexC = text.toLowerCase().indexOf("[can]");
                        if (indexM > -1 && indexM < 7) {
                            text = text.replaceFirst("\\[[m|M][u|U][s|S][t|T]\\]", "<CODE><B>[MUST]</B></CODE>");
                        } else if (indexC > -1 && indexC < 7) {
                            text = text.replaceFirst("\\[[c|C][a|A][n|N]\\]", "<CODE><B>[CAN]</B></CODE>");
                        } else {
                            text = "<CODE><B>[CAN] </B></CODE>" + text;
                        }
                        result.set(i, new ThrowsTaglet.TagCopy(tag, text));
                        pastFirst = true;
                    }
                    String[] formals = OOPTaglet.getFormalParts(tag.text());
                    if (formals.length != 0) {
                        int nbformal = formals.length;
                        int j = 0;
                        if (!pastFormal) {
                            text = String.valueOf(text) + "<CODE><B>";
                            pastFormal = true;
                        }
                        while (j != formals.length) {
                            text = String.valueOf(text) + ("<br>    " + formals[j]).trim().replaceAll(" ", "&nbsp;");
                            j++;
                            nbformal--;
                        }
                        if (nbformal == 0)
                            text = String.valueOf(text) + "</CODE></B>";
                        result.set(i, new ThrowsTaglet.TagCopy(tag, text));
                    }
                }
            }
            return result.<Tag>toArray(given);
        }

        public String exceptionName() {
            return this.xname;
        }

        public String exceptionComment() {
            return this.comment;
        }

        public ClassDoc exception() {
            return this.exception;
        }

        public Type exceptionType() {
            return this.type;
        }
    }
}
