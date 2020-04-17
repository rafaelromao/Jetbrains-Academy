import java.io.Serializable;

class User {
    public static final long serialVersionUID = 1L;

    String name;
    transient String password;
}