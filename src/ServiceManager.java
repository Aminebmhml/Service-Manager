import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ServiceManager {
    public static void main(String[] args) {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.startsWith("windows")) {
            String[] serviceNames = {"Assistance IP", "AnyDesk Service"}; // Add names of the services you want to check

            try {
                for (String serviceName : serviceNames) {
                    boolean serviceRunning = isServiceRunning(serviceName);
                    if (serviceRunning) {
                        System.out.println("The service " + serviceName + " is running.");
                    } else {
                        startService(serviceName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isServiceRunning(String serviceName) throws Exception {
        Process p = Runtime.getRuntime().exec("net start");
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String text;
        boolean serviceRunning = false;

        // Check each line for the service name
        while ((text = in.readLine()) != null) {
            if (text.trim().toLowerCase().startsWith(serviceName.toLowerCase())) {
                serviceRunning = true;
                break;
            }
        }
        in.close();

        return serviceRunning;
    }

    private static void startService(String serviceName) throws Exception {
        // Start the service
        Process startProcess = Runtime.getRuntime().exec("net start \"" + serviceName + "\"");
        startProcess.waitFor();

        // Capture error stream
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(startProcess.getErrorStream()));
        String errorText;
        while ((errorText = errorReader.readLine()) != null) {
            System.err.println("Error: " + errorText);
        }
        errorReader.close();

        // Check if service started successfully
        if (startProcess.exitValue() == 0) {
            System.out.println("The service " + serviceName + " was not running and has been started.");
        } else {
            System.out.println("Failed to start the service " + serviceName + ".");
        }
    }
}
