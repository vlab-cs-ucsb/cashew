#!/bin/bash

passwordsfile=rockyou1k16.txt
expname=rockyou1k16
seriesname=obscure


function jpf() {
	java -Xmx2g -jar ${HOME}/phab/jpf-core/build/RunJPF.jar $@
}

expdir=exps.${expname}

mkdir -p ${expdir}


# Run everything

for flavor in nocache trivialcaching cashew
do
	echo 'Flushing database'
	redis-cli flushall
	echo ''

	for pw in $(cat ${passwordsfile})
	do
		echo "Running ${flavor}.${pw}"
		jpf ${seriesname}.${flavor}.jpf +target.args=${pw} > ${expdir}/${flavor}.${pw}.log
	done

	echo 'Saving database for future reference'
	redis-cli save
	cp /var/lib/redis/dump.rdb redis_after.${expname}.${flavor}.rdb

	echo ''
	echo ''
done


# Now postprocess the results

echo ''
echo '~~~~~~~~ Postprocessing log files ... ~~~~~~~~'
echo ''

for flavor in nocache
do
	sat_time=$(grep ABCService::timeConsumption ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t/1000.0}')
	echo $flavor sat_time $sat_time
	count_time=$(grep ABCCountService::timeConsumption ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t/1000.0}')
	echo $flavor count_time $count_time
	satpluscount_time=$(grep "\(ABCService\|ABCCountService\)::timeConsumption" ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t/1000.0}')
	echo $flavor satpluscount_time $satpluscount_time
	satpluscountplusnorm_time=$(grep "::timeConsumption" ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t/1000.0}')
	echo $flavor satpluscountplusnorm_time $satpluscountplusnorm_time
	echo
done

for flavor in trivialcaching cashew
do
	sat_time=$(grep ABCService::timeConsumption ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t/1000.0}')
	echo $flavor sat_time $sat_time
	count_time=$(grep ABCCountService::timeConsumption ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t/1000.0}')
	echo $flavor count_time $count_time
	satpluscount_time=$(grep "\(ABCService\|ABCCountService\)::timeConsumption" ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t/1000.0}')
	echo $flavor satpluscount_time $satpluscount_time
	satpluscountplusnorm_time=$(grep "::timeConsumption" ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t/1000.0}')
	echo $flavor satpluscountplusnorm_time $satpluscountplusnorm_time
	echo
	sat_hits=$(grep ABCService::cacheHits ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t}')
	echo $flavor sat_hits $sat_hits
	sat_misses=$(grep ABCService::cacheMisses ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t}')
	echo $flavor sat_misses $sat_misses
	sat_hitmissratio=$(python -c "print(float($sat_hits)/$sat_misses)")
	echo $flavor sat_hitmissratio $sat_hitmissratio
	sat_hitpercentage=$(python -c "print(float($sat_hits)/($sat_hits + $sat_misses))")
	echo $flavor sat_hitpercentage $sat_hitpercentage
	echo
	count_hits=$(grep ABCCountService::cacheBoundedHits ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t}')
	echo $flavor count_hits $count_hits
	count_misses=$(grep ABCCountService::cacheMisses ${expdir}/${flavor}.*.log | awk '{t=t+$3}END{print t}')
	echo $flavor count_misses $count_misses
	count_hitmissratio=$(python -c "print(float($count_hits)/$count_misses)")
	echo $flavor count_hitmissratio $count_hitmissratio
	count_hitpercentage=$(python -c "print(float($count_hits)/($count_hits + $count_misses))")
	echo $flavor count_hitpercentage $count_hitpercentage
	echo
done

