package ph.edu.dlsu.greencanyonairbnb.model.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String messages) {
        super(messages);
    }
}
