package ast;

import org.eclipse.jdt.core.dom.*;

public class MyVisitor extends ASTVisitor {

    @Override
    public boolean visit(IfStatement node) {
        // 処理

        return super.visit(node);
    }

    @Override
    public boolean visit(MethodInvocation node) {
        // 処理

    	String typeName = this.getMethodInvocator(node);
        System.out.println( typeName );
		System.out.println(node.getExpression() + "." +  node.getName().toString() + "Count()");

        return super.visit(node);
    }


    public String getMethodInvocator(MethodInvocation node) {
		String result = "";

        ITypeBinding typeBinding = node.getExpression().resolveTypeBinding();

        if(typeBinding != null)	result = typeBinding.getQualifiedName();

		return result;
    }

    public void printLength(ASTNode node) {
    	int offset = node.getStartPosition();
    	int length = node.getLength();
    	System.out.println("start:" + offset + "  end:" + (offset + length));
    }

}
