package interfaces;

/**
 * Creation of authentication CONTRACT
 * --
 * Defines two main methods that
 * Classes that implements this INTERFACE
 * MUST define each one.
 * --
 * Is design for be used by the systems users.
 */
public interface Autenticable {

    boolean logIn();
    void logOut();
}
