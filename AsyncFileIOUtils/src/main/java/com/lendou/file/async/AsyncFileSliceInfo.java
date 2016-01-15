package com.lendou.file.async;

/**
 * 每个文件切片信息
 */
class AsyncFileSliceInfo {

    private long left;
    private long cur;
    private long right;

    public AsyncFileSliceInfo(long left, long right) {
        this.left = left;
        this.right = right;
    }

    public long getLeft() {
        return left;
    }

    public long getCur() {
        return cur;
    }

    public long getRight() {
        return right;
    }

    public void setCur(long cur) {
        this.cur = cur;
    }

    public boolean isCompleted() {
        return cur == right;
    }
}
