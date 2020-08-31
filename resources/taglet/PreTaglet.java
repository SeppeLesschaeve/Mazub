package taglet;

import com.sun.javadoc.Tag;
import java.util.Map;

public class PreTaglet extends OOPTaglet{

    private static final String NAME = "pre";

    private static final String HEADER = "Precondition";

    public PreTaglet() {
        super("pre", "Precondition", false);
    }

    public static void register(Map tagletMap) {
        OOPTaglet.register(tagletMap, new PreTaglet());
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
