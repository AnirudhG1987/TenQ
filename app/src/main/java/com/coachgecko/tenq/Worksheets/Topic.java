package com.coachgecko.tenq.Worksheets;

import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by Anirudh on 2/5/2017.
 */
@Data

public class Topic {

    private String key;
    private String topicName;

    public Topic() {
    }

    @Builder
    public Topic(String topicName) {
        this.topicName = topicName;
    }

}
