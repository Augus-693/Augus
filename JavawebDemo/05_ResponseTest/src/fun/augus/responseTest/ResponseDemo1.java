package fun.augus.responseTest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/responseDemo1")
public class ResponseDemo1 extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("Demo1被访问了");

        /*//访问/responseDemo1，会自动跳转到/responseDemo2
        //1.设置状态码为302
        response.setStatus(302);
        //2.设置响应头location
        response.setHeader("location","/05_ResponseTest/responseDemo2");*/

        //动态获取虚拟目录
        String contextPath = request.getContextPath();
        //简单的重定向方法
        response.sendRedirect(contextPath+"/responseDemo2");
        //response.sendRedirect("/05_ResponseTest/responseDemo2");
        //response.sendRedirect("https://www.baidu.com/");

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
