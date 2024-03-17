package com.example.signproject.ViewModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class setActionScoresViewModel {
    private int team;
    private String competition;
    private Object actions_scores;

    public String getActions_scoresAsString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(actions_scores);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
