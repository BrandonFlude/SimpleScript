/* Generated By:JJTree: Do not edit this line. ASTClassCall.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=com.brandonflude.ldi.simplescript.interpreter.BaseASTNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.brandonflude.ldi.simplescript.parser.ast;

public
class ASTClassCall extends SimpleNode {
  public ASTClassCall(int id) {
    super(id);
  }

  public ASTClassCall(SimpleScript p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SimpleScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7a9a0258f2b3ea86c58a9ef433bd8ae6 (do not edit this line) */
