rm abc_results_password_secure
touch abc_results_password_secure

for max_int in 3 7 15 31 63 127 255
do

  for pass_length in {1..20}
  do

    for run in {1..1}
    do
        echo "$max_int $pass_length $run"

        ( /usr/bin/time -f "TIME:%e" jpf src/examples/fse/PasswordCheckNoStringLatte.jpf +target.args=$pass_length +symbolic.max_int=$max_int | tail -n 2 | grep "FSE RESULT" ) >> abc_results_password_secure 2>&1

	tail abc_results_password_secure -n 2

    done
  done
done


grep 'FSE\|TIME' abc_results_password_secure > abc_results_password_secure_clean
cat abc_results_password_secure_clean

