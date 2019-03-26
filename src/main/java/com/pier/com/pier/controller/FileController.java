package com.pier.com.pier.controller;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

import javax.print.attribute.standard.Media;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.lang.System.out;

/**
 * @auther zhongweiwu
 * @date 2019/3/21 11:56
 */
@Controller
public class FileController {

    private Logger log = Logger.getLogger(this.getClass());

    public static final String filepath = "/root/file";

    @RequestMapping("/index")
    public String index(){
       return "FileUpAndDown";
    }

    @RequestMapping(value = "/fileUpload")
    public String testUpload(HttpServletRequest request, @RequestParam(value = "desc", required = false) String desc,
                             @RequestParam("file") CommonsMultipartFile file) throws Exception {
        ServletContext servletContext = request.getServletContext();//获取ServletContext的对象 代表当前WEB应用
        //String realPath = servletContext.getRealPath("/root/uploads");//得到文件上传目的位置的真实路径
        out.println("realPath :" + filepath);
        File file1 = new File(filepath);
        if (!file1.exists()) {
            file1.mkdir();   //如果该目录不存在，就创建此抽象路径名指定的目录。
        }
        /*String prefix = UUID.randomUUID().toString();
        prefix = prefix.replace("-", "");
        String fileName = prefix + "_" + file.getOriginalFilename();*///使用UUID加前缀命名文件，防止名字重复被覆盖
        String fileName = file.getOriginalFilename();

        /*InputStream in = file.getInputStream();

        OutputStream out = new FileOutputStream(new File(realPath + "\\" + fileName));//指定输出流的位置;

        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
            out.flush();                //类似于文件复制，将文件存储到输入流，再通过输出流写入到上传位置
        }                               //这段代码也可以用IOUtils.copy(in, out)工具类的copy方法完成

        out.close();
        in.close();*/

        try {
            file.transferTo(new File(filepath + "/"+ fileName));
        }
        catch (Exception e) {
            log.error("upload file error", e);
            request.setAttribute("errorMsg", e.getMessage());
            return "error";
        }

        return "FileUpAndDown";
    }


    /**
     * 下载文件
     * @param request
     * @param filename
     * @return
     * @throws Exception
     */
    @RequestMapping("/fileDownload")
    public ResponseEntity<byte[]> fileDownLoad(HttpServletRequest request, @RequestParam String filename) throws Exception {

        //ServletContext servletContext = request.getServletContext();

        byte[] body = null;
        // 返回下一次对此输入流调用的方法可以不受阻塞地从此输入流读取（或跳过）的估计剩余字节数
        //filename = new String(filename.getBytes("ISO8859-1"), "utf-8");//防止中文乱码
        InputStream in = new FileInputStream(new File(filepath+"/"+filename));//将该文件加入到输入流之中
        body = new byte[in.available()];
        in.read(body);//读入到输入流里面
        HttpHeaders headers = new HttpHeaders();//设置响应头
        // Content-Disposition用于下载
        headers.add("Content-Disposition", "attachment;filename=" + filename);
        HttpStatus statusCode = HttpStatus.OK;//设置响应吗
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body, headers, statusCode);
        return response;
    }

    /**
     * 预览文件
     * @param request
     * @param filename
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/preview"/*, produces = MediaType.IMAGE_JPEG_VALUE*/)
    public ResponseEntity<byte[]> preview(HttpServletRequest request, @RequestParam String filename,
                RedirectAttributes attr) throws Exception {

        byte[] body = null;
        //filename = new String(filename.getBytes("ISO8859-1"), "utf-8");//防止中文乱码
        InputStream in = new FileInputStream(new File(filepath+"/"+filename));//将该文件加入到输入流之中
        body = new byte[in.available()];
        in.read(body);//读入到输入流里面
        HttpHeaders headers = new HttpHeaders();//设置响应头
        if (filename.endsWith("jpg")){
            headers.setContentType(MediaType.IMAGE_JPEG);
        } else if (filename.endsWith("pdf")){
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else if (filename.endsWith("xml") || filename.endsWith("xsd")){
            headers.setContentType(MediaType.APPLICATION_XML);
        } else if (filename.endsWith("txt")){
            headers.setContentType(MediaType.TEXT_PLAIN);
        } else if (filename.endsWith("mp3") || filename.endsWith("mp4")){
            // 音频可以播放，视频不能播放
            //attr.addAttribute("filename", filename);
        } else {
            headers.setContentType(MediaType.TEXT_PLAIN);
        }
        //headers.add("Content-Disposition", "attachment;filename=" + filename);
        HttpStatus statusCode = HttpStatus.OK;//设置响应吗
        ResponseEntity<byte[]> response = new ResponseEntity<>(body, headers, statusCode);
        return response;
    }

    /**
     * 音频流读取
     * @param filename
     * @param response
     * @throws Exception
     */
    @RequestMapping("/play/audio")
    public @ResponseBody void video(@RequestParam String filename, HttpServletResponse response)throws Exception{
        //filename = new String(filename.getBytes("ISO8859-1"), "utf-8");//防止中文乱码
        File file = new File(filepath + "/" + filename);
        FileInputStream in = new FileInputStream(file);
        ServletOutputStream out = response.getOutputStream();
        byte[] b = null;
        while(in.available() >0) {
            if(in.available()>10240) {
                b = new byte[10240];
            }else {
                b = new byte[in.available()];
            }
            in.read(b, 0, b.length);
            out.write(b, 0, b.length);
        }
        in.close();
        out.flush();
        out.close();
    }

    /**
     * 视频播放
     * @param filename
     * @throws Exception
     */
    @RequestMapping("/play/video")
    public ModelAndView video(@RequestParam String filename)throws Exception{
        //filename = new String(filename.getBytes("ISO8859-1"), "utf-8");//防止中文乱码
        return new ModelAndView("video", "realpath", filepath + "/" + filename);
    }

    @RequestMapping("/display")
    @ResponseBody
    public Object display(HttpServletRequest request){
        File files = new File(filepath);
        File[] fileList = files.listFiles();
        //List<String> fileNames = new ArrayList<>();
        Map<String, String> fileNames = new HashMap<>();
        if(fileList == null){
            return null;
        }
        for (File file:fileList){
            fileNames.put(file.getName(), ""+(file.length()/1024)+"kb");
        }

        Object json = JSON.toJSON(fileNames);
        return json;
    }
}