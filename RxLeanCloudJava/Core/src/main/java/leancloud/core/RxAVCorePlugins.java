package leancloud.core;

import leancloud.internal.IHttpClient;
import leancloud.internal.IJSONClient;

public class RxAVCorePlugins {
    private static RxAVCorePlugins ourInstance = new RxAVCorePlugins();

    public static RxAVCorePlugins getInstance() {
        return ourInstance;
    }

    private RxAVCorePlugins() {
    }

    private IHttpClient httpClient;

    public void setHttpClient(IHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public IHttpClient getHttpClient() {
        return httpClient;
    }

    private IJSONClient json;

    public void setJson(IJSONClient json) {
        this.json = json;
    }

    public IJSONClient getJson() {
        return json;
    }

}
