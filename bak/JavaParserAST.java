package ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utility.file.xml.reader.MyXmlReader;

public class JavaParserAST {

	public void parseLevel2(String[] srcDirs, String[] src, String classPath){

		// Create AST Parser
		ASTParser parser = ASTParser.newParser(AST.JLS8);

		final String[] sourcePathDirs = srcDirs;
		String[] classPaths;
		try {
			classPaths = getClassPaths(classPath);
			final String[] sources = src;
			final String[] encodings = null;

			final Map<String,String> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
			parser.setCompilerOptions(options);
			parser.setResolveBindings(true);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			parser.setBindingsRecovery(true);
			parser.setStatementsRecovery(true);

			// setEnvironment の第一引数にはクラスパスの配列．第二引数にはソースコードを検索するパスの配列
	        // 第三第四については何も考えず null, true ．納得いかない時はIBMのASTPa...
			parser.setEnvironment(classPaths, sourcePathDirs, null, true);

			String[] keys = new String[] {};

		    FileASTRequestor astRequestor = new FileASTRequestor() {

				final List<CompilationUnit> scannedUnits = new ArrayList<CompilationUnit>();

				// createASTsはソースコードを解析して抽象構文木（CompilationUnit）を作成する処理は実装されているが、
				// それをどのように扱うか（CompilationUnit.accept()までの一連の処理）は自分で実装する必要がある模様
				@Override
		        public void acceptAST(String sourceFilePath, CompilationUnit ast) {
					scannedUnits.add(ast);
					ast.accept(new MyVisitor());
		            //logger.fine("acceptAST: " + sourceFilePath);
//		            if (checkCompilationErrors(sourceFilePath, ast)) {
//		                handler.handleParsedUnit(sourceFilePath, ast);
//		            }
		        }
		    };

			parser.createASTs(sources, encodings, keys, astRequestor, new NullProgressMonitor());
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	public static void parseLevel1(String source){

		// Create AST Parser
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(true);
		CompilationUnit unit = (CompilationUnit) parser.createAST(new NullProgressMonitor());


		unit.accept(new MyVisitor());

	}


	private static String[] getClassPaths(String targetPath) throws Exception{
		String[] ret = null;

		MyXmlReader reader = new MyXmlReader();

		try {

			Node root = reader.getRootNode(targetPath);

			NodeList nodes = root.getChildNodes();
			Node node = null;

			for (int i = 0; i < nodes.getLength(); i++) {
				node = nodes.item(i);

				if("classpath".equals(nodes.item(i).getNodeName())){
					node = nodes.item(i);
					break;
				}
			}

			if(node == null){
				// なにもしない
			}else{
				nodes = node.getChildNodes();
				String lib;
				ArrayList<String> libpaths = new ArrayList<String>();

				for (int i = 0; i < nodes.getLength(); i++) {

					node = nodes.item(i);

					// <classpathentry kind="lib" path="D:..."> の値を取得
					if("classpathentry".equals(node.getNodeName()) &&
							reader.existAttributeValue(node.getAttributes(), "kind", "lib")){

						lib = node.getAttributes().getNamedItem("path").getNodeValue();
						libpaths.add(lib);
					}
				}

				if(0 < libpaths.size()){
					ret = libpaths.toArray(new String[0]);
				}
			}

		} catch (Exception e) {
			throw e;
		}

		return ret;
	}


	private String[] getSystemEnv(String name){
		String env_value;
		String[] ret;

		// 環境変数の取得
		env_value = System.getenv(name);

		if (env_value == null){
			ret = null;
		}else{
			ret = env_value.split(";");
		}

		return ret;
	}
}
