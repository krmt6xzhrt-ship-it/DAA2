public class MyLinkedList implements IntList {
    private static class Node {
        int value;Node next;
        Node(int x){value=x;}
    }
    private Node head,tail;
    private int size;
    private final Metrics count=new Metrics();
    public int size(){return size;}
    public Metrics metrics(){return count;}
    private void check(int i){if(i<0||i>=size)throw new IndexOutOfBoundsException();}
    private Node node(int index){
        Node current=head;
        for(int i=0;i<index;i++){current=current.next;count.steps++;}
        return current;
    }
    public void add(int x){
        Node n=new Node(x);
        if(size==0){head=n;count.moves++;}else{tail.next=n;count.moves++;}
        tail=n;count.moves++;size++;
    }
    public void add(int index,int x){
        if(index<0||index>size)throw new IndexOutOfBoundsException();
        if(index==size){add(x);return;}
        Node n=new Node(x);
        if(index==0){n.next=head;head=n;count.moves+=2;}
        else{Node prev=node(index-1);n.next=prev.next;prev.next=n;count.moves+=2;}
        size++;
    }
    public int get(int index){check(index);return node(index).value;}
    public int remove(int index){
        check(index);Node removed;
        if(index==0){removed=head;head=head.next;count.moves++;}
        else{Node prev=node(index-1);removed=prev.next;prev.next=removed.next;count.moves++;if(removed==tail){tail=prev;count.moves++;}}
        size--;
        if(size==0){tail=null;count.moves++;}
        return removed.value;
    }
    public boolean contains(int x){
        Node current=head;
        while(current!=null){
            count.comparisons++;
            if(current.value==x)return true;
            current=current.next;count.steps++;
        }
        return false;
    }
}
