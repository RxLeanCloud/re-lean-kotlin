package rx.leancloud.core;

import rx.leancloud.internal.IHttpClient;
import rx.leancloud.internal.IJSONClient;
import rx.leancloud.internal.IKVStorage;

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

    private IKVStorage kvStorage;

    public void setKVStorage(IKVStorage kvStorage) {
        this.kvStorage = kvStorage;
    }
    public IKVStorage getKVStorage() {
        return kvStorage;
    }
}
