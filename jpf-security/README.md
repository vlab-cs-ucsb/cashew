This distribution contains three components: *jpf-core*, *jpf-symbc*, and *jpf-security*. 

 * *jpf-core* contains the main functionality of Java PathFinder tool, which is a custom VM that allows search and backtracking capabilities. 
 * *jpf-symbc* contains the symbolic execution tool
 * *jpf-security* contains the extensions that we have developed for the DARPA ISSTAC project. 

In the following, we describe how to run worst case complexity analysis and side channel analysis.

# Executing JPF #

To execute jpf, simply invoke:

```$STAC_ROOT/stacgit/scripts/run-jpf.sh jpffilename```

where ```jpffilename``` is the name of a .jpf file. The .jpf file contains all information needed to execute jpf on a program.

# Worst-Case Time Complexity Analysis using a simple listener  #
It is possible to simply determine the WCT by evaluating all possible execution paths. An example for this is:

```$STAC_ROOT/stacgit/scripts/run-jpf.sh $JPF_SECURITY/src/examples/BubbleSort.jpf```

This example will print out the worst case execution path and the input which led to it.

This approach is not practical for large inputs, therefore we've developed 2 other approaches:

* Worst-Case Time Complexity Analysis using Heuristics
* Worst-Case Time Complexity Analysis using Distributed execution


# Worst-Case Time Complexity Analysis using Heuristics #
`jpf-security` contains a component that can be used for analyzing the worst case complexity of programs. The approach taken, is to gradually increase the input size to the target program and note the execution time for each run. The obtained data can be used with regression analysisi (not included as part of the package) to determine the worst case complexity.

The worst case time complexity analysis uses a search heuristic that enables analyzing programs with large input size. The heuristic works in a two phase fashion, using behavior extracted at small input sizes to guide the search at large input sizes. In the following, we provide instructions on how to perform both phases and demonstrate them on a concrete example.

The two-step process is the following:

1. It performs a full exploration of the program on a small input size. Meanwhile, the decision frequencies are recorded by annotating the CFGs of the explored methods. The deicions on the path yielding the worst case behavior (in terms of search depth), are used for guiding the search in the subsequent phase.
2. The annotated CFG *guides* the exploration on larger input sizes (for which full exploration is intractable) in the following way: whenever a condition is executed with a symbolic decision predicate, the corresponding condition is consulted in the CFG. If the condition *only* has counts on one branch, it will selected for exploration -- the other branch will *not* be explored regardless of feasibility; it is pruned from the search. In addition to this memoryless guidance policy, the analysis **also** has support for history-based guidance that takes into account the previous decisions made on the current path in order to resolve choices. The history-based analysis yields better results for, e.g., merge sort.

In the following, we provide instructions on how to perform both phases and demonstrate them on a concrete example.

## Overview ##
In the following, `$JPF_SECURITY/src/examples/benchmarks/heuristics` contains example `.jpf` files that are setup for worst-case complexity analysis. These are:

* BellmanFord_shell.jpf
* BinaryTreeSearch_shell.jpf
* Dijkstra_shell.jpf
* HeapInsertJDK15_shell.jpf
* InsertionSort_shell.jpf
* MergeSortJDK15_shell.jpf
* QuickSortJDK15_shell.jpf
* RedBlackTreeSearch_shell.jpf
* SortedListInsertion_shell.jpf
* Tsp_shell.jpf

Each `.jpf` file contains the basic setup to run the worst case analysis.

## Usage ##

The Java PathFinder shell `wcanalysis.WorstCaseAnalyzer` can be used to set up Phase 1 and Phase 2.

## Concrete Example ##
We use insertion sort as example of the worst case analysis.

```$STAC_ROOT/stacgit/scripts/run-jpf.sh $JPF_SECURITY/src/examples/benchmarks/heuristics/InsertionSort_shell.jpf```

This will generate a results folder `$JPF_SECURITY/insertion_sort_results`

In the results folder, a subfolder `verbose/heuristic/` will be generated that contains the constraints found for input size 2 to 40 for insertion sort. In addition the file `benchmarks.InsertionSort.csv` is produced which summarizes all the results.
**Note** that consecutive runs of the analysis will produced and new data set that is **appended** to this file. 

The csv file includes various statistics:

* **inputSize** Current input size to the SUT. It assumes this information is available in the jpf file as the value of the `target.args` property (it is for the examples in benchmarks.heuristic)
* **wcInstrExec** Shows how many instructions were executed on the worst case path
* **cfgInputSize** Specifies which input size was used for generating the annotated CFG used for the heuristic. This will by default not be added to the .csv file
* **analysisTime** The analysis time when using the heuristic
* **mem** The peak memory consumption during heuristic-based exploration
* **depth** The maximum depth recorded (which constitutes the worst case path)
* **paths** Number of paths explored with the search heuristic
* **resolvedChoicesNum** How many decisions that were resolved as a consequence of using the heuristic
* **unresolvedChoicesNum** How many decisions that could not be resolved by the heuristic
* **newChoicesNum** New decisions encountered during exploration for which the heuristic has no information
* **wcConstraint** The constraint recorded for the worst case path. Any solution to this provides test inputs that are guaranteed to exercise worst case behavior of the SUT

In addition, data points <inputSize, depth> (here depth is the notion of worst case for a path) are generated, and the shell will automatically generate a plot showing the raw data, and the various fitted functions based on regression analysis. The user can zoon in on the graph by highlighting a region.

#### Configuration ####
The following must be supplied in jpf file (or imported from another jpf file using the `@include` directive):

```
@using jpf-security
@using jpf-symbc

shell=wcanalysis.WorstCaseAnalyzer

classpath=${jpf-security}/build/examples
target=benchmarks.InsertionSort

symbolic.worstcase.policy.inputsize=XXX
symbolic.worstcase.input.max=YYY

symbolic.wc.policy.history.size=ZZZ

```
The `classpath` variable should be updated according to the system under test. `target` denotes the entry point of the system under test.

Replace `XXX` with the input size at which the policy should be obtained. This corrsponds to phase 1. Replace `YYY` with the maximum input size at which the heuristic search (phase 2) should be run. The heuristic search will run from input size 1-`YYY`.

`symbolic.wc.policy.history.size` is important because it controls whether the guidance policy produced in phase 1 is memoryless or history-based. By setting this variable to 0, a memoryless policy is used; otherwise, a history with the specified size `ZZZ` will be used.

### Optional Configuration ###
In addition, the user can optionally use the following for the `WorstCaseAnalyzer` shell:

* **```symbolic.heuristic.measuredmethods=<method desc(s)>```**  A list (separated by semicolons) that specifies from which method(s), the value for the worst case path should start counting. Default is the value of ```symbolic.method``` i.e. the "symbolic target method".

* **```symbolic.worstcase.verbose=<true | false>```** This will generate verbose output e.g. analysis statistics. 

* **```symbolic.worstcase.outputpath=<path>```** This will output the contraints for each worst case path and in addition summarize analysis statistics in a csv file. See above.

* **```symbolic.worstcase.req.maxinputsize=<Number>```** Plot the budget input size from the requirement

* **```symbolic.worstcase.req.maxres=<Number>```** Plot the budget max resource size from the requirement

* **```symbolic.worstcase.predictionmodel.size=<Number>```** The maximum plotted domain of the fitting functions.

* **```symbolic.worstcase.reusepolicy=<true | false>```** By setting this to true, a computed policy will be reused if it has been previously computed.

# Worst-Case Time Complexity Analysis using Distributed execution #

Another approach to do WCT analysis is to execute the job in a cloud instrastructure, running on multiple machines. This is essentially a naive algorithm, elaborating all possible execution paths.
This feature is experimental at this point.

An example to submit the job is:

```cd $STAC_ROOT/stacgit/scripts; ./submit-job.sh SPF -f BubbleSort.class -e 32 -a "-clsname BubbleSort -symbmethod 'BubbleSort.sort(sym)' -initdepth 6 -maxdepth 10000"```

This will execute the analysis of a BubbleSort on 32 nodes, on our remote infrastructure. The job status and results can be monitored at:

```https://stac.isis.vanderbilt.edu/dashboard.php```


# Side Channel Analysis #

**jpf-security** computes the amount of information revealed to an attacker via side channels. Types of side channels that can be analyzed are:

* Timing
* Memory bytes allocated
* Bytes written to a file
* Bytes written to a socket  


The analysis is based on Information Theory, and the number of bits that is leaked by the program is measured using Channel Capacity and Shannon entropy. The result of the analysis is the number of bits of the secret that are revealed to the attacker. The analysis uses symbolic execution to ennumeraste all the symbolic paths through a program (where the secret is set to a symbolic value) and it computes the "cost" of each path in terms to time to execute each instruction, memory bytes allocated, bytes written to a file and bytes written to a socket. These costs are treated as classical "observable". The costs for each path are collected into a set of observable Obs. The channel capacity is then CC<log_2(|Obs|). 
The Shannon entropy is H=-Sum p_o log_2 p_o, where p_o is the probability of observing cost "o" and is computed using model counting over the constraints collected with symbolic execution.


## Usage ##

These are instructions how to create a .jpf file from scratch. To see the examples we've supplied, this can be skipped.

Step 1: Define in the configuration file what an attacker can observe:

* **`listener=sidechannel.TimingChannelQuantifier`**: this listener quantifies the leakage when the attacker observes the execution time of the program.
* **`listener=sidechannel.MemoryChannelQuantifier`**: this listener quantifies the leakage when the attacker observes the number of bytes allocated.
* **`listener=sidechannel.FileChannelQuantifier`**: this listener quantifies the leakage when the attacker observes the number of bytes written to files.
* **`listener=sidechannel.NetworkChannelQuantifier`**: this listener quantifies the leakage when the attacker observes the number of bytes written to sockets.  

Step 2: Specify model counter tool in the configuration file. Currently available model counters are ABC and Latte. There are three possible configurations :  
```
model_count.mode=abc.linear_integer_arithmetic
```
or
```
 model_count.mode=latte
 symbolic.reliability.tmpDir=${jpf-security}/build/tmp
 symbolic.reliability.omegaPath=${jpf-security}/tools/omega/oc
 symbolic.reliability.lattePath=${jpf-security}/tools/latte-integrale-1.7.3/bin/count
```
or
```
# for model counting string constraints
symbolic.strings=true
symbolic.string_dp=ABC
model_count.mode=abc.string
```


Step 3: specify the limit of the variables:  

```
	symbolic.min_int=0
	symbolic.max_int=9
```

### Examples ###

```$JPF_SECURITY/src/examples/sidechannel/``` contains examples (Java sources and jpf configuration files) for quantification of side-channel leaks.  An example can be run as follows: 

```$STAC_ROOT/stacgit/scripts/run-jpf.sh $JPF_SECURITY/src/examples/sidechannel/InsecureChecker.jpf```

where, ```InsecureChecker.jpf``` is the jpf configuration file for the Java program ```InsecureChecker.java```. The exected output for the example above are:

```
  Shannon leakage is: 0.5210541044776923
  Channel capacity is: 2.321928094887362
```

* The first output is the leakage measured in Shannon entropy with the assumption that the secret has uniform distribution. This result can be understood as follows: when the attacker has no *a priori* knowledge about the secret, after the observation, he can learn 0.521 bits of the secret.
* The second output is the **channel capacity**, which means the maximum leakage over all possible distribution. This result can be understood as follows: for all possible scenerios, with all possible *a priori* knowledge of the attacker, the program can reveal maximum 2.32 bits of the secret to the attacker.

Other examples are:

* `$JPF_SECURITY/src/examples/sidechannel/InsecureChecker.jpf`: an example that leaks information via timing channel.
* `$JPF_SECURITY/src/examples/sidechannel/TestMemory.jpf`: an example that leaks information via the number of bytes allocated.
* `$JPF_SECURITY/src/examples/sidechannel/SimpleFileWriter.jpf`: an example that leaks information via writing to files.
* `$JPF_SECURITY/src/examples/sidechannel/SendTextClient.jpf`: an example that leaks information via writing to sockets.  
* `$JPF_SECURITY/src/examples/RSASimplified.jpf`: an example of a simplified RSA cryptography program.
