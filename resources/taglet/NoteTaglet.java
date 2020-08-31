package taglet;

import com.sun.javadoc.Tag;
import java.util.Map;

public class NoteTaglet extends OOPTaglet{

    private static final String NAME = "note";

    private static final String HEADER = "Note";

    public NoteTaglet() {
        super("note", "Note", false);
    }

    public static void register(Map tagletMap) {
        OOPTaglet.register(tagletMap, new NoteTaglet());
    }

    public boolean inField() {
        return true;
    }

    public boolean inConstructor() {
        return true;
    }

    public boolean inMethod() {
        return true;
    }

    public boolean inOverview() {
        return true;
    }

    public boolean inPackage() {
        return true;
    }

    public boolean inType() {
        return true;
    }

    public boolean isInlineTag() {
        return false;
    }

    protected String getFormattedInformal(Tag tag, String informalText) {
        return informalText;
    }
}
