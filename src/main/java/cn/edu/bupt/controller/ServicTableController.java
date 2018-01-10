package cn.edu.bupt.controller;

import cn.edu.bupt.utils.HttpUtil;
import cn.edu.bupt.utils.ResponceUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2017/12/26.
 *
 *  -- 该类的所有接口返回采用统一json
 */
@RestController
@RequestMapping("/api/service")
@Slf4j
public class ServicTableController {
    @Value("${bupt.thingsboard.host}")
    String thingsboardHost ;

    @Value("${bupt.thingsboard.port}")
    String thingsboardPort ;

    @Autowired
    HttpServletRequest request;

    @Autowired
    ResponceUtil responceUtil ;

    @RequestMapping("/saveGroup")
    public String saveDeviceTable(@RequestBody String json) {
        String url = "http://"+getServer()+"/api/servicetable/saveServiceGroup";
        try{
            String responce = HttpUtil.sendPostToThingsboard(url,null,new JsonParser().parse(json).getAsJsonObject(),request.getSession());
            return responceUtil.onSuccess(responce);
        }catch(Exception e){
            return responceUtil.onFail("保存失败: - " + e.toString());
        }
    }

    @RequestMapping("/deleteGroup")
    public String deleteGroup(@RequestBody String json) {
        String url = "http://"+getServer()+"/api/servicetable/deleteServiceGroup";
        try{
            String responce = HttpUtil.sendPostToThingsboard(url,null,new JsonParser().parse(json).getAsJsonObject(),request.getSession());
            return responceUtil.onSuccess(responce);
        }catch(Exception e){
            return responceUtil.onFail("删除失败: - " + e.toString());
        }
    }

    @RequestMapping("/saveServiceToGroup")
    public String saveServiceToGroup(@RequestBody String json) {
        String url = "http://"+getServer()+"/api/servicetable/add";
        try{
            JsonObject asJsonObject = (JsonObject)new JsonParser().parse(json);
            String responce = HttpUtil.sendPostToThingsboard(url,null, asJsonObject, request.getSession());
            return responceUtil.onSuccess(responce);
        }catch(Exception e){
            return responceUtil.onFail("保存失败: - " + e.toString());
        }
    }

    @RequestMapping("/deleteServiceFromGroup")
    public String deleteServiceFromGroup(@RequestBody String json) {
        String url = "http://"+getServer()+"/api/servicetable/delete";
        try{
            JsonObject asJsonObject = (JsonObject)new JsonParser().parse(json);
            String responce = HttpUtil.sendPostToThingsboard(url,null, asJsonObject, request.getSession());
            return responceUtil.onSuccess(responce);
        }catch(Exception e){
            return responceUtil.onFail("删除失败: - " + e.toString());
        }
    }

    @RequestMapping("/serviceTables")
    public String serviceTableLists() {
        String url = "http://"+getServer()+"/api/servicetable/getAll";
        try{
            String s = HttpUtil.sendGetToThingsboard(url, null, request.getSession());
            return responceUtil.onSuccess(s) ;
        }catch(Exception e){
            return responceUtil.onFail("保存失败: - " + e.toString());
        }
    }

    @RequestMapping(value = "/services/{manufacture}/{deviceType}/{model}/tail", method = RequestMethod.GET)
    public String serviceTableList(@PathVariable String manufacture,@PathVariable String deviceType,@PathVariable String model) {
        String requestAddr = String.format("/api/services/%s/%s/%s", manufacture, deviceType, model) ;
        String url = "http://"+getServer() + requestAddr;
        try{
            String response = HttpUtil.sendGetToThingsboard(url, null, request.getSession());
            return responceUtil.onSuccess(response) ;
        }catch(Exception e){
            return responceUtil.onFail("can't link to thingsboard: " + e) ;
        }
    }

    private String getServer() {
        return thingsboardHost+":"+thingsboardPort ;
    }
}
