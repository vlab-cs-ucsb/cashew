abc_data = csvread('abc_results_clean');
latte_data = csvread('latte_results_clean');


time_i 		= 1;
alpha_i 	= 2;
passlen_i 	= 3;
prob_i 		= 4;
entropy_i 	= 5; 

alpha_size = 32;

abc_time_alpha_size = abc_data(abc_data(:,alpha_i) == alpha_size,time_i);
abc_time_alpha_size_average = mean(reshape(abc_time_alpha_size, 10, length(abc_time_alpha_size)/10));

latte_time_alpha_size = latte_data(latte_data(:,alpha_i) == alpha_size,time_i);
latte_time_alpha_size_average = mean(reshape(latte_time_alpha_size, 10, length(latte_time_alpha_size)/10));


pw_lengths = 1:length(abc_time_alpha_size_average);

plot(pw_lengths, latte_time_alpha_size_average, 'b', pw_lengths, abc_time_alpha_size_average, 'r');
xlabel('Password Length');
ylabel('Time');
title(['Time vs. Password Length for Alphabet Size ', num2str(alpha_size)]);


print(['time_pw_len_alpha_', num2str(alpha_size), '.jpg']);


alpha_size = 4;

abc_prob_alpha_size = abc_data(abc_data(:,alpha_i) == alpha_size,prob_i);
abc_prob_alpha_size_average = mean(reshape(abc_prob_alpha_size, 10, length(abc_prob_alpha_size)/10));

latte_prob_alpha_size = latte_data(latte_data(:,alpha_i) == alpha_size,time_i);
latte_prob_alpha_size_average = mean(reshape(latte_prob_alpha_size, 10, length(latte_prob_alpha_size)/10));

plot(abc_prob_alpha_size_average);

abc_entropy_alpha_size = abc_data(abc_data(:,alpha_i) == alpha_size,entropy_i);
abc_entropy_alpha_size_average = mean(reshape(abc_entropy_alpha_size, 10, length(abc_entropy_alpha_size)/10));

plot(abc_entropy_alpha_size_average);
xlabel('Password Length');
ylabel('Entropy');
title(['Single Run Entropy vs. Password Length for Alphabet Size ', num2str(alpha_size)]);

print(['entropy_pw_len_alpha_', num2str(alpha_size), '.jpg']);

pw_length = 4;

abc_entropy_pw_length = abc_data(abc_data(:,passlen_i) == pw_length,entropy_i);
abc_entropy_pw_length_average = mean(reshape(abc_entropy_pw_length, 10, length(abc_entropy_pw_length)/10));

alpha_sizes = 2.^((1:7)+1);

plot(alpha_sizes,abc_entropy_pw_length_average);
xlabel('Alphabet Size');
ylabel('Entropy');
title(['Single Run Entropy vs. Alphabet Size for Password Length', num2str(alpha_size)]);

print(['entropy_alpha_pw_len', num2str(pw_length), '.jpg']);




abc_data_secure = csvread('abc_results_password_secure_clean');