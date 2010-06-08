package br.unicarioca.redesepistemicas.bo;

import java.util.Random;

import javax.swing.DefaultListModel;

import br.unicarioca.redesepistemicas.modelo.AgenteEpistemico;

public class QuickSort {
    public static final Random RND = new Random();
 
    private static  void swap(DefaultListModel array, int i, int j) {
        Object tmp = array.get(i);
        array.set(i, array.get(j));
        array.set(j, tmp);
    }
 
    private static int partition(DefaultListModel array, int begin, int end) {
        int index = begin + RND.nextInt(end - begin + 1);
        AgenteEpistemico pivot = (AgenteEpistemico) array.get(index);
        swap(array, index, end);
        for (int i = index = begin; i < end; ++i) {
            if (((Double)((AgenteEpistemico)array.get(i)).getPesoReputacao()).compareTo(pivot.getPesoReputacao()) >= 0) {
                swap(array, index++, i);
            }
        }
        swap(array, index, end);
        return (index);
    }
 
    private static void qsort(DefaultListModel array, int begin, int end) {
        if (end > begin) {
            int index = partition(array, begin, end);
            qsort(array, begin, index - 1);
            qsort(array, index + 1, end);
        }
    }
 
    public static void sort(DefaultListModel array) {
        qsort(array, 0, array.getSize() - 1);
    }   
}
