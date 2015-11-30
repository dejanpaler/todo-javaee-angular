package com.todoapp.logging;

public interface Log {

  void info(String msg);

  void info(String msg, Object param);

  void info(String msg, Object... params);

}
