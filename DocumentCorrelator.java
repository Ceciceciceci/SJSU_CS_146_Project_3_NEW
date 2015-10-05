import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Lambda on 10/4/2015.
 */
public class DocumentCorrelator {
  protected class Tuple{
    private int _freq_1 = 0;
    private int _freq_2 = 0;

    public Tuple(int num1, int num2){
      this._freq_1 = num1;
      this._freq_2 = num2;
    } // END CONSTRUCTOR

    public int get_diff(){
      return _freq_1 - _freq_2;
    } // END get_diff METHOD
  }

  final String OPTIONS_ALG[] = {"-b", "-a", "-h"};

  private DataCount<String> [] _base_doc = null;
  private DataCount<String> [] _comp_doc = null;

  public DocumentCorrelator(String [] args){
    this._parse_cmd_args(args);
  } // END CONSTRUCTOR

  private DataCount<String> [] _parse_file(String file_name, int option){
    DataCount<String>[] counts = null;
    try {
      FileWordReader reader = new FileWordReader(file_name);
      String word = reader.nextWord();
      switch (option) {
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
          return null;
      } // END SWITCH STATEMENT
    }
    catch (IOException e) {
      System.err.println("Error processing " + file_name + e);
      System.exit(1);
    }
    return counts;
  } // END _parse_file METHOD

  private void _parse_cmd_args(String [] args){
    int option = -1;

    if(args.length != 3){
      System.out.println("[INSUFFICIENT NUMBER OF COMMAND LINE ARGUMENT");
      System.out.println("Usage: [ -b | -a | -h ] <filename1> <filename2>");
      _usage();
    } // END IF STATEMENT

    for(int idx=0; idx<OPTIONS_ALG.length; idx++){
      if(OPTIONS_ALG[idx].compareTo(args[0]) == 0){
        option = idx;
        break;
      } // END IF STATEMENT
    } // END FOR LOOP

    this._base_doc = this._parse_file(args[1], option);
    this._comp_doc = this._parse_file(args[2], option);

    // SORT ARRAY BY ALPHABETICAL ORDER
    this._insertion_sort(this._base_doc, 0, this._base_doc.length, 0);
    this._insertion_sort(this._comp_doc, 0, this._comp_doc.length, 0);

    // MAKING THE MATRIX
    ArrayList<Tuple> matrix = _make_matrix();

    double similarity_num = this._get_euclidean_distance(matrix);

    this._insertion_sort(this._base_doc, 0, this._base_doc.length, 1);
    this._insertion_sort(this._comp_doc, 0, this._comp_doc.length, 1);
    this.print_output(similarity_num);
  }

  private void print_output(double similarity_num){
    System.out.println("*****************************");
    System.out.println("* DOCUMENT CORRELATION INTO *");
    System.out.println("*****************************");
    System.out.println("[TOP 10 WORDS]");
    System.out.println("---------------------------------");
    System.out.println("|     DOC A     |     DOC B     |");
    System.out.println("---------------------------------");
    System.out.println("| WORD  | FREQ  | WORD  | FREQ  |");
    System.out.println("---------------------------------");
    for(int i=0; i<10; i++){
      System.out.printf(
          "|%7s|%7d|%7s|%7d|\n",
          this._base_doc[i].data, this._base_doc[i].count,
          this._comp_doc[i].data, this._comp_doc[i].count);
    } // END FOR LOOP
    System.out.println("---------------------------------");
    similarity_num /= 100;
    System.out.printf("Similarity : %7.2f", similarity_num);
    System.out.println(" %");
  }

  private ArrayList<Tuple> _make_matrix(){
    int base_idx = 0;
    int comp_idx = 0;

    ArrayList<Tuple> matrix = new ArrayList<>();
    while(base_idx < this._base_doc.length &&
        comp_idx < this._comp_doc.length){
      int cmp_num = this._base_doc[base_idx].data.compareTo(
          this._comp_doc[comp_idx].data
      );
      if(cmp_num == 0){
        // FOUND MATCH
        matrix.add(new Tuple(
            this._base_doc[base_idx].count, this._comp_doc[comp_idx].count
        ));
        base_idx++;
        comp_idx++;
      } // END IF STATEMENT

      if(cmp_num < 0){
        matrix.add(new Tuple(this._base_doc[base_idx].count, 0));
        base_idx++;
      } // END IF STATEMENT

      if(cmp_num > 0){
        matrix.add(new Tuple(0, this._comp_doc[comp_idx].count));
        comp_idx++;
      } // END IF STATEMENT
    } // END WHILE LOOP

    if(base_idx < this._base_doc.length){
      for(int idx = base_idx; idx<this._base_doc.length; idx++){
        matrix.add(new Tuple(this._base_doc[idx].count, 0));
      } // END FOR LOOP
    } // END IF STATEMENT
    if(comp_idx < this._comp_doc.length){
      for(int idx = comp_idx; idx<this._comp_doc.length; idx++){
        matrix.add(new Tuple(0, this._comp_doc[idx].count));
      } // END FOR LOOP
    } // END IF STATEMENT

    return matrix;
  }

 private int _compare(DataCount<String> obj1, DataCount<String> obj2, int opt){
   if(opt == 0) {
     return (obj1.data.compareTo(obj2.data) < 0) ? 1 : -1;
   }
   else{
     if(obj1.count > obj2.count){
       // CURRENT COUNT IS GREATER THAN COMPARING COUNT
       return 1;
     }
     else if(obj1.count < obj2.count){
       // CURRENT COUNT IS LESS THAN COMPARING COUNT
       return -1;
     }
     else{
       return (obj1.data.compareTo(obj2.data) <0) ? 1 : -1;
     }
   }
 }  // END _compare METHOD

  // INSERTION SORT OK
  private void _insertion_sort(
      DataCount<String> [] arr, int start, int end, int opt){
    for (int itr = start + 1; itr < end; itr++){
      DataCount<String> temp = arr[itr];
      int idx = itr - 1;
      while(idx >= 0 && _compare(temp, arr[idx], opt) >= 0){
        arr[idx + 1] = arr[idx];
        idx--;
      } // END WHILE LOOP
      arr[idx + 1] = temp;
    } // END FOR LOOP
  }

  private double _get_euclidean_distance(ArrayList<Tuple> matrix){
    double sum = 0.0;
    for(Tuple item : matrix){
      sum += Math.pow(item.get_diff(), 2.0);
    } // END FOR LOOP
    return Math.sqrt(sum);
  } // END _get_euclidean_distance METHOD

  //if the user types the wrong syntax, this is the err message produced
  private static void _usage() {
    System.err.println("Usage: java Correlator [ -b | -a | -h ] <filename1> <filename2>");
    System.err.println("-b Use an Unbalanced BST in the backend ");
    System.err.println("-a Use an AVL Tree in the backend");
    System.err.println("-h Use a Hashtable in the backend");
    System.exit(1);
  }

  public static void main(String[] args) {
    DocumentCorrelator dc = new DocumentCorrelator(args);
  }
}
