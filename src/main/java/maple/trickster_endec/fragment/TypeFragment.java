package maple.trickster_endec.fragment;

import io.wispforest.endec.Endec;
import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.Identifier;
import maple.trickster_endec.endecs.EndecTomfoolery;


import java.util.Map;

public record TypeFragment(FragmentType<?> typeType) implements Fragment {
    public static final StructEndec<TypeFragment> ENDEC = EndecTomfoolery.lazy(() -> StructEndecBuilder.of(
            Identifier.ENDEC
                    .<FragmentType<?>>xmap(FragmentType.REGISTRY::get, FragmentType::getID)
                    .fieldOf("of_type", TypeFragment::typeType),
            TypeFragment::new
    ));



    public String getId() {
        return typeType.getID().toString();
    }

    @Override
    public FragmentType<?> type() {
        return FragmentType.TYPE;
    }
}