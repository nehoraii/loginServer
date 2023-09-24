package com.example.loginserver.logic;

public class Security {
    private static int numToEncode=11;
    public static String decipherFromClientForUser(String str){
        char[] st=str.toCharArray();
        int key=(st[0]*st[st.length-1])/numToEncode;
        replace(st,1,st.length-2);
        removeAdding(st,key,1,st.length-1);
        return new String(st);
    }
    public static String decipherFromClientForPass(String str){
        char[] st=str.toCharArray();
        int key=(st[0]+st[st.length-1])/numToEncode;
        replace(st,1,st.length-2);
        removeAdding(st,-key,1, st.length-1);
        return new String(st);
    }
    private static void replace(char[] str,int startIndex,int lastIndex){
        char temp;
        for (int i = startIndex; i <lastIndex/2 ; i++) {
            temp=str[i];
            str[i]=str[lastIndex-i+1];
            str[lastIndex-i+1]=temp;
        }
    }
    private static void removeAdding(char[] str,int key,int startIndex,int lastIndex){
        for (int i = startIndex; i < lastIndex; i++) {
            str[i]-=key;
        }
    }
}
