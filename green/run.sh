#!/bin/bash

PHAB_HOME=${HOME}/phab
GREEN=${PHAB_HOME}/green
SYMBC=${PHAB_HOME}/jpf-symbc
CORE=${PHAB_HOME}/jpf-core
INPUTDIR=${GREEN}/kaluza/uniqdata

function runkal() {
	java -Djava.library.path=/usr/local/lib \
		-cp ${GREEN}/green.jar:${GREEN}/lib/antlr-4.5.3-complete.jar:${GREEN}/lib/jedis-2.0.0.jar:${GREEN}/bin:${SYMBC}/build/main:${CORE}/build/main \
		edu.ucsb.cs.vlab.RunGreenKaluza $@
}


#
# Flush the cache DB. We only need to do this once because:
#
#   - First we run nocache mode, which does not use the cache at all.
#
#   - The we run cashew mode, starting with an empty cache.
#
#   - Finally, we run all the other cashew-except-xxxx modes.
#     For these modes we are not measuring execution time, but merely the
#     effect on orbit size statistics of turning off each layer. Thus, we
#     leave the cache enabled and we don't flush it. This is much faster
#     than starting from scratch, and does not interfere with the
#     computation of the orbit sizes.
#

echo 'Flushing cache DB'
redis-cli flushall
echo ''


# Run everything!

for conf in \
	kaluza.nocache.conf \
	kaluza.cashew.conf \
	kaluza.cashew-except-order.conf \
	kaluza.cashew-except-reduce.conf \
	kaluza.cashew-except-remove.conf \
	kaluza.cashew-except-renameAlph.conf \
	kaluza.cashew-except-renameVar.conf
do
	expsdir=exps.${conf}.output
	mkdir -p ${expsdir}
	echo "~~~~ Running and saving to ${expsdir}"
	for constraint in $(ls ${INPUTDIR} | grep 'smt2$')
	do
		echo Solving ${constraint} using ${conf}
		runkal ${INPUTDIR}/${constraint} ${conf} > ${expsdir}/${constraint}
	done
	echo ''
done
echo ''


# Compute the total solving time for nocache and cashew modes.

for conf in \
	kaluza.nocache.conf \
	kaluza.cashew.conf
do
	expsdir=exps.${conf}.output
	echo "~~~~ Solving time statistics for ${expsdir}:"
	grep totalSolvingTime ${expsdir}/*.smt2 | awk '{print$3}' | st --transpose
	echo ''
done
echo ''


# Compute the orbits for each configuration.

for expsdir in $(ls | grep 'exps.*.output$')
do
	echo "~~~~ Orbit size statistics for ${expsdir} (big constraints only)"
	cat ${expsdir}/big*.smt2 | grep Canonicalized | sed 's/Canonicalized: //' | sort | uniq -c | sort -nr -k1 | tee ${expsdir}.big.orbits | awk '{print$1}' | st --transpose
	echo "~~~~ Orbit size statistics for ${expsdir} (small constraints only)"
	cat ${expsdir}/small*.smt2 | grep Canonicalized | sed 's/Canonicalized: //' | sort | uniq -c | sort -nr -k1 | tee ${expsdir}.small.orbits | awk '{print$1}' | st --transpose
	echo "~~~~ Orbit size statistics for ${expsdir} (all constraints)"
	cat ${expsdir}/*.smt2 | grep Canonicalized | sed 's/Canonicalized: //' | sort | uniq -c | sort -nr -k1 | tee ${expsdir}.all.orbits | awk '{print$1}' | st --transpose
	echo 'See *.orbits files for further details'
	echo ''
done

