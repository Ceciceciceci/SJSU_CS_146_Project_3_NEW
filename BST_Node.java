/**
 * Created by Lambda on 10/3/2015.
 */
public class BST_Node<T> {
  private T           _value  = null;
  private int         _freq   = -1;
  private int         _height = -1;
  private BST_Node<T> _left   = null;
  private BST_Node<T> _right  = null;

  /**
   * Create BST_Node object with default values
   *
   * @param input_value the recording data
   */
  public BST_Node(T input_value){
    this._value  = input_value;
    this._left   = null;
    this._right  = null;
    this._height = 1;
    this._freq   = 1;
  } // END CONSTRUCTOR

  /**
   * Append a new node/sub-tree to the right pointer
   *
   * @param input_node a new node or a sub-tree
   */
  public void append_right(BST_Node<T> input_node){
    this._right = input_node;
  } // END append_right METHOD

  /**
   * Append a new node/sub-tree to the left pointer
   *
   * @param input_node a new node or a sub-tree
   */
  public void append_left(BST_Node<T> input_node){
    this._left = input_node;
  } // END append_left METHOD

  /**
   * Manipulate the storing data
   *
   * @param new_value the new data that needed to be stored in the node
   */
  public void change_value(T new_value){
    this._value = new_value;
  } // END change_value METHOD

  /**
   * Determine if the current node is a leaf node
   *
   * @return a boolean value that indicates leaf node or not
   *         true  : not a leaf node (has at least one child node)
   *         false : a leaf node
   */
  public boolean has_child(){
    return this._left != null || this._right != null;
  } // END has_child METHOD

  /**
   * Retrieve the left child node/sub-tree
   *
   * @return a reference to the node
   */
  public BST_Node<T> get_left(){
    return this._left;
  } // END get_left METHOD

  /**
   * Retrieve the right child node/sub-tree
   *
   * @return a reference to the node
   */
  public BST_Node<T> get_right(){
    return this._right;
  } // END get_right METHOD

  /**
   * Retrieve the storing value in the BST node
   *
   * @return the storing value
   */
  public T get_value(){
    return this._value;
  } // END get_value METHOD

  /**
   * Retrieving the frequency counter from the node
   *
   * @return the frequency counter
   */
  public int get_freq(){
    return this._freq;
  }

  /**
   * Retrieving the height of the current node
   *
   * @return the height of the current node
   */
  public int get_height(){
    return this._height;
  }

  /**
   * Assign a height to the current node
   *
   * @param input_height represents the height of the node
   */
  public void set_height(int input_height){
    this._height = input_height;
  } // END set_height METHOD

  /**
   * Incrementing the frequency counter in the node
   */
  public void inc_freq(){
    this._freq++;
  }

  public int get_height_difference(){
    int left_height = (this._left == null)?
        0: this._left._height;
    int right_height = (this._right == null)?
        0: this._right._height;
    return left_height - right_height;
  }
} // END BST_Node CLASS
