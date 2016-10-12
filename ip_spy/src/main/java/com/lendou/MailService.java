package com.lendou;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dm.model.v20151123.SingleSendMailRequest;
import com.aliyuncs.dm.model.v20151123.SingleSendMailResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.lang3.Validate;

/**
 * 发送邮件
 */
public final class MailService {

    private final String ALIYUN_REGION = "cn-beijing";

    // 邮箱帐号
    private final String ACCOUNT_NAME = "info@info.gyc2c.com";


    private IAcsClient client;
    public MailService() {
        IClientProfile profile = DefaultProfile.getProfile(
                ALIYUN_REGION,
                ConfigManager.get(ConfigName.ACCESS_KEY_ID),
                ConfigManager.get(ConfigName.ACCESS_KEY_SECRET));
        client = new DefaultAcsClient(profile);
    }

    /**
     * 给邮箱发送验证码
     *
     * @param mail 用户邮箱
     * @param ip 验证码
     * @return 是否发送成功
     */
    public boolean sendMail(String mail, String ip) {

        Validate.notEmpty(mail);
        Validate.notEmpty(ip);

        String htmlContent = String.format("<div><p style='font-weight: bold;'>亲爱的用户:</p><p>您好! 当前公司外网IP地址: </p><p style='font-size: 18px; color: #1AE61A;'>%s</p><br/><p>源合网团队</p><p>%s</p></div>", ip, TimeUtils.getTimeFromNow(0));

        SingleSendMailRequest request = new SingleSendMailRequest();
        request.setAccountName(ACCOUNT_NAME);
        request.setFromAlias("源合网");
        request.setToAddress(mail);
        request.setAddressType(1);
        request.setTagName("CIRI");
        request.setReplyToAddress(false);
        request.setSubject("源合网 - 邮箱身份认证");
        request.setHtmlBody(htmlContent);

        request.setFromAlias("公司内网");
        request.setSubject("公司内网当前外网IP");

        try {
            SingleSendMailResponse response = client.getAcsResponse(request);
            return true;
        } catch (ClientException e) {
            return false;
        }
    }
}
