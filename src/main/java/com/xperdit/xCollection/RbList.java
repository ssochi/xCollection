package com.xperdit.xCollection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Copyright reserved by Beijing Xperdit Technology Co., Ltd. 9/29 0029.
 */
public class RbList<V> implements List<V> {

    private boolean init = false;
    private Node<V> root = null;
    private  static boolean RED = true;
    private  static boolean BLACK = false;

    @Override
    public int size() {
        if (root ==null)
            return 0;
        else {
            return root.n_left+ root.n_right+1;
        }
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<V> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(V v) {
        add(size(),v);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends V> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public V get(int index) {
        return getNode(index).val;
    }

    private Node<V> getNode(int index) {
        Node<V> tmp = root;
        int pre = 0;
        while (tmp!=null){
            int cur = pre+tmp.n_left;
            if (index>cur){
                pre = pre+tmp.n_left+1;
                tmp = tmp.right;
            }else if(index<cur){
                tmp = tmp.left;
            }else {
                return tmp;
            }
        }
        throw new RuntimeException();

    }

    @Override
    public V set(int index, V element) {
        getNode(index).val = element;
        return element;
    }

    @Override
    public void add(int index, V element) {
        if (index>size())
            throw new IndexOutOfBoundsException();
        if(!init&&index==0){
            root = new Node<V>(element);
            root.color = false;
            init = true;
        }else {
            counterpoiseList(insert(index,element));
        }
    }

    private void counterpoiseList(Node<V> x) {
        x.color = RED;
        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                Node<V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        leftReverse(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rightReverse(parentOf(parentOf(x)));
                }
            } else {
                Node<V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rightReverse(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    leftReverse(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }

    private void rightReverse(Node<V> p) {
        if (p != null) {
            Node<V> l = p.left;

            int r1 = p.n_right;
            int r2 = l.n_right;

            p.left = l.right;
            if (l.right != null) l.right.parent = p;
            l.parent = p.parent;
            if (p.parent == null)
                root = l;
            else if (p.parent.right == p)
                p.parent.right = l;
            else p.parent.left = l;
            l.right = p;
            p.parent = l;

            l.n_right = r1+r2+1;
            p.n_left = r2;
        }
    }
    private void leftReverse(Node<V> p) {
        if (p != null) {
            Node<V> r = p.right;

            int l1 = p.n_left;
            int l2 = r.n_left;

            p.right = r.left;
            if (r.left != null)
                r.left.parent = p;
            r.parent = p.parent;
            if (p.parent == null)
                root = r;
            else if (p.parent.left == p)
                p.parent.left = r;
            else
                p.parent.right = r;
            r.left = p;
            p.parent = r;

            r.n_left = l1+l2+1;
            p.n_right = l2;
        }
    }

    private Node<V> insert(int index, V element) {
        Node<V> tmp = root;
        int pre = 0;
        Node<V> flash = new Node<V>(element);
        Node<V> preNode = null;
        Boolean preLeft = false;
        while (tmp!=null){
            int cur = pre+tmp.n_left;
            if (index>cur){
                pre = pre+tmp.n_left+1;
                tmp.n_right++;
                preNode = tmp;
                preLeft = false;
                tmp = tmp.right;
            }else if(index<=cur){
                tmp.n_left++;
                preNode = tmp;
                preLeft = true;
                tmp = tmp.left;
            }
        }
        union(flash,preNode,preLeft);
        return flash;
    }

    private void union(Node<V> flash, Node<V> tmp,boolean preLeft) {
        if (preLeft){
            flash.parent = tmp;
            tmp.left = flash;
        }else{
            flash.parent = tmp;
            tmp.right = flash;
        }
    }

    @Override
    public V remove(int index) {
        Node<V> tar = getNode(index);
        return removeNode(tar);
    }
    private   boolean colorOf(Node<V> p) {
        return (p != null && p.color);
    }

    private  Node<V> parentOf(Node<V> p) {
        return (p == null ? null: p.parent);
    }
    private  Node<V> leftOf(Node<V> p) {
        return (p == null) ? null: p.left;
    }

    private Node<V> rightOf(Node<V> p) {
        return (p == null) ? null: p.right;
    }
    private V removeNode(Node<V> tar) {
        if (tar.left!=null&&tar.right!=null){
            Node<V> sur = successor(tar);
            tar.val = sur.val;
            tar = sur;
        }
        Node<V> replacement = (tar.left!=null?tar.left:tar.right);
        if (replacement!=null){

            replacement.parent = tar.parent;
            if (tar.parent ==null)
                root = replacement;
            else if(tar == tar.parent.left){
                tar.parent.left = replacement;
            }else{
                tar.parent.right = replacement;
            }

            rebalance(replacement);

            tar.left=tar.right=tar.parent =null;
            if (!tar.color){
                fixAfterDeletion(replacement);
            }

        }else if(tar.parent == null){
            root = null;
            init = false;
        }else{
            if (!tar.color){
                fixAfterDeletion(tar);
            }
            if (tar.parent != null){

                rebalance(tar);

                if (tar == tar.parent.left)
                    tar.parent.left = null;
                else if (tar == tar.parent.right)
                    tar.parent.right = null;
                tar.parent = null;
            }
        }



        return null;
    }

    private void rebalance(Node<V> replacement) {
        Node<V> fa = replacement.parent;
        Node<V> ch = replacement;
        while (fa != null){
            if (ch == fa.left) fa.n_left--;
            else fa.n_right--;
            ch = fa;
            fa = ch.parent;
        }
    }

    private void fixAfterDeletion(Node<V> x) {
        while (x != root && colorOf(x) == BLACK) {
            if (x == leftOf(parentOf(x))) {
                Node<V> sib = rightOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    leftReverse(parentOf(x));
                    sib = rightOf(parentOf(x));
                }
                if (colorOf(leftOf(sib))  == BLACK &&
                        colorOf(rightOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(rightOf(sib)) == BLACK) {
                        setColor(leftOf(sib), BLACK);
                        setColor(sib, RED);
                        rightReverse(sib);
                        sib = rightOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(rightOf(sib), BLACK);
                    leftReverse(parentOf(x));
                    x = root;
                }
            } else { // 跟前四种情况对称
                Node<V> sib = leftOf(parentOf(x));
                if (colorOf(sib) == RED) {
                    setColor(sib, BLACK);
                    setColor(parentOf(x), RED);
                    rightReverse(parentOf(x));
                    sib = leftOf(parentOf(x));
                }
                if (colorOf(rightOf(sib)) == BLACK &&
                        colorOf(leftOf(sib)) == BLACK) {
                    setColor(sib, RED);
                    x = parentOf(x);
                } else {
                    if (colorOf(leftOf(sib)) == BLACK) {
                        setColor(rightOf(sib), BLACK);
                        setColor(sib, RED);
                        leftReverse(sib);
                        sib = leftOf(parentOf(x));
                    }
                    setColor(sib, colorOf(parentOf(x)));
                    setColor(parentOf(x), BLACK);
                    setColor(leftOf(sib), BLACK);
                    rightReverse(parentOf(x));
                    x = root;
                }
            }
        }
        setColor(x, BLACK);
    }

    private  void setColor(Node<V> p, boolean c) {
        if (p != null)
            p.color = c;
    }

    private Node<V> successor(Node<V> tar) {
        Node<V> res ;
        if (tar == null) return null;

        if(tar.right != null){
            res = tar.right;
            while (res.left != null) res = res.left;
        }else {
            Node<V> ch = tar;
            res = ch.parent;
            while (res!=null&&ch == res.right){
                ch = res;
                res = res.parent;
            }
        }
        return res;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<V> listIterator() {
        return null;
    }

    @Override
    public ListIterator<V> listIterator(int index) {
        return null;
    }

    @Override
    public List<V> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public String toString() {
        int len = size();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i=0;i<size();i++){
            sb.append(get(i)).append(",");
        }
        return sb.substring(0,sb.length()-1)+"]";
    }
}
class Node<V>{
    int n_left;
    int n_right;
    boolean color = true;
    Node<V> left;
    Node<V> right;
    Node<V> parent;
    V val;
    Node(V val){
        this.val = val;
    }
}




