package rx.leancloud.internal;

import rx.leancloud.core.RxAVClient;
import rx.leancloud.core.RxAVException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AVObjectController {

    private IAVEncoder objectEncoder;

    private IAVCommandRunner commandRunner;

    private IAVDecoder objectDecoder;

    public AVObjectController(IAVEncoder avEncoder, IAVDecoder avDecoder, IAVCommandRunner commandRunner) {
        this.objectEncoder = avEncoder;
        this.objectDecoder = avDecoder;
        this.commandRunner = commandRunner;
    }

    public AVObjectState save(AVObjectState state) throws RxAVException {
        AVCommand command = this.packRequest(state);
        try {
            AVCommandResponse response = this.commandRunner.execute(command);
            return this.unpackResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RxAVException e) {
            throw e;
        }
        return null;
    }

    public void removeReadOnlyFields(AVObjectState state) {
        String[] internalKeys = new String[]{"objectId", "updatedAt", "createdAt"};
        for (String internalKey : internalKeys) {
            if (state.serverData.containsKey(internalKey)) {
                state.serverData.remove(internalKey);
            }
        }
    }

    public void removeRelationFields(AVObjectState state) {
        ArrayList<String> relationKeys = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : state.serverData.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                Map<String, Object> vMap = (Map<String, Object>) value;
                if (vMap.containsKey("__type")) {
                    if (vMap.get("__type") == "Relation") {
                        relationKeys.add(entry.getKey());
                    }
                }
            }
        }
        relationKeys.forEach((key) -> state.serverData.remove(key));
    }

    public AVCommand packRequest(AVObjectState state) {
        AVObjectState clonedState = state.mutatedClone();
        this.removeReadOnlyFields(clonedState);
        this.removeRelationFields(clonedState);

        Map<String, Object> mutableEncoded = new HashMap<>();
        for (Map.Entry<String, Object> entry : state.serverData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Object encodedValue = this.objectEncoder.encode(value);
            mutableEncoded.put(key, encodedValue);
        }
        AVCommand command = new AVCommand();
        String relativeUrl = clonedState.objectId == null ? "/classes/" + clonedState.className : "/classes/" + clonedState.className + "/" + clonedState.objectId;

        command.method = clonedState.objectId == null ? "POST" : "PUT";
        command.url = RxAVClient.getInstance().getCurrentApp().getUrl(relativeUrl);
        command.jsonData = mutableEncoded;
        command.headers = RxAVClient.getInstance().getCurrentApp().getHttpHeaders();

        return command;
    }

    public AVObjectState unpackResponse(AVCommandResponse response) {
        return this.unpackSavedResponse(response);
    }

    public AVObjectState unpackSavedResponse(AVCommandResponse response) {
        AVObjectState serverState = this.decode(response.jsonBody());
        return serverState;
    }

    public AVObjectState decode(Map<String, Object> serverData) {
        AVObjectState serverState = new AVObjectState();
        Map<String, Object> clonedData = new HashMap<>();
        clonedData.putAll(serverData);
        serverState.serverData = new HashMap<>();
        if (clonedData.containsKey("createdAt")) {
            String createdAtISOString = (String) clonedData.get("createdAt");
            try {
                serverState.createdAt = AVInternalPlugins.getInstance().getAVDateFormatter().parse(createdAtISOString);
                clonedData.remove("createdAt");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (clonedData.containsKey("updatedAt")) {
            String updatedAtISOString = (String) clonedData.get("updatedAt");
            try {
                serverState.updatedAt = AVInternalPlugins.getInstance().getAVDateFormatter().parse(updatedAtISOString);
                clonedData.remove("updatedAt");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (clonedData.containsKey("objectId")) {
            serverState.objectId = (String) clonedData.get("objectId");
            clonedData.remove("objectId");
        }

        for (Map.Entry<String, Object> entry : clonedData.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Object encodedValue = this.objectDecoder.decode(value);
            serverState.serverData.put(key, encodedValue);
        }
        return serverState;
    }
}
