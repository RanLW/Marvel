

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author carri
 */
@WebServlet(name = "MarvelServlet", urlPatterns = {"/MarvelServlet/*"})
public class MarvelServlet extends HttpServlet {
    
    MarvelModel ipm = null;  // The "business model" for this app

    // Initiate this servlet by instantiating the model that it will use.
    @Override
    public void init() {
        ipm = new MarvelModel();
    }
    
    // GET returns a value given a key
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Console: doGET visited t1");
        System.out.println(request.getPathInfo());
        // The value is on the path /name so skip over the '/'
        String path = request.getPathInfo();
        
        
        // return 401 if value not provided
        if(path == null || path.equals("") || path.equals("/")) {
            response.setStatus(401);
            return;      
        }
        String value = (path).substring(1);  
        // Things went well so set the HTTP response code to 200 OK
        response.setStatus(200);
        // tell the client the type of the response
        response.setContentType("application/json");

        // return the JSONObject from a GET request
        JSONObject result = ipm.doMarvelSearch(value);
        PrintWriter out = response.getWriter();
        out.println(result);            
    }
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet MarvelServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet MarvelServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
}
