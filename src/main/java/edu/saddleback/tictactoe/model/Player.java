package edu.saddleback.tictactoe.model;

import java.util.Objects;

//import org.mindrot.BCrypt;

public class Player {
    private String id;
    private String username;
    private String hashedPassword;

    public Player() {
        this.id = "";
        this.username = "";
        this.hashedPassword = "";
    }

    public Player(String username, String password) {
        this.username = username;
        setPassword(password);
    }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return true;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {

    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(username, player.username) &&
                Objects.equals(hashedPassword, player.hashedPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, hashedPassword);
    }
}
