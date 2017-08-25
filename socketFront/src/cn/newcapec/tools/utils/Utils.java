package cn.newcapec.tools.utils;

import java.math.BigInteger;
import java.util.Random;
import java.util.UUID;

/**
 * 通用工具类
 *
 * @author WEIXING
 */
public class Utils {

    /**
     * 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取token
     *
     * @return
     */
    public static String getToken() {
        return getUUID().replace("-", "");
    }

    /**
     * 获取区间内的随机数
     *
     * @param n1
     * @param n2
     * @return
     */
    public static int random(int n1, int n2) {
        return new Random().nextInt(n2 - n1 + 1) + n1;
    }

    /**
     * 获取指定长度的随机数字
     *
     * @param digit
     * @return
     */
    public static String getNumberString(int digit) {
        StringBuffer nstr = new StringBuffer();
        for (int i = 0; i < digit; i++) {
            nstr.append(random(0, 9));
        }
        return nstr.toString();
    }

    /**
     * 获得数字的字符串形式，长度不足len的前面补0
     *
     * @param num
     * @param len
     * @return
     */
    public static String getNumString(int num, int len) {
        return getNumString("" + num, len);
    }

    /**
     * 获得数字的字符串形式，长度不足len的前面补0
     *
     * @param num
     * @param len
     * @return
     */
    public static String getNumString(String num, int len) {
        StringBuffer sb = new StringBuffer(num);
        int off = len - sb.length();
        if (off > 0) {
            for (int i = 0; i < off; i++) {
                sb.insert(0, "0");
            }
        }
        return sb.toString();
    }

    /**
     * 昵称合法性验证
     *
     * @param nickname
     * @return
     */
    public static boolean checkNickname(String nickname) {
        try {
            nickname = new String(nickname.getBytes("ISO-8859-1"), "UTF-8");
            return nickname.matches("^[A-Za-z0-9_\u4e00-\u9fa5]{1,12}$");
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 邮箱地址合法性验证
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        try {
            email = new String(email.getBytes("ISO-8859-1"), "UTF-8");
            return email.matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$");
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 手机号码合法性验证
     *
     * @param tel
     * @return
     * @author 吴培培
     * @date 20160511
     */
    public static boolean checkTel(String tel) {
        try {
            return tel.matches("^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\\d{8})$");
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 证件号码合法性验证
     *
     * @param certno
     * @return
     * @author 吴培培
     * @date 20160511
     */
    public static boolean checkCertno(String certno) {
        try {
            return certno.matches("^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$");
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * 获得网络请求类型
     *
     * @param netType
     * @return
     */
    public static String getNetType(int netType) {
        String result = "";
        switch (netType) {
            case Constant.NETTYPE_HTTP:
                result = "http://";
                break;
            case Constant.NETTYPE_HTTPS:
                result = "https://";
                break;
            case Constant.NETTYPE_SOCKET:
                result = "socket://";
                break;
        }
        return result;
    }

    /**
     * 通过客户代码获得行业Id
     *
     * @param customerunitcode
     * @return
     */
    public static Integer getIndustryByCustomerunitcode(String customerunitcode) {
        Integer industryId = null; //行业id
        try {
            industryId = Integer.valueOf(customerunitcode.substring(3, 7));
        } catch (Exception e) {
            industryId = null;
        }
        return industryId;
    }
    public static void main(String[] args) {
        long lo = 71678728079417L;
        String ss = Long.toHexString(lo);
//        BigInteger asn = new BigInteger();
        System.out.println(Double.valueOf(".01"));
        System.out.println((long) 0);
        System.out.println( (int) ( Long.parseLong("00000072",16)));
    }
}
