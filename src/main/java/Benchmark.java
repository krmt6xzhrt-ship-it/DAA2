import java.util.Random;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

public class Benchmark {
    private static volatile long sink;
    private static long[] run(String workload,String variant,boolean linked,int[] values,int[] indexes,int[] queries){
        IntList list=linked?new MyLinkedList():new DynamicArray();
        MinHeap heap=new MinHeap();
        if(!workload.equals("W4"))for(int x:values)list.add(x);
        Metrics m=workload.equals("W4")?heap.metrics():list.metrics();m.reset();
        long sum=0,start=System.nanoTime();
        switch(workload){
            case "W1":for(int i:indexes)sum+=list.get(i);break;
            case "W2":for(int x:queries)if(list.contains(x))sum++;break;
            case "W3":
                int pos=variant.equals("head")?0:values.length/2;
                for(int i=0;i<1000;i++)list.add(pos,i);
                for(int i=0;i<1000;i++)sum+=list.remove(pos);
                break;
            case "W4":
                for(int x:values)heap.insert(x);
                int previous=Integer.MIN_VALUE;
                for(int i=0;i<values.length;i++){
                    int x=heap.extractMin();
                    if(x<previous)throw new IllegalStateException("Unsorted heap output");
                    previous=x;sum+=x;
                }
                break;
        }
        long elapsed=System.nanoTime()-start;sink=sum;
        return new long[]{elapsed,m.steps,m.moves,m.comparisons};
    }
    private static void measure(PrintWriter out,String w,String v,boolean linked,int[] data,int[] indexes,int[] queries){
        for(int i=0;i<2;i++)run(w,v,linked,data,indexes,queries);
        long[][] runs=new long[5][];
        for(int i=0;i<5;i++)runs[i]=run(w,v,linked,data,indexes,queries);
        for(int i=1;i<5;i++){
            long[] x=runs[i];int j=i-1;
            while(j>=0&&runs[j][0]>x[0]){runs[j+1]=runs[j];j--;}
            runs[j+1]=x;
        }
        long[] r=runs[2];
        String name=w.equals("W4")?"MinHeap":linked?"MyLinkedList":"DynamicArray";
        out.printf(java.util.Locale.US,"%s,%s,%s,%d,%.6f,%d,%d,%d%n",w,v,name,data.length,r[0]/1000000.0,r[1],r[2],r[3]);out.flush();
        System.out.println(w+" "+v+" "+name+" n="+data.length);
    }
    public static void main(String[] args)throws Exception{
        Files.createDirectories(Path.of("results"));
        try(PrintWriter out=new PrintWriter("results/results.csv")){
            out.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");
            for(int n:new int[]{100,1000,10000,100000}){
                Random random=new Random(42);int[] data=new int[n],indexes=new int[10000],queries=new int[1000];
                for(int i=0;i<n;i++)data[i]=random.nextInt(1000000);
                for(int i=0;i<indexes.length;i++)indexes[i]=random.nextInt(n);
                for(int i=0;i<queries.length;i++)queries[i]=i%2==0?data[random.nextInt(n)]:-1-random.nextInt(1000000);
                for(boolean linked:new boolean[]{false,true}){
                    measure(out,"W1","-",linked,data,indexes,queries);
                    measure(out,"W2","-",linked,data,indexes,queries);
                    measure(out,"W3","head",linked,data,indexes,queries);
                    measure(out,"W3","middle",linked,data,indexes,queries);
                }
                measure(out,"W4","-",false,data,indexes,queries);
            }
        }
    }
}
