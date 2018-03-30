package com.alipay.config;
import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016091000479702";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCutv4P+oODZoukFOxZGLQS4e+3Yx2pTug8a4rTV62FSxqFJDaRiHB6M1/JSumVSIXGz8bk2NnePfDa4fD8CYsxZwfJT+bZlY4LaccbpiLSnPAam6GGC1jVi3+Ii4Y/upg5+21UhfxX1Q/9shcemFf0jFxqY80iP0Qr+WrCjBEH1MlZH5PY5g2c1Q9vnJoo/x9GZjq0nxspRYAbZhfFytzE1EuuLd/Feq293FwK5KKbuGwsAjcKrY8xQkvI5JxCd5DoqT2LwfkN27ns0MgO7CXrMfcrGACxl1YmzPbn7SpogGhxgCzemHYrz63noafy/Z4nVls4pgN3zaZMPgCBJXnzAgMBAAECggEAIsTD1oYx+cpEa8jGKAzkeQWBYOtWkcTkLnHwVJKUM3x4sJo+ScPvLkJfA33m8hBWnvPmTtBguvt0nPptrJpxmsk4IXXHKPG8LMVGHFh5gZQ9q+3gvP4xFX4HSTMK6k/Fz3Qbk8B+MTRzVDdGpo4GouHz2jA2OGSCuoWOG10oXnhDgQ2gXj+lMLT285j+WivpJuh4SkR08DiSP2a25Pe3bGss6ZnsbK5alvmMJebwZsgDmC8pXbeUci9kQZRYo++Tref8emSaB5nrkUtCpg7pwjn8FdsD0jxK4sxUf5kwbn59vg6N9aUzC4TTQuhtHRxWU/a9YLVch8POHC4GLc1ugQKBgQDm+5Y6/YJky7nDKgGrYMBibABzpwJA8crI8xuugR93edxHfIgqj5K6rDE2V/z/jHeR1rNNe9J1ZL+zQYRci/qKIDTYrNtSxp20gTdeC16WC0QZwQewxQTl6/sdPS9aan1cTIebcmoSVAw0fHp9kARdy0L+Bprk7Z+FqS1mTccDoQKBgQDBo0aAoKjNrEegbfaZxCg3jo8XrJeRdXARQSlFltL1uDYipInVlhyRuQ+gCXJoQcUa3n/WG7G4SYKWT7JVJF3dBdxDOCbn/ejjKeed4wRSBXbya5zIa8PXiOs5fyHXOM1MhDRbenzQQju0n+HgCp16TFoAcK8xSGq0Z0/W75QVEwKBgFQcf3thv27nBPE9xbfblpMAkzWKNnbh1Z9Rb94e5Q+Dz6Q0g/1DpQHXHGCWr+l7BDuWPFrV4TCgjHJBRGrTClvsGHJil8dPzjPh7gCQEVSAK79ZHGzRgdQcF6hxFGJPbvECaUtA4cFk28DtJ0m7/OsOzolkbFk68qEglwiu/+6hAoGBAJyau2tOBBeLDMbACF/l2AzOnAWujUqT6xXZWFKyn10hJY8w+VSnOeXVAMESwtd00b9gUY1NCpebxCRiwjy/07Xshm5K3JRlstL90hfMQXNAfimjRMKG/XW0g2EV472Xb9pBVcLiNk2MM5NqdxhmHWSKmYOfnuDBy6Kpp1WSn3XHAoGBAKt9IN3H3Q+dvkHl3Kd2Tqq9ISXwgXqapei0r7bkG6Vdl2OdCOzvEo1QYEiW418+oBNq3r1K9Rtuz/GArZ3gVlkkzp29/v/sCHa8+i52QjWxuO3ABDXd8IWmh7p0rv6AhsLAAlaGydf9WWLZO5fewThmwk3FNiezjwmlM0pLsTSr";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApePWSKHBWSRfXpFIWnGwLxjcS6IgCNx83xd5pbjTlK6IKNWJZnRlPbx2hBU9cYqSW/QGDeUeYwJ+XhQQVcfIJHhIrpUddx17JVpjzj9M821YjsWMql38MFSihlf3Pk6sCKFrcabJNmYN65EvZVmbhlgSR1nBpJ9+UwBOwJFOtg6d+9UBdlBkD7JGMMFqTYaa1XU054fbLenqw3mbQPbyvmKW8nQqF+wA3ukT6GW+X1P2fIkkiG7Rt1rUSOw7Y5xuA6OvNN8tE/+UpmGnOnLT6k31diDiOKEmk2zaXIcl570RS+nWTwIwmHFLwYzXwBLiNvhKFyiX6+jqBqJ+4/ZEoQIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://liumiao.tunnel.qydev.com/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://liumiao.tunnel.qydev.com/return_url.jsp";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

