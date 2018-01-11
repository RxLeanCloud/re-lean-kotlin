package HttpClient;

import leancloud.core.LeanCloud;
import leancloud.internal.HttpResponse;
import leancloud.internal.IHttpClient;
import leancloud.internal.HttpRequest;
import okhttp3.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class GeneralHttpClient implements IHttpClient {
    private static GeneralHttpClient ourInstance = new GeneralHttpClient();

    public static GeneralHttpClient getInstance() {
        return ourInstance;
    }

    OkHttpClient client = new OkHttpClient();

    @Override
    public HttpResponse execute(HttpRequest request) throws IOException {
        Request.Builder builder = new Request.Builder();
        MediaType CONTENT_TYPE = MediaType.parse(String.format("%s; charset=utf-8", request.getContentType()));
        switch (request.method.toUpperCase()) {
            case "GET": {
                builder.get();
                break;
            }
            case "DELETE": {
                builder.delete();
                break;
            }
            case "PUT": {
                try {
                    builder.put(RequestBody.create(CONTENT_TYPE, this.readBytes(request.getBody())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "POST": {
                try {
                    builder.post(RequestBody.create(CONTENT_TYPE, this.readBytes(request.getBody())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            default: {
                builder.get();
            }
        }
        request.headers.forEach((k, v) -> builder.addHeader(k, v));
        Request okHttpRequest = builder.url(request.getUrl()).build();
        Response okHttpResponse = this.client.newCall(okHttpRequest).execute();

        HttpResponse response = new HttpResponse();
        response.statusCode = okHttpResponse.code();
        response.body = okHttpResponse.body().bytes();

        return response;
    }

    public byte[] readBytes(InputStream inputStream) throws IOException {
        byte[] b = new byte[4096];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int c;
        while ((c = inputStream.read(b)) != -1) {
            stream.write(b, 0, c);
        }
        stream.flush();
        byte[] result = stream.toByteArray();
        return result;
    }
}
