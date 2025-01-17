package maple.trickster_endec.fragment;

import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;

import java.util.List;

public record ListFragment(List<Fragment> fragments) implements Fragment {
    public static final StructEndec<ListFragment> ENDEC = StructEndecBuilder.of(
            Fragment.ENDEC.listOf().fieldOf("fragments", ListFragment::fragments),
            ListFragment::new
    );

    @JSExport
    @JSProperty
    public JSArray<Fragment> getFragments() {
        var array = new JSArray<Fragment>();
        fragments.forEach(array::push);
        return array;
    }

    @Override
    public FragmentType<?> type() {
        return FragmentType.LIST;
    }
}