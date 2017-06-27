rm abc_results_crime
touch abc_results_crime

for max_int in 3 7 15 31 63 127 255
do

  for pass_length in {1..10}
  do

    for run in {1..1}
    do
        echo "$max_int $pass_length $run"

        ( /usr/bin/time -f "TIME:%e" jpf src/examples/fse/CrimeNoStringLatteABC.jpf +symbolic.max_int=$max_int +model_count.mode=abc.linear_integer_arithmetic +target.args=$pass_length | tail -n 2 | grep "FSE RESULT" ) >> abc_results_crime 2>&1

	tail abc_results_crime -n 1

    done
  done
done


grep 'FSE\|TIME' abc_results_crime > abc_results_crime_clean
cat abc_results_crime_clean

