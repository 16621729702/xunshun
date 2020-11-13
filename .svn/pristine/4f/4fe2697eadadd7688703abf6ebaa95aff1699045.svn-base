package com.wink.livemall.live.dto;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="lm_live_good")
public class LmLiveGood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "JDBC")
    private int id;
    private int liveid;
    private int good_id;

    public LmLiveGood() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLiveid() {
        return liveid;
    }

    public void setLiveid(int liveid) {
        this.liveid = liveid;
    }

    public int getGood_id() {
        return good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }
}
