package maple.trickster_endec.fragment;

import io.wispforest.endec.StructEndec;
import maple.trickster_endec.endecs.EndecTomfoolery;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSMethod;

import java.util.List;
import java.util.Random;

public record ZalgoFragment(int index) implements Fragment {
    public static final StructEndec<ZalgoFragment> ENDEC = EndecTomfoolery.unit(ZalgoFragment::new);
    public static final Random RANDOM = new Random(0xABABABA);
    public static final List<String> SILLIES = List.of(
            "you just",
            "lost",
            "the game"
    );

    public ZalgoFragment() {
        this(RANDOM.nextInt(SILLIES.size()));
    }

    @JSExport
    @JSMethod
    public void noop() {}

    @Override
    public FragmentType<?> type() {
        return FragmentType.ZALGO;
    }

}