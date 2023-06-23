package DesignPattern.Decorator.Toppings;

import DesignPattern.Decorator.Base.BasePizza;

public class ExtraMushroom extends ToppingsDecorator{
    BasePizza basePizza;

    public ExtraMushroom(BasePizza basePizza){
        this.basePizza = basePizza;
    }


    @Override
    public int cost() {
        return this.basePizza.cost() + 30;
    }
}
