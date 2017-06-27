#!/bin/bash

arraysize=10

function jpf() {
	java -Xmx2g -jar ${HOME}/pldi/jpf-core/build/RunJPF.jar $@
}

#for method in Quick Heap Merge Selection Insertion Bubble Binary
#do
#	for bound in 8  # 4 8 12 16
#	do
#		echo ""
#		echo "Running method=${method} arraysize=${arraysize} bound=${bound}"
#		echo ""
#		jpf sorting_nostore.jpf +model_count.string_length_bound=${bound} +target.args=${arraysize},${method} > \
#			curr/sorting.${method}.${arraysize}.${bound}.nostore.log
#	done
#done


for method in Quick Heap Merge Selection Insertion Bubble Binary
do
	echo ""
	echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~ FLUSHING DATABASE ~~~~~~~~~~~~~~~~~~~~~~~~~~"
	echo ""
	redis-cli flushall
	for bound in 8 12 16 20  # 4 8 12 16 20 24
	do
		echo ""
		echo "Running method=${method} arraysize=${arraysize} bound=${bound}"
		echo ""
		jpf sorting.jpf +model_count.string_length_bound=${bound} +target.args=${arraysize},${method} > \
			curr/sorting.${method}.${arraysize}.${bound}.store.log
	done
done


