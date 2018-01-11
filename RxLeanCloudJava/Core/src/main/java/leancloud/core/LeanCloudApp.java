package leancloud.core;

import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

public final class LeanCloudApp {

    public enum AVRegion {
        Public_North_CN,
        Public_East_CN,
        Public_North_US,
        Private_Custom;

        static final String api_public_north_cn = "api.leancloud.cn";
        static final String push_router_public_north_cn = "router.g0.push.leancloud.cn";
        static final String api_public_east_cn = "e1-api.leancloud.cn";
        static final String push_router_public_east_cn = "router-q0-push.leancloud.cn";
        static final String api_public_north_us = "us-api.leancloud.cn";
        static final String push_router_public_north_us = "router-a0-push.leancloud.cn";

        private Map<String, String> hostMap = new HashMap<>();

        public void initHost(String appId) {
            String schema = "https://";
            switch (this) {
                case Public_North_US: {
                    hostMap.put(this.toString(), String.format("%s%s", schema, api_public_north_us));
                    hostMap.put(this.toString() + "/rtmRouter", String.format("%s%s", schema, push_router_public_north_us));
                    break;
                }
                case Public_East_CN: {
                    hostMap.put(this.toString(), String.format("%s%s", schema, api_public_east_cn));
                    hostMap.put(this.toString() + "/rtmRouter", String.format("%s%s", schema, push_router_public_east_cn));
                    break;
                }
                case Private_Custom: {
                    break;
                }
                default: {
                    String appSubDomain = appId.substring(0, 8);
                    hostMap.put(this.toString(), String.format("%s%s.api.lncld.net", schema, appSubDomain));
                    hostMap.put(this.toString() + "/engine", String.format("%s%s.engine.lncld.net", schema, appSubDomain));
                    hostMap.put(this.toString() + "/stats", String.format("%s%s.stats.lncld.net", schema, appSubDomain));
                    hostMap.put(this.toString() + "/push", String.format("%s%s.push.lncld.net", schema, appSubDomain));
                    hostMap.put(this.toString() + "/rtmRouter", String.format("%s%s.rtm.lncld.net", schema, appSubDomain));
                }
            }
        }

        public Map<String, String> getHostMap() {
            return hostMap;
        }

        public String getHost(String servicePrefix) {
            if (servicePrefix == "push" || servicePrefix == "/push") {
                return this.hostMap.get(this.toString() + "/push");
            }
            return this.hostMap.get(this.toString());
        }


        @Override
        public String toString() {
            String string;
            switch (this) {
                case Public_North_CN: {
                    string = "Public_North_CN";
                    break;
                }
                case Public_East_CN: {
                    string = "Public_East_CN";
                    break;
                }
                case Public_North_US: {
                    string = "Public_North_US";
                    break;
                }
                case Private_Custom: {
                    string = "Private_Custom";
                    break;
                }
                default: {
                    string = "Invalid http method: <" + this + ">";
                }
            }
            return string;
        }
    }


    private final String appId;
    private final String appKey;
    private final AVRegion region;

    private String apiVersion = "1.1";

    private String apiHost;
    private String rtmRouterHost;


    public LeanCloudApp(String appId, String appKey, AVRegion region) {
        this.appId = appId;
        this.appKey = appKey;
        this.region = region;

        this.region.initHost(this.appId);
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setApiServer(String hostWithSchema) {
        this.region.getHostMap().put(this.region.toString(), hostWithSchema);
    }

    public void setRTMRouter(String rtmRouterWithSchema) {
        this.region.getHostMap().put(this.region.toString() + "/rtmRouter", rtmRouterWithSchema);
    }

    public Map<String, String> getHttpHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("X-LC-Id", this.appId);
        headers.put("X-LC-Key", this.appKey);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    public String getUrl(String relativeUrl) {
        String hostWithSchema = this.region.getHost("");
        if (relativeUrl.startsWith("/push") || relativeUrl.startsWith("/installations")) {
            hostWithSchema = this.region.getHost("push");
        }
        return hostWithSchema + "/" + this.apiVersion + relativeUrl;
    }
}
