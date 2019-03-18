package com.evilflora.warframesentinel;

class MinMaxForValue {
    static float valueOf(float value, float min, float max){
        return (value <= min ? min : value >= max ? max : value);
    }
}