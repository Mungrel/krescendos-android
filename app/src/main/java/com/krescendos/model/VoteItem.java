package com.krescendos.model;

public class VoteItem<T> {

    private String dbKey;
    private Long voteCount;
    private T item;

    public VoteItem() {
    }

    public VoteItem(T item) {
        this.item = item;
        this.voteCount = 0L;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
