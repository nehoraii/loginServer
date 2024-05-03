package com.example.loginserver.logic;

import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.vo.UserVO;
//מחלקה שאחראית על ההצפנות בשרת זה
public class Security {
    /*
    מקבלת: את השם של המשתמש.
    מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת לקליינט.
    מחזירה: מחזירה את המפתח.
    */
    private static int getPersonalNameKeyClient(String name){
        char[] str=name.toCharArray();
        int sum=(str[0]+str[str.length-1]);
        return sum/13;
    }

    /*
    מקבלת: את השם משפחה של המשתמש.
    מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת לקליינט.
    מחזירה: מחזירה את המפתח.
    */
    private static int getSecNameKeyClient(String secName){
        char[] str=secName.toCharArray();
        int sum=(str[0]+str[str.length-1]);
        return sum/5;
    }

    /*
    מקבלת: את כתובת המייל של המשתמש.
    מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת לקליינט.
    מחזירה: מחזירה את המפתח.
    */
    private static int getEmailKeyClient(String email){
        char[] str=email.toCharArray();
        int sum=(str[0]-str[str.length-1]);
        return sum/7;
    }
    /*
    מקבלת: את מספר הפלאפון של המשתמש.
מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת לקליינט.
    מחזירה: מחזירה את המפתח.
    */
    private static int getPhoneNumKeyClient(String phone){
        char[] str=phone.toCharArray();
        int sum=(str[0]*str[str.length-1]);
        return sum/13;
    }

    /*
    מקבלת: את "שם המשתמש" של המשתמש.
מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת לקליינט.
    מחזירה: מחזירה את המפתח.
    */
    private static int getUserNameKeyClient(String userName){
        char[] str=userName.toCharArray();
        int sum=(str[0]*str[str.length-1]);
        return sum/11;
    }
    /*
    מקבלת: את הסיסמה של המשתמש.
מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת לקליינט.
    מחזירה: מחזירה את המפתח.
     */
    private static int getPassKeyClient(String pass){
        char[] str=pass.toCharArray();
        int sum=(str[0]+str[str.length-1]);
        return sum/11;
    }
    /*
    מקבלת: את הקוד הסודי של המשתמש.
מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת לקליינט.
מחזירה: מחזירה את המפתח.
    */
    private static int getCodeKeyClient(String secretKey){
        char[] str=secretKey.toCharArray();
        System.out.println("0: "+str[0]+" last: "+str[str.length-1]);
        int sum=(str[0]+str[str.length-1]);
        return sum/13;
    }
    /*
    מקבלת: את המפתח הסודי של המשתמש.
    מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת לקליינט.
    מחזירה: מחזירה את המפתח.
    */
    private static int getSecretKeyKeyClient(String secretKey){
        char[] str=secretKey.toCharArray();
        int sum=(str[0]+str[str.length-1]);
        return sum/19;
    }

    /*
    מקבלת: את השם של המשתמש.
    מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת למסד הנתונים.
    מחזירה: מחזירה את המפתח.
     */
    private static int getPersonalNameKeyDB(String name){
        char[] str=name.toCharArray();
        int sum=(str[0]*str[str.length-1]);
        return sum/13;
    }
    /*
    מקבלת: את השם משפחה של המשתמש.
מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת למסד הנתונים.
    מחזירה: מחזירה את המפתח.
    */
    private static int getSecNameKeyDB(String secName){
        char[] str=secName.toCharArray();
        int sum=(str[0]+str[str.length-1]);
        return sum/7;
    }
    /*
    מקבלת: את כתובת המייל של המשתמש.
    מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת למסד הנתונים.
    מחזירה: מחזירה את המפתח.
    */
    private static int getEmailKeyDB(String email){
        char[] str=email.toCharArray();
        int sum=(str[0]*str[str.length-1]);
        return sum/9;
    }

    /*
    מקבלת: את מספר הפלאפון של המשתמש.
מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת למסד הנתונים.
    מחזירה: מחזירה את המפתח.
    */
    private static int getPhoneNumKeyDB(String phone){
        char[] str=phone.toCharArray();
        int sum=(str[0]+str[str.length-1]);
        return sum/11;
    }

    /*
    מקבלת: את ה- "שם משתמש" של המשתמש.
    מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת למסד הנתונים.
מחזירה: מחזירה את המפתח.
    */
    private static int getUserNameKeyDB(String userName){
        char[] str=userName.toCharArray();
        int sum=(str[0]*str[str.length-1]);
        return sum/9;
    }

    /*
    מקבלת: את הסיסמה של המשתמש.
    מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת למסד הנתונים.
    מחזירה: מחזירה את המפתח.
    */
    private static int getPassKeyDB(String pass){
        char[] str=pass.toCharArray();
        int sum=(str[0]+str[str.length-1]);
        return sum/13;
    }

    /*
    מקבלת: את המפתח הסודי של המשתמש.
מבצעת: מבצעת חישוב על ידיד שימוש בקלט ובנוסחה מתמטית על מנת להפיק את המפתח אשר ישמש להצפנה בין השרת למסד הנתונים.
    מחזירה: מחזירה את המפתח.
    */
    private static int getSecretKeyKeyDB(String secretKey){
        char[] str=secretKey.toCharArray();
        int sum=(str[0]+str[str.length-1]);
        return sum/17;
    }

    /*public static String encodeToClientForPass(String pass){
        char[] st=pass.toCharArray();
        int key=getPassKeyClient(pass);
        removeAdding(st,1,st.length-1,key);
        replace(st,1,st.length-1);
        return new String(st);
    }

     */

    /*
    מקבלת: את הסיסמה של המשתמש.
    מבצעת: מפענחת את הסיסמה אשר קיבלה מהמשתמש.
    מחזירה: מחזירה את הסיסמה המפוענחת.
    */
    public static String decipherFromClientForPass(String pass){
        char[] st=pass.toCharArray();
        int key=getPassKeyClient(pass);
        adding(st,1,st.length-1,key);
        replace(st,1,st.length-1);
        return new String(st);
    }

    /*
    מקבלת: את האובייקט UserEntity שמייצג את המשתמש.
    מבצעת: מצפינה את נתוני המשתמש למסד הנתונים.
    מחזירה: כלום.
    */
    public static void encodeUserObjectToDB(UserEntity userEntity){
        String ans;
        if(userEntity.getUserName()!=null){
            String userName=userEntity.getUserName();
            userName=userName(userName,getUserNameKeyDB(userName),1,userName.length()-1,true);
            userEntity.setUserName(userName);
        }
        if(userEntity.getName()!=null){
            String personalName=userEntity.getName();
            ans=personalName(personalName,getPersonalNameKeyDB(personalName),1,personalName.length()-1,true);
            userEntity.setName(ans);
        }
        if(userEntity.getSecName()!=null){
            String secName=userEntity.getSecName();
            ans=secName(secName,getSecNameKeyDB(secName),1,secName.length()-1,true);
            userEntity.setSecName(ans);
        }
        if(userEntity.getEmail()!=null){
            String email=userEntity.getEmail();
            ans=email(email,getEmailKeyDB(email),1,email.length()-1,true);
            userEntity.setEmail(ans);
        }
        if(userEntity.getPhone()!=null){
            String phoneNum=userEntity.getPhone();
            ans=phoneNumber(phoneNum,getPhoneNumKeyDB(phoneNum),1,phoneNum.length()-1,true);
            userEntity.setPhone(ans);
        }
        if(userEntity.getSecretKey()!=null){
            String secretKey=userEntity.getSecretKey();
            ans=secretKey(secretKey,getSecretKeyKeyDB(secretKey),1,secretKey.length()-1,true);
            userEntity.setSecretKey(ans);
        }
    }

    /*
    מקבלת: את האובייקט UserEntity שמייצג את המשתמש.
    מבצעת: מפענחת את נתוני המשתמש ממסד הנתונים.
    מחזירה: כלום.

    */
    public static void decipherUserObjectFromDB(UserEntity userEntity){
        String ans;
        if(userEntity.getUserName()!=null){
            String userName=userEntity.getUserName();
            userName=userName(userName,getUserNameKeyDB(userName),1,userName.length()-1,false);
            userEntity.setUserName(userName);
        }
        if(userEntity.getName()!=null){
            String personalName=userEntity.getName();
            ans=personalName(personalName,getPersonalNameKeyDB(personalName),1,personalName.length()-1,false);
            userEntity.setName(ans);
        }
        if(userEntity.getSecName()!=null){
            String secName=userEntity.getSecName();
            ans=secName(secName,getSecNameKeyDB(secName),1,secName.length()-1,false);
            userEntity.setSecName(ans);
        }
        if(userEntity.getEmail()!=null){
            String email=userEntity.getEmail();
            ans=email(email,getEmailKeyDB(email),1,email.length()-1,false);
            userEntity.setEmail(ans);
        }
        if(userEntity.getPhone()!=null){
            String phoneNum=userEntity.getPhone();
            ans=phoneNumber(phoneNum,getPhoneNumKeyDB(phoneNum),1,phoneNum.length()-1,false);
            userEntity.setPhone(ans);
        }
        if(userEntity.getSecretKey()!=null){
            String secretKey=userEntity.getSecretKey();
            ans=secretKey(secretKey,getSecretKeyKeyDB(secretKey),1,secretKey.length()-1,false);
            userEntity.setSecretKey(ans);
        }
    }

    /*
    מקבלת: את האובייקט UserVo  שמייצג את המשתמש.
    מבצעת: מצפינה את נתוני המשתמש לקליינט.
    מחזירה: כלום.
     */
    public static void encodeUserObjectToClient(UserVO userVoPlusCode){
        String ans;
        if(userVoPlusCode.getUserName()!=null){
            String userName=userVoPlusCode.getUserName();
            userName=userName(userName,getUserNameKeyClient(userName),1,userName.length()-1,true);
            userVoPlusCode.setUserName(userName);
        }
        if(userVoPlusCode.getName()!=null){
            String personalName=userVoPlusCode.getName();
            ans=personalName(personalName,getPersonalNameKeyClient(personalName),1,personalName.length()-1,true);
            userVoPlusCode.setName(ans);
        }
        if(userVoPlusCode.getSecName()!=null){
            String secName=userVoPlusCode.getSecName();
            ans=secName(secName,getSecNameKeyClient(secName),1,secName.length()-1,true);
            userVoPlusCode.setSecName(ans);
        }
        if(userVoPlusCode.getEmail()!=null){
            String email=userVoPlusCode.getEmail();
            ans=email(email,getEmailKeyClient(email),1,email.length()-1,true);
            userVoPlusCode.setEmail(ans);
        }
        if(userVoPlusCode.getPhone()!=null){
            String phoneNum=userVoPlusCode.getPhone();
            ans=phoneNumber(phoneNum,getPhoneNumKeyClient(phoneNum),1,phoneNum.length()-1,true);
            userVoPlusCode.setPhone(ans);
        }
        if(userVoPlusCode.getCode()!=null){
            String code=userVoPlusCode.getCode();
            ans=code(code,getCodeKeyClient(code),1,code.length()-1,true);
            userVoPlusCode.setCode(ans);
        }
        if(userVoPlusCode.getSecretKey()!=null){
            String secretKey=userVoPlusCode.getSecretKey();
            ans=secretKey(secretKey,getSecretKeyKeyClient(secretKey),1,secretKey.length()-1,true);
            userVoPlusCode.setSecretKey(ans);
        }

    }
    /*
    מקבלת: את האובייקט UserVo   שמייצג את המשתמש.
    מבצעת: מפענחת את נתוני המשתמש מהקליינט.
    מחזירה: כלום.

    */
    public static void decipherUserObjectFromClient(UserVO userVO){
        String ans;
        if(userVO.getUserName()!=null){
            String userName=userVO.getUserName();
            userName=userName(userName,getUserNameKeyClient(userName),1,userName.length()-1,false);
            userVO.setUserName(userName);
        }
        if(userVO.getName()!=null){
            String personalName=userVO.getName();
            ans=personalName(personalName,getPersonalNameKeyClient(personalName),1,personalName.length()-1,false);
            userVO.setName(ans);
        }
        if(userVO.getSecName()!=null){
            String secName=userVO.getSecName();
            ans=secName(secName,getSecNameKeyClient(secName),1,secName.length()-1,false);
            userVO.setSecName(ans);
        }
        if(userVO.getEmail()!=null){
            String email=userVO.getEmail();
            int key=getEmailKeyClient(email);
            ans=email(email,key,1,email.length()-1,false);
            userVO.setEmail(ans);
        }
        if(userVO.getPhone()!=null){
            String phoneNum=userVO.getPhone();
            ans=phoneNumber(phoneNum,getPhoneNumKeyClient(phoneNum),1,phoneNum.length()-1,false);
            userVO.setPhone(ans);
        }
    }

    /*
    מקבלת: סיסמה.
    מבצעת: מצפינה למסד הנתונים.
    מחזירה: סיסמה מוצפנת.
    */
    public static String encodeToDBPass(String password){
        String pass;
        pass=password(password,getPassKeyDB(password),1,password.length()-1,true);
        return pass;
    }

    /*
    מקבלת: סיסמה מוצפנת.
    מבצעת: מפענחת מהמסד הנתונים.
    מחזירה: סיסמה מפוענחת.

    */
    public static String decipherFromDBPass(String password){
        String pass;
        pass=password(password,getPassKeyDB(password),1,password.length()-1,false);
        return pass;
    }

    /*
    מקבלת: מערך של תווים, אינדקס התחלתי, אינדקס סופי, מפתח.
    מבצעת: מוסיפה לכל התווים במערך בטווח שבין האינדקס הראשון לאחרון לא כולל את המפתח.
    מחזירה: כלום.

    */
    private static void adding(char[] str,int startIndex,int lastIndex,int key){
        for (int i = startIndex; i < lastIndex; i++) {
            str[i]+=key;
        }
    }

    /*מקבלת: מערך של תווים, אינדקס התחלתי, אינדקס סופי.
    מבצעת: מחליפה בצורת מראה את התווים במערך בין הטווח של האינדקס הראשון לאחרון.
מחזירה: כלום.
     */
    private static void replace(char[] str,int startIndex,int lastIndex){
        char temp;
        for (int i = startIndex; i <lastIndex/2 ; i++) {
            temp=str[i];
            str[i]=str[lastIndex-i];
            str[lastIndex-i]=temp;
        }
    }

    /*
    מקבלת: מערך של תווים, אינדקס התחלתי, אינדקס סופי, מפתח.
    מבצעת: מחסירה לכל התווים במערך בטווח שבין האינדקס הראשון לאחרון לא כולל את המפתח.
    מחזירה: כלום.
     */
    private static void removeAdding(char[] str,int startIndex,int lastIndex,int key){
        for (int i = startIndex; i < lastIndex; i++) {
            str[i]-=key;
        }
    }

    /*
    מקבלת: השם של המשתמש, אינדקס התחלתי, אינדקס סופי, להצפין א לפענח.
    מבצעת: קוראת לפונקציות על פי מה שהוגדר מראש אם זה הצפנה, ואם זה פיענוח קוראת לפונקציות ההפוכה
    מחזירה: את השם המוצפן/ המפוענח.
     */
    private static String personalName(String str,int key,int startIndex,int lastIndex,boolean encode){
        char []st=str.toCharArray();
        if(encode){
            adding(st,startIndex,lastIndex,key);
            return new String(st);
        }
        else{
            removeAdding(st,startIndex,lastIndex,key);
        }
        return new String(st);
    }
    /*
    מקבלת: השם השני של המשתמש, אינדקס התחלתי, אינדקס סופי, להצפין א לפענח.
    מבצעת: קוראת לפונקציות על פי מה שהוגדר מראש אם זה הצפנה, ואם זה פיענוח קוראת לפונקציות ההפוכה
    מחזירה: את השם השני המוצפן/ המפוענח.

     */
    private static String secName(String str,int key,int startIndex,int lastIndex,boolean encode){
        char []st=str.toCharArray();
        if(encode){
            removeAdding(st,startIndex,lastIndex,key);
            return new String(st);
        }
        else{
            adding(st,startIndex,lastIndex,key);
        }
        return new String(st);
    }
    /*
    מקבלת: כתובת המייל של המשתמש, אינדקס התחלתי, אינדקס סופי, להצפין א לפענח.
    מבצעת: קוראת לפונקציות על פי מה שהוגדר מראש אם זה הצפנה, ואם זה פיענוח קוראת לפונקציות ההפוכה
    מחזירה: את כתובת המייל של המשתמש המוצפן/ המפוענח.
    */
    private static String email(String str,int key,int startIndex,int lastIndex,boolean encode){
        char[] st=str.toCharArray();
        if(encode){
            adding(st,startIndex,lastIndex,key);
            replace(st,startIndex,lastIndex);
            return new String(st);
        }
        removeAdding(st,startIndex,lastIndex,key);
        replace(st,startIndex,lastIndex);
        return new String(st);
    }

    /*
    מקבלת: מספר הפלאפון של המשתמש, אינדקס התחלתי, אינדקס סופי, להצפין א לפענח.
    מבצעת: קוראת לפונקציות על פי מה שהוגדר מראש אם זה הצפנה, ואם זה פיענוח קוראת לפונקציות ההפוכה
    מחזירה: את מספר הפלאפון המוצפן/ המפוענח.
    */
    private static String phoneNumber(String str,int key,int startIndex,int lastIndex,boolean encode){
        char[] st=str.toCharArray();
        if(encode){
            adding(st,startIndex,lastIndex,key);
            replace(st,startIndex,lastIndex);
            return new String(st);
        }
        removeAdding(st,startIndex,lastIndex,key);
        replace(st,startIndex,lastIndex);
        return new String(st);
    }

    /*
    מקבלת: ה- "שם משתמש"  של המשתמש, אינדקס התחלתי, אינדקס סופי, להצפין א לפענח.
    מבצעת: קוראת לפונקציות על פי מה שהוגדר מראש אם זה הצפנה, ואם זה פיענוח קוראת לפונקציות ההפוכה
    מחזירה: את ה- "שם משתמש" המוצפן/ המפוענח.
    */
    private static String userName(String str,int key,int startIndex,int lastIndex ,boolean encode) {
        char[] st=str.toCharArray();
        if(encode){
            adding(st,startIndex,lastIndex,key);
            replace(st,startIndex,lastIndex);
            return new String(st);
        }
        removeAdding(st,startIndex,lastIndex,key);
        replace(st,startIndex,lastIndex);
        return new String(st);
    }

    /*
    מקבלת: המפתח הסודי של המשתמש, אינדקס התחלתי, אינדקס סופי, להצפין א לפענח.
    מבצעת: קוראת לפונקציות על פי מה שהוגדר מראש אם זה הצפנה, ואם זה פיענוח קוראת לפונקציות ההפוכה
    מחזירה: את המפתח הסודי המוצפן/ המפוענח.

    */
    private static String secretKey(String str,int key,int startIndex,int lastIndex ,boolean encode) {
        char[] st=str.toCharArray();
        if(encode){
            adding(st,startIndex,lastIndex,key);
            return new String(st);
        }
        removeAdding(st,startIndex,lastIndex,key);
        return new String(st);
    }

    /*
    מקבלת: הקוד הסודי של המשתמש, אינדקס התחלתי, אינדקס סופי, להצפין א לפענח.
    מבצעת: קוראת לפונקציות על פי מה שהוגדר מראש אם זה הצפנה, ואם זה פיענוח קוראת לפונקציות ההפוכה
    מחזירה: את הקוד הסודי המוצפן/ המפוענח.

    */
    private static String code(String str,int key,int startIndex,int lastIndex,boolean encode){
        char []st=str.toCharArray();
        System.out.println("key Code"+key);
        if(encode){
            adding(st,startIndex,lastIndex,key);
            return new String(st);
        }
        else{
            removeAdding(st,startIndex,lastIndex,key);
        }
        return new String(st);
    }

    /*
    מקבלת: הסיסמה של המשתמש, אינדקס התחלתי, אינדקס סופי, להצפין א לפענח.
    מבצעת: קוראת לפונקציות על פי מה שהוגדר מראש אם זה הצפנה, ואם זה פיענוח קוראת לפונקציות ההפוכה
    מחזירה: את הסיסמה המוצפן/ המפוענח.
     */
    private static String password(String str,int key,int startIndex,int lastIndex,boolean encode){
        System.out.println(key);
        char[] st=str.toCharArray();
        if(encode){
            removeAdding(st,startIndex,lastIndex,key);
            replace(st,startIndex,lastIndex);
            return new String(st);
        }
        adding(st,startIndex,lastIndex,key);
        replace(st,startIndex,lastIndex);
        return new String (st);
    }
}
