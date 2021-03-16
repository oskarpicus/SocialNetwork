package com.company;

import containers.QueueContainer;
import containers.StackContainer;
import domain.MessageTask;

import java.time.LocalDateTime;

public class TestContainers {

    public static MessageTask[] createMessageTaskArray(){
        MessageTask t1=new MessageTask("1","Feedback lab1",
                "Ai obtinut 9.60","Gigi", "Ana", LocalDateTime.now());
        MessageTask t2=new MessageTask("2","Feedback lab1",
                "Ai obtinut 5.60","Gigi", "Ana", LocalDateTime.now());
        MessageTask t3=new MessageTask("3","Feedback lab3",
                "Ai obtinut 10","Gigi", "Ana", LocalDateTime.now());
        return new MessageTask[]{t1,t2,t3};
    }

    public static void testStack(){
        StackContainer s=new StackContainer();
        var v=createMessageTaskArray();
        for(MessageTask m : v)
            s.add(m);
        while(!s.isEmpty())
            System.out.println(s.remove());
    }

    public static void testQueue(){
        QueueContainer q=new QueueContainer();
        var v=createMessageTaskArray();
        for(MessageTask m : v)
            q.add(m);
        while(!q.isEmpty())
            System.out.println(q.remove());
    }

    public static void main(String[] args) {
        testStack();
        System.out.println(" ");
        testQueue();
    }
}
