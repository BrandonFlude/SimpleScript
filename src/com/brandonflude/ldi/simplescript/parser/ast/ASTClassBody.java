/* Generated By:JJTree: Do not edit this line. ASTClassBody.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=com.brandonflude.ldi.simplescript.interpreter.BaseASTNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.brandonflude.ldi.simplescript.parser.ast;

public
class ASTClassBody extends SimpleNode {
  public ASTClassBody(int id) {
    super(id);
  }

  public ASTClassBody(SimpleScript p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SimpleScriptVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=664261355e05d2f67e0a55f605c24a2e (do not edit this line) */
