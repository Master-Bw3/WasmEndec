package maple.trickster_endec.fragment;

import io.wispforest.endec.Endec;
import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.core.JSMap;

import java.util.Map;

public record MapFragment(Map<Fragment, Fragment> map) implements Fragment {
    public static final StructEndec<MapFragment> ENDEC = StructEndecBuilder.of(
            Endec.map(Fragment.ENDEC, Fragment.ENDEC).fieldOf("entries", MapFragment::map),
            MapFragment::new
    );

    @JSExport
    @JSProperty
    public JSArray<JSArray<Fragment>> getEntries() {
        var array = new JSArray<JSArray<Fragment>>();
        for (var entry : map.entrySet()) {
            var pair = new JSArray<Fragment>();
            pair.push(entry.getKey());
            pair.push(entry.getValue());
            array.push(pair);
        }
        return array;
    }

    @Override
    public FragmentType<?> type() {
        return FragmentType.MAP;
    }

}
