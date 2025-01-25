package maple.trickster_endec.fragment;

import io.wispforest.endec.Endec;
import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;


public class BooleanFragment implements Fragment {
    public static final StructEndec<BooleanFragment> ENDEC = StructEndecBuilder.of(
            Endec.BOOLEAN.fieldOf("bool", BooleanFragment::isBool),
            BooleanFragment::of
    );
    public static final BooleanFragment TRUE = new BooleanFragment(true);
    public static final BooleanFragment FALSE = new BooleanFragment(false);

    public final boolean bool;

    private BooleanFragment(boolean bool) {
        this.bool = bool;
    }

    @Override
    public FragmentType<?> type() {
        return FragmentType.BOOLEAN;
    }



    public boolean isBool() {
        return bool;
    }

    @Override
    public int hashCode() {
        return bool ? 1 : 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BooleanFragment fragment && fragment.bool == bool;
    }

    @Override
    public String toString() {
        return "BooleanFragment[bool=" + bool + ']';
    }

    public static BooleanFragment of(boolean bool) {
        return bool ? TRUE : FALSE;
    }
}