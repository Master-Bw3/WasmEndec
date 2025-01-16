package maple.trickster_endec.fragment;

import io.wispforest.endec.StructEndec;
import maple.trickster_endec.endecs.EndecTomfoolery;

public record VoidFragment() implements Fragment {
    public static final VoidFragment INSTANCE = new VoidFragment();
    public static final StructEndec<VoidFragment> ENDEC = EndecTomfoolery.unit(INSTANCE);

    @Override
    public FragmentType<?> type() {
        return FragmentType.VOID;
    }
}
