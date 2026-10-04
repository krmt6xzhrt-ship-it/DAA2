import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Random;

public class StructuresTest {
    private void checkList(IntList list){
        assertEquals(0,list.size());assertFalse(list.contains(1));
        assertThrows(IndexOutOfBoundsException.class,()->list.get(0));
        assertThrows(IndexOutOfBoundsException.class,()->list.remove(0));
        assertThrows(IndexOutOfBoundsException.class,()->list.add(-1,3));
        assertThrows(IndexOutOfBoundsException.class,()->list.add(1,3));
        list.add(7);assertEquals(7,list.get(0));assertEquals(7,list.remove(0));
        list.add(2);list.add(0,2);list.add(2,9);
        assertEquals(2,list.remove(0));assertEquals(9,list.remove(1));assertEquals(2,list.remove(0));
        ArrayList<Integer> expected=new ArrayList<>();Random r=new Random(42);
        for(int k=0;k<3000;k++){
            int op=r.nextInt(5),x=r.nextInt(30);
            if(op==0){list.add(x);expected.add(x);}
            else if(op==1){int i=r.nextInt(expected.size()+1);list.add(i,x);expected.add(i,x);}
            else if(op==2&&!expected.isEmpty()){int i=r.nextInt(expected.size());assertEquals(expected.remove(i).intValue(),list.remove(i));}
            else if(op==3&&!expected.isEmpty()){int i=r.nextInt(expected.size());assertEquals(expected.get(i).intValue(),list.get(i));}
            else assertEquals(expected.contains(x),list.contains(x));
            assertEquals(expected.size(),list.size());
            for(int i=0;i<expected.size();i++)assertEquals(expected.get(i).intValue(),list.get(i));
        }
        assertThrows(IndexOutOfBoundsException.class,()->list.get(-1));
        assertThrows(IndexOutOfBoundsException.class,()->list.get(list.size()));
        assertThrows(IndexOutOfBoundsException.class,()->list.remove(list.size()));
        assertThrows(IndexOutOfBoundsException.class,()->list.add(list.size()+1,3));
    }
    @Test void array(){checkList(new DynamicArray());}
    @Test void list(){checkList(new MyLinkedList());}
    @Test void heap(){
        MinHeap heap=new MinHeap();PriorityQueue<Integer> expected=new PriorityQueue<>();Random r=new Random(42);
        assertThrows(IllegalStateException.class,heap::peekMin);assertThrows(IllegalStateException.class,heap::extractMin);
        heap.insert(5);assertEquals(5,heap.peekMin());assertEquals(5,heap.extractMin());assertTrue(heap.isValid());
        for(int k=0;k<3000;k++){
            if(expected.isEmpty()||r.nextBoolean()){int x=r.nextInt(100)-50;heap.insert(x);expected.add(x);}
            else assertEquals(expected.remove().intValue(),heap.extractMin());
            assertTrue(heap.isValid());assertEquals(expected.size(),heap.size());
            if(!expected.isEmpty())assertEquals(expected.peek().intValue(),heap.peekMin());
        }
        int prev=Integer.MIN_VALUE;
        while(!expected.isEmpty()){int x=heap.extractMin();assertEquals(expected.remove().intValue(),x);assertTrue(x>=prev);prev=x;assertTrue(heap.isValid());}
        heap.insert(Integer.MAX_VALUE);heap.insert(Integer.MIN_VALUE);heap.insert(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE,heap.extractMin());assertEquals(Integer.MIN_VALUE,heap.extractMin());assertEquals(Integer.MAX_VALUE,heap.extractMin());
    }
    @Test void counters(){
        DynamicArray a=new DynamicArray();MyLinkedList l=new MyLinkedList();
        for(int i=0;i<3;i++){a.add(i);l.add(i);}
        a.metrics().reset();l.metrics().reset();a.get(2);l.get(2);
        assertEquals(1,a.metrics().steps);assertEquals(2,l.metrics().steps);
        a.metrics().reset();l.metrics().reset();a.add(0,9);l.add(0,9);
        assertEquals(3,a.metrics().moves);assertEquals(2,l.metrics().moves);
        a.metrics().reset();l.metrics().reset();assertFalse(a.contains(-1));assertFalse(l.contains(-1));
        assertEquals(4,a.metrics().comparisons);assertEquals(4,l.metrics().comparisons);
    }
}
