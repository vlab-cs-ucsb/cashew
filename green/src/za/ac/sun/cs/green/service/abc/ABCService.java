package za.ac.sun.cs.green.service.abc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.HashMap;
import java.util.Map;
import org.apfloat.Apint;
import java.math.BigInteger;


import za.ac.sun.cs.green.Green;
import za.ac.sun.cs.green.service.smtlib.SATSMTLIBService;
import za.ac.sun.cs.green.service.SATService;
import za.ac.sun.cs.green.Instance;

import gov.nasa.jpf.symbc.string.translate.TranslateToABC;
import gov.nasa.jpf.symbc.string.StringPathCondition;

import za.ac.sun.cs.green.expr.Variable;
import za.ac.sun.cs.green.expr.VisitorException;
import za.ac.sun.cs.green.service.abc.ABCTranslator;


import za.ac.sun.cs.green.expr.Visitor;
import za.ac.sun.cs.green.expr.VisitorException;
import za.ac.sun.cs.green.expr.Expression;


import choco.kernel.model.variables.integer.IntegerVariable;
import za.ac.sun.cs.green.expr.Variable;
import edu.ucsb.cs.vlab.modelling.Output;





public class ABCService extends SATService {

	
	public ABCService(Green solver) {
		super(solver);
	}

	@Override
	protected Boolean solve(Instance instance) {
		Expression e = instance.getFullExpression();
		ABCTranslator translator = new ABCTranslator();

		StringPathCondition translated_spc = translator.translate(instance.getFullExpression());
		
		Boolean o = TranslateToABC.isSat(translated_spc);
		
		return o;

	}
}