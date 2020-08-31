package taglet;

import com.sun.javadoc.Tag;
import java.util.Map;

public class PostTaglet extends OOPTaglet{

    private static final String NAME = "post";

    private static final String HEADER = "Postcondition";

    public PostTaglet() {
        super("post", "Postcondition", false);
    }

    public static void register(Map tagletMap) {
        OOPTaglet.register(tagletMap, new PostTaglet());
    }

    public boolean inField() {
        return false;
    }

    public boolean inConstructor() {
        return true;
    }

    public boolean inMethod() {
        return true;
    }

    public boolean inOverview() {
        return false;
    }

    public boolean inPackage() {
        return false;
    }

    public boolean inType() {
        return false;
    }

    public boolean isInlineTag() {
        return false;
    }

    protected String getFormattedInformal(Tag tag, String informalText) {
        return informalText;
    }
}
