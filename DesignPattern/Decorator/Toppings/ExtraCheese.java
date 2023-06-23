package DesignPattern.Decorator.Toppings;

import DesignPattern.Decorator.Base.BasePizza;

public class ExtraCheese extends ToppingsDecorator{

    BasePizza basePizza;

    public ExtraCheese(BasePizza basePizza) {
        this.basePizza = basePizza;
    }

    @Override
    public int cost() {
        return this.basePizza.cost() + 50;
    }
}
