package com.evilflora.warframesentinel.Modele;

public class MinMaxForValue {
    public static float valueOf(float value, float min, float max){
        return (value <= min ? min : value >= max ? max : value);
    }
}