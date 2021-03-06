package fun.augus.travel.web.servlet;

import fun.augus.travel.service.impl.CategoryServiceImpl;
import fun.augus.travel.domain.Category;
import fun.augus.travel.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Summerday
 */
@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    private CategoryService service = new CategoryServiceImpl();
    /**
     * 查询所有c
     * @param request
     * @param response
     */
    public void findAll(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
        //调用service查询所有
        List<Category> cs = service.findAll();

        //将序列化json返回
        writeValue(cs,response);

    }
}
