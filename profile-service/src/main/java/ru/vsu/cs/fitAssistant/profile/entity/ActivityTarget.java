package ru.vsu.cs.fitAssistant.profile.entity;

public enum ActivityTarget {
    WEIGHT_GAIN,
    WEIGHT_LOSS,
    WEIGHT_MAINTENANCE;



    public static ActivityTarget parseTarget(Integer i) {
        switch(i) {
            case 1 -> {
                return WEIGHT_GAIN;
            }
            case 2 -> {
                return WEIGHT_LOSS;
            }
            default -> {
                return WEIGHT_MAINTENANCE;
            }
        }
    }
}
