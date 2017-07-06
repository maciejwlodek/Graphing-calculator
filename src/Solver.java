import java.util.*;
import java.util.Arrays;

public class Solver {

	
	private static final String[] operations = {"(",")","^","*","/","+","-"};
	private static final String[] digits = {"0","1","2","3","4","5","6","7","8","9","."};
	//functions are: a = sqrt, b = ln, c = log, d = sin, f = cos, g = tan, h = gamma, i = abs val, j = arcsin, k = arccos, l = arctan, m=floor, n=ceiling
	private static final String[] functions = {"a","b","c","d","f","g","h","i","j","k","l","m","n"};
	
	private static List operationsList = Arrays.asList(operations);
	private static List digitsList = Arrays.asList(digits);
	private static List functionsList = Arrays.asList(functions);


	//compares the precedence of two operators
	public static boolean operationPrecedence(String op1, String op2){
		int x1=5;
		int x2=5;
		if(op1.equals("+")|| op1.equals("-")) x1=0;
		if(op1.equals("*")|| op1.equals("/")) x1=1;
		if(op1.equals("^")) x1=2;
		
		if(op2.equals("+")|| op2.equals("-")) x2=0;
		if(op2.equals("*")|| op2.equals("/")) x2=1;
		if(op2.equals("^")) x2=2;
		
		return x1>=x2;
		
	}
	
	//checks if a string can be parsed into a double
	public static boolean isNumber(String num){
		try { 
			Double.parseDouble(num); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		return true;
	}
	
	public static int findCorrespondingParenthesis(int index, String expression){
		if(expression.charAt(index)!='('){
			return -1;
		}
		int lPar=1;
		int rPar=0;
		
		int i=0;
		for(i=index;rPar<lPar;i++){
			char c = expression.charAt(i);
			if(c=='(' && i!=index){
				lPar++;
			}
			if(c==')'){
				rPar++;
			}
		}
		return i-1;
	}
	
	/*adds )'s if necessary
	 *adds necessary *'s
	 *checks for e and p (pi)
	 *renames functions
	 */
	public static String format(String expression){
		
		expression = expression.replaceAll(" ", "");
		
		expression = expression.replaceAll("arcsin", "j");
		expression = expression.replaceAll("arccos", "k");
		expression = expression.replaceAll("arctan", "l");
		
		expression = expression.replaceAll("sqrt", "a");
		expression = expression.replaceAll("ln", "b");
		expression = expression.replaceAll("log", "c");
		expression = expression.replaceAll("sin", "d");
		expression = expression.replaceAll("cos", "f");
		expression = expression.replaceAll("tan", "g");	
		expression = expression.replaceAll("gamma", "h");
		//expression = expression.replaceAll("Γ", "h");
		expression = expression.replaceAll("abs", "i");
		expression = expression.replaceAll("floor", "m");
		expression = expression.replaceAll("ceiling", "n");

		expression = expression.replaceAll("pi", "p");
		
		int rPar = 0;
		int lPar = 0;
		for(int i=0;i<expression.length();i++){
			if(expression.charAt(i)=='(') lPar++;
			if(expression.charAt(i)==')') rPar++;	
		}
		
		for(int i=0;i<expression.length();i++){
			if(expression.charAt(i)=='('){
				if(i>0){
					String c = expression.charAt(i-1)+"";
					if(digitsList.contains(c)){
						String s1 = expression.substring(0, i) + "*";
						String s2 = expression.substring(i);
						expression = s1+s2;
					}
				}
			}
			if(expression.charAt(i)==')'){
				if(i+1<expression.length()){
					String c = expression.charAt(i+1)+"";
					if(digitsList.contains(c)){
						String s1 = expression.substring(0, i+1) + "*";
						String s2 = expression.substring(i+1);
						expression = s1+s2;
					}
				}
			}
			if(expression.charAt(i)=='e' || expression.charAt(i)=='p'){
				if(i>0){
					String c = expression.charAt(i-1)+"";
					if(digitsList.contains(c)){
						String s1 = expression.substring(0, i) + "*";
						String s2 = expression.substring(i);
						expression = s1+s2;
					}
				}
				if(i+1<expression.length()){
					String c = expression.charAt(i+1)+"";
					if(digitsList.contains(c) || functionsList.contains(c)){
						String s1 = expression.substring(0, i+1) + "*";
						String s2 = expression.substring(i+1);
						expression = s1+s2;
					}
				}
			}	
			if(expression.charAt(i)=='-'){
				if(i>0 && expression.charAt(i-1)=='('){
					String s1 = expression.substring(0, i) + "0";
					String s2 = expression.substring(i);
					expression = s1+s2;
				}
				if(i==0){
					expression = "0"+expression;
				}
			}
			if(functionsList.contains(expression.charAt(i)+"")){
				if(i>0){
					String c = expression.charAt(i-1)+"";
					if(digitsList.contains(c)){
						String s1 = expression.substring(0, i) + "*";
						String s2 = expression.substring(i);
						expression = s1+s2;
					}
				}
			}
		}
		expression = expression.replaceAll("e", "2.718281828");
		expression = expression.replaceAll("p", "3.141592653");

		
		
		if(rPar<lPar){
			for(int i=0;i<lPar-rPar;i++){
				expression+=")";
			}
		}
		return expression;
	}
	
	// a,b,c,d,e,f,g,h,i,j,k,l,m,n and p are invalid variable names
	//draws parentheses around each instance of variable in function
	public static String formatFunction(String expression, String variable){
		int s = expression.length();
		for(int i=0;i<s;i++){
			if(variable.equals(expression.charAt(i)+"")){
				String s1 = expression.substring(0, i)+"(";
				String s2;
				if(i+2<expression.length())  s2 = variable + ")"+expression.substring(i+1);
				else s2 = variable + ")";
				expression = s1+s2;
				i++;
			}
		}	
		return expression;
	}
	
	public static String plugIn(String expression, String variable, double value){
		return expression.replaceAll(variable, value+"");
	}
	
	public static String plugIn(String expression, String variable, String value){
		return expression.replaceAll(variable, value);
	}
	

	//converts an infix expression into an array of operators and numbers
	public static ArrayList<String> toNumBlock(String expression){
				
		ArrayList<String> blocks = new ArrayList<String>();
		String currentBlock = "";
		for(int i=0;i<expression.length();i++){
			String letter = expression.charAt(i)+"";
			if(!digitsList.contains(letter) && !operationsList.contains(letter) && !functionsList.contains(letter)){
				//System.out.println("Invalid character: " + letter);
				return null;
			}
			if(digitsList.contains(letter)){
				currentBlock+=letter;
			}
			else{ 
				blocks.add(currentBlock);
				blocks.add(letter);
				currentBlock="";
			}		
		}
		blocks.add(currentBlock);
		boolean x=true;
		while(x){
			x = blocks.remove("");
		}
		
		return blocks;
	}
	
	//takes an infix list and converts to postfix
	public static ArrayList<String> toPostfix(ArrayList<String> blocks){
		
		Stack<String> operationStack = new Stack<String>();
		
		ArrayList<String> result = new ArrayList<String>();
		
		for(String x:blocks){
			if(isNumber(x)){
				result.add(x);
			}
			else{
				if(x.equals("(")){
					operationStack.push(x);
				}
				else if(operationsList.contains(x)&& !x.equals(")")){
					if(!operationStack.isEmpty()){
						boolean b = true;
						while(b && !operationStack.isEmpty()){
							String pastOperation = operationStack.peek();
							if(pastOperation.equals("(")){
								break;
							}
							if(operationPrecedence(pastOperation, x)){
								operationStack.pop();
								result.add(pastOperation);
							}
							else b=false;
						}
					}
					operationStack.push(x);

				}
				if(x.equals(")")){
					String pastOperation = operationStack.peek();
					while(!operationStack.isEmpty() && !pastOperation.equals("(")){
						operationStack.pop();
						result.add(pastOperation);
						if(!operationStack.isEmpty()) pastOperation = operationStack.peek();
					}
					if(!operationStack.isEmpty()){
						operationStack.pop();
					}
				}
			}
			
			
		}
		while(!operationStack.isEmpty()){
			result.add(operationStack.pop());
		}
		
		return result;
	}
	
	//calculates the value of a postfix list
	public static double calculatePostfix(ArrayList<String> expression){
		double result=0;
		Stack<String> myStack = new Stack<String>();
		for(String x:expression){
			if(myStack.isEmpty()){
				myStack.push(x);
			}
			else{
				if(isNumber(x)){
					myStack.push(x);
				}
				else{
					String num2 = myStack.pop();
					String num1 = myStack.pop();
					double calculation = performSingleCalculation(num1,num2,x);
					myStack.push(calculation+"");

				}
			}
		}
		String resultString = myStack.pop();
		if(resultString.contains("E-")){
			return 0;
		}
		if(resultString.contains("E")){
			return 9999999;
		}
		result = Double.parseDouble(resultString);
		return result;
		
		
	}
	public static double performSingleCalculation(String num1, String num2, String operation){
		double d1 = Double.parseDouble(num1);
		double d2 = Double.parseDouble(num2);
		double result=0.0;
		if(operation.equals("+")){
			result = d1+d2;
		}
		if(operation.equals("-")){
			result = d1-d2;
		}
		if(operation.equals("*")){
			result = d1*d2;
		}
		if(operation.equals("/")){
			result = d1/d2;
		}
		if(operation.equals("^")){
			result = Math.pow(d1, d2);
		}		
		result = (double) Math.round(result * 1000000) / 1000000;
		return result;

	}
	public static double fullCalculation(String expression){
		expression = format(expression);
		for(int i=0;i<expression.length();i++){
			String s = expression.charAt(i)+"";
			if(functionsList.contains(s)){
				if(expression.charAt(i+1)!='('){
					System.out.println("fix format issue: sin x --> sin(x)");
					System.exit(0);
				}
				else{
					int a = i+1;
					int b = findCorrespondingParenthesis(a, expression);
					String subExpression = expression.substring(a+1, b);
					double subCalculation = fullCalculation(subExpression);
					
					String sub1 = expression.substring(0,i);
					String sub2 = "("+ calculateFunction(subCalculation,s)+")";
					String temp=sub2;
					if(temp.contains("E")){
						sub2 = "(Infinity)";
					}
					if(temp.contains("E-")){
						sub2 = "(0)";
					}
					String sub3 = expression.substring(b+1);
					if(sub2.equals("(Infinity)")){
						//System.out.println("Overflow");
						return Integer.MAX_VALUE;
						//System.exit(0);
					}
					if(sub2.equals("(NaN)")){
						//System.out.println("Error");
						return Integer.MIN_VALUE;
						//System.exit(0);
					}
					sub2 = format(sub2);

					expression = sub1+sub2+sub3;
				}
			}
		}
		
		
		ArrayList<String> blocks = toNumBlock(expression);
		ArrayList<String> postfix = toPostfix(blocks);
		
		double d = calculatePostfix(postfix);
		return d;
		
		
	}
	
	public static double evaluateFunction(String function, String variable, double value){
		function = formatFunction(function,variable);
		function = plugIn(function, variable,value);
		function = formatFunction(function,variable);
		return fullCalculation(function);
	}
	
	//returns a function that approximates f'
	public static String deriv(String function, String variable, double dx){
		
		function = format(function);
		String f1 = "("+ plugIn(function, variable, "("+variable+ "+"+dx+")") + ")";
		String f2 = "("+ function + ")";
		String derivative = "("+f1+"-"+f2+")"+"/"+"("+dx+")";
		return derivative;

	}

	
	//functions are: a = sqrt, b = ln, c = log, d = sin, f = cos, g = tan, h = gamma, i = abs val, j=arcsin, k=arccos, l=arctan, m=floor, n=ceiling
	public static String calculateFunction(double operand, String function){
		double result = 0;
		
		if(function.equals("a")){
			result = Math.sqrt(operand);
		}
		if(function.equals("b")){
			result = Math.log(operand);
		}
		if(function.equals("c")){
			result = Math.log10(operand);
		}
		if(function.equals("d")){
			result = Math.sin(operand);
		}
		if(function.equals("f")){
			result = Math.cos(operand);
		}
		if(function.equals("g")){
			result = Math.tan(operand);
		}
		if(function.equals("h")){
			result = lanczos_gamma(operand);
		}
		if(function.equals("i")){
			result = Math.abs(operand);
		}
		if(function.equals("j")){
			result = Math.asin(operand);
		}
		if(function.equals("k")){
			result = Math.acos(operand);
		}
		if(function.equals("l")){
			result = Math.atan(operand);
		}
		if(function.equals("m")){
			result = Math.floor(operand);
		}
		if(function.equals("n")){
			result = Math.ceil(operand);
		}
		result = (double) Math.round(result * 1000000) / 1000000;
		return result+"";
	}
	
//	//calculate the gamma function (from 
//	public static double logGamma(double x) {
//		double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
//		double ser = 1.0 + 76.18009173 / (x + 0) - 86.50532033 / (x + 1) + 24.01409822 / (x + 2) - 1.231739516 / (x + 3)
//				+ 0.00120858003 / (x + 4) - 0.00000536382 / (x + 5);
//		return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
//	}
//
//	public static double gamma(double x) {
//		return Math.exp(logGamma(x));
//	}
	
	
	//calculate the gamma function (code from https://rosettacode.org/wiki/Gamma_function#Java)
	public static double lanczos_gamma(double x){
		double[] p = {0.99999999999980993, 676.5203681218851, -1259.1392167224028,
			     	  771.32342877765313, -176.61502916214059, 12.507343278686905,
			     	  -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7};
		int g = 7;
		if(x < 0.5) return Math.PI / (Math.sin(Math.PI * x)*lanczos_gamma(1-x));
 
		x -= 1;
		double a = p[0];
		double t = x+g+0.5;
		for(int i = 1; i < p.length; i++){
			a += p[i]/(x+i);
		}
 
		return Math.sqrt(2*Math.PI)*Math.pow(t, x+0.5)*Math.exp(-t)*a;
	}

}
