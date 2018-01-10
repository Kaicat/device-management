package cn.edu.bupt.controller;

import cn.edu.bupt.data.CachForDeviceService;
import cn.edu.bupt.utils.HttpUtil;
import cn.edu.bupt.utils.ResponceUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/12/23.
 *
 *  -- 该类的所有接口返回采用统一json
 */
@RestController
@RequestMapping("/api/shadow")
@Slf4j
public class ShadowController {
    @Value("${bupt.thingsboard.host}")
    String thingsboardHost ;

    @Value("${bupt.thingsboard.port}")
    String thingsboardPort ;

    @Autowired
    HttpServletRequest request;

    @Autowired
    ResponceUtil responceUtil ;

    @RequestMapping("/{deviceId}")
    public String getDeviceShadow(@PathVariable("deviceId") String deviceId){
        String url = "http://"+getServer()+"/api/shadow/"+deviceId;
        JsonObject body = new JsonObject();
        body.addProperty("requestName","get");
//        JsonObject res = new JsonObject();
        try{
            String s = HttpUtil.sendPostToThingsboard(url,null,body,request.getSession());
//            res.addProperty("responce_code",0);
            JsonObject obj = new JsonParser().parse(s).getAsJsonObject();
            CachForDeviceService.put(deviceId,obj);
//            res.add("responce_msg",obj);
            return responceUtil.onSuccess(obj) ;
        }catch(Exception e){
            return responceUtil.onFail(e);
        }
    }

    @RequestMapping("/control/{deviceId}")
    public String controlDevice(@RequestBody String bd,@PathVariable("deviceId") String deviceId){
        String url = "http://"+getServer()+"/api/shadow/"+deviceId;
        JsonObject body = new JsonObject();
        body.addProperty("requestName","serviceCall");
        JsonObject paramsAndServiceName = new JsonParser().parse(bd).getAsJsonObject();
//        String serviceNmae = paramsAndServiceName.get("serviceName").getAsString();
//        JsonObject service = CachForDeviceService.get(deviceId,serviceNmae);
//        paramsAndServiceName.remove("serviceName");
//        service.get("serviceBody").getAsJsonObject().add("params",paramsAndServiceName);
//        body.add("requestBody",service);
        body.add("requestBody",paramsAndServiceName);
//        JsonObject res = new JsonObject();
        try{
            String s = HttpUtil.sendPostToThingsboard(url,null,body,request.getSession());
//            res.addProperty("responce_code",0);
//            res.addProperty("responce_msg",s);
            return responceUtil.onSuccess(s);
        }catch(Exception e){
            return responceUtil.onFail(e);
        }
    }

    private String getServer() {
        return thingsboardHost+":"+thingsboardPort ;
    }
}
