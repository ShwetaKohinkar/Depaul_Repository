package hw2;

/**
 * MyLinked class
 */
public class MyLinked {
    static class Node {
        public Node() {
        }

        public double item;
        public Node next;
    }

    int N;
    Node first;

    /**
     * Default constructor - Initializing class member variables
     */
    public MyLinked()
    {
        first = null;
        N = 0;
        assert checkInvariants();
    }

    /**
     * Checking Invariants
     * @return
     */
    private boolean checkInvariants()
    {
        assert ((N != 0) || (first == null));
        Node x = first;
        for (int i = 0; i < N; i++) {
            if (x == null) {
                return false;
            }
            x = x.next;
        }
        assert (x == null);
        return true;
    }

    /**
     * Return null if first node is empty
     *
     * @return first node as null
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Return size of the list
     *
     * @return size of linked list
     */
    public int size() {
        return N;
    }

    /**
     * Adding new node in linked list
     *
     * @param item node to be added
     */
    public void add(double item)
    {
        Node newfirst = new Node();
        newfirst.item = item;
        newfirst.next = first;
        first = newfirst;
        N++;
    }

    /**
     * Delete the kth node of a linked list
     *
     * @param k Position of a node to be deleted
     */
    public void delete(int k)
    {
        if (k < 0 || k > N)
            throw new IllegalArgumentException();
        assert checkInvariants();

        Node curr = first;
        Node prev = null;

        for (int i = 1; i <= N; i++)
        {
            if (i == k)
            {
                if (k == 1)
                {
                    first = first.next;
                    N--;
                }
                else
                {
                    assert (prev != null);
                    prev.next = curr.next;
                    curr = curr.next;
                    N--;
                }
            }
            prev = curr;
            curr = curr == null ? null : curr.next;
        }
    }

    /**
     * reverse the list "in place"... without creating any new nodes
     */
    public void reverse()
    {
        assert checkInvariants();

        Node prev = null;
        Node curr = first;
        Node next = null;

        while (curr != null)
        {
            assert (curr != null);   //Verifying that the current node should not be equal to null
            next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        assert(curr == null);
        first = prev;
        return;
    }


    /**
     * Remove all the elements with item value
     * @param item Node value to be removed from the list
     */
    public void remove(double item)
    {
        assert checkInvariants();

        Node curr = first;
        Node prev = null;

        int cnt = 0;
        while (curr != null)
        {
            cnt++;

            if (first.item == item)
            {

                first = first.next;
                N--;

                if(N == 0)
                {
                    assert (first == null); // head/first node should be null for an empty list
                    return;
                }
            }

            if (prev != null && curr.item == item)
            {
                prev.next = curr.next;
                cnt = 0;
                N--;
            }

            if (cnt != 0)
            {
                prev = curr;
            }
            curr = prev.next;
            assert (N >= 0); // Verifying the size of the list after node/item removal
        }
    }

    /**
     *
     * @param s print a string
     * @param b print the items of linked list
     */
    public void print(String s, MyLinked b)
    {
        System.out.print(s + ": ");
        for (Node x = b.first; x != null; x = x.next)
            System.out.print(x.item + " ");
        System.out.println();
    }
}

































