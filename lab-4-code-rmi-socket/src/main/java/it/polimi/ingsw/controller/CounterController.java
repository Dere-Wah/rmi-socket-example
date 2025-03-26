package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.CounterModel;

public class CounterController {

    final CounterModel model;

    public CounterController() {
        this.model = new CounterModel();
    }

    public void add(Integer number) {
        synchronized (this.model) {
            this.model.add(number);
        }
    }

    public boolean reset() {
        synchronized (this.model) {
            if (this.model.getState() == 0) {
                return false;
            } else {
                this.model.add(-this.model.getState());
                return true;
            }
        }
    }

    public Integer getState() {
        return model.getState();
    }

}
