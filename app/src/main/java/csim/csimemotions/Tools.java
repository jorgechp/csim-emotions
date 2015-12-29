package csim.csimemotions;

/**
 * Created by jorge on 24/12/15.
 */
public class Tools {

    /**
     * http://stackoverflow.com/questions/1128723/how-can-i-test-if-an-array-contains-a-certain-value
     *
     * @param array
     * @param v
     * @param <T>
     * @return
     */
    public static <T> boolean contains2(final T[] array, final T v) {
        if (v == null) {
            for (final T e : array)
                if (e == null)
                    return true;
        } else {
            for (final T e : array)
                if (e == v || v.equals(e))
                    return true;
        }

        return false;
    }
}
