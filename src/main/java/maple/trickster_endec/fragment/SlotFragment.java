package maple.trickster_endec.fragment;

import com.mojang.datafixers.util.Either;
import io.wispforest.endec.Endec;
import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.endecs.EitherEndec;
import maple.trickster_endec.endecs.EndecTomfoolery;

import java.util.Optional;
import java.util.UUID;

public record SlotFragment(  int slot, Optional<Either<EndecTomfoolery.VectorI, UUID>> source) implements Fragment {
    public static final StructEndec<SlotFragment> ENDEC = StructEndecBuilder.of(
            Endec.INT.fieldOf("slot", SlotFragment::slot),
            EndecTomfoolery.safeOptionalOf(new EitherEndec<>(EndecTomfoolery.ALWAYS_READABLE_BLOCK_POS, EndecTomfoolery.UUID, true)).fieldOf("source", SlotFragment::source),
            SlotFragment::new
    );



    public Object getSource() {
        if (source.isEmpty()) {
            return null;
        } else if (source.get().right().isPresent()) {
            return source.get().right().get().toString();
        } else {
           return source.get().left().get();
        }
    }

    @Override
    public FragmentType<?> type() {
        return FragmentType.SLOT;
    }
}