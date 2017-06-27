package modelcounting.nonlinear;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.microsoft.z3.ApplyResult;
import com.microsoft.z3.BitVecExpr;
import com.microsoft.z3.BitVecSort;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.Expr;
import com.microsoft.z3.Goal;
import com.microsoft.z3.Tactic;

public class Z3Bitblaster {

	Context ctx;

	public Z3Bitblaster(Context ctx) {
		this.ctx = ctx;
	}

	public BoolExpr[] bitblast(List<BoolExpr> formula, Map<String, Integer> id_table) {
		Set<Expr> vars = collectVars(formula);
		MappingResult mr = mapBitvector(vars);

		int id = 1;
		for (Expr var : mr.vars) {
			String name = var.getFuncDecl().getName().toString();
			id_table.put(name, id);
			id++;
		}

		Goal g = ctx.mkGoal(false, false, false);
		g.add(mr.clauses.toArray(new BoolExpr[] {}));
		g.add(formula.toArray(new BoolExpr[] {}));

		Tactic t = ctx.then(ctx.mkTactic("simplify"), ctx.mkTactic("bit-blast"), ctx.mkTactic("tseitin-cnf"));
		ApplyResult ar = t.apply(g);
		return ar.getSubgoals()[0].getFormulas();
	}

	public String toDimacs(Iterable<BoolExpr> formula, Map<String, Integer> idTable) {
		StringBuilder body = new StringBuilder();
		int projectionScope = idTable.size();
		int nclauses = 0;

		List<List<Integer>> clauses = new ArrayList<>();
		for (BoolExpr expr : formula) {
			List<Integer> dimacsClause = dimacsVisitor(expr, idTable);
			clauses.add(dimacsClause);
			nclauses++;
		}

		// some tools require projection variables to be the last 
		// variables in the cnf file

		int nVars = idTable.size();
		List<List<Integer>> renamedClauses = new ArrayList<>();
		for (List<Integer> clause : clauses) {
			List<Integer> renamedClause = new ArrayList<>();
			for (Integer var : clause) {
		        boolean isNeg = var < 0;
		        int renamedVar;
		        int abs_var = Math.abs(var);
		        if (abs_var <= projectionScope) 
		            renamedVar = abs_var - projectionScope + nVars;
		        else
		            renamedVar = abs_var - projectionScope;

		        renamedVar = renamedVar * (isNeg ? -1 : 1);
		        renamedClause.add(renamedVar);
			}
			renamedClauses.add(renamedClause);
		}
		
		for (List<Integer> clause : renamedClauses) {
			StringBuilder tmp = new StringBuilder();
			for (Integer i : clause) {
				tmp.append(i);
				tmp.append(' ');
			}
			body.append(tmp.toString());
			body.append("0\n");
		}

		StringBuilder header = new StringBuilder();
		header.append("p cnf " + idTable.size() + " " + nclauses + "\n");
		header.append("cr ");
		for (int i = nVars - projectionScope + 1; i < nVars + 1; i++) {
			header.append(" ");
			header.append(i);
		}
		header.append("\n");

		return header.toString() + body.toString();
	}

	static class MappingResult {
		public final List<BoolExpr> clauses;
		public final List<Expr> vars;

		public MappingResult(List<BoolExpr> clauses, List<Expr> vars) {
			this.clauses = clauses;
			this.vars = vars;
		}

		public MappingResult() {
			this.clauses = new ArrayList<>();
			this.vars = new ArrayList<>();
		}

		@Override
		public String toString() {
			return "MappingResult [clauses=" + clauses + ", vars=" + vars + "]";
		}

	}

	public MappingResult mapBitvector(Iterable<Expr> vars) {
		MappingResult mr = new MappingResult();
		for (Expr var : vars) {
			String name = var.getFuncDecl().getName().toString();
			int size = ((BitVecSort) var.getSort()).getSize();
			for (int i = 0; i < size; i++) {
				BoolExpr mapped = ctx.mkBoolConst(name + "!" + i);
				BitVecExpr extracted = ctx.mkExtract(i, i, (BitVecExpr) var);
				BoolExpr clause = ctx.mkEq(mapped, ctx.mkEq(extracted, ctx.mkBV(1, 1)));
				mr.clauses.add(clause);
				mr.vars.add(mapped);
			}
		}

		return mr;
	}

	public List<Integer> dimacsVisitor(Expr formula, Map<String, Integer> idTable) {
//		System.out.println(formula);
		List<Integer> result;

		if (formula.isApp() && formula.getArgs().length == 0) {
			String name = formula.getFuncDecl().getName().toString();
			int id;
			if (idTable.containsKey(name)) {
				id = idTable.get(name);
			} else {
				id = idTable.size() + 1;
				idTable.put(name, id);
			}

			result = new ArrayList<>();
			result.add(id);
		} else if (formula.isNot()) {
			result = new ArrayList<>();
			List<Integer> varId = dimacsVisitor(formula.getArgs()[0], idTable);
			assert varId.size() == 1;
			result.add(varId.get(0) * -1);
		} else if (formula.isOr()) {
			result = new ArrayList<>();
			for (Expr child : formula.getArgs()) {
				List<Integer> childResult = dimacsVisitor(child, idTable);
				assert childResult.size() == 1;
				result.add(childResult.get(0));
			}
		} else {
			throw new RuntimeException("Unhandled expr: " + formula.getASTKind());
		}

		return result;
	}

	public static Set<Expr> collectVars(Iterable<BoolExpr> formulas) {
		Set<Expr> vars = new LinkedHashSet<>();
		for (BoolExpr expr : formulas) {
			vars.addAll(collectVars(expr));
		}
		return vars;
	}

	public static Set<Expr> collectVars(Expr formula) {
		Set<Expr> vars = new LinkedHashSet<>();
//		System.out.println(">> " + formula);

		if (formula.isBV() && formula.isApp() && formula.getArgs().length == 0) {
			vars.add(formula);
			int size = ((BitVecSort) formula.getSort()).getSize();
//			System.out.println(size);
		} else if (formula.isQuantifier()) {
			throw new RuntimeException("Found a quantifier!");
		} else if (formula.isApp()) {
			for (Expr arg : formula.getArgs()) {
				vars.addAll(collectVars(arg));
			}
		} else {
//			System.err.println("Unhandled case? " + formula.getASTKind() + " " + formula);
			return Collections.emptySet();
		}

		return vars;
	}

	public String generateDimacs(List<BoolExpr> formula) {
		Map<String, Integer> id_table = new HashMap<>();
		BoolExpr[] bitblasted = bitblast(formula, id_table);
		String dimacs = toDimacs(Arrays.asList(bitblasted), id_table);
		return dimacs;
	}

	public static void main(String[] args) {
		Context ctx = new Context();
		BoolExpr expr = ctx.parseSMTLIB2File(args[0], null, null, null, null);
		System.out.println(expr);
		Set<Expr> vars = collectVars(expr);
		System.out.println(vars);

		Z3Bitblaster bt = new Z3Bitblaster(ctx);
		System.out.println(bt.mapBitvector(vars));
		List<BoolExpr> tmp = new ArrayList<>();
		tmp.add(expr);
		String dimacs = bt.generateDimacs(tmp);
		System.out.println(dimacs);
	}
}