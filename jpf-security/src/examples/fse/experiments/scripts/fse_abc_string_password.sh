
for pass_length in {1..20}
  do

  for run in {1..3}
  do
    echo "$pass_length $run"

    /usr/bin/time -f "TIME:%e" jpf src/examples/fse/PasswordCheckWithString.jpf +target.args=$pass_length,l +model_count.string_length_bound=$pass_length | grep "asdfasdf"

  done
done

