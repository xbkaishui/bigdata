package mvel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;
import org.mvel2.ParserContext;
import org.mvel2.ast.Function;
import org.mvel2.compiler.CompiledExpression;
import org.mvel2.compiler.ExpressionCompiler;


import static org.mvel2.util.CompilerTools.extractAllDeclaredFunctions;

/**
 * MVEL DOC
 * http://mvel.documentnode.com/
 * Created by xbkaishui on 16/12/11.
 */
public class MvelTest {
    /**
     * test get inputs
     */
    public static void testProperty() {
        String exp = "c != null && foo.bar.name == 'dog' && foo.bar.woof";
        exp = "Math.max(a,b)+c+1.0";
        //        exp ="test != foo && bo.addSomething(trouble) && 1 + 2 / 3 == 1; String bleh = foo; twa = bleh;";
        ParserContext pCtx = ParserContext.create();
        MVEL.analysisCompile(exp, pCtx);
        System.out.println("inputs:\t" + pCtx.getInputs());
        System.out.println("vals:" + pCtx.getVariables());
    }

    public static void testParseExpression() {
        Map imports = new HashMap();
        imports.put("time", MVEL.getStaticMethod(System.class, "currentTimeMillis",
            new Class[0])); // import a static method
        String exp = "time() + bb";
        exp = "c != null && foo.bar.name == 'dog' && foo.bar.woof";
        //        exp = "mus(ss)";
        ExpressionCompiler compiler = new ExpressionCompiler(exp);
        Serializable s = compiler.compile();
        CompiledExpression compiledExpression = (CompiledExpression) s;
        Map<String, Function> funcs = extractAllDeclaredFunctions(compiledExpression);
        System.out.println(funcs);
    }

    public static void simpleTest() {
        String exp = "a+b+1";
        exp = "Math.ceil( (double) x / 3 ) + bb";
        exp = "";
        //        exp = "x/4";
        Object result = MVEL.compileExpression(exp);
        System.out.println(result);
    }

    public static void main(String[] args) {
        //        testParseExpression();
        testProperty();
    }
}
