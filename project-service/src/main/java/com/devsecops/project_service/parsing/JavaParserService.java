package com.devsecops.project_service.parsing;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class JavaParserService {

    private static final Logger logger = LoggerFactory.getLogger(JavaParserService.class);

    public List<MethodUnit> parseProject(String projectRootPath) throws IOException {
        List<MethodUnit> methodUnits = new ArrayList<>();
        Path root = Paths.get(projectRootPath);

        // Configure Symbol Solver
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        try {
            combinedTypeSolver.add(new JavaParserTypeSolver(root));
        } catch (Exception e) {
            logger.warn("Could not add JavaParserTypeSolver for root {}: {}", root, e.getMessage());
        }
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);

        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setSymbolResolver(symbolSolver);
        JavaParser javaParser = new JavaParser(parserConfiguration);

        try (Stream<Path> paths = Files.walk(root)) {
            List<Path> javaFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .collect(Collectors.toList());

            for (Path javaFile : javaFiles) {
                try {
                    ParseResult<CompilationUnit> result = javaParser.parse(javaFile);
                    if (result.isSuccessful() && result.getResult().isPresent()) {
                        extractMethodUnits(result.getResult().get(), javaFile.toString(), methodUnits);
                    }
                } catch (Exception e) {
                    logger.error("Failed to parse file: " + javaFile, e);
                }
            }
            logger.info("Parsing complete for {}. Total methods extracted: {}", projectRootPath, methodUnits.size());
        }
        return methodUnits;
    }

    private void extractMethodUnits(CompilationUnit cu, String filePath, List<MethodUnit> methodUnits) {
        cu.findAll(ClassOrInterfaceDeclaration.class).forEach(c -> {
            String className = c.getFullyQualifiedName().orElse(c.getNameAsString());

            c.getMethods().forEach(m -> {
                MethodUnit unit = new MethodUnit();
                unit.setFilePath(filePath);
                unit.setClassName(className);
                unit.setMethodName(m.getNameAsString());

                // Extract annotations
                List<String> annotations = m.getAnnotations().stream()
                        .map(a -> a.getNameAsString())
                        .collect(Collectors.toList());
                unit.setAnnotations(annotations);

                // Body text
                unit.setMethodBody(m.getBody().map(Node::toString).orElse(""));
                unit.setLineNumber(m.getBegin().map(p -> p.line).orElse(-1));

                // Called methods
                Set<String> calledMethods = new HashSet<>();
                m.findAll(MethodCallExpr.class).forEach(mc -> calledMethods.add(mc.getNameAsString()));
                unit.setCalledMethodNames(calledMethods);

                // Cyclomatic complexity
                unit.setCyclomaticComplexity(computeCyclomaticComplexity(m));

                methodUnits.add(unit);
            });
        });
    }

    private int computeCyclomaticComplexity(MethodDeclaration m) {
        final int[] complexity = {1}; // Base complexity

        m.findAll(Node.class).forEach(node -> {
            if (node instanceof IfStmt || node instanceof ForStmt || node instanceof WhileStmt
                    || node instanceof DoStmt || node instanceof CatchClause) {
                complexity[0]++;
            } else if (node instanceof SwitchEntry) {
                SwitchEntry se = (SwitchEntry) node;
                if (!se.getLabels().isEmpty()) {
                    complexity[0]++;
                }
            } else if (node instanceof BinaryExpr) {
                BinaryExpr.Operator op = ((BinaryExpr) node).getOperator();
                if (op == BinaryExpr.Operator.AND || op == BinaryExpr.Operator.OR) {
                    complexity[0]++;
                }
            }
        });
        return complexity[0];
    }
}
