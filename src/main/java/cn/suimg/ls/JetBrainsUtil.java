package cn.suimg.ls;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * JetBrains 激活工具类
 */
public class JetBrainsUtil {

    /**
     * sign Template
     */
    private static final String SIGN_TEMPLATE="<!-- %s -->\n";

    /**
     * obtain Ticket Template
     */
    private static final String OBTAIN_TICKET_TEMPLATE=
            "<ObtainTicketResponse>\n" +
                    "    <message></message>\n" +
                    "    <prolongationPeriod>607875500</prolongationPeriod>\n" +
                    "    <responseCode>OK</responseCode>\n" +
                    "    <salt>%s</salt>\n" +
                    "    <ticketId>1</ticketId>\n" +
                    "    <ticketProperties>licensee=%s\tlicenseType=0\t</ticketProperties>\n" +
                    "</ObtainTicketResponse>";

    /**
     * release Ticket Template
     */
    private static final String RELEASE_TICKET_TEMPLATE=
            "<ReleaseTicketResponse>\n" +
                    "    <message></message>\n" +
                    "    <responseCode>OK</responseCode>\n" +
                    "    <salt>%s</salt>\n" +
                    "</ReleaseTicketResponse>";

    /**
     * prolong Ticket Template
     */
    private static final String PROLONG_TICKET_TEMPLATE=
            "<ProlongTicketResponse>\n" +
                    "    <message></message>\n" +
                    "    <responseCode>OK</responseCode>\n" +
                    "    <salt>%s</salt>\n" +
                    "    <ticketId>1</ticketId>\n" +
                    "</ProlongTicketResponse>";

    /**
     * ping Template
     */
    private static final String PING_TEMPLATE=
            "<PingResponse>\n" +
                    "    <message></message>\n" +
                    "    <responseCode>OK</responseCode>\n" +
                    "    <salt>%s</salt>\n" +
                    "</PingResponse>";

    /**
     * Private Key
     */
    private static final String JETBRAINS_PRIVATE_KEY=
            "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAt5yrcHAAjhglnCEn"+
                    "6yecMWPeUXcMyo0+itXrLlkpcKIIyqPw546bGThhlb1ppX1ySX/OUA4jSakHekNP"+
                    "5eWPawIDAQABAkBbr9pUPTmpuxkcy9m5LYBrkWk02PQEOV/fyE62SEPPP+GRhv4Q"+
                    "Fgsu+V2GCwPQ69E3LzKHPsSNpSosIHSO4g3hAiEA54JCn41fF8GZ90b9L5dtFQB2"+
                    "/yIcGX4Xo7bCvl8DaPMCIQDLCUN8YiXppydqQ+uYkTQgvyq+47cW2wcGumRS46dd"+
                    "qQIhAKp2v5e8AMj9ROFO5B6m4SsVrIkwFICw17c0WzDRxTEBAiAYDmftk990GLcF"+
                    "0zhV4lZvztasuWRXE+p4NJtwasLIyQIgVKzknJe8VOt5a3shCMOyysoNEg+YAt02"+
                    "O98RPCU0nJg=";


    /**
     * obtainTicket
     * @param salt salt
     * @param userName username
     * @return xml
     */
    public static String obtainTicket(String salt,String userName){
        String data = String.format(OBTAIN_TICKET_TEMPLATE,salt,userName);
        String sign = String.format(SIGN_TEMPLATE,sign(data));
        return sign + data;
    }

    /**
     * releaseTicket
     * @param salt salt
     * @return xml
     */
    public static String releaseTicket(String salt){
        String data = String.format(RELEASE_TICKET_TEMPLATE,salt);
        String sign = String.format(SIGN_TEMPLATE,sign(data));
        return sign + data;
    }

    /**
     * prolongTicket
     * @param salt salt
     * @return xml
     */
    public static String prolongTicket(String salt){
        String data = String.format(PROLONG_TICKET_TEMPLATE,salt);
        String sign = String.format(SIGN_TEMPLATE,sign(data));
        return sign + data;
    }

    /**
     * ping
     * @param salt salt
     * @return xml
     */
    public static String ping(String salt){
        String data = String.format(PING_TEMPLATE,salt);
        String sign = String.format(SIGN_TEMPLATE,sign(data));
        return sign + data;
    }

    /**
     * Sign
     * @param data string
     * @return sign
     */
    public static String sign(String data) {
        try {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(JETBRAINS_PRIVATE_KEY.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
            Signature signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateK);
            signature.update(data.getBytes());
            byte[] sign = signature.sign();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < sign.length; i++) {
                int v = sign[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
