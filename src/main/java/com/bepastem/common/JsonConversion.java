package com.bepastem.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;

public class JsonConversion {

    public static Object requestPayloadToJson(HttpServletRequest request, Class<?>className) throws IOException {
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        return gson.fromJson(reader, (Type) className);
    }

    public static Object fromJsonToClassObject(JsonObject jsonObject, Class<?>className) {
        Gson gson = new Gson();
        return gson.fromJson(jsonObject, (Type) className);
    }

    public static Object fromRequestInputStreamToJsonObject(HttpServletRequest request, Class<?>className) {
        try{
            StringBuilder JSON_STRING = new StringBuilder();
            BufferedReader br = request.getReader();
            String str = null;
            while ((str = br.readLine()) != null) {
                JSON_STRING.append(str);
                System.out.println(str);
            }

            JSONObject emp=(new JSONObject(JSON_STRING)).getJSONObject("json");
            System.out.println("emp: " + emp.toString());

            return emp;
            //textView1.setText(str);

            /*var jObj = JsonObject.class; jObj.par
            var name = jObj.get("user").getAsJsonObject();
            String pwd = jObj.getString("Pwd");
            String command = jObj.getString("Command");

            JSONObject json = new JSONObject();
            response.setContentType("application/json");
            response.setHeader("Cache-Control", "nocache");
            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.print(json.toString()); */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

   /* @Autowired
    private JacksonTester<User> json;

    public void testSerialize() throws Exception {
        User details = new User("Honda", "Civic", "email");
        this.json.write(details);
    }

    public void testDeserialize() throws Exception {
        String content = "{\"make\":\"Ford\",\"model\":\"Focus\"}";
        var h = this.json.parse(content);
        var t = this.json.parseObject(content);
    } */


}
