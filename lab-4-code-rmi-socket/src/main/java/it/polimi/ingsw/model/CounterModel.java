package it.polimi.ingsw.model;

public class CounterModel {
    private Integer state = 0;

    public void add(Integer number) {
        this.state += number;
    }

    public Integer getState() {
        return this.state;
    }
}