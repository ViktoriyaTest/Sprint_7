package org.example;

public class CourierLogIn {
    private String login;
    private String password;

    private CourierLogIn(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierLogIn from(Courier courier) {
        return new CourierLogIn(courier.getLogin(), courier.getPassword());
    }

    public static CourierLogIn noLoginFrom(Courier courier) {
        return new CourierLogIn("", courier.getPassword());
    }

    public static CourierLogIn noPasswordFrom(Courier courier) {
        return new CourierLogIn(courier.getLogin(), "");
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}