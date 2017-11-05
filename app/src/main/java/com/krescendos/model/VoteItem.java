package com.krescendos.model;

public class VoteItem<T> {

    private String itemId;
    private Long voteCount;
    private T item;

    public VoteItem() {
    }

    public VoteItem(String itemId, T item) {
        this.itemId = itemId;
        this.voteCount = 0L;
        this.item = item;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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
