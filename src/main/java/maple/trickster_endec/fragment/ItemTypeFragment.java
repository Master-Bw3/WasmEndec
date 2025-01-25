package maple.trickster_endec.fragment;

import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.Identifier;


public record ItemTypeFragment(  String item) implements Fragment {
    public static final StructEndec<ItemTypeFragment> ENDEC = StructEndecBuilder.of(
            Identifier.ENDEC.xmap(Identifier::toString, Identifier::of).fieldOf("item", ItemTypeFragment::item),
            ItemTypeFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.ITEM_TYPE;
    }

}