#!/bin/bash

arraysize=$1

for method in Quick Heap Merge Selection Insertion Bubble Binary
do
	for bound in 8 10 12 14 16 18 20 22 24 26 28 30 32
	do
		n=$(grep elapsed curr.fatimavsgreenabconly/sorting.${method}.${arraysize}.${bound}.greenabconly.nostore.log || echo "elapsed time: NA")
		f=$(grep elapsed curr.fatimavsgreenabconly/sorting.${method}.${arraysize}.${bound}.fatima.log || echo "elapsed time: NA")
		echo "$method arraysize: $arraysize bound: $bound nostore: $n fatima: $f"
	done
done


