package com.example.loginserver.logic;

import com.example.loginserver.entity.LoginEntity;
import com.example.loginserver.vo.LoginVo;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import de.taimos.totp.TOTP;

public class LoginLogic {
    public static String getTOTPCode(String secretKey) {
        Base32 base32 = new Base32();
        byte[] bytes = base32.decode(secretKey);
        String hexKey = Hex.encodeHexString(bytes);
        return TOTP.getOTP(hexKey);
    }
    public static void copyProperty(LoginEntity from, LoginVo to){
        to.setId(from.getId());
        to.setSec(from.isSec());
        to.setDate(from.getDate());
        to.setIp(from.getIp());
        to.setPass(from.getPass());
        to.setUserId(from.getUserId());
        to.setSecretCode(from.getSecretCode());
    }

}
