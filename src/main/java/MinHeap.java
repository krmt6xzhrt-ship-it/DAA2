public class MinHeap {
    private int[] data=new int[4];
    private int size;
    private final Metrics count=new Metrics();
    public Metrics metrics(){return count;}
    public int size(){return size;}
    private int read(int i){count.steps++;return data[i];}
    private boolean less(int a,int b){count.comparisons++;return a<b;}
    private void swap(int i,int j){int a=read(i),b=read(j);data[i]=b;data[j]=a;count.moves+=2;}
    public void insert(int x){
        if(size==data.length){
            int[] next=new int[data.length*2];
            for(int i=0;i<size;i++){next[i]=read(i);count.moves++;}
            data=next;
        }
        int i=size++;data[i]=x;
        while(i>0){int parent=(i-1)/2;if(!less(read(i),read(parent)))break;swap(i,parent);i=parent;}
    }
    public int peekMin(){if(size==0)throw new IllegalStateException();return read(0);}
    public int extractMin(){
        if(size==0)throw new IllegalStateException();
        int value=read(0);size--;
        if(size==0)return value;
        data[0]=read(size);count.moves++;
        int i=0;
        while(2*i+1<size){
            int child=2*i+1;
            if(child+1<size&&less(read(child+1),read(child)))child++;
            if(!less(read(child),read(i)))break;
            swap(i,child);i=child;
        }
        return value;
    }
    public boolean isValid(){
        for(int i=1;i<size;i++)if(data[(i-1)/2]>data[i])return false;
        return true;
    }
}
