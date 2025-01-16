package maple.trickster_endec.fragment;

import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.Identifier;

public record DimensionFragment(String world) implements Fragment {
    public static StructEndec<DimensionFragment> ENDEC = StructEndecBuilder.of(
            Identifier.ENDEC.xmap(Identifier::toString, Identifier::of).fieldOf("world", DimensionFragment::world),
            DimensionFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.DIMENSION;
    }

}