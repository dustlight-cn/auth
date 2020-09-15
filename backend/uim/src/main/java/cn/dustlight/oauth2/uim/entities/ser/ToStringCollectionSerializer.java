package cn.dustlight.oauth2.uim.entities.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@JacksonStdImpl
public class ToStringCollectionSerializer extends StaticListSerializerBase<Collection<String>> {
    public static final ToStringCollectionSerializer instance = new ToStringCollectionSerializer();

    protected ToStringCollectionSerializer() {
        super(Collection.class);
    }

    protected ToStringCollectionSerializer(ToStringCollectionSerializer src, Boolean unwrapSingle) {
        super(src, unwrapSingle);
    }

    public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
        return new ToStringCollectionSerializer(this, unwrapSingle);
    }

    protected JsonNode contentSchema() {
        return this.createSchemaNode("string", true);
    }

    protected void acceptContentVisitor(JsonArrayFormatVisitor visitor) throws JsonMappingException {
        visitor.itemsFormat(JsonFormatTypes.STRING);
    }

    public void serialize(Collection<String> value, JsonGenerator g, SerializerProvider provider) throws IOException {
        int len = value.size();
        if (len != 1 || (this._unwrapSingle != null || !provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) && this._unwrapSingle != Boolean.TRUE) {
            g.writeStartArray(value, len);
            this.serializeContents(value, g, provider);
            g.writeEndArray();
        } else {
            this.serializeContents(value, g, provider);
        }
    }

    public void serializeWithType(Collection<String> value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer.typeId(value, JsonToken.START_ARRAY));
        g.setCurrentValue(value);
        this.serializeContents(value, g, provider);
        typeSer.writeTypeSuffix(g, typeIdDef);
    }

    private final void serializeContents(Collection<String> value, JsonGenerator g, SerializerProvider provider) throws IOException {
        int i = 0;

        try {
            for (Iterator var5 = value.iterator(); var5.hasNext(); ++i) {
                Object str = var5.next();
                if (str == null) {
                    provider.defaultSerializeNull(g);
                } else {
                    g.writeString(str.toString());
                }
            }
        } catch (Exception var7) {
            this.wrapAndThrow(provider, var7, value, i);
        }
    }
}
