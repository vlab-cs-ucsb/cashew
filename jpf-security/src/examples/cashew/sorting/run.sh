#!/bin/bash

function jpf() {
	java -Xmx2g -jar ${HOME}/phab/jpf-core/build/RunJPF.jar $@
}

arraysize=7

expname=sorting
expdir=exps.${expname}
mkdir -p ${expdir}

for flavor in plaingreen
do
	for method in Quick Heap Merge Selection Insertion Bubble
	do
		echo Flushing database...
		redis-cli flushall
		echo ''
		for bound in 16 20 24 28 32 36 40 44 48 52 56 60 64
		do
			echo "Running flavor=${flavor} method=${method} arraysize=${arraysize} bound=${bound}"
			jpf sorting.${flavor}.jpf \
				+model_count.string_length_bound=${bound} \
				+target.args=${arraysize},${method} \
				> ${expdir}/${flavor}.${method}.${arraysize}.${bound}.log
			grep elapsed ${expdir}/${flavor}.${method}.${arraysize}.${bound}.log
			echo ''
		done
		echo Saving database for future reference...
		redis-cli save
		cp /var/lib/redis/dump.rdb redis_after.${expname}.${flavor}.${method}.${arraysize}.rdb
		echo ''
	done
	echo ''
done

echo ''

for flavor in cashew
do
	for method in Quick Heap Merge Selection Insertion Bubble
	do
		echo Flushing database...
		redis-cli flushall
		echo ''
		for bound in 16 20 24 28 32 36 40 44 48 52 56 60 64
		do
			echo "Running flavor=${flavor} method=${method} arraysize=${arraysize} bound=${bound}"
			jpf sorting.${flavor}.jpf \
				+model_count.string_length_bound=${bound} \
				+target.args=${arraysize},${method} \
				> ${expdir}/${flavor}.${method}.${arraysize}.${bound}.log
			grep elapsed ${expdir}/${flavor}.${method}.${arraysize}.${bound}.log
			echo ''
		done
		echo Saving database for future reference...
		redis-cli save
		cp /var/lib/redis/dump.rdb redis_after.${expname}.${flavor}.${method}.${arraysize}.rdb
		echo ''
	done
	echo ''
done

echo ''
echo ''

echo 'Recap of all execution times:'

grep elapsed ${expdir}/*.log

echo ''

