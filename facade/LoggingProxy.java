package facade;

public class LoggingProxy {
    public void logAction(String source, String message) {
        System.out.printf("log: %s, %s%n", source, message);
    }

    public void logResult(String source, String result) {
        System.out.printf("log: %s result: %s%n", source, result);
    }

    public void logError(String source, String error) {
        System.out.printf("log: %s error: %s%n", source, error);
    }
}
