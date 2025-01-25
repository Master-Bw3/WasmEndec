package maple.trickster_endec;


import maple.trickster_endec.endecs.EndecTomfoolery;
import maple.trickster_endec.fragment.*;

public class TricksterEndec {
    public static void main(String[] args) {
        var part = Fragment.fromBase64("Y2qtebdTtKQoMzm7uCS1yCo1rySzpDK+pLIglQEAc/+CKhwAAAA=");

        System.out.println(part.toBase64());
    }




}