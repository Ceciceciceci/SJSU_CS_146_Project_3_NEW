/**
 * Created by Lambda on 10/3/2015.
 */
public class AVL<T extends Comparable<T>> extends BST<T> {
  /**
   * Create an empty AVL tree object
   */
  public AVL(){
    this._root      = null;
    this._tree_size = 0;
  } // END CONSTRUCTOR

  private BST_Node<T> _right_rotate(BST_Node<T> curr_root){
    BST_Node<T> left_node                = curr_root.get_left();
    BST_Node<T> left_node_right_sub_tree = left_node.get_right();

    // ROTATION
    left_node.append_right(curr_root);
    curr_root.append_left(left_node_right_sub_tree);

    // UPDATING THE HEIGHT OF TREE NODES
    curr_root.set_height(this._get_max_height(curr_root) + 1);
    left_node.set_height(this._get_max_height(left_node) + 1);

    return left_node;
  } // END _right_rotate PRIVATE METHOD

  private BST_Node<T> _left_rotate(BST_Node<T> curr_root){
    BST_Node<T> right_node               = curr_root.get_right();
    BST_Node<T> right_node_left_sub_tree = right_node.get_left();

    // ROTATION
    right_node.append_left(curr_root);
    curr_root.append_right(right_node_left_sub_tree);

    // UPDATING THE HEIGHT OF TREE NODES
    curr_root.set_height(this._get_max_height(curr_root) + 1);
    right_node.set_height(this._get_max_height(right_node) + 1);

    return right_node;
  } // END _left_rotate PRIVATE METHOD

  private BST_Node<T> _balance_tree(BST_Node<T> curr_root, T input){
    // FIRST UPDATING THE HEIGHT OF THE CURRENT NODE
    curr_root.set_height(this._get_max_height(curr_root) + 1);

    int height_diff = curr_root.get_height_difference();

    // DETEREMINE IF ROTATION(S) IS/ARE NEEDED
    if(height_diff > 1){
      // LEFT LEFT
      if(input.compareTo(curr_root.get_left().get_value()) < 0){
        //System.out.println("Left-Left case : " + curr_root.get_value());
        // INPUT VALUE IS LESS THAN THE VALUE STORES IN LEFT CHILD
        return this._right_rotate(curr_root);
      }
      else{ // LEFT RIGHT : INPUT VALUE > LEFT CHILD VALUE
        //System.out.println("Left-Right case : " + curr_root.get_value());
        curr_root.append_left(this._left_rotate(curr_root.get_left()));
        return this._right_rotate(curr_root);
      }
    }
    if(height_diff < -1){
      // RIGHT RIGHT
      if(input.compareTo(curr_root.get_right().get_value()) > 0){
        //System.out.println("Right-Right case : " + curr_root.get_value());
        return this._left_rotate(curr_root);
      }
      else{ // RIGHT LEFT
        //System.out.println("Right-Left case : " + curr_root.get_value());
        curr_root.append_right(this._right_rotate(curr_root.get_right()));
        return this._left_rotate(curr_root);
      }
    }
    return curr_root;
  } // END _balance_tree PRIVATE METHOD

  @Override
  protected BST_Node<T> _insert(BST_Node<T> curr_root, T input){
    // VALIDATE THE ROOT
    if(curr_root == null){
      // IF TREE IS AN EMPTY TREE, CREATE THE NEW NODE AND UPDATE THE TREE SIZE
      curr_root = new BST_Node<>(input);
      this._tree_size++;
    }
    else{
      // IF TREE IS NOT EMPTY
      // FIRST, GET THE COMPARING RESULT
      int comparing_result = input.compareTo(curr_root.get_value());
      if(comparing_result == 0){
        // THE INPUT VALUE IS AS THE SAME AS THE VALUE THAT CURRENT NODE IS
        // HOLDING, THEN INCREMENT THE COUNTER INSTEAD OF CREATING A NEW NODE
        curr_root.inc_freq();
      } // END IF STATMENT
      else if(comparing_result < 0){
        // IF THE INPUT VALUE IS LESS THAN THE CURRENT VALUE, GOES TO LEFT
        curr_root.append_left(_insert(curr_root.get_left(), input));
      } // END ELSE-IF STATEMENT
      else{
        // IF THE INPUT VALUE IS GREATER THAN THE CURRENT VALUE, GOES TO RIGHT
        curr_root.append_right(_insert(curr_root.get_right(), input));
      } // END ELSE STATEMENT

      // UPDATING THE HEIGHT OF THE NODE
      curr_root = _balance_tree(curr_root, input);
    } // END OUTER ELSE STATEMENT
    return curr_root;
  } // END _insert PROTECTED METHOD

} // END AVL CLASS
