package nure.ua.mediaclient.model;

public class UserDTO {

    private String email;
    private String password;
    private String name;
    private String surname;
    private String coutry;

    public UserDTO() {
    }

    public UserDTO(String email, String password, String name, String surname, String coutry) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.coutry = coutry;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCoutry() {
        return coutry;
    }

    public void setCoutry(String coutry) {
        this.coutry = coutry;
    }
}
