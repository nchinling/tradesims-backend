package tradesims.project.tradesimsbackend.models;

public class Account {
    private String accountId;
    private String name;
    private String username;
    private String email;
    private String password;
    private Double cash;
    
    public Account() {
    }

    public Account(String accountId, String name, String username, String email, String password) {
        this.accountId = accountId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    

    public Account(String accountId, String name, String username, String email, String password, Double cash) {
        this.accountId = accountId;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.cash = cash;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return "Account [accountId=" + accountId + ", name=" + name + ", username=" + username + ", email=" + email
                + ", password=" + password + ", cash=" + cash + "]";
    }

    
}
