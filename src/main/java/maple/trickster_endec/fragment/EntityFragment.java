package maple.trickster_endec.fragment;


import com.mojang.serialization.Codec;
import io.wispforest.endec.Endec;
import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.endecs.EndecTomfoolery;


import java.util.UUID;


public record EntityFragment(UUID uuid,   String name) implements Fragment {
    public static final StructEndec<EntityFragment> ENDEC = StructEndecBuilder.of(
            EndecTomfoolery.UUID.fieldOf("uuid", EntityFragment::uuid),
            Endec.STRING.fieldOf("name", EntityFragment::name),
            EntityFragment::new
    );


    public String getUuid() {
        return uuid.toString();
    }

    @Override
    public FragmentType<?> type() {
        return FragmentType.ENTITY;
    }
}
