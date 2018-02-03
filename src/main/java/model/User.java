package model;

/**
 * @author youyinnn
 */
public class User {

    private Integer id;
    private Long username;
    private String password;
    private String nickname;
    private String gender;
    private String socialAccount;
    private String info;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username=" + username +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", socialAccount='" + socialAccount + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUsername() {
        return username;
    }

    public void setUsername(Long username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSocialAccount() {
        return socialAccount;
    }

    public void setSocialAccount(String socialAccount) {
        this.socialAccount = socialAccount;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
