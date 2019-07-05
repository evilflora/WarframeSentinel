package com.evilflora.warframesentinel.Utils;

public class MinMaxForValue {

    private MinMaxForValue() {
        // nothing, because camel are pink
    }

    public static float valueOf(float value){
        if (value <= 0F) {
            return  0F;
        } else if (value >= 100F) {
            return 100F;
        } else {
            return value;
        }
    }
}