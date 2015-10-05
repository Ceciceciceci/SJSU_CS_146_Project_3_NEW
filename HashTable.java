//import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
/**
 * @author ichiknees
 */
/**
 *
 * Stub code for an implementation of a DataCounter that uses a hash table as
 * its backing key structure. We decided to make this hashtable take in generic 
 * types as well. Below each method will be explained.
 */

public class HashTable<E extends Comparable<E>> implements DataCounter<E> {
  /**
   *
   * @author ichiknees
   * LinkedNode inner class to help store items in list to iterate over
   *
   */
  private class LinkedNode{
    private E                       value;
    private int                     frequency;
    private HashTable<E>.LinkedNode next_node;
    //not sure if this should be of linkedNode but it is getting the next value

    public LinkedNode(E key) {
      this.next_node = null;
      this.value     = key;
      this.frequency = 1;
    }

    /**
     * Copy constructor
     *
     * @param copyee
     */
    public LinkedNode(LinkedNode copyee){
      this.next_node = null;
      this.value     = copyee.value;
      this.frequency = copyee.frequency;
    }

    public LinkedNode(){
      this.next_node = null;
      this.value     = null;
      this.frequency = 0;
    }

    public void increment_frequency(){
      this.frequency++;
    }

    public E get_value() {
      return this.value;
    }

    public int get_frequency() {
      return this.frequency;
    }

    public void append_next(LinkedNode node){
      this.next_node = node;
    }

    public LinkedNode get_next_node() {
      return next_node;
    }
  }

  //final private int DEFAULT_SIZE_OF_TABLE  = 104729;
  final private int DEFAULT_SIZE_OF_TABLE  = 7;
  private int          _table_size         = 0;
  private LinkedNode[] _table              = null;
  private ArrayList<E> _cheat_sheet        = null;
  private int          _num_slots_occupied = 0;

  /**
   * Constructor for the table.
   * Should create a hashtable object
   */
  public HashTable(){
    this._table_size = DEFAULT_SIZE_OF_TABLE;
    this._table = new HashTable.LinkedNode[this._table_size]; //new hash table obj
    this._cheat_sheet = new ArrayList<>();
  }

  /**
   * Generic item e turns into a number generated from
   * a string of text.
   * @param e generic
   * @return hash  code
   *
   * REFERENCE : http://naml.us/blog/tag/thomas-wang
   *             http://burtleburtle.net/bob/
   */
  private int _get_hashkey(E e, int curr_table_size){
    String temp = e.toString();
    long address = 0;
    int str_index = 0;

    while(str_index < temp.length()){
      address += temp.charAt(str_index);
      str_index++;
    } // END WHILE LOOP

    // BITS MANIPULATION
    address = (~address) + (address << 21);
    address = address ^ (address >>> 24);
    address = (address + (address << 3)) + (address << 8); // address * 265
    address = address ^ (address >>> 14);
    address = (address + (address << 2)) + (address << 4); // address * 21
    address = address ^ (address >>> 28);
    address = address + (address << 31);

    return Math.abs((int)(address % curr_table_size));
  }

  /**
   * Search hash table based on the input value.
   *
   * @param input_value searching target
   * @return null if the node does not exist. Return a LinkedNode if found
   */
  public LinkedNode search(E input_value){
    // GET HASH KEY
    int hash_index = _get_hashkey(input_value, this._table_size);
    LinkedNode ptr = _table[hash_index];

    while(ptr != null){
      if(ptr.get_value().compareTo(input_value) == 0){
        return ptr;
      }
      ptr = ptr.get_next_node();
    } // END WHILE LOOP
    return null;  // NOT FOUND
  }

  /**
   * Gets the size of the hash table.
   * @return the number of elements in hash table
   */
  public int getSize() {
    return this._table_size;
  }

  /**
   * Get an array of all of the data counts in the DataCounter structure. The
   * array should contain exactly one DataCount instance for each unique
   * element inserted into the structure. The elements do not need to be in
   * any particular order.
   *
   * @return an array of the data counts.
   */
  public DataCount<E>[] getCounts() {
    @SuppressWarnings("unchecked")
    DataCount<E>[] counts = new DataCount[this._cheat_sheet.size()];

    if(this._table != null && this._cheat_sheet != null){
      Iterator<E> itr = _cheat_sheet.iterator();
      int idx = 0;
      while(itr.hasNext()){
        // GRAB THE HASH NODE
        LinkedNode temp = search(itr.next());
        if(temp == null){
          // ERROR
          System.out.println("[Error] Check cheat_sheet and table");
          break;
        }
        counts[idx] = new DataCount<E>(temp.get_value(), temp.get_frequency());
        idx++;
      } // END WHILE LOOP
      if(idx != (_cheat_sheet.size())){
        System.out.println("[Error] Check _cheat_sheet size");
        return null;
      }
      return counts;
    }

    return null;
  }

  /**
   * incCount method is actually a "insert" function for hash table class.
   * It would increment the frequency field if the node that holding the same
   * value already exists. Otherwise, it would create a new DataCount object
   * based on the input value and insert the new node into hash table.
   *
   * @param keydata recording value
   */
  public void incCount(E keydata) {
    // GET HASH KEY
    int hash_idx = _get_hashkey(keydata, this._table_size);

    // CHECK IF THE DATA ENTRY ALREADY EXISTS
    LinkedNode search_result = search(keydata);
    if(search_result != null){
      search_result.increment_frequency();
    } // END IF STATEMENT
    else{
      // DATA ENTRY DOES NOT EXIST
      LinkedNode temp = new LinkedNode(keydata);
      this._cheat_sheet.add(keydata);
      if(this._table[hash_idx] == null){
        // SLOT IS FREE
        this._num_slots_occupied++;
      }
      temp.append_next(
          (this._table[hash_idx] == null) ? null :
              this._table[hash_idx]
      );
      this._table[hash_idx] = temp;
      // CHECK TABLE SIZE TO SEE IF WE NEED TO DOUBLE THE SIZE OF HASH TABLE
      if((this._table_size / 4) * 3 < this._num_slots_occupied){
        // IF THE NUMBER OF AVAILABLE SLOTS IS LESS THAN 1/4 TABLE SIZE
        // WE WILL EXECUTE THE DOUBLE HASH TABLE PROCESS
        this._doubleTheTableSize();
      }
    } // END ELSE STATEMENT
    /////////////////// TEST ////////////////////////////////////////////
    //System.out.println("*-*-*-*-*-*-*-*-*-*-*");
    //System.out.println("INPUT = " + keydata.toString());
    //for(int i=0; i<this._table_size; i++){
    //  if(this._table[i] == null){
    //    continue;
    //  }
    //  System.out.print("[" + (i+1) + "]");
    //  LinkedNode ptr = this._table[i];
    //  while(ptr != null){
    //    System.out.print(
    //        "[" + ptr.get_value() + "] - " + ptr.get_frequency() + " ===> "
    //    );
    //    ptr = ptr.get_next_node();
    //  }
    //  System.out.println();
    //}
    //System.out.println("\n*********************");
    //try{
    //  System.in.read();
    //}
    //catch (Exception e){
//
    //}
    /////////////////////////////////////////////////////////////////////
  } // END incCount METHOD

  /**
   * This method will double the size of current hash table. Also, it would
   * migrate all the data entries from old table into the new bigger table
   */
  private void _doubleTheTableSize(){
    int new_slot_counter = 0;
    int new_table_size = this._table_size * 2;
    HashTable.LinkedNode new_table[] = new HashTable.LinkedNode[new_table_size];

    // COPY ALL NODES
    Iterator<E> itr = this._cheat_sheet.iterator();
    while(itr.hasNext()){
      LinkedNode ptr = search(itr.next());
      int hash_idx = this._get_hashkey(ptr.get_value(), new_table_size);
      if(new_table[hash_idx] == null){
        // THE CURRENT SLOT IN NEW HASH TABLE IS AVAILABLE
        new_table[hash_idx] = new LinkedNode(ptr);
        new_slot_counter++;
      } // END IF STATEMENT
      else{
        // THE CURRENT SLOT HAD BEEN OCCUPIED
        LinkedNode temp = new_table[hash_idx];
        new_table[hash_idx] = new LinkedNode(ptr);
        new_table[hash_idx].append_next(temp);
      } // END ELSE STATEMENT
    } // END WHILE LOOP
    // DONE COPYING ALL NODES INTO NEW HASH TABLE WITH DOUBLE THE ORIGINAL SIZE

    ///////////////////////// TEST ////////////////////////////////////////////
    //System.out.println("\n [ Table INFO ] ");
    //System.out.println("[TABLE SIZE] - " + this._table_size);
    //System.out.println("[NEW   SIZE] - " + new_table_size);
    //System.out.println("[ENTRY SIZE] - " + this._cheat_sheet.size());
    ///////////////////////////////////////////////////////////////////////////

    // ASSIGNING NEW TABLE TO THE HashTable._table POINTER
    this._table_size = new_table_size;
    this._table = new_table;
    this._num_slots_occupied = new_slot_counter;
  }

}

