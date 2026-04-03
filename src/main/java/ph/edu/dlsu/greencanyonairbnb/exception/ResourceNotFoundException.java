package ph.edu.dlsu.greencanyonairbnb.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String messages) {
        super(messages);
    }
}
