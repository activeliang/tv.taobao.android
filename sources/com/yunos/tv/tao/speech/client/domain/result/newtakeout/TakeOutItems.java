package com.yunos.tv.tao.speech.client.domain.result.newtakeout;

import java.util.List;

public class TakeOutItems {
    private List<Foods> foods;
    private Restaurant restaurant;

    public void setFoods(List<Foods> foods2) {
        this.foods = foods2;
    }

    public List<Foods> getFoods() {
        return this.foods;
    }

    public void setRestaurant(Restaurant restaurant2) {
        this.restaurant = restaurant2;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }
}
