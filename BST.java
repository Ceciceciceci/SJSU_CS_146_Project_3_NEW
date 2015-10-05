/**
 * Created by Lambda on 10/3/2015.
 */
public class BST<E extends Comparable<? super E>> implements DataCounter<E> {
  protected BST_Node<E> _root      = null;
  protected int         _tree_size = -1;

  /**
   * Creating an empty tree object
   */
  public BST(){
    _root      = null;
    _tree_size = 0;
  } // END CONSTRUCTOR

  /**
   * Insert a new value into the current tree; Only validate the input value,
   * the actual inserting process will be done by the private insert method
   *
   * @param input_value incoming value to be inserted
   */
  public void insert(E input_value){
    // VALIDATE THE INPUT VALUE
    if(input_value == null){
      return;
    } // END IF STATEMENT

    this._root = _insert(this._root, input_value);
  } // END insert METHOD

  /**
   * Search API allows user to search a node in tree based on a value
   *
   * @param search_item the value that would be used on searching
   *
   * @return a reference to the node if found; else, return a null pointer
   */
  public BST_Node<E> search(E search_item){
    return this._search(this._root, search_item);
  } // END search METHOD

  /**
   * Search the current sub-tree with a searching value provided by user.
   *
   * @param curr_root indicates the current sub-tree
   * @param search_item indicates the searching value
   *
   * @return a null pointer if not found; else, return a reference to the
   *         matching node
   */
  protected BST_Node<E> _search(BST_Node<E> curr_root, E search_item){
    if(curr_root == null){
      // NO MATCH
      return null;
    } // END IF STATEMENT
    else{
      // GET THE COMPARING RESULT
      int comparing_result = search_item.compareTo(curr_root.get_value());
      if(comparing_result == 0){
        // FOUND MATCHED ITEM
        return curr_root;
      } // END INNER IF STATEMENT
      else if(comparing_result < 0){
        // THE SEARCHING VALUE IS LESS THAN THE CURRENT VALUE; GOES TO LEFT
        return _search(curr_root.get_left(), search_item);
      } // END ELSE-IF STATEMENT
      else{
        // THE SEARCHING VALUE IS GRETER THAN THE CURRENT VALUE; GOES TO RIGHT
        return _search(curr_root.get_right(), search_item);
      } // END ELSE STATEMENT
    } // END ELSE STATEMENT
  } // END _search PROTECTED METHOD

  /**
   * Insert a new value into the current tree; Increment the frequency counter
   * if the node already exists.
   *
   * @param curr_root indicates the current root of the sub-tree
   * @param input_value the value that will be inserted into tree
   *
   * @return a reference to the current node
   */
  protected BST_Node<E> _insert(BST_Node<E> curr_root, E input_value){
    // VALIDATE THE ROOT
    if(curr_root == null){
      // IF TREE IS AN EMPTY TREE, CREATE THE NEW NODE AND UPDATE THE TREE SIZE
      curr_root = new BST_Node<>(input_value);
      this._tree_size++;
    }
    else{
      // IF TREE IS NOT EMPTY
      // FIRST, GET THE COMPARING RESULT
      int comparing_result = input_value.compareTo(curr_root.get_value());
      if(comparing_result == 0){
        // THE INPUT VALUE IS AS THE SAME AS THE VALUE THAT CURRENT NODE IS
        // HOLDING, THEN INCREMENT THE COUNTER INSTEAD OF CREATING A NEW NODE
        curr_root.inc_freq();
      } // END IF STATMENT
      else if(comparing_result < 0){
        // IF THE INPUT VALUE IS LESS THAN THE CURRENT VALUE, GOES TO LEFT
        curr_root.append_left(_insert(curr_root.get_left(), input_value));
      } // END ELSE-IF STATEMENT
      else{
        // IF THE INPUT VALUE IS GREATER THAN THE CURRENT VALUE, GOES TO RIGHT
        curr_root.append_right(_insert(curr_root.get_right(), input_value));
      } // END ELSE STATEMENT

      // UPDATING THE HEIGHT OF THE NODE
      curr_root.set_height(_get_max_height(curr_root) + 1);
    } // END OUTER ELSE STATEMENT
    return curr_root;
  } // END _insert PROTECTED METHOD

  /**
   * Retrieve the max height of the sub-tree
   *
   * @param curr_root the root of the current sub-tree
   *
   * @return the height
   */
  protected int _get_max_height(BST_Node<E> curr_root){
    if(curr_root == null){
      return 0;
    } // END IF STATEMENT
    else{
      int left_height = (curr_root.get_left() == null)?
          0: curr_root.get_left().get_height();
      int right_height = (curr_root.get_right() == null)?
          0: curr_root.get_right().get_height();
      return Math.max(left_height, right_height);
    } // END ELSE STATEMENT
  } // END _get_max_height PROTECTED METHOD

  ///////////////////////////// FOR TESTING ///////////////////////////////////
  public int get_root_height(){
    return (this._root == null)? 0: this._root.get_height();
  }

  public BST_Node<E> get_root(){
    return this._root;
  }


  /**
   * Insert a new value into the current tree; Only validate the input value,
   * the actual inserting process will be done by the private insert method
   *
   * @param data incoming value to be inserted
   */
  @Override
  public void incCount(E data) {
    // VALIDATE THE INPUT VALUE
    if(data == null){
      return;
    } // END IF STATEMENT

    this._root = _insert(this._root, data);
  }

  /**
   * Retrieving the size of the tree
   *
   * @return the size of the tree
   */
  @Override
  public int getSize() {
    return this._tree_size;
  }

  @Override
  public DataCount<E>[] getCounts() {
    // FIRST VALIDATE THE SIZE OF BST
    if(this._tree_size == 0){
      return null;
    }
    @SuppressWarnings("unchecked")
    DataCount<E>[] counts = new DataCount[this._tree_size];
    if (this._root != null)
      traverse(this._root, counts, 0);
    return counts;
  }

  /**
   * Do an inorder traversal of the tree, filling in an array of DataCount
   * objects with the count of each element. Doing an inorder traversal
   * guarantees that the result will be sorted by element. We fill in some
   * contiguous block of array elements, starting at index, and return the
   * next available index in the array.
   *
   * @param counts The array to populate.
   */
  protected int traverse(BST_Node<E> root, DataCount<E>[] counts, int idx) {
    if(root != null) {
      idx = traverse(root.get_left(), counts, idx);
      counts[idx] = new DataCount<E>(root.get_value(), root.get_freq());
      idx = traverse(root.get_right(), counts, idx + 1);
    }
    return idx;
  }
} // END BST CLASS
