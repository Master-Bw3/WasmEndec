package maple.trickster_endec.fragment;

import io.wispforest.endec.Endec;
import io.wispforest.endec.StructEndec;
import io.wispforest.endec.impl.StructEndecBuilder;
import maple.trickster_endec.endecs.EndecTomfoolery;
import org.teavm.jso.JSExport;
import org.teavm.jso.JSProperty;

import java.util.Objects;

public class VectorFragment implements Fragment {
    public static final StructEndec<VectorFragment> ENDEC = StructEndecBuilder.of(
            EndecTomfoolery.<Double, EndecTomfoolery.Vector<Double>>vectorEndec(Endec.DOUBLE, EndecTomfoolery.Vector<Double>::new, EndecTomfoolery.Vector<Double>::x, EndecTomfoolery.Vector<Double>::y, EndecTomfoolery.Vector<Double>::z)
                    .fieldOf("vector", VectorFragment::getVector),
            VectorFragment::new
    );
    public static final VectorFragment ZERO = new VectorFragment(new EndecTomfoolery.Vector<Double>(0d, 0d, 0d));

    private final EndecTomfoolery.Vector<Double> vector;

    public VectorFragment(EndecTomfoolery.Vector<Double> vector) throws RuntimeException {
        if (Double.isNaN(vector.x()) || Double.isNaN(vector.y()) || Double.isNaN(vector.z())) {
            throw new RuntimeException();
        }

        this.vector = vector;
    }

    @JSExport
    @JSProperty
    public EndecTomfoolery.Vector<Double> getVector() {
        return vector;
    }

    @Override
    public FragmentType<?> type() {
        return FragmentType.VECTOR;
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof VectorFragment v && v.vector.equals(vector);
    }


    @Override
    public int hashCode() {
        return Objects.hash(vector);
    }

    @Override
    public String toString() {
        return "VectorFragment[" + "vector=" + vector + "]";
    }
}