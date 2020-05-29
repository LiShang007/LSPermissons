package com.lishang.permissions.request;

public interface Request {
    void execute();

    void cancel();

    void setting();
}
