package taglet;

import com.sun.javadoc.Tag;
import java.util.Map;

public class ReturnTaglet extends OOPTaglet{

    private static final String NAME = "return";

    private static final String HEADER = "Returns";

    public ReturnTaglet() {
        super("return", "Returns", true);
    }

    public static void register(Map tagletMap) {
        OOPTaglet.register(tagletMap, new ReturnTaglet());
    }

    public boolean inField() {
        return false;
    }

    public boolean inConstructor() {
        return false;
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

    protected String getPluralHeader() {
        return getHeader();
    }
}
