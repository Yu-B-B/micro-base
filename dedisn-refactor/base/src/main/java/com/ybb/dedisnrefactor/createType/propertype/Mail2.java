package com.ybb.dedisnrefactor.createType.propertype;

public class Mail2 implements Cloneable{

    private String from ;

    private String to;

    private String subject;

    private String content;

    public Mail2(Template template){
        this.content = template.getContent();
        this.subject = template.getSubscribe();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public Mail2 clone() {
        Mail2 obj = null;

        try {
            return (Mail2)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
