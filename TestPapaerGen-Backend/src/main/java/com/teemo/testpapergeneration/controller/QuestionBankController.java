package com.teemo.testpapergeneration.controller;

import com.alibaba.fastjson.JSONObject;
import com.teemo.testpapergeneration.entity.QuestionBank;
import com.teemo.testpapergeneration.mapper.QuestionBankMapper;
import com.teemo.testpapergeneration.services.ExcelReader;
import com.teemo.testpapergeneration.utils.MyJsonResponse;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
public class QuestionBankController {

    MyJsonResponse myJsonResponse = new MyJsonResponse();

    final
    QuestionBankMapper questionBankMapper;

    public QuestionBankController(QuestionBankMapper questionBankMapper) {
        this.questionBankMapper = questionBankMapper;
    }

    @GetMapping(value = "/getAllQuestionBank", produces = "text/plain;charset=UTF-8")
    public String getAllQuestionBank() {
        List<QuestionBank> allQuestionBank = questionBankMapper.getAllQuestionBank();
        return myJsonResponse.make200Resp(MyJsonResponse.default_200_response, allQuestionBank);
    }

    @GetMapping(value = "/getQuestionBank", produces = "text/plain;charset=UTF-8")
    public String getQuestionBank(@RequestParam("startItem") Integer startItem,
                                  @RequestParam("endItem") Integer endItem) {
        List<QuestionBank> questionBankList = questionBankMapper.getAllQuestionBank();
        return myJsonResponse.make200Resp(MyJsonResponse.default_200_response, questionBankList);
    }

    @PostMapping(value = "/insertSingleQuestionBank")
    public String insertSingleQuestionBank(@RequestBody QuestionBank questionBank) {
        questionBank.setUpdate_time(new Date());
        Integer commitStatus = questionBankMapper.insertSingleQuestionBank(questionBank);
        JSONObject retJson = new JSONObject();
        retJson.put("insertStatus", commitStatus);
        retJson.put("insertObject", questionBank);
        return myJsonResponse.make200Resp(MyJsonResponse.default_200_response, retJson);
    }

    @GetMapping(value = "/deleteSingleQuestionBank", produces = "text/plain;charset=UTF-8")
    public String deleteSingleQuestionBank(@RequestParam("id") Integer id) {
        Integer commitStatus = questionBankMapper.deleteSingleQuestionBank(id);
        JSONObject retJson = new JSONObject();
        retJson.put("deleteStatus", commitStatus);
        retJson.put("deleteObject", id);
        return myJsonResponse.make200Resp(MyJsonResponse.default_200_response, retJson);
    }

    @GetMapping(value = "/getQuestionBankById", produces = "text/plain;charset=UTF-8")
    public String getQuestionBankById(@RequestParam("id") Integer id) {
        List<QuestionBank> questionBankByIdList = questionBankMapper.getQuestionBankById(id);
        return myJsonResponse.make200Resp(MyJsonResponse.default_200_response, questionBankByIdList);
    }

    @PostMapping(value = "/updateQuestionBankById")
    public String updateQuestionBankById(@RequestBody QuestionBank questionBank) {
        questionBank.setUpdate_time(new Date());
        Integer updateStatus = questionBankMapper.updateSingleQuestionBank(questionBank);
        JSONObject retJson = new JSONObject();
        retJson.put("updateStatus", updateStatus);
        retJson.put("updateObject", questionBank);
        return myJsonResponse.make200Resp(MyJsonResponse.default_200_response, retJson);
    }

    // 导入excel文件到数据库
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam("isDeleteAll") boolean isDeleteAll) {
        System.out.println("file = " + multipartFile + ", isDeleteAll = " + isDeleteAll);
        System.out.println("QuestionBankController.uploadFile");
        System.out.println(multipartFile.getOriginalFilename() + " --> " + multipartFile.getSize() / 1024 + "k bytes");
        // MultipartFile to File
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        if (fileName == null || fileName.equals("")) {
            fileName = "data.xlsx";
        }
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 创建 File
        final File file;
        try {
            // 用uuid作为文件名，防止生成的临时文件重复
            file = File.createTempFile(UUID.randomUUID().toString(), prefix);
            multipartFile.transferTo(file);
            ExcelReader excelReader = new ExcelReader(file);
            excelReader.run();
            System.out.println(file);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        return myJsonResponse.make200Resp(MyJsonResponse.default_200_response, "retJson");
    }

    // 查询各Label1下的统计数量
    @GetMapping(value = "/getEachChapterCount", produces = "text/plain;charset=UTF-8")
    public String getEachChapterCount() {
        List<String> distinctLabel1FromQuestionBank = questionBankMapper.getDistinctLabel1FromQuestionBank();
        ArrayList<JSONObject> ret = new ArrayList<>();
        for (String eachLabel1 : distinctLabel1FromQuestionBank) {
            Integer count = questionBankMapper.getQuestionBankCountByLabel1(eachLabel1);
            JSONObject _tmp = new JSONObject();
            _tmp.put("label_1", eachLabel1);
            _tmp.put("count", count);
            ret.add(_tmp);
        }
        return myJsonResponse.make200Resp(MyJsonResponse.default_200_response, ret);
    }

    // 查询各Score下的统计数量
    @GetMapping(value = "/getEachScoreCount", produces = "text/plain;charset=UTF-8")
    public String getEachScoreCount() {
        List<Double> distinctScoreFromQuestionBank = questionBankMapper.getDistinctScoreFromQuestionBank();
        ArrayList<JSONObject> ret = new ArrayList<>();
        for (Double eachScore : distinctScoreFromQuestionBank) {
            Integer count = questionBankMapper.getQuestionBankCountByScore(eachScore);
            JSONObject _tmp = new JSONObject();
            _tmp.put("score", eachScore);
            _tmp.put("count", count);
            ret.add(_tmp);
        }
        return myJsonResponse.make200Resp(MyJsonResponse.default_200_response, ret);
    }
}
