sr_data = csvread('multi_run_results.txt');

H = sr_data(:,1)';
k = 0:length(H);
plot(k,[0,H], 'k', k, 6-[0,H], '--k', k, 6(ones(1,length(k))), 'r', 0,7, 0,-1);
xlabel('Number of Guesses');
ylabel('Entropy (bits)');
legend('Leakage', 'Remaining Entropy', 'Initial Entropy');
matlab2tikz('password_entropy_vs_guesses.tex');


sr_times = data(:,2)';
se_times = csvread('se_times.txt')';


k = 0:length(sr_times) -1;

plot(k,[0,sr_times(1:end-1)], 'k', k, [0,se_times], '--k' );
xlabel('Number of Guesses');
ylabel('Running Time (s)');
legend('Complete SE', 'Single-run SE');
matlab2tikz('time_single_run_vs_se.tex');


