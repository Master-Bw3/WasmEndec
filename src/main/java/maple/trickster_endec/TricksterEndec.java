package maple.trickster_endec;


import maple.trickster_endec.endecs.JSONSerializer;
import maple.trickster_endec.fragment.*;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSExportClasses;

@JSExportClasses({
        BlockTypeFragment.class,
        BooleanFragment.class,
        DimensionFragment.class,
        EntityFragment.class,
        EntityTypeFragment.class,
        Fragment.class,
        ItemTypeFragment.class,
        ListFragment.class,
        MapFragment.class,
        NumberFragment.class,
        Pattern.class,
        PatternGlyph.class,
        SlotFragment.class,
        SpellPart.class,
        StringFragment.class,
        TypeFragment.class,
        VectorFragment.class,
        VoidFragment.class,
        ZalgoFragment.class,
        Pattern.PatternEntry.class
})
public class TricksterEndec {
    public static void main(String[] args) {
        var part = SpellPart.fromBase64("YwqT9+ZnYGBgYGTMYWBgYGaEsBlhwqNio2JDUWwDR0MrAwMDE1hsmJEAVqVUkLQDAAA=");

        System.out.println(part.toBase64());
    }

    @JSExport
    public static SpellPart decodeBase64Spell(String base64String) {

        return SpellPart.fromBase64(base64String);
    }

}