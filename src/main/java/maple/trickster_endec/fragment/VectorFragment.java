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
            EndecTomfoolery.<Double, EndecTomfoolery.Vector>vectorEndec(Endec.DOUBLE, EndecTomfoolery.Vector::new, EndecTomfoolery.Vector::x, EndecTomfoolery.Vector::y, EndecTomfoolery.Vector::z)
                    .fieldOf("vector", VectorFragment::getVector),
            VectorFragment::new
    );
    public static final VectorFragment ZERO = new VectorFragment(new EndecTomfoolery.Vector(0d, 0d, 0d));

    private final EndecTomfoolery.Vector vector;

    public VectorFragment(EndecTomfoolery.Vector vector) throws RuntimeException {
        if (Double.isNaN(vector.x()) || Double.isNaN(vector.y()) || Double.isNaN(vector.z())) {
            throw new RuntimeException();
        }

        this.vector = vector;
    }

    @JSExport
    @JSProperty
    public EndecTomfoolery.Vector getVector() {
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