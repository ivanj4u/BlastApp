package com.aribanilia.sms.controller;

import com.aribanilia.sms.entity.ResponseService;
import com.aribanilia.sms.entity.SmsPojo;
import com.aribanilia.sms.util.SmsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsUtil util;
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @RequestMapping(value="/send",  method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseService send(@RequestBody SmsPojo pojo) throws Exception {
        logger.info("Incoming Request /sms/send ");

        ResponseService restService = new ResponseService();
        try {
            util.doSend(pojo.getNoHandphone(), pojo.getMessage());

            restService.setResponseCode(HttpStatus.OK.value() + "");
            restService.setResponseDesc(HttpStatus.OK.getReasonPhrase());
            restService.setData("true");
        } catch (Exception e) {
            e.printStackTrace();
            restService.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value() + "");
            restService.setResponseDesc(e.getMessage());
        }

        logger.info("Outgoing Request /sms/send ");

        return restService;
    }

}
