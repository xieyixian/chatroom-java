package top.javahai.chatroom.dto;

public class JoinConversationRequest {
    private String username;
    private String otherUsername;

    // Default constructor
    public JoinConversationRequest() {
    }

    // Constructor with parameters
    public JoinConversationRequest(String username, String otherUsername) {
        this.username = username;
        this.otherUsername = otherUsername;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for otherUsername
    public String getOtherUsername() {
        return otherUsername;
    }

    // Setter for otherUsername
    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }
}
