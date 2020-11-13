package com.wink.livemall.admin.api.user;

import com.wink.livemall.admin.util.JsonResult;
import com.wink.livemall.admin.util.PageUtil;
import com.wink.livemall.member.dto.LmMember;
import com.wink.livemall.member.dto.LmMemberAddress;
import com.wink.livemall.member.service.LmMemberAddressService;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(tags = "用户地址接口")
@RestController
@RequestMapping("address")
public class UserAddressController {

    Logger logger = LogManager.getLogger(UserAddressController.class);

    @Autowired
    private LmMemberAddressService lmMemberAddressService;

    /**
     * 获取收货地址列表接口
     * @return
     */
    @ApiOperation(value = "获取收货地址列表接口")
    @PostMapping("/list/{userid}")
    public JsonResult address(@ApiParam(name = "userid", value = "用户id",defaultValue = "1", required = true)@PathVariable int userid,
                              @ApiParam(name = "page", value = "页码",defaultValue = "1",required=true) @RequestParam(value = "page",required = true,defaultValue = "1") int page,
                              @ApiParam(name = "pagesize", value = "每页个数",defaultValue = "10",required=true) @RequestParam(value = "pagesize",required = true,defaultValue = "10") int pagesize,
                              HttpServletRequest request){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            List<Map<String,String>> addresslist = lmMemberAddressService.findByMemberidByapi(userid);
            jsonResult.setData(PageUtil.startPage(addresslist,page,pagesize));
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


    /**
     * 新增收货地址
     * @return
     */
    @ApiOperation(value = "新增收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "realname", value = "收货人", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "mobile", value = "手机号码", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "province", value = "省份", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "city", value = "市", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "district", value = "区", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "is_deafult", value = "是否为默认地址", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "address", value = "详细地址", defaultValue = "0", required = true)
    })
    @PostMapping("/add/{userid}")
    @Transactional
    public JsonResult add( @PathVariable int userid, HttpServletRequest request,@RequestParam(required = true) String realname,
                           @RequestParam(required = true) String mobile,
                           @RequestParam(required = true) String province,
                           @RequestParam(required = true) String city,
                           @RequestParam(required = true) int is_deafult,
                           @RequestParam(required = true) String district,
                           @RequestParam(required = true) String address
                           ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmMemberAddress lmMemberAddress = new LmMemberAddress();
            lmMemberAddress.setAddress_info(address);
            lmMemberAddress.setCity(city);
            lmMemberAddress.setUpdated_at(new Date());
            lmMemberAddress.setCreated_at(new Date());
            if(is_deafult==1){
                List<LmMemberAddress> list = lmMemberAddressService.findByMemberid(userid);
                for(LmMemberAddress lmaddress:list){
                    lmaddress.setIs_default(0);
                    lmMemberAddressService.updateService(lmaddress);
                }
                lmMemberAddress.setIs_default(is_deafult);
            }else{
                lmMemberAddress.setIs_default(is_deafult);
            }
            lmMemberAddress.setDistrict(district);
            lmMemberAddress.setMember_id(userid);
            lmMemberAddress.setMobile(mobile);
            lmMemberAddress.setProvince(province);
            lmMemberAddress.setRealname(realname);
            lmMemberAddressService.insertService(lmMemberAddress);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }


    /**
     * 编辑地址
     * @return
     */
    @ApiOperation(value = "编辑地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "id", value = "地址id", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "realname", value = "收货人", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "mobile", value = "手机号码", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "province", value = "省份", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "city", value = "市", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "is_deafult", value = "是否为默认地址", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "district", value = "区", defaultValue = "0", required = true),
            @ApiImplicitParam(name = "address", value = "详细地址", defaultValue = "0", required = true)
    })
    @PostMapping("/edit/{userid}")
    @Transactional
    public JsonResult edit( @PathVariable int userid, HttpServletRequest request,@RequestParam(required = true) String realname,
                           @RequestParam(required = true) String mobile,
                            @RequestParam(required = true) String id,
                           @RequestParam(required = true) String province,
                           @RequestParam(required = true) String city,
                           @RequestParam(required = true) String district,
                            @RequestParam(required = true) int is_deafult,
                           @RequestParam(required = true) String address
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            LmMemberAddress lmMemberAddress = lmMemberAddressService.findById(id);
            lmMemberAddress.setAddress_info(address);
            lmMemberAddress.setCity(city);
            lmMemberAddress.setCreated_at(new Date());
            lmMemberAddress.setUpdated_at(new Date());
            lmMemberAddress.setDistrict(district);
            lmMemberAddress.setMember_id(userid);
            lmMemberAddress.setMobile(mobile);
            if(is_deafult==1){
                List<LmMemberAddress> list = lmMemberAddressService.findByMemberid(userid);
                for(LmMemberAddress lmaddress:list){
                    lmaddress.setIs_default(0);
                    lmMemberAddressService.updateService(lmaddress);
                }
                lmMemberAddress.setIs_default(is_deafult);
            }else{
                lmMemberAddress.setIs_default(is_deafult);
            }
            lmMemberAddress.setProvince(province);
            lmMemberAddress.setRealname(realname);
            lmMemberAddressService.updateService(lmMemberAddress);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return jsonResult;
    }
    /**
     * 编辑地址
     * @return
     */
    @ApiOperation(value = "删除地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地址id", defaultValue = "0", required = true)
    })
    @PostMapping("/delete/{id}")
    public JsonResult edit( @PathVariable int id, HttpServletRequest request
    ){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setCode(JsonResult.SUCCESS);
        try {
            lmMemberAddressService.deleteService(id);
        } catch (Exception e) {
            jsonResult.setMsg(e.getMessage());
            jsonResult.setCode(JsonResult.ERROR);
            logger.error(e.getMessage());
        }
        return jsonResult;
    }


}
