package ph.edu.dlsu.greencanyonairbnb.exception;

public class RoleAlreadyExistException extends RuntimeException {
  public RoleAlreadyExistException(String message) {
    super(message);
  }
}
