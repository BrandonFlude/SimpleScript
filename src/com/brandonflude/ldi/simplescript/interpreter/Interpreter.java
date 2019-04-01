package com.brandonflude.ldi.simplescript.interpreter;

import com.brandonflude.ldi.simplescript.parser.ast.ASTCode;
import com.brandonflude.ldi.simplescript.parser.ast.SimpleScript;
import com.brandonflude.ldi.simplescript.parser.ast.SimpleScriptVisitor;

public class Interpreter {
	
	private static void usage() {
		System.out.println("Usage: simplescript [-d1] < <source>");
		System.out.println("         			 -d1 -- output AST");
	}
	
	public static void main(String args[]) {
		boolean debugAST = false;
		if (args.length == 1) {
			if (args[0].equals("-d1"))
				debugAST = true;
			else {
				usage();
				return;
			}
		}
		SimpleScript language = new SimpleScript(System.in);
		try {
			ASTCode parser = language.code();
			SimpleScriptVisitor nodeVisitor;
			if (debugAST)
				nodeVisitor = new ParserDebugger();
			else
				nodeVisitor = new Parser();
			parser.jjtAccept(nodeVisitor, null);
		} catch (Throwable e) {
			System.out.println(e.getMessage());
		}
	}
}
