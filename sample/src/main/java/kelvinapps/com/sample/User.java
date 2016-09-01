package kelvinapps.com.sample;

/**
 * Created by Nick Moskalenko on 17/05/2016.
 */
public class User {

    private String birthYear;
    private String fullName;

    public User(String birthYear, String fullName) {
        this.birthYear = birthYear;
        this.fullName = fullName;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    @Override
    public String toString() {
        return "User{" +
                "birthYear='" + birthYear + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
