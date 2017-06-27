#!/bin/bash

bound=10

function jpf() {
	java -Xmx2g -jar ${HOME}/pldi/jpf-core/build/RunJPF.jar $@
}


for arraysize in 8 #2 3 4 5 6 7 8 9 10 11 12 
do
	for method in Merge Selection Insertion Binary Bubble # Quick Heap Merge Selection Insertion Bubble Binary
	do
		echo ""
		echo "Running method=${method} arraysize=${arraysize} bound=${bound}"
		echo ""
		jpf sorting.somenorm.nostore.jpf +model_count.string_length_bound=${bound} +target.args=${arraysize},${method} > \
			curr/sorting.somenorm.${method}.${arraysize}.${bound}.nostore.log
	done
done


