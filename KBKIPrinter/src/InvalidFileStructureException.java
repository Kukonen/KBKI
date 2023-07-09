public class InvalidFileStructureException extends Exception {
    String message;

    InvalidFileStructureException(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Decompressing is failed. " + message;
    }
}
