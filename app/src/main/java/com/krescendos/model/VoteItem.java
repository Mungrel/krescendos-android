package com.krescendos.model;

public class VoteItem<T> {

    private Long voteCount;
    private T item;

    public VoteItem() {
    }

    public VoteItem(T item) {
        this.item = item;
        this.voteCount = 0L;
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
