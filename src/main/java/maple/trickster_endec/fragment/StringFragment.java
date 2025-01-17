package maple.trickster_endec.fragment;

import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSProperty;

public record StringFragment(@JSExport @JSProperty String value) implements Fragment {
    public static final StructEndec<StringFragment> ENDEC = StructEndecBuilder.of(
            StructEndec.STRING.fieldOf("value", StringFragment::value),
            StringFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.STRING;
    }

}