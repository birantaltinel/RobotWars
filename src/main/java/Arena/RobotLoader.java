package Arena;

import Arena.Exceptions.RobotNotLoadedException;
import net.openhft.compiler.CompilerUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class RobotLoader {
    /**
     * Compiles and loads the robot file at the specified destination, written in the custom robot language into its own thread in the memory.
     * @param robotFilePath the path to the file that describes the robot to be loaded.
     * @returns the created Robot object.
     */
    public Robot load(String robotFilePath) throws RobotNotLoadedException {
        Path path = Paths.get(robotFilePath);
        String robotName = StringUtils.capitalize(path.getFileName().toString().split("\\.")[0]);
        try {
            String robotFile = new Scanner(new File(robotFilePath)).useDelimiter("\\Z").next();

            String className = String.format("Arena.%s", robotName);

            StringBuilder sb = new StringBuilder();
            sb.append("package Arena;\n");
            sb.append(String.format("public class %s extends Robot implements Runnable {\n", robotName));
            sb.append("    public void run() {\n");
            sb.append(robotFile);
            sb.append("    }\n");
            sb.append("}\n");
            String javaCode = sb.toString();

            Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
            return (Robot) aClass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | FileNotFoundException e) {
            e.printStackTrace();
        }
        throw new RobotNotLoadedException(String.format("Failed to load robot: %s", robotName));
    }

    public static void main(String[] args) {
        RobotLoader rl = new RobotLoader();
        try{
            Robot rabbit = rl.load("src/main/java/SampleRobots/Rabbit.robot");
            rabbit.run();
        } catch (RobotNotLoadedException e) {
            e.printStackTrace();
        }
    }
}
