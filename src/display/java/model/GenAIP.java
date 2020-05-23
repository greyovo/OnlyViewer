package display.java.model;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * https://ai.baidu.com/ai-doc/OCR 有修改
 *
 * @author Kevin
 * @since 2020/4/27
 **/

public class GenAIP {
    private static final String APP_ID = "19635799";
    private static final char last2byte = (char) Integer.parseInt("00000011", 2);
    private static final char last4byte = (char) Integer.parseInt("00001111", 2);
    private static final char last6byte = (char) Integer.parseInt("00111111", 2);
    private static final char lead6byte = (char) Integer.parseInt("11111100", 2);
    private static final char lead4byte = (char) Integer.parseInt("11110000", 2);
    private static final char lead2byte = (char) Integer.parseInt("11000000", 2);
    private static final char[] encodeTable = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};

    /**
     * 获取API访问token
     * 该token需30日更新一次
     * Latest:2020/4/27
     *
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     */
    static String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk + "&";
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder(32);
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            /*
             * 返回结果示例
             */
            JSONObject jsonObject = new JSONObject(result.toString());
            return jsonObject.getString("access_token");
        } catch (Exception e) {
            System.err.println("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

    /**
     * 根据文件路径读取byte[] 数组
     */
    static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length())) {
                BufferedInputStream in = null;
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }
                return bos.toByteArray();
            }
        }
    }

    /**
     * Base64
     *
     * @param from
     */
    static String encode(byte[] from) {
        StringBuilder to = new StringBuilder((int) ((double) from.length * 1.34D) + 3);
        int num = 0;
        char currentByte = 0;

        int i;
        for (i = 0; i < from.length; ++i) {
            for (num %= 8; num < 8; num += 6) {
                switch (num) {
                    case 0:
                        currentByte = (char) (from[i] & lead6byte);
                        currentByte = (char) (currentByte >>> 2);
                    case 1:
                    case 3:
                    case 5:
                    default:
                        break;
                    case 2:
                        currentByte = (char) (from[i] & last6byte);
                        break;
                    case 4:
                        currentByte = (char) (from[i] & last4byte);
                        currentByte = (char) (currentByte << 2);
                        if (i + 1 < from.length) {
                            currentByte = (char) (currentByte | (from[i + 1] & lead2byte) >>> 6);
                        }
                        break;
                    case 6:
                        currentByte = (char) (from[i] & last2byte);
                        currentByte = (char) (currentByte << 4);
                        if (i + 1 < from.length) {
                            currentByte = (char) (currentByte | (from[i + 1] & lead4byte) >>> 4);
                        }
                }
                to.append(encodeTable[currentByte]);
            }
        }

        if (to.length() % 4 != 0) {
            for (i = 4 - to.length() % 4; i > 0; --i) {
                to.append("=");
            }
        }
        return to.toString();
    }

    /**
     * Http工具
     *
     * @param requestUrl
     * @param accessToken
     * @param params
     * @throws Exception
     */
    static String post(String requestUrl, String accessToken, String params)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return GenAIP.post(requestUrl, accessToken, contentType, params);
    }

    private static String post(String requestUrl, String accessToken, String contentType, String params)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return GenAIP.post(requestUrl, accessToken, contentType, params, encoding);
    }

    private static String post(String requestUrl, String accessToken, String contentType, String params, String encoding)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return GenAIP.postGeneralUrl(url, contentType, params, encoding);
    }

    private static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding)
            throws Exception {
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();

        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        StringBuilder result = new StringBuilder(32);
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result.append(getLine);
        }
        in.close();
        String totalStr = result.toString();
        return jsonWords(totalStr);
    }

    // JSONObject处理API返回的数据
    private static String jsonWords(String totalStr) {
        net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(totalStr);
        net.sf.json.JSONArray wordsArray = net.sf.json.JSONArray.fromObject(json.getString("words_result"));
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < wordsArray.size(); i++) {
            net.sf.json.JSONObject words = wordsArray.getJSONObject(i);
            sb.append(words.getString("words")).append("\n");
        }
        if (sb.length() != 0)
            return sb.toString();
        return null;
    }

    // 正则表达式提取words内容 曾经使用的提取方法
    static String regexWords(String totalStr) {
        String words = totalStr.split("\"words_result\":")[1];

        String[] wordsDivided = words.split("}, ");

        String regEX = ": \".+?\"";
        Pattern pattern = Pattern.compile(regEX);
        List<String> strList = new ArrayList<>();
        for (int i = 0; i < wordsDivided.length; i++) {
            Matcher matcher = pattern.matcher(wordsDivided[i]);
            if (matcher.find()) {
                String temp = matcher.group();
                temp = temp.substring(temp.indexOf("\"") + 1, temp.lastIndexOf("\""));
                strList.add(temp);
            }
        }
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < strList.size(); i++) {
            sb.append(strList.get(i)).append("\n");
        }
        return sb.toString();
    }
}
