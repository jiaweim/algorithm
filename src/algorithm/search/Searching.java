package algorithm.search;

/**
 * @author JiaweiMao
 * @version 1.0.0
 * @since 04 Nov 2017, 7:27 PM
 */
public class Searching
{

    public static int linearSearch(Object[] array, Object toSearch)
    {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(toSearch))
                return i;
        }
        return -1;
    }

}
