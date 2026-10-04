public class DynamicArray implements IntList {
    private int[] data=new int[4];
    private int size;
    private final Metrics count=new Metrics();
    public int size(){return size;}
    public Metrics metrics(){return count;}
    private void check(int i){if(i<0||i>=size)throw new IndexOutOfBoundsException();}
    private void grow(){
        if(size<data.length)return;
        int[] next=new int[data.length*2];
        for(int i=0;i<size;i++){next[i]=data[i];count.steps++;count.moves++;}
        data=next;
    }
    public void add(int x){grow();data[size++]=x;}
    public void add(int index,int x){
        if(index<0||index>size)throw new IndexOutOfBoundsException();
        grow();
        for(int i=size;i>index;i--){data[i]=data[i-1];count.steps++;count.moves++;}
        data[index]=x;size++;
    }
    public int get(int index){check(index);count.steps++;return data[index];}
    public int remove(int index){
        check(index);int value=data[index];count.steps++;
        for(int i=index;i<size-1;i++){data[i]=data[i+1];count.steps++;count.moves++;}
        size--;return value;
    }
    public boolean contains(int x){
        for(int i=0;i<size;i++){count.steps++;count.comparisons++;if(data[i]==x)return true;}
        return false;
    }
}
