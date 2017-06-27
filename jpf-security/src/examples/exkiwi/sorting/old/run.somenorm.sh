#!/bin/bash

arraysize=7

function jpf() {
	java -Xmx2g -jar ${HOME}/pldi/jpf-core/build/RunJPF.jar $@
}

for method in Binary Merge Insertion Selection Bubble Quick Heap
do
	for bound in 8 16 32
	do
		echo ""
		echo "Running method=${method} arraysize=${arraysize} bound=${bound}"
		echo ""
		jpf sorting.greenabconly.nostore.jpf +model_count.string_length_bound=${bound} +target.args=${arraysize},${method} > \
			curr/sorting.greenabconly.${method}.${arraysize}.${bound}.nostore.log
	done
done


#for method in Binary Merge Insertion Selection Bubble Quick Heap
#do
#	echo ""
#	echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~ FLUSHING DATABASE ~~~~~~~~~~~~~~~~~~~~~~~~~~"
#	echo ""
#	redis-cli flushall
#	for bound in 8 12 16 20 24 28 32 # 4 8 12 16 20 24
#	do
#		echo ""
#		echo "Running method=${method} arraysize=${arraysize} bound=${bound}"
#		echo ""
#		jpf sorting.somenorm.jpf +model_count.string_length_bound=${bound} +target.args=${arraysize},${method} > \
#			curr/sorting.somenorm.${method}.${arraysize}.${bound}.store.log
#	done
#done


