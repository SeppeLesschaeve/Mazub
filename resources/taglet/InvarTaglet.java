package taglet;

import com.sun.javadoc.Tag;
import java.util.Map;

public class InvarTaglet extends OOPTaglet{

    private static final String NAME = "invar";

    private static final String HEADER = "Invariant";

    public InvarTaglet() {
        super("invar", "Invariant", false);
    }

    public static void register(Map tagletMap) {
        OOPTaglet.register(tagletMap, new InvarTaglet());
    }

    public boolean inField() {
        return true;
    }

    public boolean inConstructor() {
        return false;
    }

    public boolean inMethod() {
        return false;
    }

    public boolean inOverview() {
        return false;
    }

    public boolean inPackage() {
        return false;
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
