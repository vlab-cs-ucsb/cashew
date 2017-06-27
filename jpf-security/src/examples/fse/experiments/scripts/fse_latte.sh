
rm latte_results
touch latte_results

for max_int in 3 7 15 31 63 127 255 
do

  for pass_length in {1..20}
  do 

    for run in {1..10}
    do 
	echo "$max_int $pass_length $run"

	( /usr/bin/time -f "TIME:%e" jpf src/examples/fse/PasswordCheckNoStringLatte.jpf +target.args=$pass_length +symbolic.max_int=$max_int +model_count.mode=latte | tail -n 2 | grep "FSE RESULT" ) >> latte_results 2>&1
 
    done
  done
done


grep 'FSE\|TIME' latte_results > latte_results_clean
cat latte_results_clean

