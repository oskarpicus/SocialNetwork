package domain;

import utils.Constants;

import java.time.LocalDateTime;

public class MessageTask extends Task{

    private String mesaj;
    private String from;
    private String to;
    private LocalDateTime date;

    public MessageTask(String taskID, String descriere, String mesaj, String from, String to, LocalDateTime date) {
        super(taskID, descriere);
        this.mesaj = mesaj;
        this.from = from;
        this.to = to;
        this.date = date;
    }

    public void execute(){
        System.out.println(this);
    }

    @Override
    public String toString(){
        return super.toString()+" "+mesaj+" "+to+" "+from+" "+date.format(Constants.DATE_TIME_FORMATTER);
    }
}
