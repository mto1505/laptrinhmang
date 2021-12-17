/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MinhTo
 */
public class Message implements Serializable {

    private String type;
    private List<Object> parameters;
    private Object object;
    private Map<String, Object> map = new HashMap();
    
    public Message() {
    }
    
    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Object getObject() {
        return object;
    }

    public void addAttribute(String key, Object value) {
        map.put(key, value);

    }

    public void removeAttribute(String key) {
        map.remove(key);
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Message(String type, List<Object> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Object o) {
        this.parameters.add(o);
    }

}
