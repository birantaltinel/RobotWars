package Arena;

import Arena.Exceptions.RobotNotLoadedException;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RobotLoader {

    public Robot load(String filePath, String robotName) throws FileNotFoundException, RobotNotLoadedException {
        String robotFile = new Scanner(new File(filePath)).useDelimiter("\\Z").next();
        String mainProjectPath = "src/main/java/Arena/";
        String generatedRobotFilePath = String.format("%s%s.java", mainProjectPath, robotName);

        StringBuilder sb = new StringBuilder();
        sb.append("package Arena;\n");
        sb.append(String.format("public class %s extends Robot implements Runnable {\n", robotName));
        sb.append("    public void run() {\n");
        sb.append(robotFile);
        //sb.append("        System.out.println(\"Euroka\");");
        sb.append("    }\n");
        sb.append("}\n");

        File customRobotJava = new File(generatedRobotFilePath);
        Robot robot = null;
        if (customRobotJava.getParentFile().exists() || customRobotJava.getParentFile().mkdirs()) {
            try {
                Writer writer = null;
                try {
                    writer = new FileWriter(customRobotJava);
                    writer.write(sb.toString());
                    writer.flush();
                } finally {
                    try {
                        writer.close();
                    } catch (Exception e) {
                    }
                }

                /** Compilation Requirements *********************************************************************************************/
                DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

                // This sets up the class path that the compiler will use.
                // I've added the .jar file that contains the DoStuff interface within in it...
                List<String> optionList = new ArrayList<String>();
                optionList.add("-classpath");
                optionList.add(System.getProperty("java.class.path") + ";dist/InlineCompiler.jar");

                Iterable<? extends JavaFileObject> compilationUnit
                        = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(customRobotJava));
                JavaCompiler.CompilationTask task = compiler.getTask(
                        null,
                        fileManager,
                        diagnostics,
                        optionList,
                        null,
                        compilationUnit);
                /********************************************************************************************* Compilation Requirements **/
                if (task.call()) {
                    /** Load and execute *************************************************************************************************/
                    //System.out.println("Yipe");
                    // Create a new custom class loader, pointing to the directory that contains the compiled
                    // classes, this should point to the top of the package structure!
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{new File(mainProjectPath).toURI().toURL()});
                    // Load the class from the classloader by name....
                    Class<?> loadedClass = classLoader.loadClass(String.format("Arena.%s", robotName));
                    // Create a new instance...
                    Object obj = loadedClass.getDeclaredConstructor().newInstance();
                    // Santity check
                    if (obj instanceof Robot) {
                        // Cast to the Robot interface
                        robot = (Robot)obj;
                        // Run it baby
                        robot.run();
                        return robot;
                    }
                    /************************************************************************************************* Load and execute **/
                } else {
                    for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                        System.out.format("Error on line %d in %s%n",
                                diagnostic.getLineNumber(),
                                diagnostic.getSource().toUri());
                    }
                }
                fileManager.close();
            } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException exp) {
                exp.printStackTrace();
            }
        }
        throw new RobotNotLoadedException(String.format("Failed to load robot: %s", robotName));
    }

    public static void main(String[] args) {
        RobotLoader rl = new RobotLoader();
        try {
            Robot rb = rl.load("src/main/java/SampleRobots/Rabbit", "Rabbit");
        } catch (FileNotFoundException | RobotNotLoadedException e) {
            e.printStackTrace();
        }
    }
}
