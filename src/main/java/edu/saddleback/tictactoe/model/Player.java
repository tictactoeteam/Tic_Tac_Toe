package edu.saddleback.tictactoe.model;

import java.util.Objects;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

public class Player {
    private UUID id;
    private String username;
    private String hashedPassword;

    private boolean disabled;

    public Player() {
        this.id = UUID.randomUUID();
        this.username = "";
        this.hashedPassword = "";
        this.disabled = false;
    }

    public Player(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        setPassword(password);
        this.disabled = false;
    }

    public UUID getId() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        // do not allow disabled users to log in
        if (this.disabled) {
            return false;
        }

        return BCrypt.checkpw(password, this.hashedPassword);
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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
