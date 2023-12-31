package com.coding.crab.temp;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class Temp {

    private int number;

    public void method(){

    }

    public static void main(String[] args) {
        Temp temp = new Temp();
        int number1 = temp.getNumber();

        System.out.println("number1 = " + number1);

        log.info("number1 = {}", number1);
    }
}
