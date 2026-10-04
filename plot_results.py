import csv
from pathlib import Path
import matplotlib.pyplot as plt

rows=list(csv.DictReader(open("results/results.csv")))
for workload in ["W1","W2","W3","W4"]:
    variants=["head","middle"] if workload=="W3" else ["-"]
    fig,axes=plt.subplots(len(variants),2,figsize=(12,4*len(variants)),squeeze=False)
    for r,variant in enumerate(variants):
        selected=[x for x in rows if x["workload"]==workload and x["variant"]==variant]
        names=sorted(set(x["structure"] for x in selected))
        for name in names:
            data=sorted([x for x in selected if x["structure"]==name],key=lambda x:int(x["n"]))
            n=[int(x["n"]) for x in data]
            axes[r,0].plot(n,[float(x["time_ms"]) for x in data],"o-",label=name)
            for metric,style in [("steps","-"),("moves","--"),("comparisons",":")]:
                axes[r,1].plot(n,[int(x[metric]) for x in data],marker="o",linestyle=style,label=name+" "+metric)
        for c in range(2):
            ax=axes[r,c];ax.set_xscale("log");ax.set_xlabel("n (elements)");ax.grid(True,alpha=.25);ax.legend(fontsize=8)
            ax.set_title(workload+(" / "+variant if variant!="-" else ""))
        axes[r,0].set_ylabel("Median time (ms)")
        axes[r,1].set_ylabel("Operations (count)");axes[r,1].set_yscale("symlog",linthresh=1);axes[r,1].set_ylim(bottom=0)
    fig.tight_layout();Path("results/plots").mkdir(parents=True,exist_ok=True)
    fig.savefig("results/plots/"+workload+".png",dpi=160);plt.close(fig)
