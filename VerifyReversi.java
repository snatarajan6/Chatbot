import java.io.*;

/**
 * Created by sanjay on 10/30/17.
 */
public class VerifyReversi
{
    private static final String inputFileName = "TestCases.txt";
    private static final String outputFileName = "Result.txt";
    private static final String classPath = "./src/ -cp ";

    private static FileWriter fileWriter = null;
    private static PrintWriter pw = null;
    private static BufferedReader br = null;

    private static void runProcess(String command) throws Exception
    {
        Process process = Runtime.getRuntime().exec(command);
        printLines(command, process.getInputStream());
        process.waitFor();
    }

    private static void printLines(String command, InputStream ins) throws Exception
    {
        String output;

        if (fileWriter == null)
        {
            fileWriter = new FileWriter(outputFileName);
        }
        if (pw == null)
        {
            pw = new PrintWriter(fileWriter);
        }

        try
        {
            br = new BufferedReader(new InputStreamReader(ins));
            command = command.replace(classPath, "");
            pw.println(command);
            while ((output = br.readLine()) != null)
            {
                pw.println(output.trim());
            }
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            if (br != null)
            {
                br.close();
            }
        }
    }

    public static void main(String[] args)
    {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;

        try
        {
            fileReader = new FileReader(inputFileName);
            bufferedReader = new BufferedReader(fileReader);
            String command;

            while ((command = bufferedReader.readLine()) != null)
            {
                if (command.startsWith("java"))
                {
                    command = command.replace("java ", "java " + classPath);
                    try
                    {
                        runProcess(command);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            try
            {
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
                if (br != null)
                {
                    br.close();
                }
                if (fileReader != null)
                {
                    fileReader.close();
                }
                if (fileWriter != null)
                {
                    fileWriter.close();
                }
                if (pw != null)
                {
                    pw.close();
                }
            }
            catch (IOException ioException)
            {
                ioException.printStackTrace();
            }
        }
    }
}
