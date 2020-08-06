package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ArrayListTypeFieldDeserializer extends FieldDeserializer {
    private ObjectDeserializer deserializer;
    private int itemFastMatchToken;
    private final Type itemType;

    public ArrayListTypeFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        super(clazz, fieldInfo);
        if (fieldInfo.fieldType instanceof ParameterizedType) {
            Type argType = ((ParameterizedType) fieldInfo.fieldType).getActualTypeArguments()[0];
            if (argType instanceof WildcardType) {
                Type[] upperBounds = ((WildcardType) argType).getUpperBounds();
                if (upperBounds.length == 1) {
                    argType = upperBounds[0];
                }
            }
            this.itemType = argType;
            return;
        }
        this.itemType = Object.class;
    }

    public int getFastMatchToken() {
        return 14;
    }

    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        if (parser.lexer.token() == 8) {
            setValue(object, (String) null);
            return;
        }
        ArrayList list = new ArrayList();
        ParseContext context = parser.getContext();
        parser.setContext(context, object, this.fieldInfo.name);
        parseArray(parser, objectType, list);
        parser.setContext(context);
        if (object == null) {
            fieldValues.put(this.fieldInfo.name, list);
        } else {
            setValue(object, (Object) list);
        }
    }

    public final void parseArray(DefaultJSONParser parser, Type objectType, Collection array) {
        Type itemType2 = this.itemType;
        ObjectDeserializer itemTypeDeser = this.deserializer;
        if (objectType instanceof ParameterizedType) {
            if (itemType2 instanceof TypeVariable) {
                TypeVariable typeVar = (TypeVariable) itemType2;
                ParameterizedType paramType = (ParameterizedType) objectType;
                Class<?> objectClass = null;
                if (paramType.getRawType() instanceof Class) {
                    objectClass = (Class) paramType.getRawType();
                }
                int paramIndex = -1;
                if (objectClass != null) {
                    int i = 0;
                    int size = objectClass.getTypeParameters().length;
                    while (true) {
                        if (i >= size) {
                            break;
                        } else if (objectClass.getTypeParameters()[i].getName().equals(typeVar.getName())) {
                            paramIndex = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                if (paramIndex != -1) {
                    itemType2 = paramType.getActualTypeArguments()[paramIndex];
                    if (!itemType2.equals(this.itemType)) {
                        itemTypeDeser = parser.getConfig().getDeserializer(itemType2);
                    }
                }
            } else if (itemType2 instanceof ParameterizedType) {
                ParameterizedType parameterizedItemType = (ParameterizedType) itemType2;
                Type[] itemActualTypeArgs = parameterizedItemType.getActualTypeArguments();
                if (itemActualTypeArgs.length == 1 && (itemActualTypeArgs[0] instanceof TypeVariable)) {
                    TypeVariable typeVar2 = (TypeVariable) itemActualTypeArgs[0];
                    ParameterizedType paramType2 = (ParameterizedType) objectType;
                    Class<?> objectClass2 = null;
                    if (paramType2.getRawType() instanceof Class) {
                        objectClass2 = (Class) paramType2.getRawType();
                    }
                    int paramIndex2 = -1;
                    if (objectClass2 != null) {
                        int i2 = 0;
                        int size2 = objectClass2.getTypeParameters().length;
                        while (true) {
                            if (i2 >= size2) {
                                break;
                            } else if (objectClass2.getTypeParameters()[i2].getName().equals(typeVar2.getName())) {
                                paramIndex2 = i2;
                                break;
                            } else {
                                i2++;
                            }
                        }
                    }
                    if (paramIndex2 != -1) {
                        itemActualTypeArgs[0] = paramType2.getActualTypeArguments()[paramIndex2];
                        itemType2 = new ParameterizedTypeImpl(itemActualTypeArgs, parameterizedItemType.getOwnerType(), parameterizedItemType.getRawType());
                    }
                }
            }
        }
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == 14) {
            if (itemTypeDeser == null) {
                itemTypeDeser = parser.getConfig().getDeserializer(itemType2);
                this.deserializer = itemTypeDeser;
                this.itemFastMatchToken = this.deserializer.getFastMatchToken();
            }
            lexer.nextToken(this.itemFastMatchToken);
            int i3 = 0;
            while (true) {
                if (lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (lexer.token() == 16) {
                        lexer.nextToken();
                    }
                }
                if (lexer.token() == 15) {
                    lexer.nextToken(16);
                    return;
                }
                array.add(itemTypeDeser.deserialze(parser, itemType2, Integer.valueOf(i3)));
                parser.checkListResolve(array);
                if (lexer.token() == 16) {
                    lexer.nextToken(this.itemFastMatchToken);
                }
                i3++;
            }
        } else {
            if (itemTypeDeser == null) {
                itemTypeDeser = parser.getConfig().getDeserializer(itemType2);
                this.deserializer = itemTypeDeser;
            }
            array.add(itemTypeDeser.deserialze(parser, itemType2, 0));
            parser.checkListResolve(array);
        }
    }
}
