package maple.trickster_endec.fragment;

import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.Identifier;

public record EntityTypeFragment(String entityType) implements Fragment {
    public static final StructEndec<EntityTypeFragment> ENDEC = StructEndecBuilder.of(
            Identifier.ENDEC.xmap(Identifier::toString, Identifier::of).fieldOf("entity_type", EntityTypeFragment::entityType),
            EntityTypeFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.ENTITY_TYPE;
    }
}
