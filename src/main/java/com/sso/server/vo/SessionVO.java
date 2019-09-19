package com.sso.server.vo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionVO implements Serializable {
    private Map<String, String> attributes = new HashMap();
    private Map<String, String> attributesType = new HashMap();
    private long createTime = 0L;
    private String id = null;
    private int maxInactiveInterval = 1800;
    private volatile boolean isNew = false;

    public String toJson() {
        return JSONObject.toJSONString(this);
    }

    public String getId() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString().replace("-", "");
            this.isNew = true;
        }

        return this.id;
    }

    public <T> T getAttribute(String key) {
        String attrJsonStr = (String)this.attributes.get(key);
        if (attrJsonStr == null) {
            return null;
        } else {
            String className = (String)this.attributesType.get(key);

            try {
                return (T) JSONObject.parseObject(attrJsonStr, Class.forName(className));
            } catch (Exception var5) {
                var5.printStackTrace();
                return null;
            }
        }
    }

    public void setAttribute(String key, Object value) {
        try {
            this.attributes.put(key, JSONObject.toJSONString(value));
            this.attributesType.put(key, value.getClass().getName());
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public void removeAttribute(String arg0) {
        this.attributes.remove(arg0);
        this.attributesType.remove(arg0);
    }

    public SessionVO() {
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public Map<String, String> getAttributesType() {
        return this.attributesType;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public int getMaxInactiveInterval() {
        return this.maxInactiveInterval;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public void setAttributesType(HashMap<String, String> attributesType) {
        this.attributesType = attributesType;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
}

