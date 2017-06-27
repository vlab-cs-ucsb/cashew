#!/bin/bash

arraysize=6

function jpf() {
	java -Xmx2g -jar ${HOME}/pldi/jpf-core/build/RunJPF.jar $@
}

for method in Quick Heap Merge Selection Insertion Bubble Binary
do
	for bound in 10 14 18 22 26 28 30 32
	do
		echo ""
		echo "Running method=${method} arraysize=${arraysize} bound=${bound}"
		echo ""
		jpf sorting.greenabconly.nostore.jpf +model_count.string_length_bound=${bound} +target.args=${arraysize},${method} > \
			curr.fatimavsgreenabconly/sorting.${method}.${arraysize}.${bound}.greenabconly.nostore.log
	done
done




