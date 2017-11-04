package algorithm.sorts;

import java.util.Arrays;

/**
 * @author JiaweiMao
 * @version 1.0.0
 * @since 04 Nov 2017, 3:47 PM
 */
public class Sorting
{
    public static <T extends Comparable<T>> T[] insertSort(T[] unsorted)
    {
        int length = unsorted.length;
        for (int j = 1; j < length; j++) {
            T key = unsorted[j];

            int i = j - 1;
            while (i >= 0) {
                if (unsorted[i].compareTo(key) > 0) {
                    unsorted[i + 1] = unsorted[i];
                    i = i - 1;
                } else
                    break;
            }
            unsorted[i + 1] = key;
        }

        return unsorted;
    }


    public static void main(String[] args)
    {
        Double[] array = new Double[]{2.0, 3.0, 1.0, 6.3, 1.2, 4.8};
        Double[] doubles = Sorting.insertSort(array);
        System.out.println(Arrays.toString(doubles));
    }

}
