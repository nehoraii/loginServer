package com.example.loginserver.server;

import com.example.loginserver.entity.LoginEntity;
import com.example.loginserver.entity.PasswordEntity;
import com.example.loginserver.entity.UserEntity;
import com.example.loginserver.enums.ErrorsEnum;
import com.example.loginserver.logic.LoginLogic;
import com.example.loginserver.logic.Security;
import com.example.loginserver.repository.LoginRepository;
import com.example.loginserver.vo.LoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
//קלאס האחראי ללוגיקה ולבאת המידע המתאים וסימנונו מהמסד מידע
public class LoginServer {
    private int  sizeSpam=50; //שדה המכיל את גודל השגיאות התחברות המקסימלי שאם המשתמש עובר אותו זה נחשב כבר תקיפת ספאם.
    private int sizeBlock=3;//שדה המכיל את גודל השגיאות התחברות המקסימלי שאם המשתמש עובר אותו אנו נאלץ לחסום את המשתמש.
    private int timeBlockMinutes=15;//שדה המכיל את הזמן שנאלץ לחסום את המשתמש במידה ונחסם.
    private int timeBetweenSpamMinutes= 15;//שדה המכיל את הזמן שנאלץ לחסום את המשתמש במידה ויש תקיפת ספאם על חשבונו.
    @Autowired
    private UserServer userServer; //אובייקט הכלה מסוג UserServer.
    @Autowired
    private LoginRepository loginRepository; // אובייקט הכלה מסוג LoginRepository.

    /*
    מקבלת: כלום.
    מבצעת: מביאה את הערך מהשדה.
    מחזירה: את מספר הדקות שיש להמתין לאחר ספאם.
    */
    public int getTimeBetweenSpamMinutes() {
        return timeBetweenSpamMinutes;
    }

    /*
    מקבלת: כלום.
    מבצעת: מביאה את הערך מהשדה.
מחזירה: את מספר הניסיונות שכשלו המקסימאלי שאז יקרא ספאם.
    */
    public int getSizeSpam() {
        return sizeSpam;
    }

    /*
    מקבלת: כלום.
    מבצעת: מביאה את הערך מהשדה.
    מחזירה: את מספר הניסיונות שכשלו המקסימאלי שהמשתמש ייחסם.

    */
    public int getSizeBlock() {
        return sizeBlock;
    }

    /*
    מקבלת: כלום.
    מבצעת: מביאה את הערך מהשדה.
    מחזירה: את מספר הדקות שיש להמתין לאחר חסימה.
    */
    public int getTimeBlockMinutes() {
        return timeBlockMinutes;
    }

    /*
    מקבלת: אובייקט LoginVo.
    מבצעת: בודקת אם צריך חסימה או שיש התקפת ספאם או אםא המשתמש לא קיים או אם הסיסמה לא נכונה.
    מחזירה: מחזירה אם הצליחה לעדכן בהצלחה ואם לא מחזירה את סיבת הבעיה.

    */
    public LoginVo save(LoginVo loginVo){
        String decipherPass=Security.decipherFromClientForPass(loginVo.getPass());
        loginVo.setPass(decipherPass);
        ErrorsEnum e;
        LoginVo loginVoFun=new LoginVo();
        loginVo.setDate(new Date());

        UserEntity user;
        user=getByUserId(loginVo.getUserId());
        if(user==null){
            String passToDB;
            passToDB=Security.encodeToDBPass(loginVo.getPass());
            loginVo.setPass(passToDB);
            saveObject(loginVo);
            loginVo.setSec(false);
            loginVoFun.setE(ErrorsEnum.USER_NOT_FOUND_ERROR);
            return loginVoFun;
        }
        e=checkSpam(loginVo.getUserId(),loginVo.getDate());
        if(e!=ErrorsEnum.GOOD){
            System.out.println(e);
            loginVo.setSec(false);
            //הוספתי FALSE
            saveObject(loginVo);
            String passToDB;
            passToDB=Security.encodeToDBPass(loginVo.getPass());
            loginVo.setPass(passToDB);
            loginVoFun.setE(ErrorsEnum.SPAM);
            return loginVoFun;
        }
        e=checkBlock(loginVo.getUserId(),loginVo.getDate());
        if(e!= ErrorsEnum.GOOD){
            loginVo.setSec(false);
            String passToDB;
            passToDB=Security.encodeToDBPass(loginVo.getPass());
            loginVo.setPass(passToDB);
            saveObject(loginVo);
            loginVoFun.setE(ErrorsEnum.BLOCK);
            return loginVoFun;
        }
        String loginPass;
        PasswordEntity pass;
        loginPass= loginVo.getPass();
        pass=getPassByUserId(loginVo.getUserId());
        String userPass;
        userPass=Security.decipherFromDBPass(pass.getPass());
        if(!loginPass.equals(userPass)){
            String passToDB;
            passToDB=Security.encodeToDBPass(loginVo.getPass());
            loginVo.setPass(passToDB);
            loginVo.setSec(false);
            saveObject(loginVo);
            loginVoFun.setE(ErrorsEnum.WRONG_PASS_ERROR);
            return loginVoFun;
        }
        loginVo.setSec(true);
        String passToDB;
        passToDB=Security.encodeToDBPass(loginVo.getPass());
        loginVo.setPass(passToDB);
        saveObject(loginVo);
        loginVoFun.setE(ErrorsEnum.GOOD);
        loginVoFun.setId(loginVo.getId());
        return loginVoFun;
    }

    /*
    מקבלת: אובייקט LoginVo.
    מבצעת: מעדכנת את השדה של הקוד הסודי לאותו אובייקט.
    מחזירה: מחזירה אם הצליחה לעדכן בהצלחה ואם לא מחזירה את סיבת הבעיה.
    */
    private ErrorsEnum updateToSecretKey(LoginVo loginVo){
        Optional<LoginEntity> loginEntity=getById(loginVo.getId());
        if(!loginEntity.isPresent()){
            return ErrorsEnum.NOT_FOUND;
        }
        loginEntity.get().setSecretCode(loginVo.getSecretCode());
        loginEntity.get().setSec(loginVo.isSec());
        loginRepository.save(loginEntity.get());
        return ErrorsEnum.GOOD;
    }
    /*public LoginVo getLoginObjectByUserId(LoginVo loginVo){
        long userId=loginVo.getUserId();
        Optional<LoginEntity> loginEntity;
        try {
            loginEntity=loginRepository.getLoginObjectByUserId(userId);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
        if(!loginEntity.isPresent()){
            return null;
        }
        loginEntity.get().setPass(loginEntity.get().getPass());
        BeanUtils.copyProperties(loginEntity.get(),loginVo);
        return loginVo;
    }
     */

    /*
    מקבלת: אובייקט LoginVo.
    מבצעת: שומרת אותו במסד הנתונים.
    מחזירה: כלום.

    */
    private void saveObject(LoginVo loginVo){
        LoginEntity bean= new LoginEntity();
        BeanUtils.copyProperties(loginVo,bean);
        bean=loginRepository.save(bean);
        BeanUtils.copyProperties(bean,loginVo);
    }

    /*
    מקבלת: מזהה ייחודי של האובייקט LoginVo.
    מבצעת: מביאה את השורה מהמסד נתונים.
    מחזירה: אובייקט המייצג את השורה.
    */
    private Optional<LoginEntity> getById(Long id){
        Optional<LoginEntity> user=loginRepository.findById(id);
        return user;
    }

    /*
    מקבלת: מזהה ייחודי של האובייקט ,LoginVo, ותאריך.
    מבצעת: בודקת האם צריך לחסום את המשתמש או שהוא כבר חסום.
    מחזירה: מחזירה את סיבת הבעיה.
    */
    private ErrorsEnum checkBlock(Long id,Date dateUser){
        Optional<List<LoginEntity>> user;
        user=loginRepository.getLastThree(id,getSizeBlock());
        if(!user.isPresent()){
            return ErrorsEnum.GOOD;
        }
        if(user.get().size()<getSizeBlock()){
            return ErrorsEnum.GOOD;
        }
        for (int i = 0; i <user.get().size() ; i++) {
            if(user.get().get(i).isSec()){
                return ErrorsEnum.GOOD;
            }
        }
        Instant instant = dateUser.toInstant();
        LocalDateTime localDateTimeUser = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        Instant instant2 = user.get().get(0).getDate().toInstant();
        LocalDateTime localDateTimeLastOfUser = LocalDateTime.ofInstant(instant2, ZoneId.systemDefault());
        long minutesBetween = ChronoUnit.MINUTES.between(localDateTimeUser, localDateTimeLastOfUser);
//        int minutesUser=dateUser.getMinutes();
//        int minutesLastOfUser=user.get().get(0).getDate().getMinutes();
//        long minutes=minutesUser-minutesLastOfUser;
        if(minutesBetween>getTimeBlockMinutes()){
            return ErrorsEnum.GOOD;
        }
        return ErrorsEnum.BLOCK;
    }

    /*
    מקבלת: מזהה ייחודי של האובייקט ,LoginVo, ותאריך.
מבצעת: בודקת האם יש עלינו מתקפת ספאם.
    מחזירה: מחזירה את סיבת הבעיה.
    */
    private ErrorsEnum checkSpam(Long id,Date dateUser){
        Optional<List<LoginEntity>>user;
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateUser);
        cal.add(Calendar.MINUTE, -getTimeBetweenSpamMinutes());
        Timestamp timestamp = new Timestamp(cal.getTimeInMillis());
        user=loginRepository.getSpam(id,timestamp);

        if(!user.isPresent()){
            return ErrorsEnum.GOOD;
        }
        if(user.get().size() < getSizeSpam()){
            return ErrorsEnum.GOOD;
        }
        for (int i = 0; i < user.get().size(); i++) {
            if(user.get().get(i).isSec()){
                return ErrorsEnum.GOOD;
            }
        }
        return ErrorsEnum.SPAM;
    }

    /*
    מקבלת: מזהה ייחודי של המשתמש.
    מבצעת: מביאה את המידע מהמסד נתונים.
    מחזירה: מחזירה את האובייקט  שמייצג את השורה במסד הנתונים.
    */
    private UserEntity getByUserId(Long userId){
        Optional<UserEntity> user;
        user=loginRepository.getUserByUserId(userId);
        if(!user.isPresent()){
            return null;
        }
        return user.get();
    }

    /*
    מקבלת: מזהה ייחודי של המשתמש.
    מבצעת: מביאה את האובייקט סיסמה מהמסד נתונים.
    מחזירה: מחזירה את האובייקט  שמייצג את השורה במסד הנתונים.
    */
    private PasswordEntity getPassByUserId(Long userId){
        Object[] obj;
        obj=loginRepository.getPassByUserId(userId);
        obj=(Object[])obj[0];
        PasswordEntity password=new PasswordEntity();
        password.setId((Long) obj[0]);
        password.setUserId((Long) obj[1]);
        password.setPass((String) obj[2]);
        password.setDate((Date) obj[3]);
        return password;
    }

    /*
    מקבלת: אובייקט LoginVo.
    מבצעת: בודקת לפי המפתח הסודי האם הקוד הסודי הזמני תואם למה שהמערכת הוציאה
    מחזירה: מחזירה אובייקט ובתוך השדה שמכיל את התוצאה מחזירה את סיבת הבעיה במידה ויש.
    */
    public LoginVo checkSecretCode(LoginVo loginVo){
        String key=userServer.getSecretKey(loginVo.getUserId());
        UserEntity userEntity=new UserEntity();
        userEntity.setSecretKey(key);
        Security.decipherUserObjectFromDB(userEntity);
        key=userEntity.getSecretKey();
        String code=LoginLogic.getTOTPCode(key);
        if(code.equals(loginVo.getSecretCode())){
            loginVo.setE(ErrorsEnum.GOOD);
            loginVo.setSec(true);
            updateToSecretKey(loginVo);
            return loginVo;
        }
        else {
            loginVo.setSec(false);
            loginVo.setE(ErrorsEnum.SECRET_CODE_ERROR);
            updateToSecretKey(loginVo);
        }
        return loginVo;
    }
}
