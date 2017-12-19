package jhyun.cbr.storage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@ApiModel("사용자")
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"})
})
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ApiModelProperty(value = "사용자 ID", required = true)
    @Column(name = "username")
    private String username;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @Column(name = "password")
    private String password;

    @ApiModelProperty(value = "사용자ROLE", example = "USER", required = true)
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ApiModelProperty(value = "사용자 활성여부", required = true)
    @Column(name = "active")
    private Boolean active;

    public User() {
    }

    public User(Long id, String username, String password, UserRole role, Boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(id, user.id) &&
                Objects.equal(username, user.username) &&
                Objects.equal(password, user.password) &&
                Objects.equal(active, user.active) &&
                Objects.equal(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, username, password, role, active);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("password", password)
                .add("role", role)
                .add("active", active)
                .toString();
    }
}
