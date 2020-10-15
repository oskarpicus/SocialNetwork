package com.company;

import domain.MessageTask;
import runners.*;
import utils.Strategy;

import java.time.LocalDateTime;

public class TestStrategyTaskRunner {

    public static MessageTask[] createMessageTaskArray(){
        MessageTask t1=new MessageTask("1","Feedback lab1",
                "Ai obtinut 9.60","Gigi", "Ana", LocalDateTime.now());
        MessageTask t2=new MessageTask("2","Feedback lab1",
                "Ai obtinut 5.60","Gigi", "Ana", LocalDateTime.now());
        MessageTask t3=new MessageTask("3","Feedback lab3",
                "Ai obtinut 10","Gigi", "Ana", LocalDateTime.now());
        return new MessageTask[]{t1,t2,t3};
    }

    public static Strategy getStrategy(String str){
        switch (str){
            case "FIFO"->{
                return Strategy.FIFO;
            }
            case "LIFO"->{
                return Strategy.LIFO;
            }
            default ->{
                return null;
            }
        }
    }

    public static void testStrategyTaskRunner(String str){
        StrategyTaskRunner s=new StrategyTaskRunner(getStrategy(str));
        var v=createMessageTaskArray();
        for(var e : v)
            s.addTask(e);
        s.executeAll();
    }

    public static void main(String[] args) {
        testStrategyTaskRunner(args[0]);
    }
}
