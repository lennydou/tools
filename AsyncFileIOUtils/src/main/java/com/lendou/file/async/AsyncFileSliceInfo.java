package com.lendou.file.async;

/**
 * 每个文件切片信息
 */
class AsyncFileSliceInfo {

    private int left;
    private int cur;
    private int right;

    public AsyncFileSliceInfo(int left, int right) {
        this.left = left;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public int getCur() {
        return cur;
    }

    public int getRight() {
        return right;
    }

    public void setCur(int cur) {
        this.cur = cur;
    }

    public boolean isCompleted() {
        return cur == right;
    }
}
