package leancloud.internal;

import leancloud.core.LeanCloudApp;
import leancloud.core.RxAVCorePlugins;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AVCommand extends HttpRequest {

    public String relativeUrl;

    public Map<String, Object> jsonData;

    public void setJsonData(Map<String, Object> jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public InputStream getBody() {
        try {


            String jsonString = RxAVCorePlugins.getInstance().getJson().encode(this.jsonData);
            InputStream stringStream = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8.name()));

//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ObjectOutputStream oos = null;
//            oos = new ObjectOutputStream(baos);
//            oos.writeObject(this.jsonData);
//            oos.flush();
//            oos.close();
//
//            InputStream stream = new ByteArrayInputStream(baos.toByteArray());
            return stringStream;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return super.getBody();
    }
}
