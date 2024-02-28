package fpt.fall23.onlearn.dto.firebase;

import java.util.List;

public class SubscriptionRequestDto {
    String topicName;
    List<String> tokens;
    public SubscriptionRequestDto(String topicName, List<String> tokens) {
        super();
        this.topicName = topicName;
        this.tokens = tokens;
    }


    public SubscriptionRequestDto() {
        // TODO Auto-generated constructor stub
    }


    public String getTopicName() {
        return topicName;
    }


    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }


    public List<String> getTokens() {
        return tokens;
    }


    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }


}