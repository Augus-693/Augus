package fun.augus.travel.web.servlet;

import fun.augus.travel.service.impl.FavoriteServiceImpl;
import fun.augus.travel.service.impl.RouteServiceImpl;
import fun.augus.travel.domain.PageBean;
import fun.augus.travel.domain.Route;
import fun.augus.travel.domain.User;
import fun.augus.travel.myenum.CategoryEnum;
import fun.augus.travel.service.FavoriteService;
import fun.augus.travel.service.RouteService;
import fun.augus.travel.utils.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Summerday
 */
@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {

    private static final String COUNT = "count";
    private static final String ISTHEMETOUR = "isThemeTour";
    private static final String RDATE = "rdate";
    private static final String DEFAULT = "count";

    private RouteService routeService= new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();
    /**
     * 分页查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收参数
        String currentPageStr = request.getParameter("currentPage");
        //String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");


        String rname = null;
        //用户输入的查询rname
        String rnameStr = request.getParameter("rname");

        if(rnameStr!=null&&rnameStr.length()>0&&!"null".equals(rnameStr)) {
            rname = new String(rnameStr.getBytes("iso8859-1"), "utf-8");
        }

        int cid = 0;
        //处理参数
        if(cidStr!=null&&cidStr.length()>0&& !"null".equals(cidStr)){
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 1;
        //处理参数
        if(!StringUtils.isEmpty(currentPageStr)){
            currentPage = Integer.parseInt(currentPageStr);
        }
        int pageSize = 10;
        //处理参数
        //if(pageSizeStr!=null&&pageSizeStr.length()>0){
        //    pageSize = Integer.parseInt(pageSizeStr);
        //}

        //调用service查询pagebean对象
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize,rname);
        //将pagebean对象序列化未json返回
        writeValue(pb,response);

    }

    /**
     * 根据id查询一个旅游线路的详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //接收id

        String rid = request.getParameter("rid");
        //调用service查询route对象

        Route route = routeService.findOne(rid);

        //转为json,返回客户端
        writeValue(route,response);

    }

    /**
     * 判断当前登录用户是否收藏过该线路
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取线路id
        String rid = request.getParameter("rid");
        //获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");

        int uid;
        if(user == null){
            //用户未登录
            uid = 0;
        }else{
            //用户已经登录
            uid = user.getUid();
        }

        //调用FavoriteService查询是否收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //将flag写回客户端
        writeValue(flag,response);
    }

    /**
     * 添加收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取线路
        String rid = request.getParameter("rid");
        //获取当前登录的用户
        User user = (User) request.getSession().getAttribute("user");

        int uid;
        if(user == null){
            //用户未登录
            return;
        }else{
            //用户已经登录
            uid = user.getUid();
        }
        //调用service完成添加
        favoriteService.add(rid,uid);

    }

    /**
     * 分页查询我的收藏
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取用户的uid和currentpage
        String uidStr = request.getParameter("uid");
        String currentPageStr = request.getParameter("currentPage");
        int uid = Integer.parseInt(uidStr);
        int currentPage = 1;
        //处理参数
        if(currentPageStr!=null&&currentPageStr.length()>0){
            currentPage = Integer.parseInt(currentPageStr);
        }
        int pageSize = 4;
        PageBean<Route> pb = favoriteService.pageFavorite(uid,currentPage,pageSize);
        writeValue(pb,response);
    }


    /**
     * 热门推荐
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void hotQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //显示的热门数量
        int top  = 5;
        List<Route> routeList = favoriteService.hotQuery(top);

        writeValue(routeList,response);

    }

    /**
     * 收藏排行榜
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void favoriteRank(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取数据
        String rnameStr = request.getParameter("rname");
        String rname = null;
        if(rnameStr!=null){
            //中文乱码问题
            rname = new String(rnameStr.getBytes("iso8859-1"),"utf-8");
        }

        //获取当前页码数
        String currentPageStr = request.getParameter("currentPage");
        int currentPage = 1;
        if(!StringUtils.isEmpty(currentPageStr)){
            currentPage = Integer.parseInt(currentPageStr);
        }

        //获取价格范围
        int first = 0;

        String firstStr = request.getParameter("first");
        if(!StringUtils.isEmpty(firstStr)){
            first = Integer.parseInt(firstStr);
        }
        int last = 0;
        String lastStr = request.getParameter("last");
        if(!StringUtils.isEmpty(lastStr)){
            last = Integer.parseInt(lastStr);
        }
        //每页显示个数
        int pageSize = 4;

        PageBean<Route> pb = favoriteService.pageFavoriteRank(currentPage,pageSize,rname,first,last);

        writeValue(pb,response);
    }

    /**
     * 根据不同主题查询
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findCategory(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String categoryStr = request.getParameter("category");
        List<Route> list = new ArrayList<>();
        //页面显示的数量
        int size = 4;
        //遍历枚举实例
        for(CategoryEnum c:CategoryEnum.values()){
            //匹配传入参数category
            if(c.getCategory().equals(categoryStr)){
                //获取对应的方法名
                String methodName = c.getMethodName();
                try {
                    //根据方法名获取方法对象
                    Method method = routeService.getClass().getMethod(methodName, int.class);
                    //利用实例对象调用方法返回list
                    list = (List<Route>) method.invoke(routeService, size);
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        writeValue(list,response);
    }

}
