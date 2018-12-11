package cn.phenix.model.ui.v1.deserializer;

import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.model.ui.v1.event.SceneEvent;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

/**
 * @author xiaobin
 * @create 2017-11-28 下午12:13
 **/
public class EventDeserializers extends JsonDeserializer<List<SceneEvent>> {


    @Override
    public List<SceneEvent> deserialize(JsonParser jp, DeserializationContext deserializationContext)
            throws IOException {
        List<SceneEvent> result = Lists.newArrayList();
        ObjectMapper mapper = new ObjectMapper();
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        for (JsonNode n : node) {
            String eventID = n.get("eventID").asText();

            if (Strings.isBlank(eventID) || EventMap.VALUES.get(eventID) == null) {
               // throw new ExportException("存在不支持的类型");
                continue;
            }

            Class<? extends SceneEvent> instanceClass = EventMap.VALUES.get(eventID);

            result.add(mapper.treeToValue(n, instanceClass));
        }

        return result;
    }
}
