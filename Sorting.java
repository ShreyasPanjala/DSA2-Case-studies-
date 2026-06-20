import java.util.*;

public class Sorting {
    static Random rand = new Random(1);

    static void mergeSort(int[] a, int l, int r) {
        if (l >= r) return;

        int m = (l + r) / 2;

        mergeSort(a, l, m);
        mergeSort(a, m + 1, r);

        merge(a, l, m, r);
    }

    static void merge(int[] a, int l, int m, int r) {
        int[] temp = new int[r - l + 1];

        int i = l, j = m + 1, k = 0;

        while (i <= m && j <= r)
            temp[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];

        while (i <= m)
            temp[k++] = a[i++];

        while (j <= r)
            temp[k++] = a[j++];

        for (i = 0; i < temp.length; i++)
            a[l + i] = temp[i];
    }

    static void quickSort(int[] a, int low, int high, String pivotType) {
        if (low < high) {
            int pi = partition(a, low, high, pivotType);

            quickSort(a, low, pi - 1, pivotType);
            quickSort(a, pi + 1, high, pivotType);
        }
    }

    static int partition(int[] a, int low, int high, String type) {
        int p = low;

        if (type.equals("middle"))
            p = (low + high) / 2;
        else if (type.equals("random"))
            p = low + rand.nextInt(high - low + 1);

        swap(a, p, high);

        int pivot = a[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (a[j] <= pivot)
                swap(a, ++i, j);
        }

        swap(a, i + 1, high);

        return i + 1;
    }

    static void heapSort(int[] a) {
        int n = a.length;

        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(a, n, i);

        for (int i = n - 1; i > 0; i--) {
            swap(a, 0, i);
            heapify(a, i, 0);
        }
    }

    static void heapify(int[] a, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && a[left] > a[largest])
            largest = left;

        if (right < n && a[right] > a[largest])
            largest = right;

        if (largest != i) {
            swap(a, i, largest);
            heapify(a, n, largest);
        }
    }

    static void countingSort(int[] a) {
        int max = Arrays.stream(a).max().getAsInt();

        int[] count = new int[max + 1];

        for (int x : a)
            count[x]++;

        int index = 0;

        for (int i = 0; i <= max; i++)
            while (count[i]-- > 0)
                a[index++] = i;
    }

    static void radixSort(int[] a) {
        int max = Arrays.stream(a).max().getAsInt();

        for (int exp = 1; max / exp > 0; exp *= 10)
            radixCount(a, exp);
    }

    static void radixCount(int[] a, int exp) {
        int n = a.length;

        int[] output = new int[n];
        int[] count = new int[10];

        for (int x : a)
            count[(x / exp) % 10]++;

        for (int i = 1; i < 10; i++)
            count[i] += count[i - 1];

        for (int i = n - 1; i >= 0; i--) {
            int digit = (a[i] / exp) % 10;
            output[--count[digit]] = a[i];
        }

        for (int i = 0; i < n; i++)
            a[i] = output[i];
    }

    static void benchmark(String name, int[] data) {
        int[] a = data.clone();

        long start = System.nanoTime();

        if (name.equals("Merge Sort"))
            mergeSort(a, 0, a.length - 1);
        else if (name.equals("Quick First"))
            quickSort(a, 0, a.length - 1, "first");
        else if (name.equals("Quick Middle"))
            quickSort(a, 0, a.length - 1, "middle");
        else if (name.equals("Quick Random"))
            quickSort(a, 0, a.length - 1, "random");
        else if (name.equals("Heap Sort"))
            heapSort(a);
        else if (name.equals("Counting Sort"))
            countingSort(a);
        else if (name.equals("Radix Sort"))
            radixSort(a);

        long end = System.nanoTime();

        System.out.println(name + " Time: " + (end - start) + " ns");
    }

    static int[] createDataset(int n) {
        Random r = new Random(10);

        int[] data = new int[n];

        for (int i = 0; i < n; i++)
            data[i] = r.nextInt(10000);

        return data;
    }

    static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args) {
        int[] dataset = createDataset(5000);

        benchmark("Merge Sort", dataset);
        benchmark("Quick First", dataset);
        benchmark("Quick Middle", dataset);
        benchmark("Quick Random", dataset);
        benchmark("Heap Sort", dataset);
        benchmark("Counting Sort", dataset);
        benchmark("Radix Sort", dataset);

        System.out.println("Program Executed Successfully");
    }
}