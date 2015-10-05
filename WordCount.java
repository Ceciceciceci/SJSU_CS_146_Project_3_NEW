import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * An executable that counts the words in a files and prints out the counts in
 * descending order. You will need to modify this file.
 */
public class WordCount {
  private DataCount<String> _collection [] = null;
  private int _length                      = 0;

  static public int compare(DataCount<String> obj1, DataCount<String> obj2){
    if(obj1.count > obj2.count){
      // CURRENT COUNT IS GREATER THAN COMPARING COUNT
      return 1;
    }
    else if(obj1.count < obj2.count){
      // CURRENT COUNT IS LESS THAN COMPARING COUNT
      return -1;
    }
    else{
      /**
       * CURRENT OBJECT AND COMPARING OBJECT HAVE THE SAME COUNT
       * THEN, COMPARING BY ALPHABETICAL ORDER
       */
      return (obj1.data.compareTo(obj2.data) <0) ? 1 : -1;
    }
  }

  public void sort(DataCount<String> input_arr[]){
    // FIRST VALIDATE THE INPUT ARRAY
    if(input_arr == null){
      System.out.println("Can not sort an empty array/collection.");
      return;
    }
    this._collection = input_arr;
    this._length = input_arr.length;
    //_quick_sort_2(0, this._length - 1);
    _insertion_sort(0, this._length);
  }
  private void _swap(int left_idx, int right_idx){
    DataCount<String> temp = _collection[right_idx];
    _collection[right_idx] = _collection[left_idx];
    _collection[left_idx] = temp;
  }

  // INSERTION SORT OK
  private void _insertion_sort(int start, int end){
    for (int itr = start + 1; itr < end; itr++){
      DataCount<String> temp = _collection[itr];
      int idx = itr - 1;
      //while (idx >= 0 && temp.compareTo(_collection[idx]) >= 0){
      while(idx >= 0 && compare(temp, _collection[idx]) >= 0){
        _collection[idx + 1] = _collection[idx];
        idx--;
      } // END WHILE LOOP
      _collection[idx + 1] = temp;
    } // END FOR LOOP
  }

  private static void countWords(String file) {
    DataCounter<String> counter = new AVL<String>();

    try {
      FileWordReader reader = new FileWordReader(file);
      String word = reader.nextWord();
      while (word != null) {
        counter.incCount(word);
        word = reader.nextWord();
      }
    } catch (IOException e) {
      System.err.println("Error processing " + file + e);
      System.exit(1);
    }

    DataCount<String>[] counts = counter.getCounts();
    sortByDescendingCount(counts);
    for (DataCount<String> c : counts)
      System.out.println(c.count + " \t" + c.data);
  }

  /**
   * TODO Replace this comment with your own.
   *
   * Sort the count array in descending order of count. If two elements have
   * the same count, they should be in alphabetical order (for Strings, that
   * is. In general, use the compareTo method for the DataCount.data field).
   *
   * This code uses insertion sort. You should modify it to use a different
   * sorting algorithm. NOTE: the current code assumes the array starts in
   * alphabetical order! You'll need to make your code deal with unsorted
   * arrays.
   *
   * The generic parameter syntax here is new, but it just defines E as a
   * generic parameter for this method, and constrains E to be Comparable. You
   * shouldn't have to change it.
   *
   * @param counts array to be sorted.
   */
  private static <E extends Comparable<? super E>> void sortByDescendingCount(
      DataCount<E>[] counts) {
    for (int i = 1; i < counts.length; i++) {
      DataCount<E> x = counts[i];
      int j;
      for (j = i - 1; j >= 0; j--) {
        if (counts[j].count >= x.count) {
          break;
        }
        counts[j + 1] = counts[j];
      }
      counts[j + 1] = x;
    }
  }

  private void _parse_cmd_args(String [] args) {
    ////////////////////////////////// TESTING ////////////////////////////////
    //args = new String[3];
    //args[0] = "-a";
    //args[1] = "-num_unique";
    //args[2] = "hamlet.txt";
    ///////////////////////////////////////////////////////////////////////////

    final String OPTIONS_ALG[] = {"-b", "-a", "-h"};
    final String OPTIONS_OPT[] = {"-frequency", "-num_unique"};

    int option_1 = -1;
    int option_2 = -1;
    String file_name = null;

    if (args.length != 3) {
      // INSUFFICIENT NUMBER OF COMMAND LINE ARGUMENTS
      System.out.println("Invalid number of command line arguments");
      System.out.println("Existing the program");
      return;
    }

    for (int i = 0; i < args.length - 1; i++) {
      for (int j = 0; j < OPTIONS_ALG.length; j++) {
        if (args[i].compareTo(OPTIONS_ALG[j]) == 0) {
          option_1 = j;
        } // END IF STATEMENT
      } // END FOR LOOP
      for (int j = 0; j < OPTIONS_OPT.length; j++) {
        if (args[i].compareTo(OPTIONS_OPT[j]) == 0) {
          option_2 = j;
        } // END IF STATEMENT
      } // END FOR LOOP
    } // END FOR LOOP

    // PREPARING INPUT FILE TO READ <------------------------------- FILE READ
    file_name = args[args.length - 1];     // COPYING THE FILE NAME
    File file = new File(file_name);
    file_name = file.getAbsolutePath();
    DataCount<String>[] counts = null;
    try {
      FileWordReader reader = new FileWordReader(file_name);
      String word = reader.nextWord();
      switch (option_1) {
        case 0:
          BST<String> bst = new BST<>();
          while (word != null) {
            bst.incCount(word);
            word = reader.nextWord();
          }
          counts = bst.getCounts();
          break;
        case 1:
          AVL<String> avl = new AVL();
          while (word != null) {
            avl.incCount(word);
            word = reader.nextWord();
          }
          counts = avl.getCounts();
          break;
        case 2:
          // hash table
          HashTable<String> ht = new HashTable<>();
          while (word != null) {
            ht.incCount(word);
            word = reader.nextWord();
          }
          counts = ht.getCounts();
          break;
        default:
          System.out.println("[ERROR] EXISITNG THE PROGRAM");
          return;
      } // END SWITCH STATEMENT
    }
    catch (IOException e) {
      System.err.println("Error processing " + file_name + e);
      System.exit(1);
    }

    // PRINT OUTPUT
    this._print_output(counts, option_2);
  }

  private void _print_output(DataCount<String>[] counts, int option){
    // SORT THE COLLECTION FIRST
    this.sort(counts);

    if(option == 0){
      // PRINT OUT FREQUENCY PAIRS
      for (DataCount<String> c : counts) {
        System.out.println(c.count + " \t" + c.data);
      } // END FOR LOOP
    } // END IF STATEMENT
    else{
      for (DataCount<String> c : counts) {
        if(c.count == 1) {
          System.out.println(c.count + " \t" + c.data);
        } // END IF STATEMENT
      } // END FOR LOOP
    } // END ELSE STATEMENT
  } // END print_output METHOD

  public static void main(String[] args) {
    WordCount wc = new WordCount();
    wc._parse_cmd_args(args);
  }
}

/**

 private void _quick_sort(int low_idx, int high_idx){
 // DECLARING TWO ITERATION INDEXES FOR LATER USAGE
 int itr_fnt = low_idx;
 int itr_bck = high_idx;

 // CALCULATING THE PIVOT POINT - CHOOSING THE MEDIAN OF THREE
 int pivot = _select_pivot_median_of_three(low_idx, high_idx);

 while(itr_fnt <= itr_bck){
 if((itr_bck - itr_fnt) <= 9){
 // IF THE COLLECTION SIZE IS LESS THAN 10, USE INSERTION SORT
 this._insertion_sort(itr_fnt, itr_bck + 1);
 break;
 } // END IF STATEMENT
 else{
 //while(_collection[itr_fnt].compareTo(_collection[pivot]) > 0){
 while(compare(_collection[itr_fnt], _collection[pivot]) >0){
 itr_fnt++;
 } // END WHILE LOOP
 //while(_collection[itr_bck].compareTo(_collection[pivot]) < 0){
 while(compare(_collection[itr_bck], _collection[pivot]) >0){
 itr_bck--;
 } // END WHILE LOOP
 if(itr_fnt <= itr_bck){
 _swap(itr_fnt, itr_bck);
 itr_fnt++;
 itr_bck--;
 } // END IF STATEMENT

 //try{
 System.out.println("=============================================");
 System.out.println("[low_idx, high_idx] = " + low_idx + ", " + high_idx);
 System.out.println("[itr_fnt, itr_bck]  = " + itr_fnt + ", " + itr_bck);
 System.out.println("itr_bck  : " + _collection[itr_bck].data);
 System.out.println("itr_fnt  : " + _collection[itr_fnt].data);
 System.out.println("low_idx  : " + _collection[low_idx].data);
 System.out.println("high_idx : " + _collection[high_idx].data);
 System.out.println("---------------------------------------------");

 //System.in.read();
 //}
 //catch (IOException e) {
 //  e.printStackTrace();
 //}

 if(low_idx < itr_bck){
 _quick_sort(low_idx, itr_bck);
 }
 if(itr_fnt < high_idx){
 _quick_sort(itr_fnt, high_idx);
 }
 } // END ELSE STATEMENT
 } // END OUTER WHILE LOOP
 } // END _quick_sort PRIVATE METHOD

 private int _select_pivot_median_of_three(int low, int high){
 int median = (low + high)/2;
 //if(_collection[low].compareTo(_collection[median]) > 0){
 if(compare(_collection[low], _collection[median]) > 0){
 // _collection[low] > _collection[median]
 _swap(low, median);
 } // END IF STATEMENT
 //if(_collection[low].compareTo(_collection[high]) > 0){
 if(compare(_collection[low], _collection[high]) > 0){
 // _collection[low] > _collection[high]
 _swap(low, high);
 } // END IF STATEMENT
 //if(_collection[median].compareTo(_collection[high]) > 0){
 if(compare(_collection[median], _collection[high]) > 0){
 // _collection[median] > _collection[high]
 _swap(median, high);
 }
 return median;
 }

 private int _partition(int left_idx, int right_idx, DataCount<String> pivot){
 int left_ptr  = left_idx; // right of first elem
 int right_ptr = right_idx - 1; // left of pivot
 while (true) {
 //find bigger
 while(compare(_collection[++left_ptr], pivot) > 0)
 ;
 //find smaller  |-> while (data[--rightPtr] > pivot)
 while(compare(_collection[--right_ptr], pivot) < 0)
 ;
 if (left_ptr < right_ptr) // if pointers cross, partition done
 _swap(left_ptr, right_ptr);
 else
 break;
 }
 _swap(left_ptr, right_idx - 1); // restore pivot
 return left_ptr; // return pivot location
 }

 public void _quick_sort_2(int left, int right) {
 int size = right - left + 1;
 if (size < 10) // insertion sort if small
 _insertion_sort(left, right);
 else // quicksort if large
 {
 // CALCULATING THE PIVOT POINT - CHOOSING THE MEDIAN OF THREE
 int median = _select_pivot_median_of_three(left, right);
 //long median = medianOf3(left, right);
 int partition = _partition(left, right, _collection[median]);
 _quick_sort_2(left, partition - 1);
 _quick_sort_2(partition + 1, right);
 }
 }
 */
