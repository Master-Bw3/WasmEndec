package maple.trickster_endec.fragment;

import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.Identifier;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSProperty;

public record BlockTypeFragment(@JSExport @JSProperty String block) implements Fragment {
    public static final StructEndec<BlockTypeFragment> ENDEC = StructEndecBuilder.of(
            Identifier.ENDEC.xmap(Identifier::toString, Identifier::of).fieldOf("block", BlockTypeFragment::block),
            BlockTypeFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.BLOCK_TYPE;
    }
}
