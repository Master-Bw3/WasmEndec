package maple.trickster_endec.fragment;

import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.Identifier;
import maple.trickster_endec.endecs.EndecTomfoolery;

public record TypeFragment(FragmentType<Fragment> typeType) implements Fragment {
    public static final StructEndec<TypeFragment> ENDEC = EndecTomfoolery.lazy(() -> StructEndecBuilder.of(
            Identifier.ENDEC.xmap((x) -> (FragmentType<Fragment>) FragmentType.REGISTRY.get(x), FragmentType::getID).fieldOf("of_type", TypeFragment::typeType),
            TypeFragment::new
    ));

    @Override
    public FragmentType<?> type() {
        return FragmentType.TYPE;
    }
}