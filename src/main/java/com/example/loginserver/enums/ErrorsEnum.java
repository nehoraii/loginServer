package com.example.loginserver.enums;
//מייצג את המצב כלומר האם השמירה הצליחה האם יש בעיה ואם כן מה היא
public enum ErrorsEnum {
    URL_ERROR,
    OPEN_CONNECTION_ERROR,
    ELSE_ERROR,
    READ_ERROR,
    GOOD,
    USER_NOT_FOUND_ERROR,
    NOT_SMALL_CAPS_ERROR,
    NOT_BIG_CAPS_ERROR,
    LENGTH_ERROR,
    THERE_IS_NUMBER,
    BIRTH_DAY_ERROR,
    USER_EXIST_ERROR,
    NOT_CHANGED,
    BLOCK,
    SPAM,
    WRONG_PASS_ERROR,
    NOT_FOUND,
    SECRET_CODE_ERROR,
    LENGTH_ERROR_PASSWORD,
    NOT_BIG_CHAR,
    NOT_SMALL_CHAR,
    NOT_NUMBER,
    THE_SAME_PASSWORD,
    PAST_USE,
    SEND_GMAIL_ERROR
}
