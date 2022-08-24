package it.reactive.rulengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

@SuppressWarnings("restriction")
public class RuleEngine {
	static Logger logger = Logger.getLogger(RuleEngine.class.getName());

	static String[] SAMPLE_RULES = { "rule01", "rule02" };

	private String[] rules;

	private Context ruleContext;
	
	private boolean logToSysout = false;

	private Map<String, Value> ruleEvalMap = new HashMap<String, Value>();

	public void setRules(String[] rules) {
		this.rules = rules;
	}

	public static void main(String[] args) throws Exception {
		RuleEngine re = new RuleEngine();
		re.setRules(SAMPLE_RULES);
		re.doSample();
	}

	public RuleEngine() {
		ruleContext = Context.create();
	}
	
	private void log(String text) {
		if (logToSysout) {
			System.out.println(text);
		} else {
			logger.info(text);
		}
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doSample() throws IOException {
		logToSysout = true;
		
		Map inputMap = new HashMap();
		inputMap.put("uno", "1");
		inputMap.put("due", "2");
		log("JAVA-input: " + inputMap);

		Map outputMap = evaluateRules(inputMap);

		log("*** termine esecuzione regole *** ");
		log("JAVA-output: " + outputMap);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map evaluateRules(Map contextMap) throws IOException {
		for (String rule : rules) {
			log("*** REGOLA " + rule + " ***");

			Value ruleEval = getRuleEval(rule);

			ProxyObject po = ProxyObject.fromMap(contextMap);

			Value returnVal = ruleEval.execute(po);
			Map retMap = returnVal.as(Map.class);
			for (Object k : retMap.keySet()) {
				contextMap.put(k, retMap.get(k));
			}
		}
		return contextMap;
	}

	private Value getRuleEval(String rule) throws IOException {
		Value ruleEval = ruleEvalMap.get(rule);
		if (ruleEval == null) {
			Path rulePath = Paths.get("./src/test/resources/" + rule + ".js");
			String ruleText = new String(Files.readAllBytes(rulePath));
			ruleEval = ruleContext.eval("js", "(" + ruleText + ")");
			ruleEvalMap.put(rule, ruleEval);
		}
		return ruleEval;
	}
}
