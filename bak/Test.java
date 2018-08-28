package ast;

public class Test {

	public static void main(String[] args) {
		JavaParserAST parser = new JavaParserAST();

		String[] srcDirs = { "D://eclipse/Java/pleiades/workspace/JavaParser_AST/src/ast" };

		String classpath ="D://eclipse/Java/pleiades/workspace/JavaParser_AST/.classpath";
		String[] src = { "D://eclipse/Java/pleiades/workspace/JavaParser_AST/src/ast/Sample.java"};

		parser.parseLevel2(srcDirs, src, classpath);
	}

}
