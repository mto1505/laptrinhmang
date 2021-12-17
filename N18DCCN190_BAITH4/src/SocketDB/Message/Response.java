/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SocketDB.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MinhTo
 */
public class Response implements Serializable{
    private Object object;
    private String status;
    private Map<String,Object> map=new HashMap<>();

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public void setAttribute(String attr,Object o)
            {
                map.put(attr, o);
            }
    public void removeAttribute(String attr)
            {
                map.remove(attr);
            }
    
}
