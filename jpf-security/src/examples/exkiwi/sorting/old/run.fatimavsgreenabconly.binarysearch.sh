#!/bin/bash

arraysize=11

function jpf() {
	java -Xmx2g -jar ${HOME}/pldi/jpf-core/build/RunJPF.jar $@
}


for method in Binary
do
	for bound in 8 10 12 14 16 18 20 22 24 26 30 32
	do
		echo ""
		echo "Running method=${method} arraysize=${arraysize} bound=${bound}"
		echo ""
		jpf sorting.greenabconly.nostore.jpf +model_count.string_length_bound=${bound} +target.args=${arraysize},${method} > \
			curr.fatimavsgreenabconly.binarysearch/sorting.${method}.${arraysize}.${bound}.greenabconly.nostore.log
	done
done


for method in Binary
do
	echo ""
	echo "~~~~~~~~~~~~~~~~~~~~~~~~~~~~ FLUSHING DATABASE ~~~~~~~~~~~~~~~~~~~~~~~~~~"
	echo ""
	redis-cli flushall
	for bound in 8 10 12 14 16 18 20 22 24 26 28 30 32 
	do
		echo ""
		echo "Running method=${method} arraysize=${arraysize} bound=${bound}"
		echo ""
		jpf sorting.fatima.jpf +model_count.string_length_bound=${bound} +target.args=${arraysize},${method} > \
			curr.fatimavsgreenabconly.binarysearch/sorting.${method}.${arraysize}.${bound}.fatima.log
	done
done


