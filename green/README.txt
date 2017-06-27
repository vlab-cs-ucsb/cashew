=================================================================================
Green
=================================================================================
An extension of the GreenSolver tool (https://github.com/green-solver/green-solver). 

Extensions 
	- Handling of string constraints
	- Integration with ABC
	- New canonicalization procedure aimed at preserving the number of solutions
	- Caching of model counters


=================================================================================
Building
=================================================================================
To build ---ant build
To package -- ant package 

Before building, edit build.xml in the green directory to include the path to your jpf-symbc.jar. 

NOTE: this extension of Green depends on a built jpf-symbc project. Currently the two projects are cyclically dependent due to Green using a translator from SPF to translate to ABC. Ultimately the best scenario would be writing a translator to ABC in Green directly but for now, this problem is overcome by bootstrapping. 

A green.jar file is included in the lib directory of jpf-symbc. Include the path to this jar in your jpf-symbc/build.xml and jpf-symbc/jpf.properties. Build jpf-symbc using this jar. Then include the path to this jpf-symbc.jar in green/build.xml to rebuild Green if you make changes to it. 


=================================================================================
Using Green in SPF
=================================================================================
Configuring SPF to use Green is done through .jpf file. Include 'symbolic.green=true' to use Green. 

An example of how to configure Green and specify which services of Green to use can be found in jpf-security/main/example/green/GreenTest.jpf. 


=================================================================================
Configured Model Counters and Solvers
=================================================================================


Model Counters: ABC, LattE, barvinok
Solvers: ABC, z3, choco, choco3, cvc3

Each solver or model counter must be installed on their own. 
Currently ABC is the only option for string constraints. The newest version of z3 includes support for strings but this extension hasn't yet been integrated to Green. 

