package cn.phenix.model.ui.v1.deserializer;

import cn.phenix.cloud.core.utils.Strings;
import cn.phenix.model.ui.v1.launcher.Container;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.rmi.server.ExportException;

/**
 * @author xiaobin
 * @create 2017-11-28 下午12:13
 **/
public class UIDeserializer extends JsonDeserializer<Container> {

    @Override
    public Container deserialize(JsonParser jp, DeserializationContext deserializationContext)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectCodec oc = jp.getCodec();
        JsonNode node = oc.readTree(jp);

        String uiClassId = node.get("uiClassID").asText();

        if (Strings.isBlank(uiClassId) || UIMap.VALUES.get(uiClassId) == null) {
            throw new ExportException("存在不支持的类型");
        }

        Class<? extends Container> instanceClass = UIMap.VALUES.get(uiClassId);

        return mapper.treeToValue(node, instanceClass);
    }
}
