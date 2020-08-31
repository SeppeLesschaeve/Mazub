package taglet;

import com.sun.javadoc.Tag;
import java.util.Map;

public class EffectTaglet extends OOPTaglet{
    private static final String NAME = "effect";

    private static final String HEADER = "Effect";

    public EffectTaglet() {
        super("effect", "Effect", false);
    }

    public static void register(Map tagletMap) {
        OOPTaglet.register(tagletMap, new EffectTaglet());
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
