//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.wso2.sample;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.axiom.util.base64.Base64Utils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class JWTRoleReader extends AbstractMediator {
    private MessageContext context;
    private String JWT_JSON_Array;

    public JWTRoleReader() {
    }

    private void setMessageContext(MessageContext msg) {
        this.context = msg;
    }

    public String getJWT_JSON_Array() {
        return this.JWT_JSON_Array;
    }

    public void setJWT_JSON_Array(String JWT_JSON_Array) {
        this.JWT_JSON_Array = JWT_JSON_Array;
    }

    public boolean mediate(MessageContext context) {
        this.setMessageContext(context);
        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext)context).getAxis2MessageContext();
        Object headerObj = axis2MessageContext.getProperty("TRANSPORT_HEADERS");
        Map headers = (Map)headerObj;
        String jwt_assertion = (String)headers.get("x-jwt-assertion");
        String[] jwt_assertion_items = jwt_assertion.split("\\.");
        byte[] byteArray = Base64Utils.decode(jwt_assertion_items[1]);

        try {
            String outLoad = new String(byteArray, "UTF-8");
            JSONParser jp = new JSONParser();
            JSONObject jo = (JSONObject)jp.parse(outLoad);
            JSONArray roleJasonArray = this.getTheChildJsonObjectArray(jo, "http://wso2.org/claims/role");
            System.out.println("Role" + roleJasonArray);
            this.setROleNameToSynapseMessageContext(roleJasonArray, "role_name");
            context.setProperty("emailaddress", (String)jo.get("http://wso2.org/claims/emailaddress"));
        } catch (UnsupportedEncodingException var12) {
            var12.printStackTrace();
        } catch (ParseException var13) {
            var13.printStackTrace();
        }

        return true;
    }

    private JSONArray getTheChildJsonObjectArray(JSONObject jsonObj, String jsonProperty) {
        JSONArray array = (JSONArray)jsonObj.get(jsonProperty);
        return array;
    }

    private void setROleNameToSynapseMessageContext(JSONArray array, String synapsePropertyName) {
        Iterator var3 = array.iterator();

        while(var3.hasNext()) {
            Object value = var3.next();
            if (value instanceof String) {
                if (value.equals("hos_admin")) {
                    this.context.setProperty(synapsePropertyName, value);
                    System.out.println("This is Hospital Admin" + value);
                    return;
                }

                if (value.equals("physician")) {
                    this.context.setProperty(synapsePropertyName, value);
                    System.out.println("This is physician" + value);
                    return;
                }

                if (value.equals("patient")) {
                    this.context.setProperty(synapsePropertyName, value);
                    System.out.println("THis is patient" + value);
                    return;
                }
            }
        }

    }
}

