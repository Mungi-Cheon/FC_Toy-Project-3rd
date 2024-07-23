package org.hack.travel.global.util;

public class UserValidationUtil {

    public static boolean isEqualUser(int loginId, int compUserId){
        return loginId == compUserId;
    }
}
