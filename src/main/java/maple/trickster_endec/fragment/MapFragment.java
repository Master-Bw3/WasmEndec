package maple.trickster_endec.fragment;

import io.wispforest.endec.Endec;
import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;

import java.util.Map;

public record MapFragment(Map<Fragment, Fragment> map) implements Fragment {
    public static final StructEndec<MapFragment> ENDEC = StructEndecBuilder.of(
            Endec.map(Fragment.ENDEC, Fragment.ENDEC).fieldOf("entries", MapFragment::map),
            MapFragment::new
    );

    @Override
    public FragmentType<?> type() {
        return FragmentType.MAP;
    }

}
