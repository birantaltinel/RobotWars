package Arena;

import Arena.Exceptions.RobotNotCompiledException;
import Arena.Exceptions.RobotNotLoadedException;
import net.openhft.compiler.CompilerUtils;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class RobotLoader {
    public String compile(String filePath, String robotName) throws FileNotFoundException, RobotNotCompiledException {
        String robotFile = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
        String mainProjectPath = "src/main/java/Arena/";
        String generatedRobotFilePath = String.format("%s%s.java", mainProjectPath, robotName);

        StringBuilder sb = new StringBuilder();
        sb.append("package Arena;\n");
        sb.append(String.format("public class %s extends Robot implements Runnable {\n", robotName));
        sb.append("    public void run() {\n");
        sb.append(robotFile);
        sb.append("    }\n");
        sb.append("}\n");

        File customRobotJava = new File(generatedRobotFilePath);
        if (customRobotJava.getParentFile().exists() || customRobotJava.getParentFile().mkdirs()) {
            try {
                Writer writer = null;
                try {
                    writer = new FileWriter(customRobotJava);
                    writer.write(sb.toString());
                    writer.flush();
                } finally {
                    try {
                        assert writer != null;
                        writer.close();
                    } catch (Exception e) {
                    }
                }

                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                List<String> optionList = new ArrayList<>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path") + ";dist/InlineCompiler.jar");

                Iterable<? extends JavaFileObject> compilationUnit
                        = fileManager.getJavaFileObjectsFromFiles(Collections.singletonList(customRobotJava));
                JavaCompiler.CompilationTask task = compiler.getTask(
                        null,
                        fileManager,
                        diagnostics,
                        optionList,
                        null,
                        compilationUnit);

                if (task.call()) {
                    return mainProjectPath;
                } else {
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        System.out.format("Error on line %d in %s%n",
                                diagnostic.getLineNumber(),
                                diagnostic.getSource().toUri());
                    }
                }
                fileManager.close();
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
        throw new RobotNotCompiledException(String.format("Failed to load robot: %s", robotName));
    }

    public Robot load(String mainProjectPath, String robotName) throws RobotNotLoadedException {
        try {
            Robot robot = null;
            URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(mainProjectPath).toURI().toURL()});
            Class<?> loadedClass = null;
            loadedClass = classLoader.loadClass(String.format("Arena.%s", robotName));
            Object obj = loadedClass.getDeclaredConstructor().newInstance();

            if (obj instanceof Robot) {
                robot = (Robot)obj;
                robot.run();
                return robot;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | MalformedURLException e) {
            e.printStackTrace();
        }
        throw new RobotNotLoadedException(String.format("Failed to load robot: %s", robotName));
    }

    public void inMemoryLoader(String robotFilePath, String robotName) {
        String robotFile = null;
        try {
            robotFile = new Scanner(new File(robotFilePath)).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String className = String.format("Arena.%s", robotName);


        StringBuilder sb = new StringBuilder();
        sb.append("package Arena;\n");
        sb.append(String.format("public class %s extends Robot implements Runnable {\n", robotName));
        sb.append("    public void run() {\n");
        sb.append(robotFile);
        sb.append("    }\n");
        sb.append("}\n");
        String javaCode = sb.toString();

        Class aClass = null;
        try {
            aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
            Robot runner = (Robot) aClass.getDeclaredConstructor().newInstance();
            runner.run();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        RobotLoader rl = new RobotLoader();
        //String mainProjectPath = rl.compile("src/main/java/SampleRobots/Rabbit", "Rabbit");
        //rl.load(mainProjectPath, "Rabbit");
        rl.inMemoryLoader("src/main/java/SampleRobots/Rabbit", "Rabbit");
    }
}
